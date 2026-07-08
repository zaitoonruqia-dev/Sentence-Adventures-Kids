package com.ruqiazaitoon.sentenceadventurekids.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object ProgressRepository {
    private val _stars = MutableStateFlow(5)
    val stars: StateFlow<Int> = _stars.asStateFlow()

    private val _coins = MutableStateFlow(100)
    val coins: StateFlow<Int> = _coins.asStateFlow()

    private val _unlockedLevels = MutableStateFlow(listOf(1))
    val unlockedLevels: StateFlow<List<Int>> = _unlockedLevels.asStateFlow()

    private val _badges = MutableStateFlow(listOf("Beginner"))
    val badges: StateFlow<List<String>> = _badges.asStateFlow()

    private val _sentencesRead = MutableStateFlow(0)
    val sentencesRead: StateFlow<Int> = _sentencesRead.asStateFlow()

    fun completeLevel(level: Int) {
        val nextLevel = if (level == 0) 0 else level + 1
        if (nextLevel > 0 && nextLevel <= 4 && nextLevel !in _unlockedLevels.value) {
            _unlockedLevels.value += nextLevel
        }

        _stars.value += if (level == 0) 1 else 3
        _coins.value += if (level == 0) 5 else 12
        if (level > 0) _sentencesRead.value += 3

        val newBadge = when (level) {
            1 -> "Word Explorer"
            2 -> "Sentence Builder"
            3 -> "Story Starter"
            4 -> "Adventure Reader"
            else -> null
        }
        if (newBadge != null && newBadge !in _badges.value) {
            _badges.value += newBadge
        }
    }

    fun incrementSentencesRead() {
        _sentencesRead.value += 1
    }

    fun addRewardedPracticeBonus() {
        _stars.value += 2
        _coins.value += 20
        if ("Practice Booster" !in _badges.value) {
            _badges.value += "Practice Booster"
        }
    }

    fun resetAll() {
        _stars.value = 5
        _coins.value = 100
        _unlockedLevels.value = listOf(1)
        _badges.value = listOf("Beginner")
        _sentencesRead.value = 0
    }
}
