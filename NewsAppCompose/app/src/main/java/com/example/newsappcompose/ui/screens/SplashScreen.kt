package com.example.newsappcompose.ui.screens

import android.content.Context
import android.window.SplashScreen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.newsappcompose.R
import com.example.newsappcompose.ui.navigation.Destinations
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController){

    // lottie animasyon dosyamız import edildi ,, composition bizim lottie animasyon dosyamız
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottienewsanimation))

    // animasyon özelleştirme, başlıyor mu , loopa giriyor mu vs
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = true,
        iterations = LottieConstants.IterateForever
    )

    val context= LocalContext.current


    LaunchedEffect(key1 = Unit) {
        delay(3000)

        if (onBoardingIsFinished(context)){
            navController.popBackStack()
            navController.navigate(Destinations.Home)
        }else{
            navController.popBackStack()
            navController.navigate(Destinations.OnBoarding)
        }


    }


    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){

        LottieAnimation(composition = composition, progress = {progress}, modifier = Modifier.size(200.dp))


    }
}

private fun onBoardingIsFinished(context:Context):Boolean{
    val sharedPreferences = context.getSharedPreferences("onBoarding",Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("isFinished",false)
}