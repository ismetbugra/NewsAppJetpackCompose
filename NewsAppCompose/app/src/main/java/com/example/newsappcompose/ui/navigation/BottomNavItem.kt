package com.example.newsappcompose.ui.navigation

import android.icu.text.CaseMap.Title
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.example.newsappcompose.R

data class BottomNavItem(
    var title: String,
    var selectedIcon:ImageVector,
    var unselectedIcon:ImageVector,
    var route:Destinations
)


object items{
    var list= listOf(
        BottomNavItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = Destinations.Home
        ),
        BottomNavItem(
            title = "Favorites",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder,
            route = Destinations.Favorites
        ),
        BottomNavItem(
            title = "Search",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            route = Destinations.Search
        )
    )
}