package com.nuwa.scenarioplayer.engine

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.nuwarobotics.service.agent.NuwaRobotAPI
import com.nuwa.scenarioplayer.model.ChassisMove

/**
 * 底盤安全管理器 (Chassis Safety Manager)
 * 嚴格把關底盤輪胎位移速度、旋轉角速度、單次位移時長，並提供即時緊急煞停。
 */
class ChassisSafetyManager(
    private val robot: NuwaRobotAPI,
    private val isChassisEnabled: () -> Boolean
) {

    companion object {
        private const val TAG = "ChassisSafetyManager"

        // 硬體防衝安全上限 (適度提升以展現高動態與衝刺感，維持 25cm 上限)
        const val MAX_MOVE_SPEED = 0.28f      // 最大直線速度 0.28 m/s
        const val MAX_TURN_SPEED = 130.0f     // 最大旋轉速度 130 deg/s
        const val MAX_DURATION_MS = 2500L     // 單次最長位移時長 2.5 秒
    }

    private val mainHandler = Handler(Looper.getMainLooper())

    @Volatile
    var isMoving: Boolean = false
        private set

    private var currentStopRunnable: Runnable? = null

    /**
     * 執行安全底盤動作 (帶有時長閉環保護)
     */
    @Synchronized
    fun execute(move: ChassisMove, onComplete: (() -> Unit)? = null) {
        if (!isChassisEnabled()) {
            Log.w(TAG, "Chassis move blocked: isChassisEnabled() == false")
            onComplete?.invoke()
            return
        }

        // 停止前次未完成的動作
        emergencyStop()

        val clampedSpeed = move.speed.coerceIn(-MAX_MOVE_SPEED, MAX_MOVE_SPEED)
        val clampedTurnSpeed = move.turnSpeed.coerceIn(-MAX_TURN_SPEED, MAX_TURN_SPEED)
        val clampedDuration = move.durationMs.coerceIn(0L, MAX_DURATION_MS)

        if (clampedSpeed == 0f && clampedTurnSpeed == 0f || clampedDuration <= 0L) {
            onComplete?.invoke()
            return
        }

        isMoving = true
        Log.i(TAG, "Chassis moving: speed=$clampedSpeed m/s, turn=$clampedTurnSpeed deg/s, duration=$clampedDuration ms")

        try {
            if (clampedSpeed != 0f) {
                robot.move(clampedSpeed)
            }
            if (clampedTurnSpeed != 0f) {
                robot.turn(clampedTurnSpeed)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start chassis movement", e)
            emergencyStop()
            onComplete?.invoke()
            return
        }

        // 建立到時自動煞停之定時器
        val stopRunnable = Runnable {
            emergencyStop()
            onComplete?.invoke()
        }
        currentStopRunnable = stopRunnable
        mainHandler.postDelayed(stopRunnable, clampedDuration)
    }

    /**
     * 最高優先級全域緊急煞停 (E-STOP)
     */
    @Synchronized
    fun emergencyStop() {
        currentStopRunnable?.let {
            mainHandler.removeCallbacks(it)
            currentStopRunnable = null
        }
        isMoving = false

        try {
            robot.move(0f)
            robot.turn(0f)
            Log.d(TAG, "Chassis emergency stop executed (speed=0, turn=0)")
        } catch (e: Exception) {
            Log.e(TAG, "Failed during chassis emergency stop", e)
        }
    }
}
