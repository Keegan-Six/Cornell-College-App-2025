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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

//-----------------------------SCREENS-------------------------------------------------------------
// Login Screen, displays login form


@OptIn(ExperimentalStdlibApi::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginMessage by remember { mutableStateOf("") }
    writeData()
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Login Screen",
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 20.sp,
            style = TextStyle(
                fontFamily = AppTheme.font,
                fontWeight = FontWeight.Normal
            ),
            color = AppTheme.textColor
        )
        PurpleLine()
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.padding(8.dp)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Password") },
            modifier = Modifier.padding(8.dp)
        )
        Row() {
            Button(
                onClick = {
                    val database = Firebase.database
                    val studentsRef = database.getReference("students")
                    var userFound = false;
                    // Listen for changes in the "students" node
                    studentsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            // Iterate through each student in the snapshot
                            for (studentSnapshot in dataSnapshot.children) {
                                // Get the username
                                val storedName = studentSnapshot.child("name").getValue(String::class.java)
                                val storedUsername = studentSnapshot.child("username").getValue(String::class.java)
                                val storedPassword = studentSnapshot.child("password").getValue(String::class.java)
                                // If the username is found, navigate and change the login message
                                if (storedUsername == username && storedPassword == password) {
                                    userFound = true
                                    navController.navigate("main_screen/$storedName")
                                    loginMessage = "Correct Username and Password"
                                    break
                                }
                            }
                            // If no username is found change the login message
                            if (!userFound) {
                                loginMessage = "Invalid Username or Password"
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle error
                            loginMessage = "Database error: ${error.message}"
                        }
                    })
                },
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize(),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)
            ) {
                Text(text = "Login")
            }
            Button(
                onClick = { navController.navigate("main_screen/") },
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize(),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)
            ) {
                Text(text = "Skip Login")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = loginMessage, modifier = Modifier.padding(8.dp))
    }
}
// Opening Screen or Home Screen, displays buttons to navigate to other screens
@Composable
fun MainScreen(navController: NavHostController, name: String) {
    val context = LocalContext.current
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(R.drawable.cc_logo_primary_rgb),
            contentDescription = "Cornell College Logo",
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        Text(text = "Hello $name!",
            modifier = Modifier.padding(start = 8.dp).align(Alignment.CenterHorizontally),
            fontSize = 28.sp,
            style = TextStyle(fontFamily = AppTheme.font,
                fontWeight = FontWeight.Normal),
            color = AppTheme.textColor)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Current Weather",
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 20.sp,
            style = TextStyle(fontFamily = AppTheme.font,
                fontWeight = FontWeight.Normal),
            color = AppTheme.textColor)
        PurpleLine()
        WeatherDisplay("52314")
        Button(onClick = { navController.navigate("weather2_screen/$name") },
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)) {
            Text(text = "7 Day Weather Forecast")
        }
        Text(text = "Navigation",
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(fontFamily = AppTheme.font,
                fontWeight = FontWeight.Normal),
            fontSize = 20.sp,
            color = AppTheme.textColor)
        PurpleLine()
        Button(onClick = { navController.navigate("menu_screen/$name") },
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)) {
            Text(text = "Dining Menus")
        }
        Button(onClick = { navController.navigate("schedule_screen/$name") },
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)) {
            Text(text = "Your Schedule")
        }
        Button(onClick = { navController.navigate("event_screen/$name") },
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)) {
            Text(text = "Events")
        }
        Button(onClick = { navController.navigate("map_menu/$name") },
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)) {
            Text(text = "Campus Map")
        }
        Text(text = "Quick Links",
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(fontFamily = AppTheme.font,
                fontWeight = FontWeight.Normal),
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
            Button(
                onClick = { navController.navigate("options_menu/$name") },
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text(text = "Options")
            }
    }
}
// Weather Screen, displays more weather information
@Composable
fun Weather2Screen(navController: NavHostController, name: String) {
    Column {
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "7 Day Weather Forecast",
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 20.sp,
            style = TextStyle(fontFamily = AppTheme.font,
                fontWeight = FontWeight.Normal),
            color = AppTheme.textColor)
        PurpleLine()
        Forecast("52314")
        Home(navController, name)
    }
}

// Menu Screen, displays HillTop Dining Menu
@Composable
fun MenuScreen(navController: NavHostController, name: String) {
    Column {
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "Menu Screen",
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(fontFamily = AppTheme.font,
                fontWeight = FontWeight.Normal),
            color = AppTheme.textColor)
        WebViewScreen(
            url = "https://cornell.cafebonappetit.com/",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            scroll = 1
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(30.dp)) {
            Home(navController, name)
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { navController.navigate("menu_screen_2/$name") },
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
fun MenuScreen2(navController: NavHostController, name: String) {
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Zamora's Market Menu",
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(fontFamily = AppTheme.font,
                fontWeight = FontWeight.Normal),
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
        Home(navController, name)
    }
}
// Schedule Screen, displays schedule for student
// TODO: Add schedule functionality
@Composable
fun ScheduleScreen(navController: NavHostController, name: String) {
    val context = LocalContext.current
    Column {
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "Schedule Screen for $name",
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(fontFamily = AppTheme.font,
                fontWeight = FontWeight.Normal),
            color = AppTheme.textColor)
        PurpleLine()

        Home(navController,name)
    }
}
// Event Screen, displays events for Cornell College
@Composable
fun EventScreen(navController: NavHostController, name: String) {
    Column {
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "Event Screen",
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(fontFamily = AppTheme.font,
                fontWeight = FontWeight.Normal),
            color = AppTheme.textColor)
        PurpleLine()
        WebViewScreen(
            url = "https://www.cornellcollege.edu/campus-calendar/",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), scroll = 2)
        Home(navController,name)
    }
}
@Composable
fun MapScreen(navController: NavHostController, name: String) {
    Column {
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Map Screen",
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(fontFamily = AppTheme.font,
                fontWeight = FontWeight.Normal),
            color = AppTheme.textColor
        )
        PurpleLine()
        CornellCollegeMap()
        PurpleLine()
        Row {
            Home(navController, name)
            Spacer(modifier = Modifier.width(124.dp))
            Button(
                onClick = { navController.navigate("map_menu_2/$name") },
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
fun MapScreen2(navController: NavHostController, name: String) {
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Official Cornell Map",
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(fontFamily = AppTheme.font,
                fontWeight = FontWeight.Normal),
            color = AppTheme.textColor)
        PurpleLine()
        Image(
            painter = painterResource(R.drawable.campusmap),
            contentDescription = "Official Cornell Map",
            modifier = Modifier.fillMaxWidth())
        Home(navController, name)
    }
}

// Displays Options menu
@Composable
fun OptionsMenu(navController: NavHostController, name: String) {
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Options Menu",
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 20.sp,
            style = TextStyle(fontFamily = AppTheme.font,
                fontWeight = FontWeight.Normal),
            color = AppTheme.textColor
        )
        PurpleLine()
        Text(
            text = "Change Background Color",
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(fontFamily = AppTheme.font,
                fontWeight = FontWeight.Normal),
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
            fontSize = 20.sp,
            style = TextStyle(fontFamily = AppTheme.font,
                fontWeight = FontWeight.Normal),
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
            fontSize = 20.sp,
            style = TextStyle(fontFamily = AppTheme.font,
                fontWeight = FontWeight.Normal),
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
            fontSize = 20.sp,
            style = TextStyle(fontFamily = AppTheme.font,
                fontWeight = FontWeight.Normal),
            color = AppTheme.textColor
        )
        PurpleLine()
        Row {
            Button(
                onClick = {
                    AppTheme.font = FontFamily.Default
                },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)
            ) {
                Text(text = "Restore Default Font")
            }
            Button(
                onClick = {
                    AppTheme.font = FontFamily(
                        Font(R.font.brlnsr, FontWeight.Normal),
                        Font(R.font.brlnsdb, FontWeight.Bold),
                        Font(R.font.brlnsb, FontWeight.ExtraBold),
                    )
                },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)
            ) {
                Text(text = "Berlin Sans FB Font")
            }
        }
        Button(onClick = {
            AppTheme.textColor = Color.Black
            AppTheme.backgroundColor = Color.White
            AppTheme.uiElementColor = Color(0xFF9C27B0)
        },
            modifier = Modifier.padding(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)) {
            Text(text = "Restore Default Colors")
        }
        PurpleLine()
        Row {
            Home(navController, name)
            Button(onClick = { navController.navigate("login_screen") },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.uiElementColor)) {
                Text(text = "Logout")
            }
        }
    }
}