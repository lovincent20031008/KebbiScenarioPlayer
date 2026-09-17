package com.nuwa.scenarioplayer.model

import com.nuwa.scenarioplayer.audio.SoundType

enum class ScenarioCategory {
    BUTLER,
    FRAUD_ALERT
}

enum class LedPattern {
    OFF,
    BREATH_GOLD,        // 暖金呼吸 (管家迎賓)
    CYAN_FLOW,          // 青藍流光 (空間引領)
    PURPLE_PULSE,       // 紫色脈衝 (深度思考)
    RAINBOW,            // 七彩歡慶 (歡慶擊掌)
    SOFT_WHITE,         // 柔和白光 (告別退場)
    RED_STROBE,         // 刺眼紅光爆閃 (肉身阻攔)
    RED_WHITE_ALERT,    // 紅白交替警報 (大驚後撤)
    SOLID_DARK_RED,     // 深血紅恆亮 (嚴厲指責)
    RED_AMBER_PULSE,    // 紅橙急促呼吸 (大交叉駁斥)
    POLICE_SIREN        // 紅藍警笛爆閃 (報警勸阻)
}

data class ChassisMove(
    val speed: Float = 0f,          // m/s (正為前進，負為後退，安全上限 0.32 m/s)
    val durationMs: Long = 0L,      // 移動時長 (ms，安全上限 2500 ms)
    val turnSpeed: Float = 0f       // deg/s (正為順時針，負為逆時針，安全上限 150 deg/s)
)

data class ScenarioStep(
    val name: String,
    val motionName: String? = null,
    val chassisMove: ChassisMove? = null,
    val ledPattern: LedPattern = LedPattern.OFF,
    val soundType: SoundType? = null,
    val ttsText: String? = null,
    val ttsSpeed: String = "100",
    val ttsPitch: String = "100",
    val timeoutMs: Long = 6000L
)

data class Scenario(
    val id: String,
    val title: String,
    val category: ScenarioCategory,
    val description: String,
    val badge: String,
    val steps: List<ScenarioStep>
)
