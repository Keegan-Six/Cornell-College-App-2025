package com.example.cornell_college_app_2025

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController

//-----------------------------SCREENS-------------------------------------------------------------
// Opening Screen or Home Screen, displays buttons to navigate to other screens
@Composable
fun OpeningScreen(navController: NavHostController) {
    val context = LocalContext.current
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(R.drawable.cc_logo_primary_rgb),
            contentDescription = "Cornell College Logo",
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
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
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)) {
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
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)) {
            Text(text = "Dining Menus")
        }
        Button(onClick = { navController.navigate("schedule_screen") },
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)) {
            Text(text = "Your Schedule")
        }
        Button(onClick = { navController.navigate("event_screen") },
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)) {
            Text(text = "Events")
        }
        Button(onClick = { navController.navigate("map_menu") },
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)) {
            Text(text = "Campus Map")
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
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)) {
                Text(text = "Moodle")
            }
            Button(onClick = {
                context.startActivity(Intent(Intent.ACTION_VIEW,
                    "https://selfservice.cornellcollege.edu/PowerCampusSelfService/Home".toUri()))
            },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)) {
                Text(text = "Self-Service")
            }
            Button(onClick = {
                context.startActivity(Intent(Intent.ACTION_VIEW,
                    "https://www.cornellcollege.edu/library/".toUri()))
            },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)) {
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
fun Weather2Screen(navController: NavHostController) {
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

// Menu Screen, displays HillTop Dining Menu
@Composable
fun MenuScreen(navController: NavHostController) {
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
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)
            ) {
                Text(text = "Zamora's Market Menu")
            }
        }
    }
}
// Displays Zamora's Market Menu
@Composable
fun MenuScreen2(navController: NavHostController) {
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
// Schedule Screen, displays schedule for student
// TODO: Add schedule functionality
@Composable
fun ScheduleScreen(navController: NavHostController) {
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
fun EventScreen(navController: NavHostController) {
    Column {
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "Event Screen",
            modifier = Modifier.padding(start = 8.dp),
            color = AppTheme.textColor)
        PurpleLine()
        Home(navController)
    }
}
@Composable
fun MapScreen(navController: NavHostController) {
    Column {
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Map Screen",
            modifier = Modifier.padding(start = 8.dp),
            color = AppTheme.textColor
        )
        PurpleLine()
        CornellCollegeMap()
        PurpleLine()
        Row {
            Home(navController)
            Spacer(modifier = Modifier.width(124.dp))
            Button(
                onClick = { navController.navigate("map_menu_2") },
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize(),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)
            ) {
                Text(text = "Official Cornell Map")
            }
        }
    }
}
@Composable
fun MapScreen2(navController: NavHostController) {
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Official Cornell Map",
            modifier = Modifier.padding(start = 8.dp),
            color = AppTheme.textColor)
        PurpleLine()
        Image(
            painter = painterResource(R.drawable.campusmap),
            contentDescription = "Official Cornell Map",
            modifier = Modifier.fillMaxWidth())
        Home(navController)
    }
}

// Displays Options menu
@Composable
fun OptionsMenu(navController: NavHostController) {
    Column {
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
            HelperButton(text = "Black",
                onClick = { AppTheme.backgroundColor = Color.Black },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier.padding(8.dp))
            HelperButton(text = "White",
                onClick = { AppTheme.backgroundColor = Color.White },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.padding(8.dp))
            HelperButton(text = "Gray",
                onClick = { AppTheme.backgroundColor = Color.Gray },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier.padding(8.dp))
            HelperButton(text = "Purple",
                onClick = { AppTheme.backgroundColor = Color(0xFF9C27B0) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                modifier = Modifier.padding(8.dp))
        }
        Row {
            HelperButton(text = "Red",
                onClick = { AppTheme.backgroundColor = Color.Red },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.padding(8.dp))
            HelperButton(text = "Green",
                onClick = { AppTheme.backgroundColor = Color.Green },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                modifier = Modifier.padding(8.dp))
            HelperButton(text = "Blue",
                onClick = { AppTheme.backgroundColor = Color.Blue },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                modifier = Modifier.padding(8.dp))
            HelperButton(text = "Yellow",
                onClick = { AppTheme.backgroundColor = Color.Yellow },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                modifier = Modifier.padding(8.dp))
        }
        Text(
            text = "Change Text Color",
            modifier = Modifier.padding(start = 8.dp),
            color = AppTheme.textColor
        )
        PurpleLine()
        Row {
            HelperButton(text = "Black",
                onClick = { AppTheme.textColor = Color.Black },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier.padding(8.dp))
            HelperButton(text = "White",
                onClick = { AppTheme.textColor = Color.White },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.padding(8.dp))
            HelperButton(text = "Gray",
                onClick = { AppTheme.textColor = Color.Gray },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier.padding(8.dp))
            HelperButton(text = "Purple",
                onClick = { AppTheme.textColor = Color(0xFF9C27B0) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                modifier = Modifier.padding(8.dp))

        }
        Row {
            HelperButton(text = "Red",
                onClick = { AppTheme.textColor = Color.Red },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.padding(8.dp))
            HelperButton(text = "Green",
                onClick = { AppTheme.textColor = Color.Green },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                modifier = Modifier.padding(8.dp))

            HelperButton(text = "Blue",
                onClick = { AppTheme.textColor = Color.Blue },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                modifier = Modifier.padding(8.dp))

            HelperButton(text = "Yellow",
                onClick = { AppTheme.textColor = Color.Yellow },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                modifier = Modifier.padding(8.dp))

        }
        Text(
            text = "Change UI element colors",
            modifier = Modifier.padding(start = 8.dp),
            color = AppTheme.textColor
        )
        PurpleLine()
        Row {
            HelperButton(text = "Black",
                onClick = { AppTheme.uiElementColor = Color.Black },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier.padding(8.dp))
            HelperButton(text = "White",
                onClick = { AppTheme.uiElementColor = Color.White },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.padding(8.dp))
            HelperButton(text = "Gray",
                onClick = { AppTheme.uiElementColor = Color.Gray },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier.padding(8.dp))
            HelperButton(text = "Purple",
                onClick = { AppTheme.uiElementColor = Color(0xFF9C27B0) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                modifier = Modifier.padding(8.dp))
        }
        Row {
            HelperButton(text = "Red",
                onClick = { AppTheme.uiElementColor = Color.Red },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.padding(8.dp))
            HelperButton(text = "Green",
                onClick = { AppTheme.uiElementColor = Color.Green },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                modifier = Modifier.padding(8.dp))
            HelperButton(text = "Blue",
                onClick = { AppTheme.uiElementColor = Color.Blue },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                modifier = Modifier.padding(8.dp))
            HelperButton(text = "Yellow",
                onClick = { AppTheme.uiElementColor = Color.Yellow },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                modifier = Modifier.padding(8.dp))
        }
        Text(
            text = "Miscellaneous",
            modifier = Modifier.padding(start = 8.dp),
            color = AppTheme.textColor
        )
        PurpleLine()
        Button(onClick = {
            AppTheme.textColor = Color.Black
            AppTheme.backgroundColor = Color.White
            AppTheme.uiElementColor = Color(0xFF9C27B0)
        },
            modifier = Modifier.padding(start = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)) {
            Text(text = "Restore Defaults")
        }
        Home(navController)
    }
}