package com.example.safepass2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.safepass2026.ui.SafePassScreen
import androidx.compose.material3.MaterialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Cumplimiento estricto del estándar Android 16 (API 36): Dibujado de borde a borde
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                SafePassScreen()
            }
        }
    }
}
