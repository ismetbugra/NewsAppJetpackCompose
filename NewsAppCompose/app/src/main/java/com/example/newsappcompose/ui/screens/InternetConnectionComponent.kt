package com.example.newsappcompose.ui.screens

import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.newsappcompose.utils.network.isNetworkAvailable
import kotlinx.coroutines.flow.Flow

@Composable
fun NoInternetDialog(callbackFlow: Flow<Boolean>){
    val context = LocalContext.current
    val isConnected by callbackFlow.collectAsState(initial = isNetworkAvailable(context))
    val showDialog = remember {
        mutableStateOf(!isConnected)
    }

    if (!isConnected){
        AlertDialog(title = {
            Text("Internet connection error!")
        },
            text = {
                Text("Please connect to the internet!")
            },
            confirmButton = {
                Button(onClick = {
                    if (isNetworkAvailable(context)){
                        showDialog.value=false
                    }
                }) {
                    Text("Try again")
                }
            },
            onDismissRequest = {
                Log.e("TAG","NoInternetDialog")
            }
            )
    }
}