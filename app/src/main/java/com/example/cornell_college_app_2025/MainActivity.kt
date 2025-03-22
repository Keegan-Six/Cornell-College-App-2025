package com.example.cornell_college_app_2025

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.cornell_college_app_2025.ui.theme.CornellCollegeApp2025Theme
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// SQL Info
// https://developer.android.com/training/data-storage/sqlite

// "Main" function, starts app and starts Navigation
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CornellCollegeApp2025Theme {
                    Navigation()
            }
        }
    }
}
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
    var textColor by mutableStateOf(Color.Black)
    var uiElementColor by mutableStateOf(Color(0xFF9C27B0))
}
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

//-----------------------------PREVIEWS-----------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun OpeningScreenPreview() {
    CornellCollegeApp2025Theme {
        val navController = rememberNavController()
        OpeningScreen(navController = navController)
    }
}
/*
@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    CornellCollegeApp2025Theme {
        val navController = rememberNavController()
        MenuScreen(navController = navController)
    }
}
@Preview(showBackground = true)
@Composable
fun MenuScreen2Preview() {
    CornellCollegeApp2025Theme {
        val navController = rememberNavController()
        MenuScreen2(navController = navController)
    }
}
@Preview(showBackground = true)
@Composable
fun ScheduleScreenPreview() {
    CornellCollegeApp2025Theme {
        val navController = rememberNavController()
        ScheduleScreen(navController = navController)
    }
}
 */
@Preview(showBackground = true)
@Composable
fun OptionsMenuPreview() {
    CornellCollegeApp2025Theme {
        val navController = rememberNavController()
        OptionsMenu(navController = navController)
    }
}
