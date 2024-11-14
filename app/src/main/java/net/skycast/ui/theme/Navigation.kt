package net.skycast.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {    object Weather : Screen("weather")
    object Details : Screen("details/{cityName}") {
        fun createRoute(cityName: String) = "details/$cityName"
    }
    object Favorites : Screen("favorites")
}

@Composable
fun AppNavigation(viewModel: WeatherViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Weather.route
    ) {
        composable(Screen.Weather.route) {
            WeatherScreen(
                viewModel = viewModel,
                onDetailsClick = { cityName ->
                    navController.navigate(Screen.Details.createRoute(cityName))
                },
                onFavoritesClick = {
                    navController.navigate(Screen.Favorites.route)
                }
            )
        }

        composable(Screen.Details.route) { backStackEntry ->
            WeatherDetailsScreen(
                cityName = backStackEntry.arguments?.getString("cityName") ?: "",
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}