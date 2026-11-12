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
    fun butlerTimingScenariosUseBuiltInMotionsWithSafeLoops() {
        val think = ScenarioRepository.findById("butler_think_loop")!!
        val listen = ScenarioRepository.findById("butler_listen_nod")!!
        val bye = ScenarioRepository.findById("butler_bye_happy")!!
        assertEquals("888_ML_Tinythink_25", think.steps[0].motionName)
        assertNotNull(think.steps[0].holdAtMs)
        assertNotNull(listen.steps[0].holdAtMs)
        assertEquals("666_DA_Listen", listen.steps[0].motionName)
        assertEquals("666_BA_Nodhead", listen.steps.last().motionName)
        assertNull(listen.steps.last().holdAtMs)
        assertEquals("000_J1_Bye", bye.steps[0].motionName)
        assertNotNull(bye.face)
        listOf(think, listen, bye).flatMap { it.steps }.forEach { step ->
            assertFalse("face video in ${step.name}", step.motionWithFace)
            assertNull("tts in ${step.name}", step.ttsText)
            assertNull("sound in ${step.name}", step.soundType)
            assertNull("chassis in ${step.name}", step.chassisMove)
            step.holdAtMs?.let { assertTrue("hold cap in ${step.name}", step.timeoutMs in (it + 1)..30_000L) }
        }
    }

    @Test
    fun holdWigglesAreSmallAndSlow() {
        ScenarioRepository.allScenarios.flatMap { it.steps }.flatMap { it.holdWiggle }.forEach { w ->
            assertTrue("range ${w.motorId}", w.minOffset < w.maxOffset)
            assertTrue("amp ${w.motorId}", maxOf(-w.minOffset, w.maxOffset) <= JointSafety.MAX_WIGGLE_AMPLITUDE)
            assertTrue("speed ${w.motorId}", w.speed > 0f && w.speed <= JointSafety.MAX_WIGGLE_SPEED)
            assertTrue("dwell ${w.motorId}", w.minDwellMs in 300L..w.maxDwellMs)
        }
        assertFalse(JointSafety.isWiggleAllowed(Motor.NECK_Z, 0f, 16f, 20f))
        assertFalse(JointSafety.isWiggleAllowed(Motor.NECK_Z, 0f, 10f, 50f))
        assertFalse(JointSafety.isWiggleAllowed(Motor.NECK_Z, Float.NaN, 0f, 20f))
        assertFalse(JointSafety.isWiggleAllowed(Motor.NECK_Y, 18f, 23f, 20f))
        assertTrue(JointSafety.isWiggleAllowed(Motor.NECK_Y, 15.6f, 19.6f, 20f))
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
