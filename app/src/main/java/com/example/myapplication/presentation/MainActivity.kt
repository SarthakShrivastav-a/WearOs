// MainActivity.kt
package com.example.myapplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import kotlinx.coroutines.*
import kotlin.math.sin
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                HeartRateScreen()
            }
        }
    }
}

@Composable
fun HeartRateScreen() {
    var isMonitoring by remember { mutableStateOf(false) }
    var heartRate by remember { mutableStateOf(0) }
    var time by remember { mutableStateOf(0f) }

    DisposableEffect(isMonitoring) {
        var scope: CoroutineScope? = null
        var job: Job? = null

        if (isMonitoring) {
            scope = CoroutineScope(Dispatchers.Main + Job())
            job = scope.launch {
                while (isActive) {
                    // Base heart rate between 60-100 BPM
                    val baseRate = 80

                    // Add sine wave variation (natural rhythm)
                    val sineVariation = (sin(time) * 5).toInt()

                    // Add random variation (-3 to +3)
                    val randomVariation = Random.nextInt(-3, 4)

                    // Combine variations
                    heartRate = baseRate + sineVariation + randomVariation

                    // Increment time for sine wave
                    time += 0.1f

                    delay(1000) // Update every second
                }
            }
        }

        onDispose {
            job?.cancel()
            scope?.cancel()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            // Heart rate display
            if (isMonitoring) {
                // Heart rate icon with animation
                Text(
                    text = "❤️",
                    fontSize = 24.sp,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Heart rate value
                Text(
                    text = heartRate.toString(),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // BPM label
                Text(
                    text = "BPM",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Start/Stop Button
            Button(
                onClick = { isMonitoring = !isMonitoring },
                modifier = Modifier
                    .size(if (isMonitoring) 80.dp else 100.dp)
                    .padding(4.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isMonitoring) Color.Red else Color.Green
                )
            ) {
                Text(
                    text = if (isMonitoring) "Stop" else "Start",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}