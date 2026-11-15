package com.example.w7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.w7.navigation.AppNavHost
import com.example.w7.ui.theme.W7Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            W7Theme {
                AppNavHost()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun W7Preview() {
    AppNavHost()
}