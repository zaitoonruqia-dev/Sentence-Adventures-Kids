package com.example.sentenceadventurekids.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sentenceadventurekids.ui.screens.home.HomeScreen
import com.example.sentenceadventurekids.ui.screens.journey.JourneyScreen
import com.example.sentenceadventurekids.ui.screens.journey.ReadingLevelScreen
import com.example.sentenceadventurekids.ui.screens.games.SentenceBuilderScreen
import com.example.sentenceadventurekids.ui.screens.parent.ParentDashboardScreen
import com.example.sentenceadventurekids.ui.screens.rewards.RewardsScreen
import com.example.sentenceadventurekids.ui.screens.map.ProgressMapScreen
import com.example.sentenceadventurekids.data.ProgressRepository

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onPlayClick = { navController.navigate(Screen.Journey.route) },
                onRewardsClick = { navController.navigate(Screen.Rewards.route) },
                onParentClick = { navController.navigate(Screen.ParentDashboard.route) },
            ) { navController.navigate(Screen.ProgressMap.route) }
        }
        composable(Screen.Journey.route) {
            JourneyScreen(
                onBack = { navController.popBackStack() },
                onLevelSelect = { levelId ->
                    navController.navigate(Screen.ReadingLevel.createRoute(levelId))
                }
            )
        }
        composable(
            route = Screen.ReadingLevel.route,
            arguments = listOf(
                androidx.navigation.navArgument("levelId") {
                    type = androidx.navigation.NavType.IntType
                }
            )
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("levelId") ?: 1
            ReadingLevelScreen(
                levelId = levelId,
                onBack = { navController.popBackStack() },
                onPlayGame = { sentence ->
                    navController.navigate(Screen.Game.createRoute(sentence))
                }
            )
        }
        composable(Screen.Rewards.route) {
            RewardsScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = Screen.Game.route,
            arguments = listOf(
                androidx.navigation.navArgument("sentence") {
                    type = androidx.navigation.NavType.StringType
                }
            )
        ) { backStackEntry ->
            val sentence = backStackEntry.arguments?.getString("sentence") ?: "The cat is happy"
            SentenceBuilderScreen(
                sentence = sentence,
                onBack = { navController.popBackStack() },
                onComplete = {
                    ProgressRepository.completeLevel(0) // Mini-game bonus
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.ParentDashboard.route) {
            ParentDashboardScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.ProgressMap.route) {
            ProgressMapScreen(
                onBack = { navController.popBackStack() },
                onLevelSelect = { levelId ->
                    navController.navigate(Screen.ReadingLevel.createRoute(levelId))
                }
            )
        }
    }
}
