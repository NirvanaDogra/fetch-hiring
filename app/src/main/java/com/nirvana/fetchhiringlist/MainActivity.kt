package com.nirvana.fetchhiringlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.nirvana.common_ui.theme.FetchHiringListTheme
import com.nirvana.feature_fetchhiring.view.FetchHiringScreen
import com.nirvana.feature_fetchhiring.viewmodel.FetchHiringViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FetchHiringListTheme {
                val viewModel: FetchHiringViewModel = hiltViewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FetchHiringScreen(
                        viewModel,
                        Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}