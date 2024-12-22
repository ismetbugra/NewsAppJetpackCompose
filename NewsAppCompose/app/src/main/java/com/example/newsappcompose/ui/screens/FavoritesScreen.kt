@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.newsappcompose.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.newsappcompose.R
import com.example.newsappcompose.data.entities.FavoriteNews
import com.example.newsappcompose.data.viewmodels.NewsViewModel
import com.example.newsappcompose.ui.navigation.Destinations
import com.example.newsappcompose.ui.theme.darkBlue
import com.example.newsappcompose.utils.network.NetworkConnectionState
import com.example.newsappcompose.utils.network.rememberConnectivityState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController,paddingValues: PaddingValues){

    Box(modifier = Modifier.fillMaxSize()
        .padding(paddingValues)) {

        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = {
                    Text(text = "My Favorite News", fontSize = 25.sp, fontWeight = FontWeight.W300, color = Color.White)
                },
                    colors = TopAppBarDefaults.topAppBarColors(darkBlue))
            }) {

            Column(modifier = Modifier.fillMaxSize()
                .padding(start = 0.dp,top=it.calculateTopPadding(),end=0.dp, bottom = 0.dp)) {

                Spacer(modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth())

                FavoriteNewsListSection(navController)
            }


        }
    }
}


@Composable
fun FavoriteNewsListSection(navController: NavController,viewModel: NewsViewModel= hiltViewModel()){

    LaunchedEffect(key1 = Unit) {
        viewModel.getFavoriteNewsList()
    }

    var favoriteList by remember {
        viewModel.favoriteNewsList
    }

    var isLoading by remember {
        viewModel.favoriteLoadingState
    }

    val refreshState= rememberPullToRefreshState()
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    var scope = rememberCoroutineScope()

    PullToRefreshBox(
        state = refreshState,
        isRefreshing = isLoading,
        onRefresh = {
            scope.launch {
                isRefreshing=true
                viewModel.getFavoriteNewsList()
                isRefreshing=false
            }
        }
    ) {

        // veritabanında kayıtlı fav haber yoksa fav haber listesi boş olarak belirtilecek sayfanın arka planında
        if(favoriteList.isNullOrEmpty()){
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Icon(painter = painterResource(R.drawable.empty_list), contentDescription = "", modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "You do not have favorite news, let's add news to your favorite news section", fontSize = 20.sp,
                    fontWeight = FontWeight.W400, color = Color.LightGray,
                    modifier = Modifier.padding(10.dp))
            }
        }else{
            LazyColumn() {
                items(favoriteList){
                    FavoriteNewsRow(navController,it)
                }
            }
        }
    }






}


@Composable
fun FavoriteNewsRow(navController: NavController,favoriteNews: FavoriteNews,viewModel: NewsViewModel= hiltViewModel()){

    var showNoImageText by remember {
        mutableStateOf(false)
    }

    var showNoAuthorText by remember {
        mutableStateOf(false)
    }

    val connectionState by rememberConnectivityState()

    // haberin authoru ve urlToImage ı null gelebiliyor onun başta kontrolü yapılıyor
    LaunchedEffect(key1 = Unit) {
        if (favoriteNews.author.isNullOrEmpty()){
            showNoAuthorText=true
            delay(3000)
            showNoAuthorText=false
        }

        if (favoriteNews.urlToImage.isNullOrEmpty()){
            showNoImageText=true
            delay(3000)
            showNoImageText=false
        }

    }

    var arrowState by remember {
        mutableStateOf(false)
    }

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {

        Column(modifier = Modifier.fillMaxWidth()) {

            Column(modifier = Modifier.fillMaxWidth()
                .clickable {
                    navController.navigate(Destinations.Detail(
                        author = favoriteNews.author,
                        content = favoriteNews.content,
                        description = favoriteNews.description,
                        publishedAt = favoriteNews.publishedAt,
                        title = favoriteNews.title,
                        url = favoriteNews.url,
                        urlToImage = favoriteNews.urlToImage,
                        isFavorite = favoriteNews.isFavorite,
                        favId = favoriteNews.id
                    ))
                }) {

                if (!favoriteNews.urlToImage.isNullOrEmpty()){
                    if (connectionState==NetworkConnectionState.Available){
                        AsyncImage(model = favoriteNews.urlToImage, contentDescription = "", contentScale = ContentScale.FillWidth,
                            modifier = Modifier.fillMaxWidth())
                    }

                }else{
                    if (showNoImageText){
                        Text(text = "This news don't have a image!", fontSize = 25.sp, color = Color.Red,
                            modifier = Modifier.padding(8.dp))
                    }
                }


                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {


                    if (!favoriteNews.title.isNullOrEmpty()){
                        Text(text = favoriteNews.title, fontSize = 25.sp, fontWeight = FontWeight.Bold,)
                    }else{
                        Text(text = "No title!", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color.LightGray)
                    }

                    if (!favoriteNews.author.isNullOrEmpty()){
                        Text(text = favoriteNews.author, fontSize = 15.sp, fontWeight = FontWeight.W400)
                    }else{
                        if (showNoAuthorText){
                            Text(text = "There is not author info!", color = Color.Red)
                        }
                    }
                }


            }

            IconButton(onClick = {
                arrowState=!arrowState

            }) {
                Icon(imageVector = if (arrowState) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = "")
            }

            AnimatedVisibility(visible = arrowState) {
                Row(modifier = Modifier.fillMaxWidth()
                    .background(Color.Red)
                    .height(50.dp)
                    .clickable {
                        viewModel.deleteFavoriteNews(favoriteNews)
                    },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {

                    Icon(Icons.Default.Delete, contentDescription = "", tint = Color.White)

                    Spacer(Modifier.width(4.dp))

                    Text(text = "Delete this news from your favorites", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)

                }
            }



        }




    }


}