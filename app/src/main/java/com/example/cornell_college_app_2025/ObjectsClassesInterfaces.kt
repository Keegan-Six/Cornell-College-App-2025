package com.example.cornell_college_app_2025

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//--------------------------------JSON_DATA_CLASSES-----------------------------------------------
// Weather API Data Classes
@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "location") val location: Location,
    @Json(name = "current") val current: Current,
    @Json(name = "forecast") val forecast: Forecast
)

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "name") val name: String,
    @Json(name = "region") val region: String,
    @Json(name = "country") val country: String
)

@JsonClass(generateAdapter = true)
data class Current(
    @Json(name = "temp_f") val tempF: Double,
    @Json(name = "condition") val condition: Condition,
    @Json(name = "wind_mph") val windMPH: Double,
    @Json(name = "feelslike_f") val feelsLikeF: Double,
)

@JsonClass(generateAdapter = true)
data class Condition(
    @Json(name = "text") val text: String,
    @Json(name = "icon") val icon: String,
    @Json(name = "code") val code: Int
)

@JsonClass(generateAdapter = true)
data class Forecast(
    @Json(name = "forecastday") val forecastDay: List<ForecastDay>
)

@JsonClass(generateAdapter = true)
data class ForecastDay(
    @Json(name = "date") val date: String,
    @Json(name = "day") val day: Day,
    @Json(name = "hour") val hour: List<Hour>
)

@JsonClass(generateAdapter = true)
data class Day(
    @Json(name = "maxtemp_f") val maxTempF: Double,
    @Json(name = "mintemp_f") val minTempF: Double,
    @Json(name = "condition") val condition: Condition
)

@JsonClass(generateAdapter = true)
data class Hour(
    @Json(name = "time") val time: String,
    @Json(name = "temp_f") val tempF: Double,
    @Json(name = "condition") val condition: Condition
)
//---------------------------OBJECTS_CLASSES_AND_INTERFACES---------------------------------------
// App theme
object AppTheme {
    var backgroundColor by mutableStateOf(Color.White)
    var textColor by mutableStateOf(Color(0xFF2C2A29))
    var uiElementColor by mutableStateOf(Color(0xFF523178))
    var font by mutableStateOf(MyFontFamily)
}
//fonts
val MyFontFamily = FontFamily(
    Font(R.font.brlnsr, FontWeight.Normal),
    Font(R.font.brlnsdb, FontWeight.Bold),
    Font(R.font.brlnsb, FontWeight.ExtraBold),
)
// Weather API Interface
interface WeatherApiService {
    @GET("forecast.json")
    suspend fun getWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int = 7
    ): WeatherResponse
}
object WeatherApi {
    private const val BASE_URL = "https://api.weatherapi.com/v1/"
    private const val API_KEY = "afc89ddec06b49fc9fb193227251903"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    val weatherApiService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }

    suspend fun getWeatherData(location: String): WeatherResponse? {
        return try {
            weatherApiService.getWeather(API_KEY, location)
        } catch (e: Exception) {
            Log.e("Weather", "Error getting weather data", e)
            null
        }
    }
}
