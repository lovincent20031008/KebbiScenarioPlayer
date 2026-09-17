package com.nuwa.scenarioplayer.model

object ScenarioRepository {

    val allScenarios: List<Scenario> = listOf(
        // ==========================================
        // 🎩 管家模式劇本 (Butler Suite)
        // ==========================================
        Scenario(
            id = "butler_welcome",
            title = "1. 盛大迎賓巡禮",
            category = ScenarioCategory.BUTLER,
            description = "前進 15cm 主動靠攏 + 90°深鞠躬 + 優雅單手引領",
            badge = "迎賓・前進・鞠躬",
            steps = listOf(
                ScenarioStep(
                    name = "主動趨前靠近",
                    chassisMove = ChassisMove(speed = 0.10f, durationMs = 1500L), // 前進 15cm
                    ledPattern = LedPattern.BREATH_GOLD,
                    ttsText = "主人您好！歡迎回家，今天辛苦了！",
                    timeoutMs = 2500L
                ),
                ScenarioStep(
                    name = "深情鞠躬迎賓",
                    motionName = "666_RE_Bow",
                    ledPattern = LedPattern.BREATH_GOLD,
                    timeoutMs = 4500L
                ),
                ScenarioStep(
                    name = "單手引領入室",
                    motionName = "667_P4_TalkHand",
                    ledPattern = LedPattern.BREATH_GOLD,
                    ttsText = "室內已經為您準備好舒適的環境，請進！",
                    timeoutMs = 4000L
                )
            )
        ),

        Scenario(
            id = "butler_guide",
            title = "2. 空間引領指引",
            category = ScenarioCategory.BUTLER,
            description = "旋轉 45° + 雙手展開宏大氣度 + 單手向前定住指引",
            badge = "轉向・展開・定點",
            steps = listOf(
                ScenarioStep(
                    name = "優雅轉向與雙臂展開",
                    motionName = "888_ML_HandsSideUp2_13",
                    chassisMove = ChassisMove(turnSpeed = 30f, durationMs = 1500L), // 旋轉 45度
                    ledPattern = LedPattern.CYAN_FLOW,
                    ttsText = "請往這個方向走，目標就在前方，請隨我來。",
                    timeoutMs = 4500L
                ),
                ScenarioStep(
                    name = "單手延伸指引",
                    motionName = "888_ML_RPointLisa_24",
                    ledPattern = LedPattern.CYAN_FLOW,
                    ttsText = "沿著走道直行即可抵達，請小心腳步。",
                    timeoutMs = 4000L
                )
            )
        ),

        Scenario(
            id = "butler_thinking",
            title = "3. 深度思考檢索",
            category = ScenarioCategory.BUTLER,
            description = "擬真微轉打量 + 7s 托腮沉思 + 恍然大悟抬頭",
            badge = "沉思・托腮・頓悟",
            steps = listOf(
                ScenarioStep(
                    name = "托腮沉思與打量",
                    motionName = "888_ML_Thinking_08",
                    chassisMove = ChassisMove(turnSpeed = 15f, durationMs = 600L),
                    ledPattern = LedPattern.PURPLE_PULSE,
                    ttsText = "嗯...請稍等我片刻，正在為您翻閱最新資料...",
                    timeoutMs = 8000L
                ),
                ScenarioStep(
                    name = "恍然大悟出結果",
                    motionName = "888_ML_ThinkingOh_13",
                    ledPattern = LedPattern.CYAN_FLOW,
                    ttsText = "啊！找到了！答案已經為您整理在螢幕上了！",
                    timeoutMs = 4000L
                )
            )
        ),

        Scenario(
            id = "butler_celebrate",
            title = "4. 歡慶擊掌祝賀",
            category = ScenarioCategory.BUTLER,
            description = "雀躍碎步搖擺 + 雙手高舉歡呼 + 伸出右手擊掌",
            badge = "歡呼・彩虹・擊掌",
            steps = listOf(
                ScenarioStep(
                    name = "雙臂高舉雀躍歡呼",
                    motionName = "666_SP_Cheer",
                    chassisMove = ChassisMove(turnSpeed = 25f, durationMs = 800L),
                    ledPattern = LedPattern.RAINBOW,
                    ttsText = "太棒了！恭喜主人達成目標！真是太厲害了！",
                    timeoutMs = 4500L
                ),
                ScenarioStep(
                    name = "邀請熱情擊掌",
                    motionName = "888_ML_HiFive_19",
                    ledPattern = LedPattern.RAINBOW,
                    ttsText = "來跟 Kebbi 擊個掌吧！耶！",
                    timeoutMs = 4000L
                )
            )
        ),

        Scenario(
            id = "butler_farewell",
            title = "5. 禮貌告別退場",
            category = ScenarioCategory.BUTLER,
            description = "後退 20cm 禮貌讓出走道 + 側身揮手道別",
            badge = "後退・側身・揮手",
            steps = listOf(
                ScenarioStep(
                    name = "後撤讓路與側身揮手",
                    motionName = "666_RE_TurnRBye",
                    chassisMove = ChassisMove(speed = -0.10f, durationMs = 2000L), // 後退 20cm
                    ledPattern = LedPattern.SOFT_WHITE,
                    ttsText = "那 Kebbi 先退下了，有任何需要隨時呼喚我，祝您有美好的一天！",
                    timeoutMs = 4500L
                )
            )
        ),

        // ==========================================
        // 🚨 詐騙警告劇本 (Anti-Fraud Suite)
        // ==========================================
        Scenario(
            id = "fraud_block",
            title = "6. 緊急肉身阻攔",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "前衝 20cm 突進卡位 + 橫向大字形雙臂猛烈攔截",
            badge = "衝刺・卡位・阻擋",
            steps = listOf(
                ScenarioStep(
                    name = "前衝突進阻截",
                    chassisMove = ChassisMove(speed = 0.13f, durationMs = 1500L), // 前衝約 20cm
                    ledPattern = LedPattern.RED_STROBE,
                    ttsText = "危險！請停下！不要按確認鍵！不要操作 ATM！",
                    ttsSpeed = "115",
                    timeoutMs = 2500L
                ),
                ScenarioStep(
                    name = "大字形橫向攔阻",
                    motionName = "888_ML_HandsOut_05",
                    ledPattern = LedPattern.RED_STROBE,
                    ttsText = "這絕對是詐騙陷阱，請立刻把手移開！",
                    timeoutMs = 4500L
                )
            )
        ),

        Scenario(
            id = "fraud_panic",
            title = "7. 大驚失色慌亂後撤",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "急退 25cm (嚇退三步) + 抱頭摀臉驚恐 + 劇烈顫抖",
            badge = "急退・抱頭・顫抖",
            steps = listOf(
                ScenarioStep(
                    name = "驚慌後撤與抱頭",
                    motionName = "888_ML_BUpPanic_23",
                    chassisMove = ChassisMove(speed = -0.13f, durationMs = 1900L), // 後退 25cm
                    ledPattern = LedPattern.RED_WHITE_ALERT,
                    ttsText = "天啊！這是典型詐騙手法！千萬不要匯款！",
                    ttsSpeed = "110",
                    timeoutMs = 4500L
                ),
                ScenarioStep(
                    name = "後怕顫抖防衛",
                    motionName = "888_ML_VeryScared_02",
                    ledPattern = LedPattern.RED_WHITE_ALERT,
                    ttsText = "只要一把驗證碼給出去，帳戶所有存款會被瞬間掏空！",
                    timeoutMs = 4500L
                )
            )
        ),

        Scenario(
            id = "fraud_accuse",
            title = "8. 嚴厲指責警告",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "原地有力逼近 + 憤怒挺胸 + 利劍般直指電話",
            badge = "挺胸・直指・駁斥",
            steps = listOf(
                ScenarioStep(
                    name = "憤怒挺胸逼近",
                    motionName = "888_ML_Angry_08",
                    ledPattern = LedPattern.SOLID_DARK_RED,
                    ttsText = "對方是不是自稱檢察官要監管您的帳戶？",
                    ttsSpeed = "105",
                    timeoutMs = 4000L
                ),
                ScenarioStep(
                    name = "利劍指向電話",
                    motionName = "888_ML_AngPoint_12",
                    ledPattern = LedPattern.SOLID_DARK_RED,
                    ttsText = "那是假的！警察和司法官絕不會在電話中要求監管銀行帳戶！",
                    timeoutMs = 4500L
                )
            )
        ),

        Scenario(
            id = "fraud_reject",
            title = "9. 急迫雙手大交叉駁斥",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "大幅度雙臂狂揮否定 (6s) + 堅決激烈搖頭制止",
            badge = "狂揮・狂搖・駁斥",
            steps = listOf(
                ScenarioStep(
                    name = "大幅度雙手狂揮否定",
                    motionName = "667_P4_Wong",
                    chassisMove = ChassisMove(turnSpeed = 15f, durationMs = 800L),
                    ledPattern = LedPattern.RED_AMBER_PULSE,
                    ttsText = "不對！這絕對不對！天下根本沒有保證穩賺不賠的高獲利投資！",
                    ttsSpeed = "110",
                    timeoutMs = 7000L
                ),
                ScenarioStep(
                    name = "激烈搖頭阻止",
                    motionName = "666_BA_Shakehead",
                    ledPattern = LedPattern.RED_AMBER_PULSE,
                    ttsText = "不要再相信網路上的飆股群組和明牌了！",
                    timeoutMs = 3500L
                )
            )
        ),

        Scenario(
            id = "fraud_siren",
            title = "10. 警報全開與報警勸阻",
            category = ScenarioCategory.FRAUD_ALERT,
            description = "原地 180°警車巡航旋轉 + 警笛紅藍爆閃 + 呼籲撥打 165",
            badge = "180°旋轉・警笛・165",
            steps = listOf(
                ScenarioStep(
                    name = "原地旋轉環視警報",
                    motionName = "666_PE_Power",
                    chassisMove = ChassisMove(turnSpeed = 45f, durationMs = 2000L), // 旋轉 90~180度
                    ledPattern = LedPattern.POLICE_SIREN,
                    ttsText = "全機警報啟動！請您立刻保持冷靜，深呼吸，千萬不要衝動！",
                    ttsSpeed = "110",
                    timeoutMs = 4500L
                ),
                ScenarioStep(
                    name = "雙手下壓引導撥打165",
                    motionName = "667_MG_HandsUp",
                    ledPattern = LedPattern.POLICE_SIREN,
                    ttsText = "請立刻掛斷這通電話，拿起手機撥打 165 反詐騙專線查證！",
                    timeoutMs = 4000L
                )
            )
        )
    )

    fun findById(id: String): Scenario? = allScenarios.find { it.id.equals(id, ignoreCase = true) }
}
