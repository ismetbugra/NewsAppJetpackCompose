package com.example.newsappcompose.ui.screens

import android.widget.ImageButton
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.newsappcompose.data.entities.Article
import com.example.newsappcompose.data.entities.FavoriteNews
import com.example.newsappcompose.data.viewmodels.NewsViewModel
import com.example.newsappcompose.ui.navigation.Destinations
import com.example.newsappcompose.utils.network.NetworkConnectionState
import com.example.newsappcompose.utils.network.rememberConnectivityState


@Composable
fun DetailScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    args: Destinations.Detail,
    viewModel: NewsViewModel= hiltViewModel()
){

    val author= args.author ?: ""
    val content = args.content ?: ""
    val description = args.description ?: ""
    val publishedAt = args.publishedAt ?: ""
    val modifiedPublishedAt= publishedAt.replace("T"," ").replace("Z","")
    val title = args.title ?: ""
    val url = args.url ?: ""
    val urlToImage = args.urlToImage ?: ""
    val isFavorite = args.isFavorite ?: 0

    val context= LocalContext.current
    val connectionState by rememberConnectivityState()

    var favoriteState by remember{
        mutableStateOf(if (isFavorite==0) false else true)
    }

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {

            if (connectionState==NetworkConnectionState.Available){
                if (!args.urlToImage.isNullOrEmpty()){
                    AsyncImage(model = args.urlToImage,
                        contentDescription ="",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize())
                }
            }


                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                        )
                    ), contentAlignment = Alignment.BottomStart){

                    Column(modifier = Modifier
                        //.fillMaxSize()
                        .padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {

                        if (!favoriteState){
                            IconButton(modifier = Modifier.padding(8.dp)
                                .align(Alignment.Start), onClick = {
                                    var insertNews=FavoriteNews(0,author,content,description,publishedAt,title,url,urlToImage,1)
                                    viewModel.upsertFavoriteNews(favoriteNews = insertNews)
                                    favoriteState=true

                            }) {
                                Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "", tint = Color.White)
                            }
                        }else{
                            IconButton(modifier = Modifier.padding(8.dp)
                                .align(Alignment.Start),
                                onClick = {
                                    //var deletedNews= Article()
                                    Toast.makeText(context,"News already added to favorite news section! Please check favorite news screen",Toast.LENGTH_LONG).show()

                                    //favoriteState=false

                                }) {
                                Icon(imageVector = Icons.Filled.Favorite, contentDescription = "", tint = Color.White)
                            }
                        }


                        Text(text = title, color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)



                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = author, color = Color.White, fontWeight = FontWeight.SemiBold)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = modifiedPublishedAt, color = Color.White, fontWeight = FontWeight.SemiBold)

                        Text(text = description, color = Color.White, fontSize = 20.sp)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "Click to go to news website", fontWeight = FontWeight.SemiBold,
                            textDecoration = TextDecoration.Underline, color = Color.White,
                            modifier = Modifier.clickable {
                                if (url!=""){
                                    navController.navigate(Destinations.WebViewScreen(newsUrl = url))
                                }else{
                                    Toast.makeText(context,"This news have not website",Toast.LENGTH_LONG).show()
                                }
                            })

                    }





                }




        }



}