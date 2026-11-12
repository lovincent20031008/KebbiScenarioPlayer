package com.nuwa.scenarioplayer.model

import com.nuwa.scenarioplayer.audio.SoundType

enum class ScenarioCategory {
    BUTLER,
    FRAUD_ALERT
}

/** 情緒動作用的關節編號 (對照 MOTOR_MAPPING.md) */
object Motor {
    const val NECK_Y = 1
    const val NECK_Z = 2
    const val R_SHOULDER_Z = 3
    const val R_SHOULDER_Y = 4
    const val R_SHOULDER_X = 5
    const val R_ELBOW = 6
    const val L_SHOULDER_Z = 7
    const val L_SHOULDER_Y = 8
    const val L_SHOULDER_X = 9
    const val L_ELBOW = 10
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

/**
 * 單一關節目標角度 (透過 NuwaRobotAPI.ctlMotor 直接控制)
 * motorId 對照 KebbiRemoteControl/docs/MOTOR_MAPPING.md：
 *  1 頸部俯仰(負抬正低)  2 頸部左右(正左負右)
 *  3/7 右/左肩前後(負=向前)  4/8 右/左大臂抬舉(負=前抬)
 *  5/9 右/左大臂側展(正=向外)  6/10 右/左手肘(負=屈曲)
 * 所有數值必須通過 JointSafety 白名單檢查才會被送出。
 */
data class JointTarget(
    val motorId: Int,
    val degree: Float,
    val speed: Float = JointSafety.DEFAULT_SPEED
)

/**
 * 關節安全白名單 (v2, 2026-11-12)
 * 來源：KebbiRemoteControl B 實驗版 (commit d8b7e3e 之前) 曾在實機執行的
 * safe / caution 模式數值；不採用 warning 模式的極端值 (-420、速度 300+)。
 * M3.3 實測顯示指令 -25 只有目視約 20~25 度，-45 時更小，SDK 單位不是物理角度，
 * 因此 v1 只用 M3.3 區間時幅度明顯不足。
 * 這不是機械極限；超出範圍的指令一律拒絕，不做截斷。
 */
object JointSafety {
    const val DEFAULT_SPEED = 90f   // B 版 safe 模式手肘速度
    const val MAX_SPEED = 200f      // v3：正面快速揮手需要；B 版 warning 曾用 220~340，此值仍低於它

    /** motorId -> 允許的 [min, max] 指令值 */
    val testedRanges: Map<Int, ClosedFloatingPointRange<Float>> = mapOf(
        // Motor 1：B 版未使用；SDK 文件 ±20，不碰端點。+10 以上 (低頭) 未經實測，首次播放請在旁觀察。
        1 to -10f..25f,   // v4：+25 超過 SDK 文件 ±20 (頭部左右實機範圍也曾超過 SDK 文件)；+10 以上皆未實測
        2 to -40f..40f,   // B 版 caution ±40 (交接文件：實機約 -100~55)
        3 to -120f..0f,   // B 版 caution ±120；只開放向前 (M3.3：負=向前)
        4 to -180f..0f,   // B 版 caution -180 (交接文件：右臂實機約 -420~120)
        5 to 0f..80f,     // v3 側展；SDK 上限 100 不碰端點。M3.3 右手 +20 有幅度異常紀錄
        6 to -120f..0f,   // B 版 safe/caution -120
        7 to -120f..0f,
        8 to -180f..0f,
        9 to 0f..80f,     // v3 側展；M3.3 只測到 +35
        10 to -120f..0f
    )

    fun isAllowed(target: JointTarget): Boolean {
        val range = testedRanges[target.motorId] ?: return false
        return target.degree in range && target.speed > 0f && target.speed <= MAX_SPEED
    }
}

data class ScenarioStep(
    val name: String,
    val motionName: String? = null,
    val jointTargets: List<JointTarget> = emptyList(),
    val chassisMove: ChassisMove? = null,
    val ledPattern: LedPattern = LedPattern.OFF,
    val soundType: SoundType? = null,
    val ttsText: String? = null,
    val ttsSpeed: String = "100",
    val ttsPitch: String = "100",
    val timeoutMs: Long = 6000L
)

/**
 * 整個劇本期間持續顯示的表情 (只換臉，不帶表情包的配套身體動作)。
 * name 是臉部「動畫」名稱 (例如 007_S2_Sad74)，透過 UnityFaceManager.playFaceAnimation 播放；
 * 實機確認播一次就會停在該表情，劇本結束或停止時引擎才恢復預設臉，
 * 因此表情維持時間 = 動作持續時間。
 */
data class FaceCue(val name: String)

data class Scenario(
    val id: String,
    val title: String,
    val category: ScenarioCategory,
    val description: String,
    val badge: String,
    val affectiveState: String = "",
    val theoryPaper: String = "",
    val face: FaceCue? = null,
    val steps: List<ScenarioStep>
)
