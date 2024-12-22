@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.newsappcompose.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.newsappcompose.R
import com.example.newsappcompose.data.entities.Article
import com.example.newsappcompose.data.viewmodels.NewsViewModel
import com.example.newsappcompose.ui.navigation.Destinations
import com.example.newsappcompose.ui.theme.darkBlue
import com.example.newsappcompose.utils.Resource
import com.example.newsappcompose.utils.network.NetworkConnectionState
import com.example.newsappcompose.utils.network.rememberConnectivityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController,paddingValues: PaddingValues){

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {

        val scrollBehavior=TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

        Scaffold(modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),

            topBar = {
                TopAppBar(title = {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically) {

                        Image(painter = painterResource(id = R.drawable.newswhite), contentDescription ="",
                            modifier = Modifier.size(40.dp))

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "BreakingNews", fontSize = 25.sp, fontWeight = FontWeight.W300, color = Color.White,
                            modifier = Modifier.padding(10.dp))

                    }


                },
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = darkBlue, scrolledContainerColor = darkBlue))
            }) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(start = 0.dp, top = it.calculateTopPadding(), end = 0.dp, bottom = 0.dp)
                .background(Color.White)) {

                //toolbar
                /*Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically) {

                }*/
                Spacer(modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray))

                val connectionState by  rememberConnectivityState()

                if (connectionState==NetworkConnectionState.Available){
                    NewsListSection(navController = navController)
                }else{

                   ThereIsNotInternetSection()
                }

            }
        }


    }

}

@Composable
fun NewsListSection(navController: NavController,viewModel: NewsViewModel= hiltViewModel()){
    
    var context= LocalContext.current

    //Unit burada, değişmeyen bir anahtar olarak kullanılır.
    // Yani, key1 hiçbir zaman değişmeyeceği için, LaunchedEffect yalnızca bir kez çalışır ve tekrar tetiklenmez.
    LaunchedEffect(key1 = Unit) {
            viewModel.getBreakingNews()
    }
    var loadingState by remember {
        viewModel.loadingState
    }
    var newsList by remember {
        viewModel.breakingNewsList
    }

    var errorState by remember {
        viewModel.errorState
    }

    val refreshState= rememberPullToRefreshState()
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    var scope = rememberCoroutineScope()

    PullToRefreshBox(
        state = refreshState,
        isRefreshing = loadingState,
        onRefresh = {
            scope.launch {
                    viewModel.getBreakingNews()
            }

        }
    ) {

        if (loadingState){
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = darkBlue)
            }
        }else{

            if (errorState){
                var errorMessage=viewModel.errorMessage
                Toast.makeText(context,"${errorMessage}",Toast.LENGTH_LONG).show()

            }else{
                LazyColumn {
                    items(newsList){

                        // listedki removed olan haberler gösterilmicek
                        if (it.title!="[Removed]"){
                            NewsListRow(navController = navController, newsItem = it)
                        }

                    }
                }
            }


        }

    }


}

@Composable
fun NewsListRow(navController: NavController,newsItem:Article,viewModel: NewsViewModel= hiltViewModel()){
    
    var showNoImageText by remember {
        mutableStateOf(false)
    }

    var showAuthorText by remember {
        mutableStateOf(false)
    }
    
    LaunchedEffect(key1 = newsItem.urlToImage) {
        if (newsItem.urlToImage.isNullOrEmpty()){
            showNoImageText=true
            delay(3000L)
            showNoImageText=false
        }

        if (newsItem.author.isNullOrEmpty()){
            showAuthorText=true
            delay(3000L)
            showAuthorText=false
        }
    }

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable {
            navController.navigate(
                Destinations.Detail(
                    author = newsItem.author,
                    content = newsItem.content,
                    description = newsItem.description,
                    publishedAt = newsItem.publishedAt,
                    title = newsItem.title,
                    url = newsItem.url,
                    urlToImage = newsItem.urlToImage,
                    isFavorite = newsItem.isFavorite,
                    favId = 0
                )
            )
        },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(8.dp)) {

        if (!newsItem.urlToImage.isNullOrEmpty()){
            AsyncImage(model = newsItem.urlToImage, contentDescription ="", contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth())
        }else{
            if (showNoImageText){
                Text(text = "This news don't have a image!", color = Color.Red,
                    modifier = Modifier.padding(8.dp))
            }
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)) {


            if(!newsItem.title.isNullOrEmpty()){
                Text(text = newsItem.title, fontWeight = FontWeight.Bold, fontSize = 25.sp, modifier = Modifier.padding(vertical = 6.dp))
            }else{
                Text(text = "No title!")
            }


            // kontrollerin isNullOrEmpty() ile olması lazım çünkü değerler null olabilir
            // !="" ile kontrol yaparken stringin boş olup olmadığını kontrol ediyoruz, null olmasını değil
            if (!newsItem.author.isNullOrEmpty()){
                Text(text = newsItem.author)
            }else{
                if (showAuthorText){
                    Text(text = "There is no author info", color = Color.Red)
                }
            }


        }
    }
}

@Composable
fun ThereIsNotInternetSection(){

    Column(modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Image(imageVector = Icons.Default.Warning, contentDescription = "", colorFilter = ColorFilter.tint(Color.Gray),
            modifier = Modifier.size(50.dp))
        Text("There is not internet connection", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = "Please connect to the internet to use the app!", fontSize = 20.sp, textAlign = TextAlign.Center)
    }
}