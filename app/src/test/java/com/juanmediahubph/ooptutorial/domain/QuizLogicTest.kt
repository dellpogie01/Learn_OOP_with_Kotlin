package com.juanmediahubph.ooptutorial.domain

import com.juanmediahubph.ooptutorial.data.seed.SeedContent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class QuizLogicTest {
    @Test fun gradeUsesUnroundedBoundaryAndSavedThreshold() {
        val exact = QuizLogic.grade(listOf("a", "b"), listOf("a", "x"), 50)
        assertEquals(50.0, exact.percentage, 0.0)
        assertTrue(exact.passed)
        assertFalse(QuizLogic.grade(listOf("a", "b", "c"), listOf("a", "x", "x"), 67).passed)
        assertTrue(QuizLogic.grade(listOf("a", "b", "c"), listOf("a", "b", "c"), 100).passed)
    }

    @Test fun unansweredQuestionsAreZeroPointsNotIncorrect() {
        val result = QuizLogic.grade(listOf("a", "b", "c", "d"), listOf("a", "wrong", null, null), 50)
        assertEquals(1, result.correct)
        assertEquals(1, result.incorrect)
        assertEquals(2, result.unanswered)
        assertEquals(25.0, result.percentage, 0.0)
    }

    @Test fun selectionIsUniqueAndBalancedAcrossModules() {
        val bank = (1..12).flatMap { module -> (1..12).map { q -> QuestionCandidate("m$module-q$q", "m$module", "Beginner") } }
        val selected = QuizLogic.choose(bank, (1..12).map { "m$it" }.toSet(), null, 30, Random(5), balanced = true)
        assertEquals(30, selected.size)
        assertEquals(30, selected.map { it.id }.distinct().size)
        val counts = selected.groupingBy { it.moduleId }.eachCount().values
        assertTrue(counts.maxOrNull()!! - counts.minOrNull()!! <= 1)
    }

    @Test fun selectionRejectsInsufficientUniqueQuestionsAndHonorsDifficulty() {
        val bank = listOf(QuestionCandidate("1", "m1", "beginner"), QuestionCandidate("2", "m1", "advanced"))
        assertEquals(1, QuizLogic.choose(bank, setOf("m1"), "beginner", 1).size)
        val error = runCatching { QuizLogic.choose(bank, setOf("m1"), "beginner", 2) }.exceptionOrNull()
        assertTrue(error?.message.orEmpty().contains("Only 1"))
    }

    @Test fun bundledSeedHasTwelveCompleteModulesAndAtLeastTwelveQuestionsPerModule() {
        SeedContent.validate()
        assertEquals(12, SeedContent.modules.size)
        assertEquals(24, SeedContent.lessons.size)
        assertEquals(144, SeedContent.questions.size)
        SeedContent.modules.forEach { module ->
            assertEquals(2, SeedContent.lessons.count { it.moduleId == module.id })
            assertTrue(SeedContent.questions.count { it.moduleId == module.id } >= 12)
        }
    }
}
