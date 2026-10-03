package com.marcus.frotacerta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.marcus.frotacerta.ui.navigation.AppNavigation
import com.marcus.frotacerta.ui.theme.FrotaCertaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            FrotaCertaTheme {

                AppNavigation()
            }
        }
    }
}
