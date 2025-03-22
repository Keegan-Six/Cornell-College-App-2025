package com.example.cornell_college_app_2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.cornell_college_app_2025.ui.theme.CornellCollegeApp2025Theme

// SQL Info
// https://developer.android.com/training/data-storage/room

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
