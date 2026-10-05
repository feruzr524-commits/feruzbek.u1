package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.PrankDatabase
import com.example.data.PrankRepository
import com.example.ui.PrankMainScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.util.PrankSoundAndHaptics
import com.example.viewmodel.PrankViewModel
import com.example.viewmodel.PrankViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb())
        )
        setContent {
            MyApplicationTheme {
                val context = LocalContext.current
                val repository = remember {
                    PrankRepository(PrankDatabase.getDatabase(context).prankDao())
                }
                val soundAndHaptics = remember { PrankSoundAndHaptics(context) }
                val prankViewModel: PrankViewModel = viewModel(
                    factory = PrankViewModelFactory(
                        repository = repository,
                        onPlayClick = soundAndHaptics::playTacticalClick,
                        onPlayUcDrop = soundAndHaptics::playUcDropFanfare,
                        onPlayPrankHorn = soundAndHaptics::playPrankLaughHorn
                    )
                )
                PrankMainScreen(viewModel = prankViewModel)
            }
        }
    }
}
