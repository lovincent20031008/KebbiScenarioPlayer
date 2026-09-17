package com.nuwa.scenarioplayer.model

import com.nuwa.scenarioplayer.audio.SoundType

object ScenarioRepository {

    // 🌟 60秒全劇本極限聯播 (One-Click Mega Demo - 奧特曼・格鬥・拔刀・蜂鳴號角音效全套)
    val megaDemoScenario: Scenario = Scenario(
        id = "mega_demo_60s",
        title = "✨ 60秒極限全場景聯播 (MEGA DEMO)",
        category = ScenarioCategory.BUTLER,
        description = "一鍵啟動 60 秒極限連續技：號角迎賓 ➔ 深鞠躬 ➔ 奧特曼光能引領 ➔ 帝王鼓掌 ➔ 超人飛天 ➔ 空襲蜂鳴急煞 ➔ 猛撲格鬥攔截 ➔ 抱頭慘叫急退 ➔ 大交叉駁斥 ➔ 狂搖頭 ➔ 拔刀斬斷180°自轉 ➔ 帥氣謝幕定格",
        badge = "🔥 全螢幕表情・音效轟炸・1.5x速・超浮誇",
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
            // [03] 120°/s 轉身 + 奧特曼極限光能引導 (超浮誇！)
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
            // [12] 華麗轉身帥氣揮手謝幕 (謝幕)
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

    val allScenarios: List<Scenario> = listOf(
        // ==========================================
        // 🎩 管家模式劇本 (Butler Suite - 1.5倍速・浮誇大動作)
        // ==========================================
        Scenario(
            id = "butler_welcome",
            title = "1. 盛大迎賓巡禮",
            category = ScenarioCategory.BUTLER,
            description = "0.28m/s 疾步趨前 + 90°俐落深鞠躬 + 雙手超大展開",
            badge = "疾風趨前・深鞠躬",
            steps = listOf(
                ScenarioStep(
                    name = "疾風趨前靠攏",
                    chassisMove = ChassisMove(speed = 0.28f, durationMs = 700L),
                    ledPattern = LedPattern.BREATH_GOLD,
                    ttsText = "主人，歡迎回家！",
                    ttsSpeed = "125",
                    timeoutMs = 1800L
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
                    timeoutMs = 3200L
                )
            )
        ),

        Scenario(
            id = "butler_guide",
            title = "2. 空間引領指引",
            category = ScenarioCategory.BUTLER,
            description = "130°/s 猛然轉向 60° + 奧特曼十字光能引路 + 如利劍直線指向目標",
            badge = "奧特曼引導・利劍定點",
            steps = listOf(
                ScenarioStep(
                    name = "奧特曼極限光能引領",
                    motionName = "666_PE_Ultraman",
                    chassisMove = ChassisMove(turnSpeed = 130f, durationMs = 500L),
                    ledPattern = LedPattern.CYAN_FLOW,
                    ttsText = "請往這邊走！",
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
            id = "butler_thinking",
            title = "3. 深度思考檢索",
            category = ScenarioCategory.BUTLER,
            description = "急速搖晃打量 + 仰頭單手托腮沉思 + 直身拍掌頓悟",
            badge = "打量・托腮・頓悟",
            steps = listOf(
                ScenarioStep(
                    name = "搖頭打量與托腮深思",
                    motionName = "888_ML_Thinking_08",
                    chassisMove = ChassisMove(turnSpeed = 80f, durationMs = 300L),
                    ledPattern = LedPattern.PURPLE_PULSE,
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
            id = "butler_celebrate",
            title = "4. 歡慶擊掌祝賀",
            category = ScenarioCategory.BUTLER,
            description = "尊榮 Ta-Da 迎賓號角 + 帝王盛大鼓掌 + 雙臂衝天狂歡呼 + 熱情擊掌",
            badge = "Ta-Da號角・帝王鼓掌",
            steps = listOf(
                ScenarioStep(
                    name = "帝王之尊盛大鼓掌",
                    motionName = "888_ML_Kingclap_20",
                    chassisMove = ChassisMove(turnSpeed = 100f, durationMs = 400L),
                    ledPattern = LedPattern.RAINBOW,
                    soundType = SoundType.TADA,
                    ttsText = "太棒了！耶！",
                    ttsSpeed = "130",
                    timeoutMs = 3200L
                ),
                ScenarioStep(
                    name = "超人變身英姿",
                    motionName = "667_P4_Superman",
                    ledPattern = LedPattern.RAINBOW,
                    timeoutMs = 3500L
                ),
                ScenarioStep(
                    name = "熱情伸手等待擊掌",
                    motionName = "888_ML_HiFive_19",
                    ledPattern = LedPattern.RAINBOW,
                    timeoutMs = 3200L
                )
            )
        ),

        Scenario(
            id = "butler_farewell",
            title = "5. 禮貌告別退場",
            category = ScenarioCategory.BUTLER,
            description = "大力高舉右手狂揮道別 + -0.24m/s 快速倒退滑行讓道",
            badge = "高舉狂揮・快速倒退",
            steps = listOf(
                ScenarioStep(
                    name = "大力高舉狂揮與後滑讓路",
                    motionName = "666_RE_TurnRBye",
                    chassisMove = ChassisMove(speed = -0.24f, durationMs = 600L),
                    ledPattern = LedPattern.SOFT_WHITE,
                    ttsText = "祝您順心！",
                    ttsSpeed = "125",
                    timeoutMs = 3200L
                )
            )
        ),

        // ==========================================
        // 🚨 詐騙警告劇本 (Anti-Fraud Suite - 1.5倍速・大動作格鬥阻攔・音效震撼)
        // ==========================================
        Scenario(
            id = "fraud_physical_block",
            title = "6. 猛撲卡位格鬥攔截",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "0.30m/s 疾衝肉身路障 + 空襲警報蜂鳴 + 雙拳格鬥備戰出拳威嚇",
            badge = "空襲蜂鳴・格鬥備戰",
            steps = listOf(
                ScenarioStep(
                    name = "高速猛撲卡位向前阻截",
                    chassisMove = ChassisMove(speed = 0.30f, durationMs = 700L),
                    ledPattern = LedPattern.RED_STROBE,
                    soundType = SoundType.ALARM_BUZZER,
                    ttsText = "危險！住手！",
                    ttsSpeed = "130",
                    timeoutMs = 1300L
                ),
                ScenarioStep(
                    name = "雙拳格鬥備戰拳擊出拳",
                    motionName = "666_FI_Fight",
                    ledPattern = LedPattern.RED_STROBE,
                    timeoutMs = 3500L
                )
            )
        ),

        Scenario(
            id = "fraud_panic",
            title = "7. 抱頭慘叫絕望急退",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "-0.26m/s 猛烈急退三步 + 雙手抱頭狂喊 NOOO + 恐慌緊迫警報音",
            badge = "抱頭慘叫・恐慌急退",
            steps = listOf(
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
                ScenarioStep(
                    name = "全身劇烈大顫抖",
                    motionName = "888_ML_VeryScared_02",
                    ledPattern = LedPattern.RED_WHITE_ALERT,
                    timeoutMs = 3200L
                )
            )
        ),

        Scenario(
            id = "fraud_accuse",
            title = "8. 嚴厲指責警告",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "0.26m/s 挺身逼近一步 + 大力士霸氣肌肉防禦 + 利劍猛刺直指電話",
            badge = "挺身肌肉・尖刀直指",
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
                    ttsText = "那是假的！騙人的！",
                    ttsSpeed = "130",
                    timeoutMs = 3200L
                )
            )
        ),

        Scenario(
            id = "fraud_reject",
            title = "9. 急迫雙手大交叉駁斥",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "雙臂胸前大幅狂暴交替狂揮 + 伴隨頭部全力劇烈狂搖說不",
            badge = "狂暴交替・瘋狂搖頭",
            steps = listOf(
                ScenarioStep(
                    name = "雙手大交叉狂暴揮動駁斥",
                    motionName = "667_P4_Wong",
                    chassisMove = ChassisMove(turnSpeed = 80f, durationMs = 400L),
                    ledPattern = LedPattern.RED_AMBER_PULSE,
                    ttsText = "不對！絕對不要轉帳！",
                    ttsSpeed = "130",
                    timeoutMs = 4500L
                ),
                ScenarioStep(
                    name = "全力劇烈狂搖頭說不",
                    motionName = "666_BA_Shakehead",
                    ledPattern = LedPattern.RED_AMBER_PULSE,
                    timeoutMs = 2500L
                )
            )
        ),

        Scenario(
            id = "fraud_siren",
            title = "10. 拔刀斬斷與警笛大自轉",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "150°/s 疾速自轉 180° + 俐落抽刀拔劍斬斷詐騙 + 警車雙色爆閃",
            badge = "拔刀斬斷・180°大自轉",
            steps = listOf(
                ScenarioStep(
                    name = "拔劍斬斷詐騙疾速自轉",
                    motionName = "666_FI_Draw",
                    chassisMove = ChassisMove(turnSpeed = 150f, durationMs = 1200L),
                    ledPattern = LedPattern.POLICE_SIREN,
                    ttsText = "立刻掛斷電話！撥打165！",
                    ttsSpeed = "130",
                    timeoutMs = 4000L
                ),
                ScenarioStep(
                    name = "雙手大動作下壓冷靜",
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
