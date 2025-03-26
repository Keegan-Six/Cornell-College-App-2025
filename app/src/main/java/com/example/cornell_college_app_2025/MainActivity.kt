package com.example.cornell_college_app_2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.cornell_college_app_2025.ui.theme.CornellCollegeApp2025Theme

// SQL Info
// https://developer.android.com/training/data-storage/room

// "Main" function, starts app and starts Navigation
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            CornellCollegeApp2025Theme {
                    Navigation()
            }
        }
    }
}

//-----------------------------PREVIEWS-----------------------------------------------------------

