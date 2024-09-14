package com.example.foodso_app.FoodSo_Module

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.afinal.Food.SearchScreenUI

@Composable
fun MyFoodSoApp() {
    val navController = rememberNavController()
    val viewModel: TheFoodSoViewModel = viewModel()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "Home",
            Modifier.padding(innerPadding)
        ) {
            composable("Home") { MainScreenUI(navController, viewModel) }
            composable("Search") { SearchScreenUI(navController, viewModel) }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val customTextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 16.sp
    )

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground
    ) {
        val items = listOf("Home", "Search", "Favorite", "AboutUs")

        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = getIcon(screen),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = screen,
                        fontFamily = FontFamily.Serif,
                        color = Color.Black
                    )
                },
                selected = false,
                onClick = {
                    navController.navigate(screen)
                }
            )
        }
    }
}

@Composable
fun getIcon(screen: String): ImageVector {
    return when (screen) {
        "Home" -> Icons.Filled.Home
        "Search" -> Icons.Filled.Search
        "Favorite" -> Icons.Filled.Favorite
        "AboutUs" -> Icons.Filled.Info
        else -> Icons.Filled.Home
    }
}
