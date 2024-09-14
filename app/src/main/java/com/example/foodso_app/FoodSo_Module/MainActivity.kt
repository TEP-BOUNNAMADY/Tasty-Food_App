package com.example.afinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.afinal.Food.MainScreenUI
import com.example.afinal.Food.MealDetailScreen
import com.example.afinal.Food.SearchScreenUI
import com.example.afinal.Food.TheFoodSoViewModel
import com.example.afinal.Food.BottomNavBar
import com.example.afinal.Food.getIcon

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyFoodSoApp()  // Use MyFoodSoApp here
        }
    }
}

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
            composable("MealDetail/{mealId}") { backStackEntry ->
                val mealId = backStackEntry.arguments?.getString("mealId")
                if (mealId != null) {
                    MealDetailScreen(navController = navController, mealId = mealId)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AsyncAppPreview() {
    val navController = rememberNavController()
    val viewModel: TheFoodSoViewModel = viewModel()

    MainScreenUI(navController, viewModel)
}
