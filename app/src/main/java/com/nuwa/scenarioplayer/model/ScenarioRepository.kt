package com.nuwa.scenarioplayer.model

object ScenarioRepository {

    val allScenarios: List<Scenario> = listOf(
        // ==========================================
        // 🎩 管家模式劇本 (Butler Suite - 高動態・極簡短語)
        // ==========================================
        Scenario(
            id = "butler_welcome",
            title = "1. 盛大迎賓巡禮",
            category = ScenarioCategory.BUTLER,
            description = "0.22m/s 快步滑行靠近 + 90°俐落深鞠躬 + 雙臂大幅側展引導",
            badge = "迎賓・前進・鞠躬",
            steps = listOf(
                ScenarioStep(
                    name = "疾步趨前靠攏",
                    chassisMove = ChassisMove(speed = 0.22f, durationMs = 800L), // 前進約 18cm
                    ledPattern = LedPattern.BREATH_GOLD,
                    ttsText = "主人，歡迎回家！",
                    ttsSpeed = "115",
                    timeoutMs = 1800L
                ),
                ScenarioStep(
                    name = "俐落深情鞠躬",
                    motionName = "666_RE_Bow",
                    ledPattern = LedPattern.BREATH_GOLD,
                    timeoutMs = 4000L
                ),
                ScenarioStep(
                    name = "大幅側身展開引領",
                    motionName = "888_ML_HandsSideUp2_13",
                    ledPattern = LedPattern.BREATH_GOLD,
                    timeoutMs = 3500L
                )
            )
        ),

        Scenario(
            id = "butler_guide",
            title = "2. 空間引領指引",
            category = ScenarioCategory.BUTLER,
            description = "100°/s 甩頭快轉 60° + 雙臂猛然橫向全開 + 如利劍直線定點指向",
            badge = "急轉・全開・定點",
            steps = listOf(
                ScenarioStep(
                    name = "疾速轉向與雙臂大展開",
                    motionName = "888_ML_HandsSideUp2_13",
                    chassisMove = ChassisMove(turnSpeed = 100f, durationMs = 600L), // 疾轉 60度
                    ledPattern = LedPattern.CYAN_FLOW,
                    ttsText = "請往這邊走！",
                    ttsSpeed = "120",
                    timeoutMs = 3500L
                ),
                ScenarioStep(
                    name = "利劍般直線指向目標",
                    motionName = "888_ML_RPointLisa_24",
                    ledPattern = LedPattern.CYAN_FLOW,
                    timeoutMs = 3500L
                )
            )
        ),

        Scenario(
            id = "butler_thinking",
            title = "3. 深度思考檢索",
            category = ScenarioCategory.BUTLER,
            description = "快速左右搖動打量 + 仰頭單手托腮沉思 + 直身拍掌頓悟",
            badge = "打量・托腮・頓悟",
            steps = listOf(
                ScenarioStep(
                    name = "搖頭打量與托腮深思",
                    motionName = "888_ML_Thinking_08",
                    chassisMove = ChassisMove(turnSpeed = 60f, durationMs = 400L),
                    ledPattern = LedPattern.PURPLE_PULSE,
                    timeoutMs = 4500L
                ),
                ScenarioStep(
                    name = "拍掌頓悟出結果",
                    motionName = "888_ML_ThinkingOh_13",
                    ledPattern = LedPattern.CYAN_FLOW,
                    ttsText = "找到了！請過目！",
                    ttsSpeed = "120",
                    timeoutMs = 3000L
                )
            )
        ),

        Scenario(
            id = "butler_celebrate",
            title = "4. 歡慶擊掌祝賀",
            category = ScenarioCategory.BUTLER,
            description = "原地雀躍小碎步急轉 + 雙臂直衝雲霄狂歡呼 + 熱情向前擊掌",
            badge = "衝天狂呼・彩虹・擊掌",
            steps = listOf(
                ScenarioStep(
                    name = "雙臂衝天雀躍狂歡",
                    motionName = "888_ML_BothUp_00",
                    chassisMove = ChassisMove(turnSpeed = 80f, durationMs = 500L),
                    ledPattern = LedPattern.RAINBOW,
                    ttsText = "太棒了！耶！",
                    ttsSpeed = "125",
                    timeoutMs = 3000L
                ),
                ScenarioStep(
                    name = "熱情伸手等待擊掌",
                    motionName = "888_ML_HiFive_19",
                    ledPattern = LedPattern.RAINBOW,
                    timeoutMs = 3500L
                )
            )
        ),

        Scenario(
            id = "butler_farewell",
            title = "5. 禮貌告別退場",
            category = ScenarioCategory.BUTLER,
            description = "大力高舉右手狂揮道別 + -0.20m/s 快速倒退滑行讓道",
            badge = "狂揮・後退・道別",
            steps = listOf(
                ScenarioStep(
                    name = "大力高舉狂揮與後滑讓路",
                    motionName = "888_ML_Crazywavehi_03",
                    chassisMove = ChassisMove(speed = -0.20f, durationMs = 1000L), // 後退約 20cm
                    ledPattern = LedPattern.SOFT_WHITE,
                    ttsText = "祝您順心，再見！",
                    ttsSpeed = "115",
                    timeoutMs = 3500L
                )
            )
        ),

        // ==========================================
        // 🚨 詐騙警告劇本 (Anti-Fraud Suite - 衝擊力・極簡短語)
        // ==========================================
        Scenario(
            id = "fraud_block",
            title = "6. 緊急肉身阻攔",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "0.25m/s 高速猛撲向前突進卡位 + 橫向大字形狂暴攔截",
            badge = "猛撲・卡位・大字阻擋",
            steps = listOf(
                ScenarioStep(
                    name = "高速猛撲向前阻截",
                    chassisMove = ChassisMove(speed = 0.25f, durationMs = 800L), // 衝刺約 20cm
                    ledPattern = LedPattern.RED_STROBE,
                    ttsText = "危險！住手！",
                    ttsSpeed = "130",
                    timeoutMs = 1500L
                ),
                ScenarioStep(
                    name = "雙臂大字形橫向狂暴攔阻",
                    motionName = "888_ML_HandsOut_05",
                    ledPattern = LedPattern.RED_STROBE,
                    timeoutMs = 3500L
                )
            )
        ),

        Scenario(
            id = "fraud_panic",
            title = "7. 大驚失色慌亂後撤",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "-0.22m/s 猛烈急退三步 (25cm) + 抱頭摀臉驚恐 + 全身劇烈大顫抖",
            badge = "急退三步・抱頭・劇烈顫抖",
            steps = listOf(
                ScenarioStep(
                    name = "抱頭倒退急撤三步",
                    motionName = "888_ML_BUpPanic_23",
                    chassisMove = ChassisMove(speed = -0.22f, durationMs = 1100L), // 後退約 25cm
                    ledPattern = LedPattern.RED_WHITE_ALERT,
                    ttsText = "天啊！這是詐騙！",
                    ttsSpeed = "125",
                    timeoutMs = 3500L
                ),
                ScenarioStep(
                    name = "全身劇烈大顫抖",
                    motionName = "888_ML_VeryScared_02",
                    ledPattern = LedPattern.RED_WHITE_ALERT,
                    timeoutMs = 3500L
                )
            )
        ),

        Scenario(
            id = "fraud_accuse",
            title = "8. 嚴厲指責警告",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "0.22m/s 挺身逼近一步 + 憤怒挺胸 + 單手如利劍猛刺直指電話",
            badge = "前衝挺身・利劍直指",
            steps = listOf(
                ScenarioStep(
                    name = "前衝一步憤怒挺胸",
                    motionName = "888_ML_Angry_08",
                    chassisMove = ChassisMove(speed = 0.22f, durationMs = 500L), // 前衝一步 11cm
                    ledPattern = LedPattern.SOLID_DARK_RED,
                    timeoutMs = 2500L
                ),
                ScenarioStep(
                    name = "如利劍尖刀猛刺直指電話",
                    motionName = "888_ML_AngPoint_12",
                    ledPattern = LedPattern.SOLID_DARK_RED,
                    ttsText = "那是假的！騙人的！",
                    ttsSpeed = "125",
                    timeoutMs = 3500L
                )
            )
        ),

        Scenario(
            id = "fraud_reject",
            title = "9. 急迫雙手大交叉駁斥",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "雙臂胸前大幅狂暴交替狂揮 + 伴隨頭部全力劇烈狂搖說不",
            badge = "狂暴揮臂・全力狂搖",
            steps = listOf(
                ScenarioStep(
                    name = "雙手大交叉狂暴揮動駁斥",
                    motionName = "667_P4_Wong",
                    chassisMove = ChassisMove(turnSpeed = 60f, durationMs = 500L),
                    ledPattern = LedPattern.RED_AMBER_PULSE,
                    ttsText = "不對！絕對不要轉帳！",
                    ttsSpeed = "125",
                    timeoutMs = 5000L
                ),
                ScenarioStep(
                    name = "全力劇烈狂搖頭說不",
                    motionName = "666_BA_Shakehead",
                    ledPattern = LedPattern.RED_AMBER_PULSE,
                    timeoutMs = 3000L
                )
            )
        ),

        Scenario(
            id = "fraud_siren",
            title = "10. 警報全開與報警勸阻",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "120°/s 疾速原地大自轉 180° + 警車雙色爆閃 + 雙手大動作下壓示意",
            badge = "疾速自轉・警笛・掛電話",
            steps = listOf(
                ScenarioStep(
                    name = "原地疾速自轉 180 度警報",
                    motionName = "666_PE_Power",
                    chassisMove = ChassisMove(turnSpeed = 120f, durationMs = 1500L), // 疾速自轉 180度
                    ledPattern = LedPattern.POLICE_SIREN,
                    ttsText = "警報！立刻掛掉電話！",
                    ttsSpeed = "125",
                    timeoutMs = 3500L
                ),
                ScenarioStep(
                    name = "雙手大動作下壓冷靜",
                    motionName = "667_MG_HandsUp",
                    ledPattern = LedPattern.POLICE_SIREN,
                    timeoutMs = 3500L
                )
            )
        )
    )

    fun findById(id: String): Scenario? = allScenarios.find { it.id.equals(id, ignoreCase = true) }
}
