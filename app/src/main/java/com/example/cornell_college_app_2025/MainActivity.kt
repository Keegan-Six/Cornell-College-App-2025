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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cornell_college_app_2025.ui.theme.CornellCollegeApp2025Theme
import java.time.LocalTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate

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

@JsonClass(generateAdapter = true)
data class Forecast(
    @Json(name = "forecastday") val forecastday: List<ForecastDay>
)

@JsonClass(generateAdapter = true)
data class ForecastDay(
    @Json(name = "date") val date: String,
    @Json(name = "day") val day: Day,
    @Json(name = "hour") val hour: List<Hour>
)

@JsonClass(generateAdapter = true)
data class Day(
    @Json(name = "maxtemp_f") val maxtemp_f: Double,
    @Json(name = "mintemp_f") val mintemp_f: Double,
    @Json(name = "condition") val condition: Condition
)

@JsonClass(generateAdapter = true)
data class Hour(
    @Json(name = "time") val time: String,
    @Json(name = "temp_f") val temp_f: Double,
    @Json(name = "condition") val condition: Condition
)
//---------------------------OBJECTS_CLASSES_AND_INTERFACES---------------------------------------
// App theme
object AppTheme {
    var backgroundColor by mutableStateOf(Color.White)
    var textColor by mutableStateOf(Color.Black)
    var buttonandlinecolor by mutableStateOf(Color(0xFF9C27B0))
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
//---------------------------------------MAIN_FUNCTIONS--------------------------------------------
// Displays basic weather info on main screen
@Composable
fun WeatherDisplay(location: String = "Cedar Rapids, IA") {
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
                            textDecoration = TextDecoration.Underline),
                        color = AppTheme.textColor)
                    Text(text = weatherData!!.current.condition.text,
                        color = AppTheme.textColor)
                    Text(text = "Current Temperature: ${weatherData!!.current.temp_f}°F",
                        fontSize = 16.sp,
                        color = AppTheme.textColor)
                    Text(text = "Wind: ${weatherData!!.current.wind_mph} mph",
                        color = AppTheme.textColor)
                    Text(text = "Max Temp Today: ${weatherData!!.forecast.forecastday[0].day.maxtemp_f}°F",
                        color = AppTheme.textColor)
                    Text(text = "Min Temp Today: ${weatherData!!.forecast.forecastday[0].day.mintemp_f}°F",
                        color = AppTheme.textColor)

                }
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https:" + weatherData!!.current.condition.icon)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Current Weather Icon",
                    modifier = Modifier.width(128.dp)
                        .height(128.dp) // Increased size for clarity
                )
            }
        }
    }
}
// Displays Forecast on Weather Screen 2
@Composable
fun Forecast(location: String = "52314") {
    var forecastData by remember { mutableStateOf<WeatherResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // Fetch weather data when the composable is first launched
    LaunchedEffect(key1 = location) {
        isLoading = true
        try {
            forecastData = WeatherApi.getWeatherData(location)
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
        } else if (forecastData != null) {
           for (day in forecastData!!.forecast.forecastday) {
               Text(text = day.date,
                   modifier = Modifier.padding(start = 8.dp),
                   color = AppTheme.textColor)
               PurpleLine()
               Row() {
                   Text(text = day.day.condition.text,
                       modifier = Modifier.padding(start = 8.dp),
                       color = AppTheme.textColor)
                   AsyncImage(
                       model = ImageRequest.Builder(LocalContext.current)
                           .data("https:" + day.day.condition.icon)
                           .crossfade(true)
                           .build(),
                       contentDescription = "Current Weather Icon",
                       modifier = Modifier.width(24.dp)
                           .height(24.dp) // Increased size for clarity
                   )
               }
               Text(text = "Max Temp: ${day.day.maxtemp_f}°F",
                   modifier = Modifier.padding(start = 8.dp),
                   color = AppTheme.textColor)
               Text(text = "Min Temp: ${day.day.mintemp_f}°F",
                   modifier = Modifier.padding(start = 8.dp),
                   color = AppTheme.textColor)
           }
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
    Column(modifier = Modifier
        .fillMaxSize()
        .background(AppTheme.backgroundColor)){
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            OpeningScreen(navController = navController, modifier = Modifier.fillMaxSize())
        }
        composable("weather2_screen") {
            Weather2Screen(navController = navController)
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
        composable("options_menu") {
            OptionsMenu(navController = navController)
        }
    }
    }
}
//------------------------------HELPER_FUNCTIONS---------------------------------------------------
// finds and returns correct minutes of time
fun minutes(): String {
    val minutes = LocalTime.now().minute
    if (minutes < 10) {
        return "0$minutes"
    }
    return minutes.toString()
}
// finds and returns correct hours of time
fun hours(): String {
    val hours = LocalTime.now().hour
    if (hours > 12) {
        return "${hours - 12}"
    }
    return hours.toString()
}
// button to navigate to main screen
@Composable
fun Home(navController: NavHostController){
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
// Purple line used many times in design
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
//-----------------------------SCREENS-------------------------------------------------------------
// Opening Screen or Home Screen, displays buttons to navigate to other screens
@Composable
fun OpeningScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Welcome to my app!",
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 20.sp,
            color = AppTheme.textColor)
        PurpleLine()
        Text(text = "The time is ${hours()}:${minutes()} ${if (LocalTime.now().hour < 12) "AM" else "PM"}",
            modifier = Modifier.padding(start = 8.dp),
            color = AppTheme.textColor)
        Text(text = "Current Date: ${LocalDate.now()}",
            modifier = Modifier.padding(start = 8.dp),
            color = AppTheme.textColor)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Current Weather",
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 20.sp,
            color = AppTheme.textColor)
        PurpleLine()
        WeatherDisplay("52314")
        Button(onClick = { navController.navigate("weather2_screen") },
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))) {
            Text(text = "7 Day Weather Forecast")
        }
        Text(text = "Navigation", modifier = Modifier.padding(start = 8.dp),
            fontSize = 20.sp,
            color = AppTheme.textColor)
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
        Text(text = "Quick Links",
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 20.sp,
            color = AppTheme.textColor)
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
        Button(onClick = { navController.navigate("options_menu") },
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
            Text(text = "Options")
        }
    }
}
// Weather Screen, displays more weather information
@Composable
fun Weather2Screen(navController: NavHostController, modifier: Modifier = Modifier) {
    Column {
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "7 Day Weather Forecast for Mount Vernon, Iowa",
            modifier = Modifier.padding(start = 8.dp),
            color = AppTheme.textColor)
        PurpleLine()
        Forecast("52314")
        Home(navController)
    }
}
// Schedule Screen, displays schedule for student
// TODO: Add schedule functionality
@Composable
fun ScheduleScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Column {
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "Schedule Screen",
            modifier = Modifier.padding(start = 8.dp),
            color = AppTheme.textColor)
        PurpleLine()
        Home(navController)
    }
}
// Event Screen, displays events for Cornell College
@Composable
fun EventScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Column {
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "Event Screen",
            modifier = Modifier.padding(start = 8.dp),
            color = AppTheme.textColor)
        PurpleLine()
        Home(navController)
    }
}
// Menu Screen, displays HillTop Dining Menu
@Composable
fun MenuScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Column {
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "Menu Screen",
            modifier = Modifier.padding(start = 8.dp),
            color = AppTheme.textColor)
        WebViewScreen(
            url = "https://cornell.cafebonappetit.com/",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            scroll = true
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(30.dp)) {
            Home(navController)
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
        Text(text = "Zamora's Market Menu",
            modifier = Modifier.padding(start = 8.dp),
            color = AppTheme.textColor)
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
        Home(navController)
    }
}
// Displays Options menu
@Composable
fun OptionsMenu(navController: NavHostController, modifier: Modifier = Modifier) {
    Column() {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Options Menu",
            modifier = Modifier.padding(start = 8.dp),
            color = AppTheme.textColor
        )
        PurpleLine()
            Text(
                text = "Change Background Color",
                modifier = Modifier.padding(start = 8.dp),
                color = AppTheme.textColor
            )
        PurpleLine()
        Row {
            Button(
                onClick = { AppTheme.backgroundColor = Color.Black },
                modifier = Modifier.padding(start = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            )
            {
                Text(text = "Black")
            }
            Button(
                onClick = { AppTheme.backgroundColor = Color.White },
                modifier = Modifier.padding(start = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(text = "White", color = Color.Black)
            }
        }
        Home(navController)
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
