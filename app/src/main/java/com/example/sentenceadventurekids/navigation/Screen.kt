package com.example.sentenceadventurekids.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Journey : Screen("journey")
    object ReadingLevel : Screen("reading_level/{levelId}") {
        fun createRoute(levelId: Int) = "reading_level/$levelId"
    }
    object Game : Screen("game/{sentence}") {
        fun createRoute(sentence: String) = "game/$sentence"
    }
    object Rewards : Screen("rewards")
    object ParentDashboard : Screen("parent")
    object ProgressMap : Screen("map")
}
