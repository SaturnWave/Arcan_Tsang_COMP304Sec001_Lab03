package net.skycast.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.skycast.application.WeatherParameters
import net.skycast.domain.WeatherData
import net.skycast.infrastructure.UseCaseImplementations

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weatherData: WeatherData) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

class WeatherViewModel(
    private val useCases: UseCaseImplementations
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState

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
                _uiState.value = WeatherUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    class Factory(private val useCases: UseCaseImplementations) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WeatherViewModel(useCases) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}