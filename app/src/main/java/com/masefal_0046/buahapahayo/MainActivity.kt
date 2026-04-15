package com.masefal_0046.buahapahayo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.masefal_0046.buahapahayo.navigation.SetupNavGraph
import com.masefal_0046.buahapahayo.ui.theme.BuahApaHayoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BuahApaHayoTheme {
                SetupNavGraph()
            }
        }
    }
}

