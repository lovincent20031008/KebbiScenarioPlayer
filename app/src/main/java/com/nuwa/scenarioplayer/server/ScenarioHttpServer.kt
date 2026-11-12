package com.nuwa.scenarioplayer.server

import android.util.Log
import com.nuwa.scenarioplayer.engine.ScenarioEngine
import com.nuwa.scenarioplayer.model.ScenarioRepository
import com.nuwa.scenarioplayer.preview.MotionPreview
import fi.iki.elonen.NanoHTTPD
import org.json.JSONArray
import org.json.JSONObject

class ScenarioHttpServer(
    port: Int = 8081,
    private val engine: ScenarioEngine,
    private val motionPreview: MotionPreview
) : NanoHTTPD(port) {

    companion object {
        private const val TAG = "ScenarioHttpServer"
    }

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        val method = session.method

        Log.d(TAG, "HTTP Request: $method $uri")

        return try {
            when (uri) {
                "/status" -> {
                    val json = JSONObject().apply {
                        put("ok", true)
                        put("app", "KebbiScenarioPlayer")
                        put("isPlaying", engine.isPlaying)
                        put("currentScenarioId", engine.currentScenario?.id)
                        put("currentScenarioTitle", engine.currentScenario?.title)
                        put("currentStepIndex", engine.currentStepIndex)
                    }.toString()
                    newFixedLengthResponse(Response.Status.OK, "application/json", json)
                }

                "/scenarios" -> {
                    val arr = JSONArray()
                    ScenarioRepository.allScenarios.forEach { s ->
                        arr.put(JSONObject().apply {
                            put("id", s.id)
                            put("title", s.title)
                            put("category", s.category.name)
                            put("description", s.description)
                            put("badge", s.badge)
                            put("affectiveState", s.affectiveState)
                            put("theoryPaper", s.theoryPaper)
                            put("stepCount", s.steps.size)
                        })
                    }
                    val json = JSONObject().apply {
                        put("ok", true)
                        put("count", ScenarioRepository.allScenarios.size)
                        put("scenarios", arr)
                    }.toString()
                    newFixedLengthResponse(Response.Status.OK, "application/json", json)
                }

                "/scenario/play" -> {
                    val id = session.parameters["id"]?.firstOrNull()
                    if (id == null) {
                        val json = JSONObject().apply {
                            put("ok", false)
                            put("error", "Missing required parameter 'id'")
                        }.toString()
                        return newFixedLengthResponse(Response.Status.BAD_REQUEST, "application/json", json)
                    }

                    val scenario = ScenarioRepository.findById(id)
                    if (scenario == null) {
                        val json = JSONObject().apply {
                            put("ok", false)
                            put("error", "Scenario not found for id: $id")
                        }.toString()
                        return newFixedLengthResponse(Response.Status.NOT_FOUND, "application/json", json)
                    }

                    val success = engine.play(scenario)
                    val json = JSONObject().apply {
                        put("ok", success)
                        put("scenarioId", scenario.id)
                        put("scenarioTitle", scenario.title)
                        if (!success) {
                            put("reason", "Another scenario is currently playing or motors unavailable")
                        }
                    }.toString()
                    newFixedLengthResponse(Response.Status.OK, "application/json", json)
                }

                // 結束目前的維持姿勢 (模擬「回覆到了」或「長輩說完了」)
                "/scenario/next" -> {
                    val ok = engine.next()
                    newFixedLengthResponse(Response.Status.OK, "application/json",
                        JSONObject().apply {
                            put("ok", ok)
                            if (!ok) put("reason", "no hold step is playing")
                        }.toString())
                }

                "/scenario/stop", "/demo/stop" -> {
                    engine.stop()
                    val json = JSONObject().apply {
                        put("ok", true)
                        put("stopped", true)
                    }.toString()
                    newFixedLengthResponse(Response.Status.OK, "application/json", json)
                }

                // 只換臉、不動任何馬達 (表情測試用)
                "/face/play" -> {
                    val key = session.parameters["key"]?.firstOrNull()
                    val ok = key != null && engine.playFaceAnimationNow(key)
                    newFixedLengthResponse(Response.Status.OK, "application/json",
                        JSONObject().apply { put("ok", ok); put("key", key) }.toString())
                }

                // ===== 內建動作預覽 (測試用) =====
                // 只查長度，不會動：/motion/info?name=666_RE_Bye 或 names=a,b,c
                "/motion/info" -> {
                    val names = (session.parameters["names"]?.firstOrNull()?.split(",")
                        ?: listOfNotNull(session.parameters["name"]?.firstOrNull()))
                        .map { it.trim() }.filter { it.isNotEmpty() }.take(40)
                    val arr = JSONArray()
                    names.forEach { n ->
                        val info = motionPreview.info(n)
                        arr.put(JSONObject().apply {
                            put("name", info.name)
                            put("ok", info.ok)
                            put("durationSec", info.durationSec ?: JSONObject.NULL)
                            if (info.error != null) put("error", info.error)
                        })
                    }
                    newFixedLengthResponse(Response.Status.OK, "application/json",
                        JSONObject().apply { put("ok", true); put("motions", arr) }.toString())
                }

                // 播放單一內建動作 (會動！)：/motion/play?name=666_RE_Bye&face=true
                "/motion/play" -> {
                    val name = session.parameters["name"]?.firstOrNull()
                    val withFace = session.parameters["face"]?.firstOrNull() == "true"
                    val json = JSONObject()
                    when {
                        name == null || !motionPreview.isValidName(name) -> { json.put("ok", false); json.put("error", "invalid name") }
                        engine.isPlaying -> { json.put("ok", false); json.put("error", "a scenario is playing") }
                        else -> { json.put("ok", motionPreview.play(name, withFace)); json.put("name", name); json.put("face", withFace) }
                    }
                    newFixedLengthResponse(Response.Status.OK, "application/json", json.toString())
                }

                // 播放並記錄關節軌跡 (會動！)：/motion/trace?name=888_ML_Tinythink_25
                "/motion/trace" -> {
                    val name = session.parameters["name"]?.firstOrNull()
                    if (name == null || !motionPreview.isValidName(name) || engine.isPlaying) {
                        return newFixedLengthResponse(Response.Status.OK, "application/json",
                            JSONObject().apply { put("ok", false); put("error", "invalid name or scenario playing") }.toString())
                    }
                    val dur = motionPreview.info(name).durationSec ?: 3f
                    val rows = motionPreview.trace(name, (dur * 1000).toLong() + 800L)
                    val arr = JSONArray()
                    rows.forEach { r -> arr.put(JSONArray().apply { r.forEach { v -> put(if (v.isNaN()) JSONObject.NULL else v.toDouble()) } }) }
                    newFixedLengthResponse(Response.Status.OK, "application/json",
                        JSONObject().apply {
                            put("ok", true); put("name", name); put("durationSec", dur.toDouble())
                            put("columns", JSONArray(listOf("ms", "m1", "m2", "m3", "m4", "m5", "m6", "m7", "m8", "m9", "m10")))
                            put("rows", arr)
                        }.toString())
                }

                "/motion/stop" -> {
                    motionPreview.stop()
                    newFixedLengthResponse(Response.Status.OK, "application/json",
                        JSONObject().apply { put("ok", true) }.toString())
                }

                "/face/anim" -> {
                    val name = session.parameters["name"]?.firstOrNull()
                    val ok = name != null && engine.playFaceAnimationNow(name)
                    newFixedLengthResponse(Response.Status.OK, "application/json",
                        JSONObject().apply { put("ok", ok); put("name", name) }.toString())
                }

                "/face/reset" -> {
                    engine.resetFace()
                    newFixedLengthResponse(Response.Status.OK, "application/json",
                        JSONObject().apply { put("ok", true) }.toString())
                }

                "/demo/start", "/demo" -> {
                    val demo = ScenarioRepository.megaDemoScenario
                    val success = engine.play(demo)
                    val json = JSONObject().apply {
                        put("ok", success)
                        put("scenarioId", demo.id)
                        put("scenarioTitle", demo.title)
                        put("durationEst", "60s")
                        if (!success) {
                            put("reason", "Another scenario is currently playing or motors unavailable")
                        }
                    }.toString()
                    newFixedLengthResponse(Response.Status.OK, "application/json", json)
                }

                else -> {
                    val json = JSONObject().apply {
                        put("ok", false)
                        put("error", "Endpoint not found: $uri")
                    }.toString()
                    newFixedLengthResponse(Response.Status.NOT_FOUND, "application/json", json)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling HTTP request", e)
            val json = JSONObject().apply {
                put("ok", false)
                put("error", e.message)
            }.toString()
            newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "application/json", json)
        }
    }
}
