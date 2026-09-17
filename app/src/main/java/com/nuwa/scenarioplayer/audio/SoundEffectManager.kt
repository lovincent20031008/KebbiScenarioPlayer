package com.nuwa.scenarioplayer.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import java.io.File

enum class SoundType {
    TADA,           // 尊榮迎賓號角 Ta-Da
    ALARM_BUZZER,   // 空襲警報蜂鳴
    VERY_ALARMED,   // 極度緊急快節奏警報
    DING            // 提示清脆鈴聲
}

class SoundEffectManager(private val context: Context) {

    companion object {
        private const val TAG = "SoundEffectManager"
    }

    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<SoundType, Int>()
    private var isLoaded = false

    init {
        try {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            soundPool = SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(audioAttributes)
                .build()

            loadSystemSounds()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize SoundPool: ${e.message}", e)
        }
    }

    private fun loadSystemSounds() {
        val pool = soundPool ?: return

        val files = mapOf(
            SoundType.TADA to "/system/media/audio/notifications/TaDa.ogg",
            SoundType.ALARM_BUZZER to "/system/media/audio/alarms/Alarm_Buzzer.ogg",
            SoundType.VERY_ALARMED to "/system/media/audio/ringtones/VeryAlarmed.ogg",
            SoundType.DING to "/system/media/audio/ringtones/Ding.ogg"
        )

        files.forEach { (type, path) ->
            if (File(path).exists()) {
                val soundId = pool.load(path, 1)
                soundMap[type] = soundId
                Log.d(TAG, "Loaded sound $type from $path (soundId=$soundId)")
            } else {
                Log.w(TAG, "Sound file not found: $path")
            }
        }
        isLoaded = true
    }

    fun play(type: SoundType, volume: Float = 1.0f) {
        val pool = soundPool ?: return
        val soundId = soundMap[type] ?: return
        try {
            pool.play(soundId, volume, volume, 1, 0, 1.0f)
            Log.i(TAG, "Playing sound effect: $type")
        } catch (e: Exception) {
            Log.e(TAG, "Error playing sound $type: ${e.message}")
        }
    }

    fun stopAll() {
        soundPool?.autoPause()
    }

    fun release() {
        try {
            soundPool?.release()
            soundPool = null
            soundMap.clear()
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing sound pool: ${e.message}")
        }
    }
}
