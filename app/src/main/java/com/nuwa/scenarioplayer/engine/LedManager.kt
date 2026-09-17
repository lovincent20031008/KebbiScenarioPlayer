package com.nuwa.scenarioplayer.engine

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.nuwarobotics.service.agent.NuwaRobotAPI
import com.nuwa.scenarioplayer.model.LedPattern

class LedManager(
    private val robot: NuwaRobotAPI,
    private val isLedEnabled: () -> Boolean
) {

    companion object {
        private const val TAG = "LedManager"

        private const val LED_ID_HEAD = 1
        private const val LED_ID_CHEST = 2
        private const val LED_ID_RIGHT_HAND = 3
        private const val LED_ID_LEFT_HAND = 4
    }

    private val mainHandler = Handler(Looper.getMainLooper())
    private var currentLoopRunnable: Runnable? = null
    private var tick = 0

    @Synchronized
    fun applyPattern(pattern: LedPattern) {
        stopLoop()

        if (!isLedEnabled()) {
            return
        }

        when (pattern) {
            LedPattern.OFF -> reset()

            LedPattern.BREATH_GOLD -> {
                // 暖金色 (255, 180, 20)
                setAllLeds(255, 180, 20)
            }

            LedPattern.CYAN_FLOW -> {
                // 青藍光 (0, 220, 255)
                setAllLeds(0, 220, 255)
            }

            LedPattern.PURPLE_PULSE -> {
                // 紫色 (180, 30, 255)
                setAllLeds(180, 30, 255)
            }

            LedPattern.RAINBOW -> {
                // 七彩輪播定時器
                val colors = listOf(
                    Triple(255, 0, 0),
                    Triple(255, 127, 0),
                    Triple(255, 255, 0),
                    Triple(0, 255, 0),
                    Triple(0, 150, 255),
                    Triple(160, 32, 240)
                )
                tick = 0
                val runnable = object : Runnable {
                    override fun run() {
                        val c = colors[tick % colors.size]
                        setAllLeds(c.first, c.second, c.third)
                        tick++
                        mainHandler.postDelayed(this, 250)
                    }
                }
                currentLoopRunnable = runnable
                mainHandler.post(runnable)
            }

            LedPattern.SOFT_WHITE -> {
                setAllLeds(200, 200, 200)
            }

            LedPattern.RED_STROBE -> {
                // 紅光急促爆閃 (每 150ms)
                tick = 0
                val runnable = object : Runnable {
                    override fun run() {
                        if (tick % 2 == 0) {
                            setAllLeds(255, 0, 0)
                        } else {
                            setAllLeds(0, 0, 0)
                        }
                        tick++
                        mainHandler.postDelayed(this, 150)
                    }
                }
                currentLoopRunnable = runnable
                mainHandler.post(runnable)
            }

            LedPattern.RED_WHITE_ALERT -> {
                // 紅白交替
                tick = 0
                val runnable = object : Runnable {
                    override fun run() {
                        if (tick % 2 == 0) {
                            setAllLeds(255, 0, 0)
                        } else {
                            setAllLeds(255, 255, 255)
                        }
                        tick++
                        mainHandler.postDelayed(this, 220)
                    }
                }
                currentLoopRunnable = runnable
                mainHandler.post(runnable)
            }

            LedPattern.SOLID_DARK_RED -> {
                setAllLeds(255, 0, 0)
            }

            LedPattern.RED_AMBER_PULSE -> {
                // 紅/橙交替
                tick = 0
                val runnable = object : Runnable {
                    override fun run() {
                        if (tick % 2 == 0) {
                            setAllLeds(255, 0, 0)
                        } else {
                            setAllLeds(255, 80, 0)
                        }
                        tick++
                        mainHandler.postDelayed(this, 300)
                    }
                }
                currentLoopRunnable = runnable
                mainHandler.post(runnable)
            }

            LedPattern.POLICE_SIREN -> {
                // 紅藍交替警笛
                tick = 0
                val runnable = object : Runnable {
                    override fun run() {
                        if (tick % 2 == 0) {
                            // 左紅右藍
                            setIndividualLed(LED_ID_HEAD, 255, 0, 0)
                            setIndividualLed(LED_ID_CHEST, 255, 0, 0)
                            setIndividualLed(LED_ID_LEFT_HAND, 255, 0, 0)
                            setIndividualLed(LED_ID_RIGHT_HAND, 0, 0, 255)
                        } else {
                            // 左藍右紅
                            setIndividualLed(LED_ID_HEAD, 0, 0, 255)
                            setIndividualLed(LED_ID_CHEST, 0, 0, 255)
                            setIndividualLed(LED_ID_LEFT_HAND, 0, 0, 255)
                            setIndividualLed(LED_ID_RIGHT_HAND, 255, 0, 0)
                        }
                        tick++
                        mainHandler.postDelayed(this, 180)
                    }
                }
                currentLoopRunnable = runnable
                mainHandler.post(runnable)
            }
        }
    }

    @Synchronized
    fun reset() {
        stopLoop()
        try {
            robot.setLedColor(LED_ID_HEAD, 0, 0, 0, 0)
            robot.setLedColor(LED_ID_CHEST, 0, 0, 0, 0)
            robot.setLedColor(LED_ID_RIGHT_HAND, 0, 0, 0, 0)
            robot.setLedColor(LED_ID_LEFT_HAND, 0, 0, 0, 0)
            robot.enableSystemLED()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to reset LEDs", e)
        }
    }

    private fun stopLoop() {
        currentLoopRunnable?.let {
            mainHandler.removeCallbacks(it)
            currentLoopRunnable = null
        }
    }

    private fun setAllLeds(r: Int, g: Int, b: Int) {
        try {
            robot.disableSystemLED()
            robot.setLedColor(LED_ID_HEAD, 255, r, g, b)
            robot.setLedColor(LED_ID_CHEST, 255, r, g, b)
            robot.setLedColor(LED_ID_RIGHT_HAND, 255, r, g, b)
            robot.setLedColor(LED_ID_LEFT_HAND, 255, r, g, b)
        } catch (e: Exception) {
            Log.e(TAG, "setAllLeds failed", e)
        }
    }

    private fun setIndividualLed(id: Int, r: Int, g: Int, b: Int) {
        try {
            robot.disableSystemLED()
            robot.setLedColor(id, 255, r, g, b)
        } catch (e: Exception) {
            Log.e(TAG, "setIndividualLed failed", e)
        }
    }
}
