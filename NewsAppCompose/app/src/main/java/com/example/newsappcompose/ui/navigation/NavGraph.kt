package com.example.newsappcompose.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.newsappcompose.ui.screens.DetailScreen
import com.example.newsappcompose.ui.screens.FavoritesScreen
import com.example.newsappcompose.ui.screens.HomeScreen
import com.example.newsappcompose.ui.screens.OnBoardingScreen
import com.example.newsappcompose.ui.screens.SearchScreen
import com.example.newsappcompose.ui.screens.SplashScreen
import com.example.newsappcompose.ui.screens.WebViewScreen

@Composable
fun NavGraph(navHostController: NavHostController,paddingValues: PaddingValues){

    NavHost(navController = navHostController, startDestination = Destinations.Splash,
        enterTransition = {
             fadeIn(animationSpec = tween(1000))
        },
        exitTransition = {
             fadeOut(animationSpec = tween(1000))
        }) {
        composable<Destinations.Home>(){
            HomeScreen(navController = navHostController, paddingValues = paddingValues)
        }

        // sayfanın kendisine özgü geçiş animasyonu eklendi
        composable<Destinations.Detail>(enterTransition = {
            slideInHorizontally(animationSpec = tween(500))
        },
            exitTransition = {
                slideOutHorizontally(animationSpec = tween(500))+ fadeOut(animationSpec = tween(500))
            }) {
            val args = it.toRoute<Destinations.Detail>()
            DetailScreen(navController = navHostController, paddingValues = paddingValues,args)
        }

        composable<Destinations.Favorites> {
            FavoritesScreen(navController = navHostController, paddingValues = paddingValues)
        }

        composable<Destinations.Search> {
            SearchScreen(navController = navHostController, paddingValues = paddingValues)
        }

        composable<Destinations.WebViewScreen> {
            val args = it.toRoute<Destinations.WebViewScreen>()
            WebViewScreen(navController = navHostController, args, paddingValues =paddingValues )
        }

        composable<Destinations.Splash> {
            SplashScreen(navHostController)
        }

        composable<Destinations.OnBoarding> {
            OnBoardingScreen(navHostController,paddingValues)
        }
    }
}