package com.nuwa.scenarioplayer.server

import android.util.Log
import com.nuwa.scenarioplayer.engine.ScenarioEngine
import com.nuwa.scenarioplayer.model.ScenarioRepository
import fi.iki.elonen.NanoHTTPD
import org.json.JSONArray
import org.json.JSONObject

class ScenarioHttpServer(
    port: Int = 8081,
    private val engine: ScenarioEngine
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

                "/scenario/stop", "/demo/stop" -> {
                    engine.stop()
                    val json = JSONObject().apply {
                        put("ok", true)
                        put("stopped", true)
                    }.toString()
                    newFixedLengthResponse(Response.Status.OK, "application/json", json)
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
