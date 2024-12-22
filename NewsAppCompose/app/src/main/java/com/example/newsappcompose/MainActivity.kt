package com.example.newsappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.newsappcompose.ui.navigation.NavGraph
import com.example.newsappcompose.ui.navigation.items
import com.example.newsappcompose.ui.screens.NoInternetDialog
import com.example.newsappcompose.ui.theme.NewsAppComposeTheme
import com.example.newsappcompose.ui.theme.darkBlue
import com.example.newsappcompose.utils.network.NetworkStatusChecker
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.last

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            //statusbarcolor degistirme
            window.statusBarColor=getColor(R.color.darkBlue)

            /*val context= LocalContext.current
            val internetFlow= NetworkStatusChecker.networkChecker(context)
            NoInternetDialog(internetFlow)*/

            NewsAppComposeTheme {

                var navController= rememberNavController()

                var selectedBottomNavItemIndex by rememberSaveable {
                    mutableStateOf(0)
                }

                var list = items.list

                Scaffold(modifier = Modifier.fillMaxSize()
                    .background(Color.White),
                    bottomBar = {
                        Card(shape = RoundedCornerShape(30.dp),
                            elevation = CardDefaults.elevatedCardElevation(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                            NavigationBar(modifier = Modifier
                                //.clip(RoundedCornerShape(30.dp))
                                .fillMaxWidth()
                                .background(Color.Transparent)
                                ,
                                containerColor = Color.White,
                                tonalElevation = 50.dp) {
                                list.forEachIndexed { index, bottomNavItem ->
                                    NavigationBarItem(
                                        selected = index == selectedBottomNavItemIndex,
                                        onClick = {
                                            navController.navigate(bottomNavItem.route)
                                            selectedBottomNavItemIndex=index



                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (index == selectedBottomNavItemIndex){
                                                    bottomNavItem.selectedIcon
                                                }else{
                                                    bottomNavItem.unselectedIcon
                                                },
                                                contentDescription ="", tint = darkBlue
                                            )
                                        }, colors = NavigationBarItemDefaults.colors(indicatorColor = Color.White)
                                        )
                                }
                            }
                        }

                    }

                    ) { innerPadding ->

                    NavGraph(navHostController = navController, paddingValues = innerPadding)

                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewsAppComposeTheme {

    }
}