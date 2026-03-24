package dev.paraspatil.trackmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.paraspatil.trackmate.ui.navigation.AppNavigation
import dev.paraspatil.trackmate.ui.theme.TrackMateTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           TrackMateTheme {
               AppNavigation()
           }
        }
    }
}