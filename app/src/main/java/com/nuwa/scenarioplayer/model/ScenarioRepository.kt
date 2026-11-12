package com.nuwa.scenarioplayer.model

import com.nuwa.scenarioplayer.audio.SoundType

/**
 * 集中管理所有多模態情境劇本
 * 完全依據 2021-2024 近年頂級 HRI 期刊與會議論文設計：
 * - ACM THRI (2022): 臉部表情與肢體動作一致性 (Facial-Bodily Congruence)
 * - IEEE RO-MAN (2021/2023): 運動學特徵參數化映射 (Velocity & Spatial Expansion)
 * - IEEE RA-L (2022): 實體位移突進空間侵入與急煞 (Physical Approach & Urgency Signaling)
 * - Frontiers in Robotics and AI (2023/2024): 社交機器人作為第三方強勢干預者 (Active Intervener)
 */
object ScenarioRepository {

    // 🌟 60秒全劇本極限聯播 (One-Click Mega Demo - 奧特曼・格鬥・拔刀・蜂鳴號角音效全套)
    val megaDemoScenario: Scenario = Scenario(
        id = "mega_demo_60s",
        title = "✨ 60秒極限全場景聯播 (MEGA DEMO)",
        category = ScenarioCategory.BUTLER,
        description = "一鍵啟動 60 秒極限連續技：號角迎賓 ➔ 深鞠躬 ➔ 奧特曼光能引領 ➔ 帝王鼓掌 ➔ 超人飛天 ➔ 空襲蜂鳴急煞 ➔ 猛撲格鬥攔截 ➔ 抱頭慘叫急退 ➔ 大交叉駁斥 ➔ 狂搖頭 ➔ 拔刀斬斷180°自轉 ➔ 帥氣謝幕定格",
        badge = "🔥 旗艦整合・12幕高潮連續技",
        affectiveState = "全頻譜情緒跨越 (從親和喜悅到極度恐慌威懾)",
        theoryPaper = "HRI 2021-2024 多模態時序同步統整體系",
        steps = listOf(
            // [01] 疾步趨前迎賓 (前衝 0.28m/s + Ta-Da 尊榮號角)
            ScenarioStep(
                name = "疾風趨前靠攏",
                chassisMove = ChassisMove(speed = 0.28f, durationMs = 700L),
                ledPattern = LedPattern.BREATH_GOLD,
                soundType = SoundType.TADA,
                ttsText = "主人！歡迎回家！",
                ttsSpeed = "125",
                timeoutMs = 1800L
            ),
            // [02] 俐落深鞠躬 (90°深情)
            ScenarioStep(
                name = "90度俐落深鞠躬",
                motionName = "666_RE_Bow",
                ledPattern = LedPattern.BREATH_GOLD,
                timeoutMs = 3800L
            ),
            // [03] 120°/s 轉身 + 奧特曼極限光能引導
            ScenarioStep(
                name = "奧特曼極限光能引導",
                motionName = "666_PE_Ultraman",
                chassisMove = ChassisMove(turnSpeed = 120f, durationMs = 500L),
                ledPattern = LedPattern.CYAN_FLOW,
                ttsText = "請跟我來！",
                ttsSpeed = "125",
                timeoutMs = 3800L
            ),
            // [04] 帝王之尊盛大鼓掌狂歡慶祝
            ScenarioStep(
                name = "帝王盛大鼓掌雀躍歡慶",
                motionName = "888_ML_Kingclap_20",
                chassisMove = ChassisMove(turnSpeed = 90f, durationMs = 400L),
                ledPattern = LedPattern.RAINBOW,
                ttsText = "太棒了！耶！",
                ttsSpeed = "130",
                timeoutMs = 3200L
            ),
            // [05] 超人變身英姿
            ScenarioStep(
                name = "超人飛天極致浮誇英姿",
                motionName = "667_P4_Superman",
                ledPattern = LedPattern.RAINBOW,
                timeoutMs = 3800L
            ),
            // [06] 巨變！突發驚駭警報 (空襲蜂鳴警報！)
            ScenarioStep(
                name = "突發蜂鳴警報驚駭縮身",
                motionName = "666_SA_Shocked",
                soundType = SoundType.ALARM_BUZZER,
                ledPattern = LedPattern.RED_STROBE,
                timeoutMs = 1800L
            ),
            // [07] 0.30m/s 高速猛撲向前突進卡位 + 雙拳格鬥備戰出拳攔阻
            ScenarioStep(
                name = "猛撲卡位雙拳格鬥攔截",
                motionName = "666_FI_Fight",
                chassisMove = ChassisMove(speed = 0.30f, durationMs = 700L),
                ledPattern = LedPattern.RED_STROBE,
                ttsText = "危險！住手！",
                ttsSpeed = "130",
                timeoutMs = 3500L
            ),
            // [08] -0.26m/s 雙手抱頭狂喊 NOOOOO 絕望急退三步
            ScenarioStep(
                name = "抱頭狂喊拒絕絕望急退",
                motionName = "888_ML_Noooo_21",
                chassisMove = ChassisMove(speed = -0.26f, durationMs = 900L),
                ledPattern = LedPattern.RED_WHITE_ALERT,
                soundType = SoundType.VERY_ALARMED,
                ttsText = "天啊！那是詐騙！",
                ttsSpeed = "130",
                timeoutMs = 3800L
            ),
            // [09] 雙臂胸前大交叉狂暴交替揮動
            ScenarioStep(
                name = "雙手大交叉狂暴揮動駁斥",
                motionName = "667_P4_Wong",
                chassisMove = ChassisMove(turnSpeed = 70f, durationMs = 400L),
                ledPattern = LedPattern.RED_AMBER_PULSE,
                ttsText = "絕對不要轉帳！",
                ttsSpeed = "130",
                timeoutMs = 4200L
            ),
            // [10] 瘋狂狂搖頭說不
            ScenarioStep(
                name = "全力劇烈狂搖頭說不",
                motionName = "666_BA_Shakehead",
                ledPattern = LedPattern.RED_AMBER_PULSE,
                timeoutMs = 2500L
            ),
            // [11] 俐落抽刀拔劍斬斷詐騙 + 150°/s 原地疾速大自轉 180° + 警笛狂閃
            ScenarioStep(
                name = "拔劍斬斷詐騙疾速自轉",
                motionName = "666_FI_Draw",
                chassisMove = ChassisMove(turnSpeed = 150f, durationMs = 1200L),
                ledPattern = LedPattern.POLICE_SIREN,
                ttsText = "立刻掛斷電話！撥打165！",
                ttsSpeed = "130",
                timeoutMs = 4000L
            ),
            // [12] 華麗轉身帥氣揮手謝幕
            ScenarioStep(
                name = "側身帥氣揮手完美謝幕",
                motionName = "666_RE_TurnRBye",
                ledPattern = LedPattern.SOFT_WHITE,
                ttsText = "守護您的安全！",
                ttsSpeed = "125",
                timeoutMs = 3500L
            )
        )
    )

    // ==========================================
    // 💬 情緒動作定義 (必須宣告在 allScenarios 之前，避免初始化順序造成 null)
    // ==========================================

    // ---- 可調參數 v3 (2026-11-12 實機回饋) ----
    // 正面：側邊上下快速揮動 (側展關節 M5/M9)
    private const val SIDE_UP = 80f          // 側舉上緣；SDK 上限 100 不碰端點
    private const val SIDE_LOW = 20f         // 揮動下緣，不完全放下讓揮動連續
    private const val SIDE_SPEED = 200f
    private const val SIDE_ELBOW = -30f      // 手肘微彎較自然
    private const val SWING_MS = 600L
    // 中立：雙手說話手勢 (胸前高度、手掌輕擺)，頭微側；不舉高、不低頭、不點頭、不觸碰自己
    private const val TALK_ARM = -90f        // 雙大臂前抬到胸前高度
    private const val TALK_ELBOW = -60f      // 前臂向前
    private const val TALK_ELBOW_BEAT = -30f // 手掌輕擺的另一端
    private const val TALK_OPEN = 20f        // 雙臂微向外 (M9 已測 +20；右手 M5 +20 曾有幅度異常紀錄)
    private const val TALK_SPEED = 90f
    private const val TALK_HEAD = 15f        // 頭左右搖擺幅度 (M2：正=機器人左、負=右；已測 -15~+25)
    private const val HEAD_SWAY_SPEED = 60f  // 30° 擺幅 / 60 ≈ 0.5 秒，配合每拍 BEAT_MS
    private const val BEAT_MS = 500L
    // 負面：低頭 + 垂手 + 收肩 + 難過表情
    private const val SAD_HEAD = 25f         // ⚠️ 超過 SDK 文件 ±20，未經實測；有異音或卡住立即停止
    private const val SAD_SHOULDER = -120f
    private const val SAD_SPEED = 60f
    /**
     * 表情 = 臉部「動畫」名稱 (只換臉，不帶表情包配套動作)；這台凱比的臉部角色是 007。
     * 2026-11-12 實機以 /face/anim 截圖確認：播一次會停住，/face/reset 後恢復預設臉。
     * (表情包 JSON 如 007_P4_RPSTie 會連帶 robotMotion 身體動作，所以不用 JSON 名稱)
     */
    private val SAD_FACE = FaceCue("007_S2_Sad74")      // 難過流淚
    private val HAPPY_FACE = FaceCue("007_J3_Happy61")  // 星星眼開心

    private fun sideArms(deg: Float) = listOf(
        JointTarget(Motor.R_SHOULDER_X, deg, SIDE_SPEED),
        JointTarget(Motor.L_SHOULDER_X, deg, SIDE_SPEED)
    )

    /** 正面：雙臂從側邊對稱快速上下揮動 (Dael C8 / Gao B4) */
    val sentimentPositive: Scenario = Scenario(
        id = "butler_sentiment_positive",
        title = "S1. 正面回覆・雙臂側邊揮動 (Positive)",
        category = ScenarioCategory.BUTLER,
        description = "雙臂由側邊快速上下揮動 4 次後回位，無台詞",
        badge = "雙臂側舉快速揮動",
        affectiveState = "Positive Valence, High Arousal (喜悅)",
        theoryPaper = "Dael 2012 C8 (Elated joy 70%)；Gao 2024 B4 (Happiness 95.45%)",
        face = HAPPY_FACE,
        steps = listOf(
            ScenarioStep(
                name = "雙臂側舉・手肘微彎",
                jointTargets = listOf(
                    JointTarget(Motor.R_SHOULDER_Y, 0f, SIDE_SPEED),
                    JointTarget(Motor.L_SHOULDER_Y, 0f, SIDE_SPEED),
                    JointTarget(Motor.R_ELBOW, SIDE_ELBOW, SIDE_SPEED),
                    JointTarget(Motor.L_ELBOW, SIDE_ELBOW, SIDE_SPEED)
                ) + sideArms(SIDE_UP),
                ledPattern = LedPattern.BREATH_GOLD,
                timeoutMs = 700L
            ),
            ScenarioStep("揮下 1", jointTargets = sideArms(SIDE_LOW), ledPattern = LedPattern.BREATH_GOLD, timeoutMs = SWING_MS),
            ScenarioStep("揮上 2", jointTargets = sideArms(SIDE_UP), ledPattern = LedPattern.BREATH_GOLD, timeoutMs = SWING_MS),
            ScenarioStep("揮下 2", jointTargets = sideArms(SIDE_LOW), ledPattern = LedPattern.BREATH_GOLD, timeoutMs = SWING_MS),
            ScenarioStep("揮上 3", jointTargets = sideArms(SIDE_UP), ledPattern = LedPattern.BREATH_GOLD, timeoutMs = SWING_MS),
            ScenarioStep("揮下 3", jointTargets = sideArms(SIDE_LOW), ledPattern = LedPattern.BREATH_GOLD, timeoutMs = SWING_MS),
            ScenarioStep("揮上 4", jointTargets = sideArms(SIDE_UP), ledPattern = LedPattern.BREATH_GOLD, timeoutMs = SWING_MS),
            ScenarioStep(
                name = "回位",
                jointTargets = sideArms(0f) + listOf(
                    JointTarget(Motor.R_ELBOW, 0f, SIDE_SPEED),
                    JointTarget(Motor.L_ELBOW, 0f, SIDE_SPEED)
                ),
                ledPattern = LedPattern.BREATH_GOLD,
                timeoutMs = 700L
            )
        )
    )

    /** 雙手手掌同步輕擺，頭同時左右搖擺 */
    private fun talkBeat(elbow: Float, head: Float) = listOf(
        JointTarget(Motor.L_ELBOW, elbow, TALK_SPEED),
        JointTarget(Motor.R_ELBOW, elbow, TALK_SPEED),
        JointTarget(Motor.NECK_Z, head, HEAD_SWAY_SPEED)
    )

    /** 中立：說話手勢 (論文無直接證據；避開各情緒群集特徵，最接近 Dael C2 無獨特模式) */
    val sentimentNeutral: Scenario = Scenario(
        id = "butler_sentiment_neutral",
        title = "S2. 中立回覆・說話手勢 (Neutral)",
        category = ScenarioCategory.BUTLER,
        description = "雙手抬到胸前、手掌同步輕擺兩下像在說明，頭同時左右搖擺，然後回位；維持預設臉",
        badge = "胸前說話手勢・頭左右擺",
        affectiveState = "Neutral Valence, Moderate Arousal (一般對話)",
        theoryPaper = "排除法：手在中間高度、頭水平、中速；避開 C8/B4(喜)、C10/B11(悲)、C11 觸碰自己(娛樂)、B7 叉腰(怒)",
        steps = listOf(
            ScenarioStep(
                name = "雙手抬到胸前・頭轉左",
                jointTargets = listOf(
                    JointTarget(Motor.L_SHOULDER_Y, TALK_ARM, TALK_SPEED),
                    JointTarget(Motor.L_ELBOW, TALK_ELBOW, TALK_SPEED),
                    JointTarget(Motor.L_SHOULDER_X, TALK_OPEN, TALK_SPEED),
                    JointTarget(Motor.R_SHOULDER_Y, TALK_ARM, TALK_SPEED),
                    JointTarget(Motor.R_ELBOW, TALK_ELBOW, TALK_SPEED),
                    JointTarget(Motor.R_SHOULDER_X, TALK_OPEN, TALK_SPEED),
                    JointTarget(Motor.NECK_Z, TALK_HEAD, 55f)
                ),
                ledPattern = LedPattern.CYAN_FLOW,
                timeoutMs = 1100L
            ),
            ScenarioStep("手掌擺 1", jointTargets = talkBeat(TALK_ELBOW_BEAT, -TALK_HEAD), ledPattern = LedPattern.CYAN_FLOW, timeoutMs = BEAT_MS),
            ScenarioStep("手掌回 1", jointTargets = talkBeat(TALK_ELBOW, TALK_HEAD), ledPattern = LedPattern.CYAN_FLOW, timeoutMs = BEAT_MS),
            ScenarioStep("手掌擺 2", jointTargets = talkBeat(TALK_ELBOW_BEAT, -TALK_HEAD), ledPattern = LedPattern.CYAN_FLOW, timeoutMs = BEAT_MS),
            ScenarioStep("手掌回 2", jointTargets = talkBeat(TALK_ELBOW, TALK_HEAD), ledPattern = LedPattern.CYAN_FLOW, timeoutMs = BEAT_MS),
            ScenarioStep(
                name = "回位",
                jointTargets = listOf(
                    JointTarget(Motor.L_SHOULDER_Y, 0f, TALK_SPEED),
                    JointTarget(Motor.L_ELBOW, 0f, TALK_SPEED),
                    JointTarget(Motor.L_SHOULDER_X, 0f, TALK_SPEED),
                    JointTarget(Motor.R_SHOULDER_Y, 0f, TALK_SPEED),
                    JointTarget(Motor.R_ELBOW, 0f, TALK_SPEED),
                    JointTarget(Motor.R_SHOULDER_X, 0f, TALK_SPEED),
                    JointTarget(Motor.NECK_Z, 0f, 55f)
                ),
                ledPattern = LedPattern.CYAN_FLOW,
                timeoutMs = 1100L
            )
        )
    )

    /** 負面：低頭 + 雙手下垂 + 雙肩向前併攏 + 難過表情 (Gao B11；低頭替代軀幹前彎) */
    val sentimentNegative: Scenario = Scenario(
        id = "butler_sentiment_negative",
        title = "S3. 負面回覆・低頭垂手收肩 (Negative)",
        category = ScenarioCategory.BUTLER,
        description = "低頭、雙臂下垂、雙肩向前併攏並顯示難過表情，停留後回位；頸部回位有 4~5 秒兩段式延遲",
        badge = "低頭・垂手・收肩・難過臉",
        affectiveState = "Negative Valence, Low Arousal (悲傷／同理)",
        theoryPaper = "Gao 2024 B11 (Sadness 66.67%)；Dael 2012 C10 (Sadness 50%)；低頭為軀幹前彎之替代",
        face = SAD_FACE,
        steps = listOf(
            ScenarioStep(
                name = "低頭・垂手・雙肩前收・難過臉",
                jointTargets = listOf(
                    JointTarget(Motor.NECK_Y, SAD_HEAD, 40f),            // ⚠️ 低頭方向未實測
                    JointTarget(Motor.R_SHOULDER_Y, 0f, SAD_SPEED),
                    JointTarget(Motor.L_SHOULDER_Y, 0f, SAD_SPEED),
                    JointTarget(Motor.R_ELBOW, 0f, SAD_SPEED),
                    JointTarget(Motor.L_ELBOW, 0f, SAD_SPEED),
                    JointTarget(Motor.R_SHOULDER_Z, SAD_SHOULDER, SAD_SPEED),
                    JointTarget(Motor.L_SHOULDER_Z, SAD_SHOULDER, SAD_SPEED)
                ),
                ledPattern = LedPattern.SOFT_WHITE,
                timeoutMs = 2000L
            ),
            ScenarioStep("停留", ledPattern = LedPattern.SOFT_WHITE, timeoutMs = 2000L),
            ScenarioStep(
                name = "回位",
                jointTargets = listOf(
                    JointTarget(Motor.NECK_Y, 0f, 40f),
                    JointTarget(Motor.R_SHOULDER_Z, 0f, SAD_SPEED),
                    JointTarget(Motor.L_SHOULDER_Z, 0f, SAD_SPEED)
                ),
                ledPattern = LedPattern.SOFT_WHITE,
                // 雙肩 120 / 60 = 2 秒回位 (最慢的關節)；原本 7 秒等頸部兩段式回位，
                // 造成身體停了表情還在。改成跟可見動作一致，表情同時恢復。
                timeoutMs = 2200L
            )
        )
    )

    val allScenarios: List<Scenario> = listOf(
        // ==========================================
        // 🎩 管家日常聊天與伴侶劇本 (Butler Companion Suite - 8 套)
        // ==========================================
        Scenario(
            id = "butler_welcome",
            title = "1. 盛大迎賓問候 (Welcoming)",
            category = ScenarioCategory.BUTLER,
            description = "0.28m/s 疾步前趨 + 90°俐落深鞠躬 + 雙臂超大側展",
            badge = "疾風趨前・深情鞠躬",
            affectiveState = "+Valence, Moderate Arousal (親和歡喜)",
            theoryPaper = "IEEE RO-MAN 2021/2023 高空間擴展度",
            steps = listOf(
                ScenarioStep(
                    name = "疾風趨前靠攏",
                    chassisMove = ChassisMove(speed = 0.28f, durationMs = 700L),
                    ledPattern = LedPattern.BREATH_GOLD,
                    soundType = SoundType.TADA,
                    ttsText = "主人，歡迎回家！",
                    ttsSpeed = "125",
                    timeoutMs = 2000L
                ),
                ScenarioStep(
                    name = "俐落深情鞠躬",
                    motionName = "666_RE_Bow",
                    ledPattern = LedPattern.BREATH_GOLD,
                    timeoutMs = 3800L
                ),
                ScenarioStep(
                    name = "雙臂大幅側展引領",
                    motionName = "888_ML_HandsSideUp2_13",
                    ledPattern = LedPattern.BREATH_GOLD,
                    ttsText = "今天一切都順心嗎？",
                    ttsSpeed = "120",
                    timeoutMs = 3200L
                )
            )
        ),

        Scenario(
            id = "butler_listening",
            title = "2. 專注傾聽共感 (Active Listening)",
            category = ScenarioCategory.BUTLER,
            description = "身體微前傾側耳傾聽 + 慢速點頭共感 (低喚起舒緩)",
            badge = "微傾側耳・頻率點頭",
            affectiveState = "Moderate Valence, Low Arousal (平靜包容)",
            theoryPaper = "ACM THRI 2022 眼神頭部動態共感",
            steps = listOf(
                ScenarioStep(
                    name = "身體微前傾側耳傾聽",
                    motionName = "888_ML_Thinking_08",
                    ledPattern = LedPattern.CYAN_FLOW,
                    ttsText = "我在聽，您慢慢說～",
                    ttsSpeed = "110",
                    timeoutMs = 3800L
                ),
                ScenarioStep(
                    name = "認真點頭肯定共感",
                    motionName = "666_PE_Good",
                    ledPattern = LedPattern.CYAN_FLOW,
                    ttsText = "我完全明白您的心情。",
                    ttsSpeed = "115",
                    timeoutMs = 3200L
                )
            )
        ),

        Scenario(
            id = "butler_thinking",
            title = "3. 沉思檢索運算 (Cognitive Search)",
            category = ScenarioCategory.BUTLER,
            description = "急速搖晃打量 + 仰頭單手托腮沉思 + 直身拍掌頓悟",
            badge = "打量・托腮・頓悟",
            affectiveState = "Neutral Valence, Low Arousal (專注計算)",
            theoryPaper = "ACM THRI 2022 認知運算外顯化",
            steps = listOf(
                ScenarioStep(
                    name = "搖頭打量與托腮深思",
                    motionName = "888_ML_Thinking_08",
                    chassisMove = ChassisMove(turnSpeed = 80f, durationMs = 300L),
                    ledPattern = LedPattern.PURPLE_PULSE,
                    ttsText = "讓我想想，正在為您查詢！",
                    ttsSpeed = "120",
                    timeoutMs = 4000L
                ),
                ScenarioStep(
                    name = "拍掌頓悟出結果",
                    motionName = "888_ML_ThinkingOh_13",
                    ledPattern = LedPattern.CYAN_FLOW,
                    ttsText = "找到了！請過目！",
                    ttsSpeed = "125",
                    timeoutMs = 2800L
                )
            )
        ),

        Scenario(
            id = "butler_praise",
            title = "4. 正面肯定誇讚 (Praise & Affirmation)",
            category = ScenarioCategory.BUTLER,
            description = "拍手大拇指筆直向前 + 輕快微跳躍動 (垂直向上加速度)",
            badge = "大拇指直指・躍動讚好",
            affectiveState = "High Valence, High Arousal (強烈認同)",
            theoryPaper = "IEEE RO-MAN 2021/2023 向上加速度",
            steps = listOf(
                ScenarioStep(
                    name = "大拇指筆直向前點頭誇讚",
                    motionName = "666_PE_Good",
                    ledPattern = LedPattern.RAINBOW,
                    ttsText = "哇！您太厲害了，真是好棒！",
                    ttsSpeed = "125",
                    timeoutMs = 3500L
                ),
                ScenarioStep(
                    name = "熱情伸手肯定鼓勵",
                    motionName = "888_ML_HiFive_19",
                    ledPattern = LedPattern.RAINBOW,
                    timeoutMs = 3200L
                )
            )
        ),

        Scenario(
            id = "butler_celebrate",
            title = "5. 歡慶盛大祝賀 (Celebration)",
            category = ScenarioCategory.BUTLER,
            description = "尊榮 Ta-Da 迎賓號角 + 帝王盛大鼓掌 + 雙臂衝天狂歡呼",
            badge = "Ta-Da號角・帝王鼓掌",
            affectiveState = "++Valence, ++Arousal (狂喜爆發)",
            theoryPaper = "IEEE RA-L 2022 上揚掌聲正向反饋",
            steps = listOf(
                ScenarioStep(
                    name = "帝王之尊盛大鼓掌",
                    motionName = "888_ML_Kingclap_20",
                    chassisMove = ChassisMove(turnSpeed = 100f, durationMs = 400L),
                    ledPattern = LedPattern.RAINBOW,
                    soundType = SoundType.TADA,
                    ttsText = "太棒了！恭喜主人！",
                    ttsSpeed = "130",
                    timeoutMs = 3200L
                ),
                ScenarioStep(
                    name = "超人變身英姿狂歡呼",
                    motionName = "667_P4_Superman",
                    ledPattern = LedPattern.RAINBOW,
                    ttsText = "真是值得慶祝的一刻！",
                    ttsSpeed = "125",
                    timeoutMs = 3500L
                )
            )
        ),

        Scenario(
            id = "butler_apology",
            title = "6. 委婉致歉自省 (Apology & De-escalation)",
            category = ScenarioCategory.BUTLER,
            description = "後退半步給予安全感 + 雙手下壓低頭誠懇道歉鞠躬",
            badge = "主動後退・低頭合十",
            affectiveState = "Negative Valence, Low Arousal (自省謙和)",
            theoryPaper = "ACM THRI 2022 負向後撤主動降壓",
            steps = listOf(
                ScenarioStep(
                    name = "後退半步謙遜低頭",
                    motionName = "666_RE_Bow",
                    chassisMove = ChassisMove(speed = -0.15f, durationMs = 400L),
                    ledPattern = LedPattern.SOFT_WHITE,
                    ttsText = "真的很抱歉，是我不夠貼心！",
                    ttsSpeed = "115",
                    timeoutMs = 3500L
                ),
                ScenarioStep(
                    name = "雙手下壓誠懇受教",
                    motionName = "667_MG_HandsUp",
                    ledPattern = LedPattern.SOFT_WHITE,
                    ttsText = "我會立刻改進！",
                    ttsSpeed = "115",
                    timeoutMs = 3200L
                )
            )
        ),

        Scenario(
            id = "butler_guide",
            title = "7. 空間引領指引 (Guiding & Recommending)",
            category = ScenarioCategory.BUTLER,
            description = "130°/s 轉向 60° + 奧特曼十字光能引路 + 利劍直線指向",
            badge = "130°/s急轉・奧特曼光能",
            affectiveState = "+Valence, Moderate Arousal (熱情引導)",
            theoryPaper = "IEEE RO-MAN 2021/2023 直線聚焦空間引導",
            steps = listOf(
                ScenarioStep(
                    name = "奧特曼極限光能引領",
                    motionName = "666_PE_Ultraman",
                    chassisMove = ChassisMove(turnSpeed = 130f, durationMs = 500L),
                    ledPattern = LedPattern.CYAN_FLOW,
                    ttsText = "請跟我往這邊走！",
                    ttsSpeed = "125",
                    timeoutMs = 3800L
                ),
                ScenarioStep(
                    name = "利劍般直線指向目標",
                    motionName = "888_ML_RPointLisa_24",
                    ledPattern = LedPattern.CYAN_FLOW,
                    timeoutMs = 3200L
                )
            )
        ),

        Scenario(
            id = "butler_farewell",
            title = "8. 溫馨告別待命 (Farewell & Standby)",
            category = ScenarioCategory.BUTLER,
            description = "大力高舉右手狂揮道別 + -0.24m/s 快速倒退滑行讓道歸位",
            badge = "狂揮道別・快速後滑",
            affectiveState = "Sustained Valence, Low Arousal (溫馨敬重)",
            theoryPaper = "IEEE RO-MAN 2021/2023 柔和減速平靜交接",
            steps = listOf(
                ScenarioStep(
                    name = "大力高舉狂揮與後滑讓路",
                    motionName = "666_RE_TurnRBye",
                    chassisMove = ChassisMove(speed = -0.24f, durationMs = 600L),
                    ledPattern = LedPattern.SOFT_WHITE,
                    ttsText = "主人好好休息，隨時叫我，拜拜！",
                    ttsSpeed = "120",
                    timeoutMs = 3500L
                )
            )
        ),

        // ==========================================
        // 💬 管家回覆情緒動作 (Sentiment Suite - 3 套)
        // 對應 SafeCall 後端 x-sentiment-label: positive / neutral / negative
        // 設計依據：Dael et al. (2012) Emotion 12(5); Gao et al. (2024) Biomimetics 9(11)
        // 限制：無台詞、無音效、無底盤 (管家語音由 SafeCall 後端 MP3 播放)；
        //       關節角度僅使用 JointSafety 白名單內的已測區間
        // ==========================================
        sentimentPositive,
        sentimentNeutral,
        sentimentNegative,

        // ==========================================
        // 🚨 詐騙提醒與多層級實體阻斷劇本 (Anti-Fraud Suite - 8 套)
        // ==========================================
        Scenario(
            id = "fraud_anomaly",
            title = "9. 察覺微小異常 (Subtle Skepticism)",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "悄悄挪近半步 + 頭部微歪側聽打量通話起疑 (低度戒備)",
            badge = "挪近半步・歪頭側聽",
            affectiveState = "Neutral-Negative Valence, Low Arousal (謹慎起疑)",
            theoryPaper = "ACM THRI 2022 傾身偏側探究體態",
            steps = listOf(
                ScenarioStep(
                    name = "悄悄挪近歪頭打量通話",
                    motionName = "888_ML_Thinking_08",
                    chassisMove = ChassisMove(speed = 0.12f, durationMs = 400L),
                    ledPattern = LedPattern.BREATH_GOLD,
                    ttsText = "咦？這通電話說話的語氣怪怪的喔...",
                    ttsSpeed = "120",
                    timeoutMs = 3800L
                )
            )
        ),

        Scenario(
            id = "fraud_unknown_caller",
            title = "10. 未知來電警示 (Unknown Caller Caution)",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "原地挺身 + 單臂前推暫停手勢 + 警示微搖頭提醒勿輕信",
            badge = "挺身暫停・警示搖頭",
            affectiveState = "Negative Valence, Moderate Arousal (防備警惕)",
            theoryPaper = "IEEE RO-MAN 2021/2023 正面對峙暫停手勢",
            steps = listOf(
                ScenarioStep(
                    name = "挺身抬手暫停手勢",
                    motionName = "888_ML_RPointLisa_24",
                    ledPattern = LedPattern.RED_AMBER_PULSE,
                    ttsText = "提醒您！這是未顯示號碼的陌生來電，請小心！",
                    ttsSpeed = "125",
                    timeoutMs = 3500L
                ),
                ScenarioStep(
                    name = "謹慎微搖頭警戒",
                    motionName = "666_BA_Shakehead",
                    ledPattern = LedPattern.RED_AMBER_PULSE,
                    timeoutMs = 2500L
                )
            )
        ),

        Scenario(
            id = "fraud_accuse",
            title = "11. 敏感金錢關鍵字 (Financial Keyword Alert)",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "0.26m/s 挺身逼近 + 大力士霸氣肌肉防禦 + 利劍直指電話",
            badge = "挺身肌肉・尖刀直指",
            affectiveState = "Negative Valence, High Arousal (嚴肅指責)",
            theoryPaper = "IEEE RO-MAN 2021/2023 直線強硬指責",
            steps = listOf(
                ScenarioStep(
                    name = "挺身逼近大力士肌肉挺胸",
                    motionName = "666_PE_Hercules",
                    chassisMove = ChassisMove(speed = 0.26f, durationMs = 400L),
                    ledPattern = LedPattern.SOLID_DARK_RED,
                    timeoutMs = 2800L
                ),
                ScenarioStep(
                    name = "如利劍尖刀猛刺直指電話",
                    motionName = "888_ML_AngPoint_12",
                    ledPattern = LedPattern.SOLID_DARK_RED,
                    ttsText = "對方提到ATM轉帳與監管！那是詐騙！",
                    ttsSpeed = "130",
                    timeoutMs = 3500L
                )
            )
        ),

        Scenario(
            id = "fraud_counter_manipulation",
            title = "12. 話術情緒勒索防護 (Manipulation Counter)",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "挺胸腰側雙拳戰鬥架勢 + 大力士肌肉護盾 + 堅定阻擋視線護主",
            badge = "雙拳護主・擺身擋視線",
            affectiveState = "Protective Dominance, High Arousal (守護戰鬥)",
            theoryPaper = "Frontiers 2023/2024 第三方強勢保護介入",
            steps = listOf(
                ScenarioStep(
                    name = "挺身戰鬥防護架勢",
                    motionName = "666_FI_Fight",
                    ledPattern = LedPattern.SOLID_DARK_RED,
                    ttsText = "別怕！他在故意威脅嚇您！檢察官絕不會叫您交存摺！",
                    ttsSpeed = "130",
                    timeoutMs = 3800L
                ),
                ScenarioStep(
                    name = "大力士霸氣肌肉護盾",
                    motionName = "666_PE_Hercules",
                    ledPattern = LedPattern.SOLID_DARK_RED,
                    timeoutMs = 3000L
                )
            )
        ),

        Scenario(
            id = "fraud_block_phishing",
            title = "13. 阻擋點擊釣魚簡訊 (Phishing Interruption)",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "0.28m/s 疾衝螢幕前方 + 雙手狂推下壓止步 + 全力狂搖頭",
            badge = "0.28m/s疾衝・狂推止步",
            affectiveState = "High Urgency, High Arousal (突發打斷)",
            theoryPaper = "IEEE RA-L 2022 突進急煞強行打斷",
            steps = listOf(
                ScenarioStep(
                    name = "疾速衝向長輩手指前方阻截",
                    motionName = "667_MG_HandsUp",
                    chassisMove = ChassisMove(speed = 0.28f, durationMs = 600L),
                    ledPattern = LedPattern.RED_STROBE,
                    ttsText = "手停住！千萬不要點開簡訊連結！那是詐騙！",
                    ttsSpeed = "130",
                    timeoutMs = 3500L
                ),
                ScenarioStep(
                    name = "全力狂搖頭說不能點",
                    motionName = "666_BA_Shakehead",
                    ledPattern = LedPattern.RED_STROBE,
                    timeoutMs = 2500L
                )
            )
        ),

        Scenario(
            id = "fraud_panic",
            title = "14. 恐慌急退慘叫阻斷 (Despair Panic - NOOO)",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "-0.26m/s 猛烈急退三步 + 雙手抱頭狂喊 NOOO + 恐慌緊迫警報音",
            badge = "抱頭慘叫・恐慌急退",
            affectiveState = "Extreme Negative Valence, High Arousal (極度恐慌共鳴)",
            theoryPaper = "ACM THRI 2022 肢體面部一致性恐懼放大",
            steps = listOf(
                ScenarioStep(
                    name = "抱頭狂喊拒絕絕望急退",
                    motionName = "888_ML_Noooo_21",
                    chassisMove = ChassisMove(speed = -0.26f, durationMs = 900L),
                    ledPattern = LedPattern.RED_WHITE_ALERT,
                    soundType = SoundType.VERY_ALARMED,
                    ttsText = "天啊！NOOOOO！絕對不要把密碼告訴他！",
                    ttsSpeed = "130",
                    timeoutMs = 3800L
                ),
                ScenarioStep(
                    name = "全身劇烈大顫抖驚恐定格",
                    motionName = "888_ML_VeryScared_02",
                    ledPattern = LedPattern.RED_WHITE_ALERT,
                    timeoutMs = 3200L
                )
            )
        ),

        Scenario(
            id = "fraud_physical_block",
            title = "15. 假冒公檢法強制卡位 (Physical Combat Block)",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "0.30m/s 疾衝肉身路障 + 空襲警報蜂鳴 + 雙拳格鬥出拳威嚇",
            badge = "空襲蜂鳴・格鬥卡位",
            affectiveState = "Max Threat Response, Max Arousal (極限威脅干預)",
            theoryPaper = "IEEE RA-L 2022 物理空間侵入瓦解盲區",
            steps = listOf(
                ScenarioStep(
                    name = "高速猛撲卡位向前阻截",
                    motionName = "666_SA_Shocked",
                    chassisMove = ChassisMove(speed = 0.30f, durationMs = 700L),
                    ledPattern = LedPattern.RED_STROBE,
                    soundType = SoundType.ALARM_BUZZER,
                    ttsText = "危險！這是百分之百假冒檢察官！",
                    ttsSpeed = "130",
                    timeoutMs = 2000L
                ),
                ScenarioStep(
                    name = "雙拳格鬥備戰拳擊出拳威嚇",
                    motionName = "666_FI_Fight",
                    ledPattern = LedPattern.RED_STROBE,
                    ttsText = "立刻掛電話！",
                    ttsSpeed = "130",
                    timeoutMs = 3500L
                )
            )
        ),

        Scenario(
            id = "fraud_siren",
            title = "16. 拔刀斬斷與165通報 (Definitive Disruption 180° Spin)",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "150°/s 疾速自轉 180° + 俐落抽刀拔劍斬斷詐騙 + 警車雙色爆閃",
            badge = "拔刀斬斷・180°大自轉",
            affectiveState = "Decisive Resolution, High Dominance (終局果斷制裁)",
            theoryPaper = "Frontiers 2023/2024 第三方強勢介入提升掛電話率",
            steps = listOf(
                ScenarioStep(
                    name = "拔劍斬斷詐騙疾速自轉",
                    motionName = "666_FI_Draw",
                    chassisMove = ChassisMove(turnSpeed = 150f, durationMs = 1200L),
                    ledPattern = LedPattern.POLICE_SIREN,
                    ttsText = "立刻掛斷電話！撥打165反詐專線！",
                    ttsSpeed = "130",
                    timeoutMs = 4000L
                ),
                ScenarioStep(
                    name = "雙手大動作下壓定格謝幕",
                    motionName = "667_MG_HandsUp",
                    ledPattern = LedPattern.POLICE_SIREN,
                    timeoutMs = 3200L
                )
            )
        )
    )

    fun findById(id: String): Scenario? {
        if (id.equals(megaDemoScenario.id, ignoreCase = true) || id.equals("demo", ignoreCase = true)) {
            return megaDemoScenario
        }
        return allScenarios.find { it.id.equals(id, ignoreCase = true) }
    }
}
