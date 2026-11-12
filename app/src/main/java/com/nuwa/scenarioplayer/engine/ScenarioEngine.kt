package com.nuwa.scenarioplayer.engine

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.nuwarobotics.service.agent.NuwaRobotAPI
import com.nuwarobotics.service.agent.VoiceEventListener
import com.nuwarobotics.service.facecontrol.IonCompleteListener
import com.nuwarobotics.service.facecontrol.UnityFaceManager
import com.nuwa.scenarioplayer.audio.SoundEffectManager
import com.nuwa.scenarioplayer.model.JointSafety
import com.nuwa.scenarioplayer.model.JointTarget
import com.nuwa.scenarioplayer.model.Scenario
import com.nuwa.scenarioplayer.model.ScenarioStep

class ScenarioEngine(
    private val robot: NuwaRobotAPI,
    val chassisManager: ChassisSafetyManager,
    val ledManager: LedManager,
    val soundManager: SoundEffectManager? = null,
    private val isMotorEnabled: () -> Boolean,
    private val isTtsEnabled: () -> Boolean,
    private val listener: ScenarioEngineListener? = null
) {

    companion object {
        private const val TAG = "ScenarioEngine"
        private const val FACE_DELAY_MS = 800L
    }

    interface ScenarioEngineListener {
        fun onScenarioStarted(scenario: Scenario)
        fun onStepChanged(scenario: Scenario, stepIndex: Int, step: ScenarioStep)
        fun onScenarioCompleted(scenario: Scenario)
        fun onScenarioStopped(scenario: Scenario?)
        fun onError(message: String)
    }

    private val mainHandler = Handler(Looper.getMainLooper())

    @Volatile
    var currentScenario: Scenario? = null
        private set

    @Volatile
    var currentStepIndex: Int = -1
        private set

    @Volatile
    var isPlaying: Boolean = false
        private set

    private var watchdogRunnable: Runnable? = null

    /**
     * 啟動劇本播放
     */
    @Synchronized
    fun play(scenario: Scenario): Boolean {
        if (isPlaying) {
            Log.w(TAG, "Cannot play: another scenario is currently playing (${currentScenario?.title})")
            return false
        }

        if (!isMotorEnabled()) {
            val err = "啟動失敗：馬達安全防護未開啟"
            Log.e(TAG, err)
            listener?.onError(err)
            return false
        }

        isPlaying = true
        currentScenario = scenario
        currentStepIndex = 0

        Log.i(TAG, ">>> Starting Scenario: ${scenario.title} (${scenario.id}) with ${scenario.steps.size} steps <<<")
        listener?.onScenarioStarted(scenario)

        if (scenario.face != null) {
            // 臉部畫面需要 FACE_DELAY_MS 才會出現；動作一起延後，讓表情與動作同時開始
            startFace(scenario)
            mainHandler.postDelayed({
                if (currentScenario === scenario && isPlaying) executeStep(0)
            }, FACE_DELAY_MS)
        } else {
            executeStep(0)
        }
        return true
    }

    /**
     * 全域緊急煞停 (E-STOP)
     */
    @Synchronized
    fun stop() {
        if (!isPlaying && currentScenario == null) return

        val stoppedScenario = currentScenario
        Log.i(TAG, ">>> EMERGENCY STOP Scenario: ${stoppedScenario?.title} <<<")

        cancelWatchdog()
        isPlaying = false
        currentStepIndex = -1
        currentScenario = null

        // 1. 底盤立即煞停
        chassisManager.emergencyStop()

        // 2. 官方肢體動作停止
        try {
            if (isMotorEnabled()) {
                robot.motionStop(false)
            }
        } catch (e: Exception) {
            Log.e(TAG, "motionStop failed", e)
        }

        // 2b. 直接控制過的關節回到 0.0 參考位置 (motionStop 不會處理 ctlMotor)
        returnJointsToReference(stoppedScenario)

        // 2c. 換過臉的劇本：停止臉部動畫，回到預設臉
        resetFaceIfUsed(stoppedScenario)

        // 3. 語音中斷
        try {
            if (isTtsEnabled()) {
                robot.stopTTS()
            }
        } catch (e: Exception) {
            Log.e(TAG, "stopTTS failed", e)
        }

        // 4. 燈效復原
        ledManager.reset()

        // 5. 音效停止
        soundManager?.stopAll()

        // 6. 官方臉部視窗隱藏
        try {
            robot.hideWindow(true)
        } catch (e: Exception) {
            Log.e(TAG, "hideWindow failed", e)
        }

        listener?.onScenarioStopped(stoppedScenario)
    }

    private fun executeStep(index: Int) {
        val scenario = currentScenario ?: return
        if (index >= scenario.steps.size) {
            finishScenario()
            return
        }

        currentStepIndex = index
        val step = scenario.steps[index]
        Log.i(TAG, "Executing step [$index/${scenario.steps.size}]: ${step.name}")
        listener?.onStepChanged(scenario, index, step)

        // 1. 套用燈效
        ledManager.applyPattern(step.ledPattern)

        // 2. 觸發特殊音效
        step.soundType?.let { sound ->
            soundManager?.play(sound)
        }

        // 3. 觸發語音 TTS
        step.ttsText?.let { text ->
            if (isTtsEnabled()) {
                try {
                    robot.setSpeakParameter(VoiceEventListener.SpeakType.NORMAL, "isForced", "true")
                    robot.setSpeakParameter(VoiceEventListener.SpeakType.NORMAL, "speed", step.ttsSpeed)
                    robot.setSpeakParameter(VoiceEventListener.SpeakType.NORMAL, "pitch", step.ttsPitch)
                    robot.startTTS(text)
                } catch (e: Exception) {
                    Log.e(TAG, "TTS failed on step: ${step.name}", e)
                }
            }
        }

        // 4. 觸發底盤移動
        step.chassisMove?.let { move ->
            chassisManager.execute(move)
        }

        // 5. 觸發官方肢體動作與全螢幕臉部表情視窗 (auto_fadein = true)
        step.motionName?.let { motion ->
            if (isMotorEnabled()) {
                try {
                    robot.motionPlay(motion, true)
                } catch (e: Exception) {
                    Log.e(TAG, "motionPlay failed on motion: $motion", e)
                }
            }
        }

        // 5b. 直接關節控制 (情緒動作)：逐一檢查白名單，不在白名單內一律拒絕
        if (step.jointTargets.isNotEmpty() && isMotorEnabled()) {
            sendJointTargets(step.jointTargets)
        }

        // 6. 設定看門狗定時器 (若動作無回調或超時，自動跳入下一步)
        setWatchdog(step.timeoutMs) {
            Log.w(TAG, "Step timeout reached for: ${step.name}, advancing...")
            executeStep(index + 1)
        }
    }

    private fun sendJointTargets(targets: List<JointTarget>) {
        targets.forEach { t ->
            if (!JointSafety.isAllowed(t)) {
                Log.e(TAG, "Joint target REJECTED (outside tested range): motor=${t.motorId} deg=${t.degree} speed=${t.speed}")
                return@forEach
            }
            try {
                robot.ctlMotor(t.motorId, t.degree, t.speed)
                Log.d(TAG, "ctlMotor motor=${t.motorId} deg=${t.degree} speed=${t.speed}")
            } catch (e: Exception) {
                Log.e(TAG, "ctlMotor failed: motor=${t.motorId}", e)
            }
        }
    }

    /** 只播臉部動畫 (IFaceControlService.playUnityFace)，不帶 JSON 內的 robotMotion */
    fun playFaceAnimationNow(name: String): Boolean {
        return try {
            val face = UnityFaceManager.getInstance()
            if (!face.isServiceConnected) return false
            face.setAutoIdleAnimState(true)
            face.showUnity()
            face.playFaceAnimation(name)
            Log.i(TAG, "Face animation '$name' requested")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Face animation failed: $name", e)
            false
        }
    }

    fun resetFace() {
        try {
            val face = UnityFaceManager.getInstance()
            face.stopMotion()
            face.setAutoIdleAnimState(false)
            Log.i(TAG, "Face motion stopped (back to default face)")
        } catch (e: Exception) {
            Log.e(TAG, "Face reset failed", e)
        }
    }

    private var faceStartRunnable: Runnable? = null

    /** 劇本開始時換臉一次 (表情會停住)；FaceActivity 啟動需要時間，延遲 FACE_DELAY_MS */
    private fun startFace(scenario: Scenario) {
        val cue = scenario.face ?: return
        stopFaceStart()
        val r = Runnable {
            if (currentScenario === scenario) playFaceAnimationNow(cue.name)
        }
        faceStartRunnable = r
        mainHandler.postDelayed(r, FACE_DELAY_MS)
    }

    private fun stopFaceStart() {
        faceStartRunnable?.let { mainHandler.removeCallbacks(it) }
        faceStartRunnable = null
    }

    private fun resetFaceIfUsed(scenario: Scenario?) {
        stopFaceStart()
        if (scenario?.face == null) return
        resetFace()
    }

    /** 中途停止時，把此劇本動過的關節送回 0.0 參考指令 (MOTOR_MAPPING.md 的回位值) */
    private fun returnJointsToReference(scenario: Scenario?) {
        val motors = scenario?.steps?.flatMap { s -> s.jointTargets.map { it.motorId } }?.toSet().orEmpty()
        if (motors.isEmpty() || !isMotorEnabled()) return
        Log.i(TAG, "Returning joints $motors to 0.0 reference")
        sendJointTargets(motors.map { JointTarget(it, 0f) })
    }

    private fun finishScenario() {
        val finished = currentScenario ?: return
        Log.i(TAG, ">>> Scenario completed: ${finished.title} <<<")
        resetFaceIfUsed(finished)

        cancelWatchdog()
        chassisManager.emergencyStop()
        ledManager.reset()
        soundManager?.stopAll()

        try {
            robot.hideWindow(true)
        } catch (e: Exception) {
            Log.e(TAG, "hideWindow failed on finish", e)
        }

        isPlaying = false
        currentStepIndex = -1
        currentScenario = null

        listener?.onScenarioCompleted(finished)
    }

    // ===== 外部事件轉發入口 =====

    fun onMotionComplete(motionName: String) {
        if (!isPlaying) return
        val scenario = currentScenario ?: return
        if (currentStepIndex in scenario.steps.indices) {
            val step = scenario.steps[currentStepIndex]
            if (step.motionName == motionName) {
                Log.d(TAG, "Step motion $motionName completed. Moving to next step.")
                cancelWatchdog()
                mainHandler.post {
                    executeStep(currentStepIndex + 1)
                }
            }
        }
    }

    fun onMotionError(errCode: Int) {
        if (!isPlaying) return
        Log.e(TAG, "onMotionError received: $errCode on step $currentStepIndex. Advancing...")
        cancelWatchdog()
        mainHandler.post {
            executeStep(currentStepIndex + 1)
        }
    }

    private fun setWatchdog(timeoutMs: Long, onTimeout: () -> Unit) {
        cancelWatchdog()
        val r = Runnable {
            onTimeout()
        }
        watchdogRunnable = r
        mainHandler.postDelayed(r, timeoutMs)
    }

    private fun cancelWatchdog() {
        watchdogRunnable?.let {
            mainHandler.removeCallbacks(it)
            watchdogRunnable = null
        }
    }
}
