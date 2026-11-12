package com.nuwa.scenarioplayer.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SentimentScenarioTest {

    private val ids = listOf(
        "butler_sentiment_positive",
        "butler_sentiment_neutral",
        "butler_sentiment_negative"
    )

    @Test
    fun sentimentScenariosAreRegistered() {
        ids.forEach { assertNotNull("missing $it", ScenarioRepository.findById(it)) }
    }

    @Test
    fun everyJointTargetIsInsideTestedWhitelist() {
        ScenarioRepository.allScenarios.flatMap { it.steps }.flatMap { it.jointTargets }.forEach {
            assertTrue("not allowed: $it", JointSafety.isAllowed(it))
        }
    }

    @Test
    fun sentimentScenariosHaveNoSpeechSoundOrChassis() {
        ids.mapNotNull { ScenarioRepository.findById(it) }.flatMap { it.steps }.forEach { step ->
            assertNull("tts in ${step.name}", step.ttsText)
            assertNull("sound in ${step.name}", step.soundType)
            assertNull("chassis in ${step.name}", step.chassisMove)
        }
    }

    @Test
    fun sentimentScenariosEndWithAllUsedJointsAtZero() {
        ids.mapNotNull { ScenarioRepository.findById(it) }.forEach { sc ->
            val used = sc.steps.flatMap { s -> s.jointTargets.map { it.motorId } }.toSet()
            val last = sc.steps.last().jointTargets
            val zeroed = last.filter { it.degree == 0f }.map { it.motorId }.toSet()
            // 負面劇本的雙臂/手肘在第一步就是 0，只需回位頸部與雙肩
            val needReturn = sc.steps.flatMap { it.jointTargets }.filter { it.degree != 0f }.map { it.motorId }.toSet()
            assertTrue("${sc.id} not returned: ${needReturn - zeroed} (used=$used)", zeroed.containsAll(needReturn))
        }
    }

    @Test
    fun facesArePerScenarioAndNeutralKeepsDefaultFace() {
        assertNotNull(ScenarioRepository.findById("butler_sentiment_positive")?.face)
        assertNotNull(ScenarioRepository.findById("butler_sentiment_negative")?.face)
        assertNull(ScenarioRepository.findById("butler_sentiment_neutral")?.face)
    }

    @Test
    fun whitelistRejectsOutOfRangeAndFastCommands() {
        assertFalse(JointSafety.isAllowed(JointTarget(Motor.NECK_Y, 30f)))
        assertFalse(JointSafety.isAllowed(JointTarget(Motor.R_SHOULDER_Y, -420f)))
        assertFalse(JointSafety.isAllowed(JointTarget(Motor.L_SHOULDER_X, 100f)))
        assertFalse(JointSafety.isAllowed(JointTarget(Motor.L_SHOULDER_Y, -45f, 300f)))
        assertFalse(JointSafety.isAllowed(JointTarget(99, 0f)))
        assertEquals(10, JointSafety.testedRanges.size)
    }
}
