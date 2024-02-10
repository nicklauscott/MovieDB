package com.example.moviedb.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.data.repository.TvShowRepositoryImp
import com.example.moviedb.domain.repository.MovieRepository
import com.example.moviedb.domain.repository.TvShowRepository
import com.example.moviedb.ui.theme.MovieDBTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieDBTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // navigation
                    val test = hiltViewModel<Test>()
                }
            }
        }
    }
}

@HiltViewModel
class Test @Inject constructor(private val tvShowRepository: TvShowRepository): ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("TestViewModel-TestViewModel", "xx - Requesting")
            tvShowRepository.getAShow(1396).collect {

            }
        }
    }
}
