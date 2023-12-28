package com.example.dailysync

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.dailysync.bookRepository.BookRepository
import com.example.dailysync.bookapi.BookApi
import com.example.dailysync.bookapi.RetrofitClient
import com.example.dailysync.navigation.NavGraph
import com.example.dailysync.ui.theme.DailySyncTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    // Firebase instance variables
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var bookViewModel: BookViewModel

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val bookApi = RetrofitClient.getInstance().create(BookApi::class.java)
        val bookRepository = BookRepository(bookApi, auth)
        val bookViewModelFactory = BookViewModelFactory(bookRepository)
        bookViewModel = ViewModelProvider(this, bookViewModelFactory).get(BookViewModel::class.java)
        setContent {
            DailySyncTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController, auth, bookViewModel)           // TODO PASS 'db' AS ARG?
            }
        }
    }
}