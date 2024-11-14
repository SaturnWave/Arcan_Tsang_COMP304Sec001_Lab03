package net.skycast.ui.theme

import net.skycast.domain.WeatherData

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weatherData: WeatherData) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}