package com.example.cornell_college_app_2025

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

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
                        fontSize = 20.sp,
                        style = TextStyle(
                            fontFamily = AppTheme.font,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline),
                        color = AppTheme.textColor)
                    Text(text = weatherData!!.current.condition.text,
                        fontSize = 18.sp,
                        style = TextStyle(
                            fontFamily = AppTheme.font,
                            fontWeight = FontWeight.Normal),
                        color = AppTheme.textColor)
                    Text(text = "Current Temperature: ${weatherData!!.current.tempF}°F",
                        fontSize = 18.sp,
                        style = TextStyle(
                            fontFamily = AppTheme.font,
                            fontWeight = FontWeight.Normal),
                        color = AppTheme.textColor)
                    Text(text = "Wind: ${weatherData!!.current.windMPH} mph",
                        fontSize = 18.sp,
                        style = TextStyle(
                            fontFamily = AppTheme.font,
                            fontWeight = FontWeight.Normal),
                        color = AppTheme.textColor)
                    Text(text = "Max Temp Today: ${weatherData!!.forecast.forecastDay[0].day.maxTempF}°F",
                        fontSize = 18.sp,
                        style = TextStyle(
                            fontFamily = AppTheme.font,
                            fontWeight = FontWeight.Normal),
                        color = AppTheme.textColor)
                    Text(text = "Min Temp Today: ${weatherData!!.forecast.forecastDay[0].day.minTempF}°F",
                        fontSize = 18.sp,
                        style = TextStyle(
                            fontFamily = AppTheme.font,
                            fontWeight = FontWeight.Normal),
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
            for (day in forecastData!!.forecast.forecastDay) {
                Text(text = day.date,
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 20.sp,
                    style = TextStyle(fontFamily = AppTheme.font,
                        fontWeight = FontWeight.Normal),
                    color = AppTheme.textColor)
                PurpleLine()
                Row {
                    Text(text = day.day.condition.text,
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 18.sp,
                        style = TextStyle(fontFamily = AppTheme.font,
                            fontWeight = FontWeight.Normal),
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
                Text(text = "Max Temp: ${day.day.maxTempF}°F",
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 18.sp,
                    style = TextStyle(fontFamily = AppTheme.font,
                        fontWeight = FontWeight.Normal),
                    color = AppTheme.textColor)
                Text(text = "Min Temp: ${day.day.minTempF}°F",
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 18.sp,
                    style = TextStyle(fontFamily = AppTheme.font,
                        fontWeight = FontWeight.Normal),
                    color = AppTheme.textColor)
            }
        }
    }
}
@SuppressLint("SetJavaScriptEnabled")
@Composable
// WebViewScreen for displaying webpages
fun WebViewScreen(url: String, modifier: Modifier = Modifier, scroll: Int) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        super.onPageFinished(view, url)
                        // Scroll to the menu section using JavaScript
                        if (scroll == 1) {
                            view.evaluateJavascript(
                                "javascript:window.scrollTo(0, document.documentElement.scrollHeight / 5);",
                                null
                            )}
                        // scroll to the events section using JavaScript
                        else if (scroll == 2) {
                            view.evaluateJavascript(
                                "javascript:window.scrollTo(0, document.documentElement.scrollHeight / 2.5);",
                                null
                            )
                        }
                    }
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        request: WebResourceRequest
                    ): Boolean {
                        return false
                    }
                }
                settings.javaScriptEnabled = true
                loadUrl(url)
            }
        },
        modifier = modifier
    )
}
@Composable
fun CornellCollegeMap() {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionGranted = isGranted
    }

    // Request permission when the composable is launched
    LaunchedEffect(key1 = true) {
        val permissionStatus = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true
        } else {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    val cornellCollege = LatLng(41.925, -91.425)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cornellCollege, 17f)
    }
    if (permissionGranted) {
        GoogleMap(
            modifier = Modifier.fillMaxWidth().padding(10.dp).height(700.dp),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = true, myLocationButtonEnabled = true),
            properties = MapProperties(isMyLocationEnabled = true)
        ) {}
    }
}
@Composable
// Controls navigation between screens, allows multiple screens to be used
fun Navigation() {
    val navController = rememberNavController()
    Column(modifier = Modifier
        .fillMaxSize()
        .background(AppTheme.backgroundColor)){
        NavHost(navController = navController, startDestination = "login_screen") {
            composable("login_screen") {
                LoginScreen(navController = navController)
            }
            composable(
                route = "main_screen/{name}",
                arguments = listOf(navArgument("name") { type = NavType.StringType }))
            {
                backStackEntry -> val name = backStackEntry.arguments?.getString("name")
                MainScreen(navController = navController, name ?: "")
            }
            composable(
                route = "weather2_screen/{name}",
                arguments = listOf(navArgument("name") { type = NavType.StringType }))
            {
                backStackEntry -> val name = backStackEntry.arguments?.getString("name")
                Weather2Screen(navController = navController, name ?: "")
            }
            composable(
                route = "menu_screen/{name}",
                arguments = listOf(navArgument("name") { type = NavType.StringType }))
            {
                backStackEntry -> val name = backStackEntry.arguments?.getString("name")
                MenuScreen(navController = navController, name ?:"")
            }
            composable(
                route = "menu_screen_2/{name}",
                arguments = listOf(navArgument("name") { type = NavType.StringType }))
            {
                backStackEntry -> val name = backStackEntry.arguments?.getString("name")
                MenuScreen2(navController = navController, name ?: "")
            }
            composable(
                route = "schedule_screen/{name}",
                arguments = listOf(navArgument("name") { type = NavType.StringType }))
            {
                backStackEntry -> val name = backStackEntry.arguments?.getString("name")
                ScheduleScreen(navController = navController, name ?: "")
            }
            composable(
                route = "schedule_screen_2/{name}",
                arguments = listOf(navArgument("name") { type = NavType.StringType }))
            {
                backStackEntry -> val name = backStackEntry.arguments?.getString("name")
                ScheduleScreen2(navController = navController, name ?: "")
            }
            composable(
                route = "event_screen/{name}",
                arguments = listOf(navArgument("name") { type = NavType.StringType }))
            {
                backStackEntry -> val name = backStackEntry.arguments?.getString("name")
                EventScreen(navController = navController, name ?: "")
            }
            composable(
                route = "event_screen_2/{name}",
                arguments = listOf(navArgument("name") { type = NavType.StringType }))
            {
                backStackEntry -> val name = backStackEntry.arguments?.getString("name")
                EventScreen2(navController = navController, name ?: "")
            }
            composable(
                route = "map_menu/{name}",
                arguments = listOf(navArgument("name") { type = NavType.StringType }))
            {
                backStackEntry -> val name = backStackEntry.arguments?.getString("name")
                MapScreen(navController = navController, name ?: "")
            }
            composable("map_menu_2/{name}",
                arguments = listOf(navArgument("name") { type = NavType.StringType }))
            {
                backStackEntry -> val name = backStackEntry.arguments?.getString("name")
                MapScreen2(navController = navController, name ?: "")
            }
            composable(route = "options_menu/{name}",
                arguments = listOf(navArgument("name") { type = NavType.StringType }))
            {
                backStackEntry -> val name = backStackEntry.arguments?.getString("name")
                OptionsMenu(navController = navController, name ?: "")
                }
            }
        }
    }
//------------------------------HELPER_FUNCTIONS---------------------------------------------------
// button to navigate to main screen
@Composable
fun Home(navController: NavHostController, name: String){
    Button(
        onClick = { navController.navigate("main_screen/$name") },
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize(),
        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)
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
        color = Color(AppTheme.uiElementColor.value)
    )
}
// Buttons
@Composable
fun HelperButton(text: String, onClick: () -> Unit, colors: ButtonColors, modifier: Modifier) {
    Button(onClick = onClick,
        colors = colors,
        modifier = modifier
    ) {
        if (colors == ButtonDefaults.buttonColors(containerColor = Color.Black)) {
            Text(text, color = Color.White)
        } else {
            Text(text, color = Color.Black)
        }
    }
}
