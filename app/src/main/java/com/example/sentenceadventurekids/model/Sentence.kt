package com.example.sentenceadventurekids.model

data class Sentence(
    val id: String,
    val text: String,
    val words: List<String> = text.split(" "),
    val audioResId: Int? = null,
    val animationType: AnimationType = AnimationType.NONE
)

enum class AnimationType {
    NONE, RUN, JUMP, SLEEP, HAPPY, EAT, SMILE
}

data class Story(
    val id: String,
    val title: String,
    val sentences: List<Sentence>
)
