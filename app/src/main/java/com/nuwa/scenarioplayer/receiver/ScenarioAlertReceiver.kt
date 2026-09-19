package com.nuwa.scenarioplayer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.nuwa.scenarioplayer.MainActivity

/**
 * 接收跨 App 防詐警報廣播之系統接收器 (BroadcastReceiver)
 * 允許同機外部防詐 App 發送 Explicit Broadcast 喚醒 Kebbi 執行對應等級之警報動作
 */
class ScenarioAlertReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "ScenarioAlertReceiver"
        const val ACTION_TRIGGER_ALERT = "com.nuwa.scenarioplayer.TRIGGER_ALERT"
        const val EXTRA_SCENARIO_ID = "scenario_id"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_TRIGGER_ALERT) {
            val scenarioId = intent.getStringExtra(EXTRA_SCENARIO_ID) ?: "fraud_siren"
            Log.i(TAG, "Received TRIGGER_ALERT broadcast for scenario: $scenarioId")

            val mainActivity = MainActivity.instance
            if (mainActivity != null) {
                mainActivity.playScenarioById(scenarioId)
            } else {
                val launchIntent = Intent(context, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    putExtra(EXTRA_SCENARIO_ID, scenarioId)
                }
                context.startActivity(launchIntent)
            }
        }
    }
}
