package net.skycast.infrastructure.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.skycast.domain.Location
import net.skycast.domain.WeatherInfo

@Entity
data class WeatherRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @Embedded
    val location: Location,

    @Embedded
    val weatherInfo: WeatherInfo
)
