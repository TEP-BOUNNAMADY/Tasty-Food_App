package com.example.foodso_app.FoodSo_Module

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
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
//            composable("Favorite") { FavoriteScreen(viewModel, navController) }
//            composable("AboutUs") { AboutUsScreen() }
//            composable("MealDetail/{mealName}") { backStackEntry ->
//                val mealName = backStackEntry.arguments?.getString("mealName") ?: return@composable
//                MealDetailScreen(mealName = mealName, navController, viewModel)
//            }
        }
    }
}

@Composable
fun BottomNavBar(nc: NavHostController) {
    BottomNavigation(
        backgroundColor = Color(0xFFDC3D74),
        //contentColor = Color(0xFF00FF00) // Change the color here (e.g., green)
    ) {
        val items = listOf("Home", "Search", "Favorite", "AboutUs")

        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = getIcon(screen),
                        tint  = Color(0xFFFFFFFF), // Change the color here as well
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = screen,
                        fontFamily = FontFamily.Serif,
                        color = Color.White
                    )
                },
                selected = false,
                onClick = {
                    nc.navigate(screen)
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
