package com.juanmediahubph.ooptutorial.domain

import kotlin.random.Random

data class QuestionCandidate(val id: String, val moduleId: String, val difficulty: String)
data class Grade(val correct: Int, val incorrect: Int, val unanswered: Int, val percentage: Double, val passed: Boolean)

object QuizLogic {
    fun grade(correctOptionIds: List<String>, selectedOptionIds: List<String?>, passThreshold: Int): Grade {
        require(correctOptionIds.isNotEmpty())
        require(correctOptionIds.size == selectedOptionIds.size)
        require(passThreshold in 50..100)
        val correct = correctOptionIds.indices.count { selectedOptionIds[it] == correctOptionIds[it] }
        val unanswered = selectedOptionIds.count { it == null }
        val incorrect = selectedOptionIds.size - correct - unanswered
        val percentage = correct * 100.0 / correctOptionIds.size
        return Grade(correct, incorrect, unanswered, percentage, percentage >= passThreshold)
    }

    fun choose(
        questions: List<QuestionCandidate>,
        moduleIds: Set<String>,
        difficulty: String?,
        count: Int,
        random: Random = Random.Default,
        balanced: Boolean = false
    ): List<QuestionCandidate> {
        require(count > 0)
        val eligible = questions.filter { it.moduleId in moduleIds && (difficulty == null || it.difficulty.equals(difficulty, true)) }
        require(eligible.map { it.id }.distinct().size == eligible.size) { "Question IDs must be unique" }
        require(eligible.size >= count) { "Only ${eligible.size} unique matching questions are available" }
        if (!balanced) return eligible.shuffled(random).take(count)

        val groups = eligible.groupBy { it.moduleId }.mapValues { (_, rows) -> rows.shuffled(random).toMutableList() }.toMutableMap()
        val moduleOrder = groups.keys.shuffled(random)
        val selected = mutableListOf<QuestionCandidate>()
        while (selected.size < count) {
            for (module in moduleOrder) {
                val next = groups[module]?.removeFirstOrNull()
                if (next != null) selected += next
                if (selected.size == count) break
            }
        }
        return selected
    }
}
