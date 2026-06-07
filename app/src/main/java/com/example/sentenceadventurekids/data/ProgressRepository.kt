package com.example.sentenceadventurekids.data

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
        // Unlock next level
        val nextLevel = if (level == 0) 0 else level + 1
        if (nextLevel > 0 && nextLevel <= 4 && nextLevel !in _unlockedLevels.value) {
            _unlockedLevels.value += nextLevel
        }

        _stars.value += 3
        _coins.value += 10
        _sentencesRead.value += 3 // Each level has 3 sentences

        if (level == 3 && "Sentence Master" !in _badges.value) {
            _badges.value += "Sentence Master"
        }
    }

    fun incrementSentencesRead() {
        _sentencesRead.value += 1
    }
}
