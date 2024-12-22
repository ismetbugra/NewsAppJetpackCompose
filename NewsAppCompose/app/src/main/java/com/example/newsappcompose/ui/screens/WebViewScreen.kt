package com.example.newsappcompose.ui.screens

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.newsappcompose.ui.navigation.Destinations

@Composable
fun WebViewScreen(navController: NavController,args:Destinations.WebViewScreen,paddingValues: PaddingValues){
    val newsUrl = args.newsUrl ?: ""

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)){

        if (newsUrl!=null){

            AndroidView(factory = {
                WebView(it).apply {
                    layoutParams= ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    settings.javaScriptEnabled=true
                    webViewClient= WebViewClient()
                    loadUrl(newsUrl)
                }
            },
                update = {
                    it.loadUrl(newsUrl)
                })
        }


    }
}