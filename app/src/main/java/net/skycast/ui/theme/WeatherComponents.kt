package net.skycast.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.skycast.domain.WeatherData

@OptIn(ExperimentalMaterial3Api::class)  // Add this annotation to handle experimental API warning
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    onDetailsClick: (String) -> Unit,
    onFavoritesClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SkyCast Weather") },
                actions = {
                    IconButton(onClick = onFavoritesClick) {
                        Text("★")  // Simple star text for now
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                when (uiState) {
                    is WeatherUiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    is WeatherUiState.Success -> {
                        WeatherCard(
                            weatherData = (uiState as WeatherUiState.Success).weatherData,
                            onDetailsClick = onDetailsClick
                        )
                    }
                    is WeatherUiState.Error -> {
                        ErrorMessage(
                            message = (uiState as WeatherUiState.Error).message,
                            onRetry = { viewModel.fetchWeather() }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailsScreen(
    cityName: String,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather Details: $cityName") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("←")  // Simple back arrow text for now
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = "Detailed weather information for $cityName",
                modifier = Modifier.padding(16.dp)
            )
            // We'll add more detailed weather information here later
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorite Locations") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("←")  // Simple back arrow text for now
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = "Your favorite locations will appear here",
                modifier = Modifier.padding(16.dp)
            )
            // We'll add the list of favorite locations here later
        }
    }
}

@Composable
fun WeatherCard(
    weatherData: WeatherData,
    onDetailsClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = weatherData.location.cityName ?: "Unknown Location",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${weatherData.current.temperature}°C",
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = weatherData.current.weatherDescription ?: "",
                style = MaterialTheme.typography.bodyLarge
            )

            weatherData.current.humidity?.let {
                Text("Humidity: $it%")
            }

            weatherData.current.windSpeed?.let {
                Text("Wind Speed: $it m/s")
            }

            Button(
                onClick = { weatherData.location.cityName?.let(onDetailsClick) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("View Details")
            }
        }
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}