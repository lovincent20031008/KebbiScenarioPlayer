package com.nuwa.scenarioplayer

import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nuwa.scenarioplayer.audio.SoundEffectManager
import com.nuwa.scenarioplayer.engine.ChassisSafetyManager
import com.nuwa.scenarioplayer.engine.LedManager
import com.nuwa.scenarioplayer.engine.ScenarioEngine
import com.nuwa.scenarioplayer.model.Scenario
import com.nuwa.scenarioplayer.model.ScenarioCategory
import com.nuwa.scenarioplayer.model.ScenarioRepository
import com.nuwa.scenarioplayer.model.ScenarioStep
import com.nuwa.scenarioplayer.server.ScenarioHttpServer
import com.nuwarobotics.service.IClientId
import com.nuwarobotics.service.agent.NuwaRobotAPI
import com.nuwarobotics.service.agent.RobotEventListener
import com.nuwarobotics.service.agent.VoiceEventListener
import java.io.IOException

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ScenarioPlayerMain"
        private const val HTTP_PORT = 8081
    }

    private lateinit var robot: NuwaRobotAPI
    private lateinit var clientId: IClientId

    private lateinit var chassisManager: ChassisSafetyManager
    private lateinit var ledManager: LedManager
    private lateinit var soundManager: SoundEffectManager
    private lateinit var engine: ScenarioEngine
    private var httpServer: ScenarioHttpServer? = null

    private var robotReady = false

    // UI 元件
    private lateinit var tvHeaderStatus: TextView
    private lateinit var tvActiveStatus: TextView
    private lateinit var cbAutoFace: CheckBox
    private lateinit var btnEmergencyStop: Button
    private lateinit var btnMegaDemo: Button
    private lateinit var containerButlerCards: LinearLayout
    private lateinit var containerFraudCards: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvHeaderStatus = findViewById(R.id.tv_header_status)
        tvActiveStatus = findViewById(R.id.tv_active_status)
        cbAutoFace = findViewById(R.id.cb_auto_face)
        btnEmergencyStop = findViewById(R.id.btn_emergency_stop)
        btnMegaDemo = findViewById(R.id.btn_mega_demo)
        val btnMegaDemoBanner: Button = findViewById(R.id.btn_mega_demo_banner)
        containerButlerCards = findViewById(R.id.container_butler_cards)
        containerFraudCards = findViewById(R.id.container_fraud_cards)

        initRobotSdk()
        setupEngine()
        setupCardViews()
        startHttpServer()

        btnEmergencyStop.setOnClickListener {
            engine.stop()
        }

        val onMegaDemoClick = {
            if (!engine.isPlaying) {
                engine.play(ScenarioRepository.megaDemoScenario)
            } else {
                engine.stop()
            }
        }
        btnMegaDemo.setOnClickListener { onMegaDemoClick() }
        btnMegaDemoBanner.setOnClickListener { onMegaDemoClick() }
    }

    private fun initRobotSdk() {
        clientId = IClientId(packageName)
        robot = NuwaRobotAPI(this, clientId)

        robot.registerRobotEventListener(object : RobotEventListener {
            override fun onWikiServiceStart() {
                Log.i(TAG, "Nuwa WikiService Connected!")
                robotReady = true
                runOnUiThread {
                    tvHeaderStatus.text = "HTTP REST API: Port $HTTP_PORT | Nuwa Service: 就緒 (Ready)"
                    tvHeaderStatus.setTextColor(Color.parseColor("#2E7D32"))
                }
            }

            override fun onWikiServiceStop() {
                robotReady = false
                runOnUiThread {
                    tvHeaderStatus.text = "HTTP REST API: Port $HTTP_PORT | Nuwa Service: 停止 (Stopped)"
                    tvHeaderStatus.setTextColor(Color.parseColor("#C62828"))
                }
            }

            override fun onWikiServiceCrash() {
                robotReady = false
                runOnUiThread {
                    tvHeaderStatus.text = "HTTP REST API: Port $HTTP_PORT | Nuwa Service: 異常崩潰"
                    tvHeaderStatus.setTextColor(Color.parseColor("#C62828"))
                }
            }

            override fun onWikiServiceRecovery() {
                runOnUiThread {
                    tvHeaderStatus.text = "HTTP REST API: Port $HTTP_PORT | Nuwa Service: 復原中..."
                }
            }

            override fun onStartOfMotionPlay(motion: String) {
                Log.d(TAG, "onStartOfMotionPlay: $motion")
            }

            override fun onPauseOfMotionPlay(p0: String) {}
            override fun onStopOfMotionPlay(p0: String) {}

            override fun onCompleteOfMotionPlay(motion: String) {
                Log.d(TAG, "onCompleteOfMotionPlay: $motion")
                engine.onMotionComplete(motion)
            }

            override fun onPlayBackOfMotionPlay(p0: String) {}

            override fun onErrorOfMotionPlay(errCode: Int) {
                Log.e(TAG, "onErrorOfMotionPlay: $errCode")
                engine.onMotionError(errCode)
            }

            override fun onPrepareMotion(p0: Boolean, p1: String, p2: Float) {}
            override fun onCameraOfMotionPlay(p0: String) {}
            override fun onGetCameraPose(p0: Float, p1: Float, p2: Float, p3: Float, p4: Float, p5: Float, p6: Float, p7: Float, p8: Float, p9: Float, p10: Float, p11: Float) {}
            override fun onTouchEvent(p0: Int, p1: Int) {}
            override fun onPIREvent(p0: Int) {}
            override fun onTap(p0: Int) {
                Log.d(TAG, "onTap detected: $p0")
                if (engine.isPlaying) {
                    Log.i(TAG, "Emergency Stop triggered via robot tap sensor")
                    engine.stop()
                }
            }
            override fun onLongPress(p0: Int) {}
            override fun onWindowSurfaceReady() {}
            override fun onWindowSurfaceDestroy() {}
            override fun onTouchEyes(p0: Int, p1: Int) {}
            override fun onRawTouch(p0: Int, p1: Int, p2: Int) {}
            override fun onFaceSpeaker(p0: Float) {}
            override fun onActionEvent(p0: Int, p1: Int) {}
            override fun onDropSensorEvent(p0: Int) {}
            override fun onMotorErrorEvent(p0: Int, p1: Int) {}
        })

        if (robot.isKiWiServiceReady()) {
            robotReady = true
            tvHeaderStatus.text = "HTTP REST API: Port $HTTP_PORT | Nuwa Service: 就緒 (Ready)"
            tvHeaderStatus.setTextColor(Color.parseColor("#2E7D32"))
        }
    }

    private fun setupEngine() {
        chassisManager = ChassisSafetyManager(robot) { true }
        ledManager = LedManager(robot) { true }
        soundManager = SoundEffectManager(this)

        engine = ScenarioEngine(
            robot = robot,
            chassisManager = chassisManager,
            ledManager = ledManager,
            soundManager = soundManager,
            isMotorEnabled = { true },
            isTtsEnabled = { true },
            listener = object : ScenarioEngine.ScenarioEngineListener {
                override fun onScenarioStarted(scenario: Scenario) {
                    runOnUiThread {
                        tvActiveStatus.text = "▶ 正在播放：${scenario.title} (${scenario.badge})"
                        tvActiveStatus.setTextColor(Color.parseColor("#1565C0"))
                        if (cbAutoFace.isChecked) {
                            showFacePresenter()
                        }
                    }
                }

                override fun onStepChanged(scenario: Scenario, stepIndex: Int, step: ScenarioStep) {
                    runOnUiThread {
                        tvActiveStatus.text = "▶ 正在播放：${scenario.title} → 步驟 [${stepIndex + 1}/${scenario.steps.size}]: ${step.name}"
                    }
                }

                override fun onScenarioCompleted(scenario: Scenario) {
                    runOnUiThread {
                        tvActiveStatus.text = "✓ 劇本完成：${scenario.title} (動作與燈效已復原)"
                        tvActiveStatus.setTextColor(Color.parseColor("#2E7D32"))
                        if (cbAutoFace.isChecked) {
                            bringAppToFront()
                        }
                    }
                }

                override fun onScenarioStopped(scenario: Scenario?) {
                    runOnUiThread {
                        tvActiveStatus.text = "■ 已緊急中斷 (E-STOP)：${scenario?.title ?: "未知劇本"}"
                        tvActiveStatus.setTextColor(Color.parseColor("#C62828"))
                        if (cbAutoFace.isChecked) {
                            bringAppToFront()
                        }
                    }
                }

                override fun onError(message: String) {
                    runOnUiThread {
                        tvActiveStatus.text = "✗ 執行異常：$message"
                        tvActiveStatus.setTextColor(Color.parseColor("#C62828"))
                    }
                }
            }
        )
    }

    private fun showFacePresenter() {
        try {
            val intent = Intent().apply {
                component = ComponentName("com.nuwarobotics.app.facepresenter", "com.nuwarobotics.app.facepresenter.FaceActivity")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            Log.i(TAG, "Switched to FaceActivity successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to launch FaceActivity", e)
        }
    }

    private fun bringAppToFront() {
        try {
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            startActivity(intent)
            Log.i(TAG, "Brought MainActivity back to front")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to bring MainActivity to front", e)
        }
    }

    private fun setupCardViews() {
        val inflater = LayoutInflater.from(this)

        ScenarioRepository.allScenarios.forEach { scenario ->
            val cardView = inflater.inflate(R.layout.item_scenario_card, null, false)

            val tvTitle = cardView.findViewById<TextView>(R.id.tv_card_title)
            val tvBadge = cardView.findViewById<TextView>(R.id.tv_card_badge)
            val tvDesc = cardView.findViewById<TextView>(R.id.tv_card_desc)
            val btnPlay = cardView.findViewById<Button>(R.id.btn_card_play)

            tvTitle.text = scenario.title
            tvBadge.text = scenario.badge
            tvDesc.text = scenario.description

            if (scenario.category == ScenarioCategory.BUTLER) {
                btnPlay.backgroundTintList = getColorStateList(android.R.color.holo_blue_dark)
                containerButlerCards.addView(cardView)
            } else {
                btnPlay.backgroundTintList = getColorStateList(android.R.color.holo_red_dark)
                containerFraudCards.addView(cardView)
            }

            btnPlay.setOnClickListener {
                if (!engine.isPlaying) {
                    engine.play(scenario)
                } else {
                    engine.stop()
                }
            }
        }
    }

    private fun startHttpServer() {
        try {
            httpServer = ScenarioHttpServer(HTTP_PORT, engine)
            httpServer?.start()
            Log.i(TAG, "ScenarioHttpServer started on port $HTTP_PORT")
        } catch (e: IOException) {
            Log.e(TAG, "Failed to start ScenarioHttpServer", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::engine.isInitialized) {
            engine.stop()
        }
        if (::soundManager.isInitialized) {
            soundManager.release()
        }
        httpServer?.stop()
    }
}
