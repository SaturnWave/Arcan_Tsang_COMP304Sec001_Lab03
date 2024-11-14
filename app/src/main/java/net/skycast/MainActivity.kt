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
import net.skycast.infrastructure.UseCaseImplementations
import net.skycast.infrastructure.room.AppRepository
import net.skycast.infrastructure.weatherbit.WeatherbitApi
import net.skycast.ui.theme.AppNavigation
import net.skycast.ui.theme.SkyCastTheme
import net.skycast.ui.theme.WeatherViewModel

class MainActivity : ComponentActivity() {

    private val repository by lazy { AppRepository(context = this) }
    private val weatherApi by lazy { WeatherbitApi(key = "d62ef40519fc42989301bd120efc75e0") }
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
                    val viewModel: WeatherViewModel = viewModel(
                        factory = WeatherViewModel.Factory(useCases)
                    )

                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}