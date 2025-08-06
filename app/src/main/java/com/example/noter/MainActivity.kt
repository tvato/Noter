package com.example.noter

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.noter.ui.theme.NoterTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }
            NoterTheme(darkTheme = isDarkTheme){
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NoterApp(
                        switchMode = {
                            isDarkTheme = !isDarkTheme
                        }
                    )
                }
            }
        }
    }
}