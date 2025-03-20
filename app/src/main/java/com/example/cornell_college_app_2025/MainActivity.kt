package com.example.cornell_college_app_2025

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cornell_college_app_2025.ui.theme.CornellCollegeApp2025Theme
import com.example.cornell_college_app_2025.R
import java.time.LocalTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
// Data classes for JSON Parsing
// Weather API Data Classes
@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "location") val location: Location,
    @Json(name = "current") val current: Current
)

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "name") val name: String,
    @Json(name = "region") val region: String,
    @Json(name = "country") val country: String
)

@JsonClass(generateAdapter = true)
data class Current(
    @Json(name = "temp_f") val temp_f: Double,
    @Json(name = "condition") val condition: Condition,
    @Json(name = "wind_mph") val wind_mph: Double,
    @Json(name = "feelslike_f") val feelslike_f: Double,
)

@JsonClass(generateAdapter = true)
data class Condition(
    @Json(name = "text") val text: String,
    @Json(name = "icon") val icon: String,
    @Json(name = "code") val code: Int
)

// Weather API Interface
interface WeatherApiService {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String
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
            WeatherApi.weatherApiService.getCurrentWeather(API_KEY, location)
        } catch (e: Exception) {
            Log.e("Weather", "Error getting weather data", e)
            null
        }
    }
}
@Composable
fun WeatherDisplay(location: String = "Mount Vernon, IA") {
    var weatherData by remember { mutableStateOf<WeatherResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // Fetch weather data when the composable is first launched
    LaunchedEffect(key1 = location) {
        isLoading = true
        try {
            weatherData = WeatherApi.getWeatherData(location)
        } catch (e: Exception) {
            Log.e("Weather", "Error getting weather data", e)
            error = "Error getting weather data"
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth().padding(top = 8.dp).padding(bottom = 8.dp)
    ) {
        if (isLoading) {
            Text(text = "Loading weather...")
        } else if (error != null) {
            Text(text = "Error: $error")
        } else if (weatherData != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = "${weatherData!!.location.name}, ${weatherData!!.location.region}",
                        fontSize = 16.sp,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline))
                    Text(text = weatherData!!.current.condition.text)
                    Text(text = "Temperature: ${weatherData!!.current.temp_f}°F", fontSize = 16.sp)
                    Text(text = "Wind: ${weatherData!!.current.wind_mph} mph")

                }
            }
            // more temperature data for rest of day here
        }
    }
}
    @Composable
// WebViewScreen for displaying webpages
fun WebViewScreen(url: String, modifier: Modifier = Modifier, scroll: Boolean) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        super.onPageFinished(view, url)

                        // Scroll to the menu section using JavaScript
                        if (scroll) {
                        view.evaluateJavascript(
                            "javascript:window.scrollTo(0, document.documentElement.scrollHeight / 5.5);",
                            null
                        )}
                        Log.i("WEBVIEW","Page Finished Loading")
                    }
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        request: WebResourceRequest
                    ): Boolean {
                        return false
                    }
                }
                //webViewClient = WebViewClient() // Prevent opening in external browser
                settings.javaScriptEnabled = true
                loadUrl(url)
            }
        },
        modifier = modifier
    )
}

@Composable
// Controls navigation between screens, allows multiple screens to be used
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            OpeningScreen(navController = navController, modifier = Modifier.fillMaxSize())
        }
        composable("menu_screen") {
            MenuScreen(navController = navController)
        }
        composable("menu_screen_2") {
            MenuScreen2(navController = navController)
        }
        composable("schedule_screen") {
            ScheduleScreen(navController = navController)
        }
        composable("event_screen") {
            EventScreen(navController = navController)
        }
    }
}
// finds and returns correct mintues of time
fun minutes(): String {
    val minutes = LocalTime.now().minute
    if (minutes < 10) {
        return "0$minutes"
    }
    return minutes.toString()
}
// Opening Screen or Home Screen, displays buttons to navigate to other screens
// TODO: finish Weather
@Composable
fun OpeningScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Welcome to my app!", modifier = Modifier.padding(start = 8.dp))
        Text(text = "The time is ${LocalTime.now().hour - 12}:${minutes()}", modifier = Modifier.padding(start = 8.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Current Weather", modifier = Modifier.padding(start = 8.dp),  fontSize = 20.sp)
        PurpleLine()
        WeatherDisplay("Mount Vernon, IA")

        Text(text = "Navigation", modifier = Modifier.padding(start = 8.dp), fontSize = 20.sp)
        PurpleLine()
        Button(onClick = { navController.navigate("menu_screen") },
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))) {
            Text(text = "Dining Menus")
        }
        Button(onClick = { navController.navigate("schedule_screen") },
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))) {
            Text(text = "Your Schedule")
        }
        Button(onClick = { navController.navigate("event_screen") },
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))) {
            Text(text = "Events")
        }
        Text(text = "Quick Links", modifier = Modifier.padding(start = 8.dp), fontSize = 20.sp)
        PurpleLine()
        Row {
            Button(
                onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW,
                        "https://moodle.cornellcollege.edu/".toUri()))
                },
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))) {
                Text(text = "Moodle")
            }
            Button(onClick = {
                context.startActivity(Intent(Intent.ACTION_VIEW,
                    "https://selfservice.cornellcollege.edu/PowerCampusSelfService/Home".toUri()))
            },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))) {
                Text(text = "Self-Service")
            }
            Button(onClick = {
                context.startActivity(Intent(Intent.ACTION_VIEW,
                    "https://www.cornellcollege.edu/library/".toUri()))
            },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))) {
                Text(text = "Library")
            }
        }
    }
}
// Schedule Screen, displays schedule for student
// TODO: Add schedule functionality
@Composable
fun ScheduleScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Column {
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "Schedule Screen", modifier = Modifier.padding(start = 8.dp))
        PurpleLine()
        Button(
            onClick = { navController.navigate("main_screen") },
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
        ) {
            Text(text = "Home")
        }
    }
}
// Event Screen, displays events for Cornell College
@Composable
fun EventScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Column {
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "Event Screen", modifier = Modifier.padding(start = 8.dp))
        PurpleLine()
        Button(
            onClick = { navController.navigate("main_screen") },
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
        ) {
            Text(text = "Home")
        }
    }
}
@Composable
fun PurpleLine() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        thickness = 2.dp,
        color = Color(0xFF9C27B0)
    )
}
// Menu Screen, displays HillTop Dining Menu
@Composable
fun MenuScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Column {
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "Menu Screen")
        WebViewScreen(
            url = "https://cornell.cafebonappetit.com/",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            scroll = true
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(30.dp)) {
            Button(
                onClick = { navController.navigate("main_screen") },
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
            ) {
                Text(text = "Home")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { navController.navigate("menu_screen_2") },
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
            ) {
                Text(text = "Zamora's Market Menu")
            }
        }
    }
}
// Displays Zamora's Market Menu
@Composable
fun MenuScreen2(navController: NavHostController, modifier: Modifier = Modifier) {
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Zamora's Market Menu", modifier = Modifier.padding(start = 8.dp))
        PurpleLine()
        Image(
            painter = painterResource(R.drawable.zamora1),
            contentDescription = "Zamora's Market Menu",
            modifier = Modifier.fillMaxWidth()
        )
        Image(
            painter = painterResource(R.drawable.zamora2),
            contentDescription = "Zamora's Market Menu 2",
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { navController.navigate("main_screen") },
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
        ) {
            Text(text = "Home")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OpeningScreenPreview() {
    CornellCollegeApp2025Theme {
        val navController = rememberNavController()
        OpeningScreen(navController = navController)
    }
}
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