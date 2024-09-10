package com.example.foodso_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodso_app.FoodSo_Module.MainScreenUI
import com.example.foodso_app.FoodSo_Module.MyFoodSoApp
import com.example.foodso_app.FoodSo_Module.SearchScreenUI
import com.example.foodso_app.FoodSo_Module.TheFoodSoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AsyncApp()
            MyFoodSoApp()
        }
    }
}

@Composable
fun AsyncApp() {
    val navController = rememberNavController()
    val viewModel: TheFoodSoViewModel = viewModel()

    NavHost(navController = navController, startDestination = "Home") {
        composable("Home") { MainScreenUI(navController, viewModel) }
        composable("Search") { SearchScreenUI(navController, viewModel) }
        // Add other composable destinations here
        // composable("Favorite") { FavoriteScreen(viewModel, navController) }
        // composable("AboutUs") { AboutUsScreen() }
        // composable("MealDetail/{mealName}") { backStackEntry ->
        //     val mealName = backStackEntry.arguments?.getString("mealName") ?: return@composable
        //     MealDetailScreen(mealName = mealName, navController, viewModel)
        // }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AsyncAppPreview() {
    // Preview without navigation or ViewModel logic
    val navController = rememberNavController()
    val viewModel: TheFoodSoViewModel = viewModel()

    MainScreenUI(navController, viewModel)
}