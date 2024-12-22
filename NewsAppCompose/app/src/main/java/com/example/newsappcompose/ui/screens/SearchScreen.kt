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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.newsappcompose.R
import com.example.newsappcompose.data.entities.Article
import com.example.newsappcompose.data.viewmodels.NewsViewModel
import com.example.newsappcompose.ui.navigation.Destinations
import com.example.newsappcompose.ui.theme.darkBlue
import com.example.newsappcompose.utils.network.NetworkConnectionState
import com.example.newsappcompose.utils.network.rememberConnectivityState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController,paddingValues: PaddingValues,viewModel: NewsViewModel= hiltViewModel()) {

    var searchText by remember {
        mutableStateOf("")
    }

    var searchState by remember {
        mutableStateOf(false)
    }

    val connectionState by rememberConnectivityState()
    var isConnected by remember {
        mutableStateOf(if (connectionState == NetworkConnectionState.Available) true else false)
    }

    val scrollBehavior= TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        Scaffold(modifier = Modifier.fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = "Search the news",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.W400,
                        color = Color.White
                    )
                },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = darkBlue,
                        scrolledContainerColor = darkBlue),
                    scrollBehavior = scrollBehavior)
            }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 0.dp, top = it.calculateTopPadding(), end = 0.dp, bottom = 0.dp)
                    .background(Color.White)
            ) {

                OutlinedTextField(
                    value = searchText,
                    onValueChange = {

                        // internet kontrolu , internet varsa arama yapılır yoksa arama yapılamaz searchbara text girilemez
                        if (connectionState == NetworkConnectionState.Available) {
                            searchState = true
                            searchText = it
                            if (searchText != "") {
                                viewModel.getSearchNews(searchText)
                            } else {
                                searchState = false
                            }
                        } else {
                            isConnected = false
                        }
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "")
                    },
                    placeholder = {
                        Text(text = "Search...")
                    },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = darkBlue),
                    enabled = isConnected,
                    readOnly = !isConnected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                AnimatedVisibility(visible = !isConnected) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .background(Color.Red)
                            .size(30.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "There is not internet connection!",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))


                // arama gerçekleşmiyorsa item bulunamadığı belirtilecek
                if (!searchState) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Column() {
                            Icon(
                                painter = painterResource(R.drawable.empty_list),
                                contentDescription = "",
                                modifier = Modifier.size(200.dp, 200.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "No news found!",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontStyle = FontStyle.Italic,
                                color = Color.LightGray
                            )
                        }


                    }
                } else {
                    SearchListSection(navController = navController, searchQuery = searchText)
                }

            }


        }

    }
}

    @Composable
    fun SearchListSection(
        navController: NavController,
        searchQuery: String,
        viewModel: NewsViewModel = hiltViewModel()
    ) {

        LaunchedEffect(key1 = Unit) {
            viewModel.getSearchNews(searchQuery)
        }

        var searchList by remember {
            viewModel.searchNewsList
        }

        var isLoading by remember {
            viewModel.loadingState
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Blue)
            }
        } else {
            LazyColumn {
                items(searchList) {
                    SearchListRow(navController = navController, newsItem = it)
                }
            }
        }


    }

    @Composable
    fun SearchListRow(navController: NavController, newsItem: Article) {
        var showNoImageText by remember {
            mutableStateOf(false)
        }

        var showAuthorText by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(key1 = newsItem.urlToImage) {
            if (newsItem.urlToImage.isNullOrEmpty()) {
                showNoImageText = true
                delay(3000L)
                showNoImageText = false
            }

            if (newsItem.author == "") {
                showAuthorText = true
                delay(3000L)
                showAuthorText = false
            }
        }

        Card(
            modifier = Modifier
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
            elevation = CardDefaults.elevatedCardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {

                if (!newsItem.urlToImage.isNullOrEmpty()) {
                    AsyncImage(
                        model = newsItem.urlToImage,
                        contentDescription = "",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    if (showNoImageText) {
                        Text(text = "This news don't have a image!", color = Color.Red)
                    }
                }

                Text(
                    text = newsItem.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                if (newsItem.author != null) {
                    Text(text = newsItem.author)
                } else {
                    if (showAuthorText) {
                        Text(text = "There is no author info", color = Color.Red)
                    }
                }


            }
        }
    }

