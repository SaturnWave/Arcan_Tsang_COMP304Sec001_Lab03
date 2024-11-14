package net.skycast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import net.skycast.infrastructure.UseCaseImplementations
import net.skycast.infrastructure.room.AppRepository
import net.skycast.infrastructure.weatherbit.WeatherbitApi
import net.skycast.ui.theme.*

sealed class Screen(val route: String) {
    object Weather : Screen("weather")
    object Details : Screen("details/{cityName}") {
        fun createRoute(cityName: String) = "details/$cityName"
    }
    object Favorites : Screen("favorites")
}

class MainActivity : ComponentActivity() {

    private val repository by lazy { AppRepository(context = this) }
    private val weatherApi by lazy { WeatherbitApi(key = "7d1e78e060974166a89072938cd2b335") }
    private val useCases by lazy { UseCaseImplementations(repository, weatherApi) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SkyCastTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel = viewModel { WeatherViewModel(useCases) }

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

                        composable(
                            route = Screen.Details.route,
                            arguments = listOf(
                                navArgument("cityName") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val cityName = backStackEntry.arguments?.getString("cityName") ?: ""
                            WeatherDetailsScreen(
                                cityName = cityName,
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
            }
        }
    }
}