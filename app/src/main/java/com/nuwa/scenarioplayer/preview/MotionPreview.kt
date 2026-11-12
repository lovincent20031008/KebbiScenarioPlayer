package com.nuwa.scenarioplayer.preview

import android.util.Log
import com.nuwarobotics.service.agent.NuwaRobotAPI
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * 內建動作預覽工具 (只給測試用的 HTTP 網址使用)
 * - info：用 motionPrepare 只載入動作、讀取長度 (onPrepareMotion 回呼)，不播放
 * - play：播放單一內建動作 (會動，需有人在旁)
 * - stop：停止內建動作
 */
class MotionPreview(private val robot: NuwaRobotAPI) {

    companion object {
        private const val TAG = "MotionPreview"
        private val NAME_PATTERN = Regex("^[A-Za-z0-9_]{3,64}$")
    }

    data class Info(val name: String, val ok: Boolean, val durationSec: Float?, val error: String? = null)

    private val waiters = ConcurrentHashMap<String, Pair<CountDownLatch, Array<Any?>>>()

    @Volatile
    var playing: String? = null
        private set

    fun isValidName(name: String) = NAME_PATTERN.matches(name)

    /** 由 MainActivity 的 onPrepareMotion 轉進來 */
    fun onPrepared(isError: Boolean, motion: String, duration: Float) {
        Log.i(TAG, "onPrepareMotion: $motion isError=$isError duration=$duration")
        waiters.remove(motion)?.let { (latch, box) ->
            box[0] = isError
            box[1] = duration
            latch.countDown()
        }
    }

    fun onMotionFinished(motion: String) {
        if (playing == motion) playing = null
    }

    /** 只載入不播放，等回呼最多 timeoutMs */
    fun info(name: String, timeoutMs: Long = 4000L): Info {
        if (!isValidName(name)) return Info(name, false, null, "invalid name")
        val latch = CountDownLatch(1)
        val box = arrayOf<Any?>(null, null)
        waiters[name] = latch to box
        return try {
            robot.motionPrepare(name)
            if (!latch.await(timeoutMs, TimeUnit.MILLISECONDS)) {
                waiters.remove(name)
                Info(name, false, null, "timeout (motion may not exist)")
            } else {
                val isError = box[0] as Boolean
                Info(name, !isError, if (isError) null else box[1] as Float, if (isError) "prepare error" else null)
            }
        } catch (e: Exception) {
            waiters.remove(name)
            Log.e(TAG, "motionPrepare failed: $name", e)
            Info(name, false, null, e.message)
        }
    }

    /** 播放單一內建動作；withFace=true 會顯示動作附帶的臉部影片 */
    fun play(name: String, withFace: Boolean): Boolean {
        if (!isValidName(name)) return false
        return try {
            robot.motionPlay(name, withFace)
            playing = name
            Log.i(TAG, "motionPlay: $name withFace=$withFace")
            true
        } catch (e: Exception) {
            Log.e(TAG, "motionPlay failed: $name", e)
            false
        }
    }

    /**
     * 播放內建動作 (會動！) 並每 intervalMs 記錄 10 顆關節目前角度，用來找「姿勢停住」的時間點。
     * 回傳 [時間ms, m1..m10]；讀取失敗的值為 NaN。
     */
    fun trace(name: String, totalMs: Long, intervalMs: Long = 50L): List<FloatArray> {
        if (!isValidName(name)) return emptyList()
        val rows = ArrayList<FloatArray>()
        play(name, withFace = false)
        val start = System.currentTimeMillis()
        while (true) {
            val t = System.currentTimeMillis() - start
            if (t > totalMs) break
            val row = FloatArray(11)
            row[0] = t.toFloat()
            for (m in 1..10) {
                row[m] = try { robot.getMotorPresentPositionInDegree(m) } catch (e: Exception) { Float.NaN }
            }
            rows.add(row)
            try { Thread.sleep(intervalMs) } catch (_: InterruptedException) { break }
        }
        Log.i(TAG, "trace $name: ${rows.size} samples")
        return rows
    }

    fun stop() {
        try {
            robot.motionStop(true)
            Log.i(TAG, "motionStop")
        } catch (e: Exception) {
            Log.e(TAG, "motionStop failed", e)
        }
        playing = null
    }
}
