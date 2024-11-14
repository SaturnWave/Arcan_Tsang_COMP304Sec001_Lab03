package net.skycast.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.skycast.application.WeatherParameters
import net.skycast.infrastructure.UseCaseImplementations

class WeatherViewModel(
    private val useCases: UseCaseImplementations
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState

    init {
        fetchWeather()
    }

    fun fetchWeather(city: String = "Toronto", country: String = "CA") {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val weatherData = useCases.GetWeatherData(
                    WeatherParameters(city = city, country = country, days = 7)
                )
                _uiState.value = WeatherUiState.Success(weatherData)
                useCases.SaveWeatherData(weatherData)
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}