# Kebbi Scenario Player (凱比機器人多模態情境劇本播放器)

[![Platform](https://img.shields.io/badge/Platform-Android%208.1%20%2F%209.0%20(API%2028)-3DDC84.svg?style=flat&logo=android)]()
[![Hardware](https://img.shields.io/badge/Hardware-Nuwa%20Kebbi%20Air%20S-0288D1.svg?style=flat)]()
[![Language](https://img.shields.io/badge/Language-Kotlin%201.9-7F52FF.svg?style=flat&logo=kotlin)]()
[![Architecture](https://img.shields.io/badge/Architecture-Decoupled%20IPC%20%2F%20RaaS-FF6F00.svg?style=flat)]()
[![License](https://img.shields.io/badge/License-MIT%20%2F%20Academic%20Use-green.svg)]()

> **專案定位**：專為女媧 **Kebbi Air S** 服務型機器人打造的高階多模態情境排程引擎（Multi-modal Choreography Engine）。整合「底盤運動學平滑加減速、10 軸關節動作、多區段雙色 LED 爆閃、原生高音質警報音效，以及官方 Unity 3D FacePresenter 視窗動態生命週期調度」，為居家長輩防詐阻斷與迎賓管家提供高視覺張力的實體介入（Embodied Intervention）解決方案。

---

## 🏛️ 系統架構與跨 App 串接模型 (System Architecture)

為避免第三方業務應用（如長輩防詐辨識 App）直接耦合破碎的女媧硬體 SDK，本專案將機器人能力封裝為**高可用本機微服務（Robot-as-a-Service, RaaS）**，支援 **HTTP REST API (Port 8081)** 與 **Android 原生系統廣播 (Explicit Broadcast IPC)** 雙通道通訊：

```
+-------------------------------------------------------------+
|               外部業務端：長輩防詐辨識主應用                 |
|               (Third-Party Scam Detector App)               |
+-------------------------------------------------------------+
               │                                │
      [通道 1: 本機 HTTP POST]        [通道 2: Explicit Broadcast]
    http://127.0.0.1:8081/alert       com.nuwa.scenarioplayer.TRIGGER_ALERT
               │                                │
               ▼                                ▼
+-------------------------------------------------------------+
|              Kebbi Scenario Player 核心排程中樞              |
|                                                             |
|  ┌──────────────────┐  ┌────────────────┐  ┌─────────────┐  |
|  | ScenarioEngine   |  | SafetyManager  |  | NanoHTTPD   |  |
|  | (時序同步調度器) |  | (底盤速度邊界) |  | (Port 8081) |  |
|  └──────────────────┘  └────────────────┘  └─────────────┘  |
+-------------------------------------------------------------+
                               │
       ┌───────────────────────┼───────────────────────┐
       ▼                       ▼                       ▼
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
|  底盤運動學  |       | 10軸關節伺服 |       | 官方Unity 3D |
| (0.30m/s疾衝 |       | (奧特曼光能/ |       | FacePresenter|
| 150°/s自轉)  |       | 雙拳格鬥拔刀)|       | (動態視窗搶佔|
└──────────────┘       └──────────────┘       | 嘴型字幕同步)|
                                              └──────────────┘
```

---

## 🌟 技術亮點 (Key Features)

### 1. 五維度多模態時序同步排程 (Multi-Modal Synchronization)
- 原生女媧 SDK 的致動器（馬達、底盤、語音、臉部、LED）彼此獨立破碎。本引擎透過協同時間軸（Coordinated Timeline）實現毫秒級精準同步：
  - **底盤加減速平滑曲線**：前進疾衝最高 $0.30\text{ m/s}$，原地旋轉最高 $150^\circ\text{/s}$，具備物理急煞緩衝。
  - **10 軸伺服馬達動作鏈**：精準調用官方高階動作軌道（如 `666_PE_Ultraman`, `666_FI_Fight`, `666_FI_Draw`, `888_ML_Kingclap_20`）。
  - **動態雙色警笛 LED**：支援頭頂、面部與胸前多區段同步呼吸與紅藍警車爆閃。
  - **Android 系統級原生音頻**：直接呼叫 Media Player 載入高分貝 Ta-Da 號角、空襲蜂鳴與恐慌急迫警報。

### 2. 攻克官方 3D FacePresenter 視窗遮擋問題
- 女媧機器人 3D 臉部由系統獨立 Unity 應用（`com.nuwarobotics.app.facepresenter`）渲染。
- 本專案實作了**跨進程 Activity 生命週期管理**：
  - **劇本播放時**：自動觸發 Intent 將官方 3D 臉部推至最前台，展現生動眼神眨眼、TTS 即時唇形開合與大字繁中台詞字幕。
  - **表演完畢或緊急煞停時**：透過 `FLAG_ACTIVITY_REORDER_TO_FRONT` 平滑還原控制介面，不銷毀背景狀態。

### 3. 主動式硬體安全邊界與觸控搶佔 (Hardware Safety & E-STOP)
- **速度約束攔截器**：任何異常或超限速度指令均被 `ChassisSafetyManager` 即時平滑夾制。
- **實體觸控緊急搶佔 (Physical Tap Sensor Override)**：表演進行中只要長輩或使用者輕拍 Kebbi 頭頂或胸前觸控區（`onTap`），系統硬體瞬態煞停，所有馬達歸位並切回待命介面。

---

## 🎭 劇本庫與 60 秒旗艦聯播 (Scenario Catalog)

### 🌟 60 秒全場景聯播 (MEGA DEMO)
串聯 12 幕高潮連續技，展示全機極限動態性能：
$$\text{Ta-Da迎賓號角} \rightarrow \text{90}^\circ\text{深鞠躬} \rightarrow \text{奧特曼光能} \rightarrow \text{帝王鼓掌} \rightarrow \text{超人飛天} \rightarrow \text{空襲急煞} \rightarrow \text{雙拳格鬥卡位} \rightarrow \text{抱頭NOOO急退} \rightarrow \text{交叉駁斥} \rightarrow \text{狂搖頭} \rightarrow \text{拔刀180}^\circ\text{大自轉} \rightarrow \text{側身謝幕}$$

### 劇本詳細規格 (16 套 HRI 論文導向情境)

#### 🎩 管家日常聊天與伴侶劇本 (Butler Daily Companion Suite - 8 套)
| 劇本 ID | 名稱 | 情緒維度 (Russell 座標) | 實體動作特徵 | 2021-2024 HRI 論文依據 |
| :--- | :--- | :--- | :--- | :--- |
| `butler_welcome` | 1. 盛大迎賓問候 | +Valence, Moderate Arousal | 0.28m/s 疾步前趨 + 90°俐落深鞠躬 + 雙臂超展開 | IEEE RO-MAN 2021/2023 高空間擴展度傳達親和力 |
| `butler_listening` | 2. 專注傾聽共感 | Moderate Valence, Low Arousal | 身體微前傾側耳傾聽 + 慢速點頭共感 (低喚起舒緩) | ACM THRI 2022 眼神頭部動態共感顯著提升親和信任 |
| `butler_thinking` | 3. 沉思檢索運算 | Neutral Valence, Low Arousal | 托腮打量深思 + 仰頭眼球轉動 + 拍掌頓悟 | ACM THRI 2022 認知運算外顯化降低等待焦慮 |
| `butler_praise` | 4. 正面肯定誇讚 | High Valence, High Arousal | 拍手大拇指筆直向前 + 輕快微跳躍動 | IEEE RO-MAN 2021/2023 向上加速度強化成就感 |
| `butler_celebrate` | 5. 歡慶盛大祝賀 | ++Valence, ++Arousal | 尊榮 Ta-Da 迎賓號角 + 帝王連續鼓掌 + 超人歡呼 | IEEE RA-L 2022 上揚掌聲正向反饋效應 |
| `butler_apology` | 6. 委婉致歉自省 | Negative Valence, Low Arousal | 後退半步給予安全感 + 雙手下壓低頭誠懇鞠躬 | ACM THRI 2022 負向後撤主動降低對峙壓力 |
| `butler_guide` | 7. 空間引領指引 | +Valence, Moderate Arousal | 130°/s 急速轉向 60° + 奧特曼十字光能 + 定點直指 | IEEE RO-MAN 2021/2023 直線聚焦空間引導 |
| `butler_farewell` | 8. 溫馨告別待命 | Sustained Valence, Low Arousal | 大力高舉右手狂揮道別 + -0.24m/s 滑行後退歸位 | IEEE RO-MAN 2021/2023 柔和減速平靜交接 |

#### 🚨 詐騙提醒與多層級實體阻斷劇本 (Anti-Fraud Multi-Tier Suite - 8 套)
| 劇本 ID | 名稱 | 情緒維度 (Russell 座標) | 實體動作特徵 | 2021-2024 HRI 論文依據 |
| :--- | :--- | :--- | :--- | :--- |
| `fraud_anomaly` | 9. 察覺微小異常 | Neutral-Neg, Low Arousal | 悄悄挪近半步 + 歪頭側聽打量通話起疑 (低度戒備) | ACM THRI 2022 傾身偏側探究體態 |
| `fraud_unknown_caller` | 10. 未知來電警示 | Negative Valence, Mod Arousal | 原地挺身 + 單臂前推暫停手勢 + 警示微搖頭提醒 | IEEE RO-MAN 2021/2023 正面對峙暫停手勢建立邊界 |
| `fraud_accuse` | 11. 敏感金錢關鍵字 | Negative Valence, High Arousal | 0.26m/s 挺身逼近 + 大力士霸氣肌肉防禦 + 尖刀直指 | IEEE RO-MAN 2021/2023 直線強硬指責打破迷思 |
| `fraud_counter_manipulation` | 12. 話術情緒勒索防護 | Protective Dominance, High Arousal | 挺胸腰側雙拳戰鬥架勢 + 大力士肌肉護盾 + 護主卡位 | Frontiers 2023/2024 第三方強勢保護介入 |
| `fraud_block_phishing` | 13. 阻擋點擊釣魚簡訊 | High Urgency, High Arousal | 0.28m/s 疾衝螢幕前方 + 雙手狂推下壓止步 + 狂搖頭 | IEEE RA-L 2022 突進急煞強行打斷盲點 |
| `fraud_panic` | 14. 恐慌急退慘叫阻斷 | Extreme Neg, High Arousal | -0.26m/s 猛烈急退三步 + 雙手抱頭狂喊 NOOO + 警報 | ACM THRI 2022 肢體面部一致性恐懼放大 |
| `fraud_physical_block` | 15. 假冒公檢法強制卡位 | Max Threat, Max Arousal | 0.30m/s 疾衝肉身路障 + 空襲警報蜂鳴 + 猛烈雙拳出拳 | IEEE RA-L 2022 物理空間侵入瓦解長輩認知盲區 |
| `fraud_siren` | 16. 拔刀斬斷與165通報 | Decisive Resolution, High Dominance | 150°/s 疾速自轉 180° + 俐落抽刀拔劍斬斷 + 警車爆閃 | Frontiers 2023/2024 第三方強勢介入提升掛電話率 |

---

## 🔌 合作夥伴跨 App 串接指南 (Integration Guide)

第三方應用（如長輩防詐 App）可任選以下兩種標準通訊協定，呼叫機器人發動警報：

### 方式 A：本機 HTTP REST API 請求 (推薦)
向本機 `8081` 埠發送 HTTP POST 請求：

```kotlin
// Kotlin (OkHttp 範例)
val client = OkHttpClient()
val request = Request.Builder()
    .url("http://127.0.0.1:8081/scenario/play?id=fraud_siren")
    .post("{}".toRequestBody("application/json".toMediaType()))
    .build()

client.newCall(request).enqueue(object : Callback {
    override fun onResponse(call: Call, response: Response) {
        // 成功觸發機器人
    }
    override fun onFailure(call: Call, e: IOException) {
        // 異常處理
    }
})
```

- **一鍵啟動 60 秒示範**：`POST http://127.0.0.1:8081/demo/start`
- **全域緊急煞停**：`POST http://127.0.0.1:8081/demo/stop`

---

### 方式 B：Android 系統廣播 (Broadcast IPC)
發送明確廣播（Explicit Broadcast），安全相容 Android 8.0+ 背景執行限制：

```kotlin
val intent = Intent("com.nuwa.scenarioplayer.TRIGGER_ALERT").apply {
    setPackage("com.nuwa.scenarioplayer") // 明確指定目標 Package
    putExtra("scenario_id", "fraud_siren") // 指定劇本 ID
}
context.sendBroadcast(intent)
```

---

## 🛠️ 建置與部署 (Build & Installation)

### 環境需求
- **硬體**：Nuwa Kebbi Air S (Android 8.1 / 9.0)
- **建置工具**：JDK 17+, Android SDK (Build-Tools 34.0.0+), Gradle 8.9

### 建置命令
```bash
# 1. 複製專案
git clone https://github.com/lovincent20031008/KebbiScenarioPlayer.git
cd KebbiScenarioPlayer

# 2. 編譯 Debug APK
./gradlew assembleDebug

# 3. 透過 ADB 安裝至 Kebbi 實機
adb install -r app/build/outputs/apk/debug/app-debug.apk

# 4. 啟動播放器主介面
adb shell am start -n com.nuwa.scenarioplayer/.MainActivity
```

---

## 📄 開發規範與宣告 (License & Notice)
- 本專案採用標準 [MIT 授權條款](LICENSE)。
- 內建動作呼叫需依賴女媧原廠執行階段服務（Nuwa KiWi Service）與相關韌體支援。
