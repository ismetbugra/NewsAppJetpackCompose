package com.example.newsappcompose.ui.screens

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newsappcompose.R
import com.example.newsappcompose.ui.navigation.Destinations
import com.example.newsappcompose.ui.theme.darkBlue
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(navController: NavController,paddingValues: PaddingValues){
    val icons = listOf(
        R.drawable.breakingnewsicon,
        R.drawable.bookmarkicon,
        R.drawable.searchicon
    )

    val descriptionText= listOf(
        "You can access all the news shared around the world",
        "You can bookmark the news you interested. Then you can access your bookmarked news without internet connection!",
        "You can search for news on the subject you are curious about."
    )

    val titles = listOf(
        "Explore the breaking news",
        "Bookmark the news you interested",
        "Search the news"
    )


    val pagerState = rememberPagerState(pageCount = icons.size)

    Column(modifier = Modifier.fillMaxSize()
        .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
       ) {

        // horizontal pager accompanish olan--- viewpager görevi görüyor horzontalpager
        HorizontalPager(state = pagerState,
            modifier = Modifier.padding(30.dp)) {currentPage->

            Column(modifier = Modifier.wrapContentSize()
                .padding(26.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
               ) {

                Image(painter = painterResource(icons[currentPage]), contentDescription = "",
                    modifier = Modifier.size(100.dp))

                Text(text = titles[currentPage], fontSize = 25.sp, fontWeight = FontWeight.Bold)

                Text(text = descriptionText[currentPage], fontSize = 20.sp, fontWeight = FontWeight.W400)
            }

        }

        // kendi olusturdugumuz PageIndicatorumuz
        PageIndicator(pageCount = icons.size,currentPage= pagerState.currentPage)

        ButtonSection(pagerState = pagerState, navController = navController)
    }
}

// kendi olusturdugumuz PageIndicatorumuz
@Composable
fun PageIndicator(pageCount:Int,currentPage:Int){

    Row (horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(60.dp)){

        //repeat fonku, sayfa sayısı kadar IndicatiorSingleDot compoosablesini tekrar edecek UIda
        repeat(pageCount){

            // kendi olusturduğumuz custom singledot composablemiz
            IndicatorSingleDot(isSelected = it==currentPage)
            //Eğer tekrarın mevcut indeksi (it) aktif sayfaya (currentPage) eşitse, bu dot seçili (highlighted) olarak işaretlenir.
            // singledot composablesinde buyuklugu ve renk değişimi buna göre olur. seçiliyse mavi ve daha büyük dot olusur

        }
    }
}

// kendi olusturduğumuz custom singledot composablemiz
@Composable
fun IndicatorSingleDot(isSelected:Boolean){

    val width = animateDpAsState(targetValue = if (isSelected) 35.dp else 15.dp)
    Box(modifier = Modifier.padding(2.dp)
        .width(width.value)
        .height(15.dp)
        .clip(CircleShape)
        .background(if (isSelected) darkBlue else Color.Gray))
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ButtonSection(pagerState: PagerState,navController: NavController){

    // next back butonlarına tıklanınca pagerda hareket etmek için
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    Box(modifier = Modifier.padding(30.dp)
        .fillMaxSize(),
        contentAlignment = Alignment.BottomStart
        ){

        if (pagerState.currentPage!=2){
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
               ) {

                TextButton(onClick = {

                    scope.launch {
                        if (pagerState.currentPage>0){
                            val prevPage= pagerState.currentPage-1
                            pagerState.scrollToPage(prevPage)
                        }

                    }
                }) {
                    Text(text = "Back", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                }

                TextButton(onClick = {

                    scope.launch {
                        val nextPage= pagerState.currentPage+1
                        pagerState.scrollToPage(nextPage)
                    }
                }) {
                    Text(text = "Next", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                }
            }
        }else{
            OutlinedButton(onClick = {
                // onboard gösterme durumu true olacak sharedPreferences değişecek falsedan true olacak ve splash screende onboarda geçilmicek
                onBoardingIsFinished(context)
                navController.popBackStack()
                navController.navigate(Destinations.Home)
            },
                colors = ButtonDefaults.buttonColors(contentColor = darkBlue,
                    containerColor = Color.LightGray),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(text = "Let's Start!", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// bu fonk sharedPReferencesi değiştiriyor böylece onboard gösterme durumu true oluyor
private fun onBoardingIsFinished(context: Context){
    val sharedPreferences = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("isFinished",true)
    editor.apply()

}