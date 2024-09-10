package com.example.foodso_app.FoodSo_Module

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun SearchScreenUI(nc: NavController, vm: TheFoodSoViewModel) {
    val meals by vm.meals.collectAsState()
    val error by vm.error.collectAsState()
    val searchQuery = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {
                TextField(
                    value = searchQuery.value,
                    onValueChange = { query ->
                        searchQuery.value = query
                        vm.searchMeals(query)
                    },
                    label = { Text("Search Food",
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontSize = 15.sp,
                            color = Color(0xFFF806B6)
                        ))
                    },
                    colors = TextFieldDefaults.textFieldColors(

                        textColor = Color(0xFF000000),
                        placeholderColor = Color(0xFFFFFFFF),
                        backgroundColor = Color(0xFFFFFFFF),
                        focusedIndicatorColor = Color(0xFFF806B6),
                        unfocusedIndicatorColor = Color(0xFFb0b4b7)

                    ),

                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                error?.let { errorMessage ->
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (meals.isEmpty() && searchQuery.value.isNotEmpty()) {
                        item {
                            Text(
                                "No meals found",
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    } else {
                        items(meals) { meal ->
                            MealItems(
                                meal = meal,
                                viewModel = vm,
                                onClick = {
                                    nc.navigate("MealDetail/${meal.strMeal}")
                                },
                                onFavoriteChanged = { isFavorite ->
                                    coroutineScope.launch {
                                        val message = if (isFavorite) {
                                            "Added to favorites"
                                        } else {
                                            "Removed from favorites"
                                        }
                                        snackbarHostState.showSnackbar(message)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun MealItems(
    meal: Meal?,
    viewModel: TheFoodSoViewModel,
    onClick: (Meal) -> Unit,
    onFavoriteChanged: (Boolean) -> Unit
) {
    meal?.let {
        var isFavorite by remember { mutableStateOf(viewModel.isFavorite(meal.idMeal)) }
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center)
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(it) }
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,

            ) {
                AsyncImage(
                    model = it.strMealThumb,
                    contentDescription = it.strMeal,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = it.strMeal,
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            color = Color(0xFF000000)
                        ),
                    )
                    Text(
                        text = "From: ${meal.strArea ?: "Unknown"}",
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp,
                        ),
                        color = Color(0xFF000000)
                    )
                }
                IconButton(
                    onClick = {
                        viewModel.toggleFavorite(it)
                        isFavorite = !isFavorite
                        onFavoriteChanged(isFavorite)
                    }
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (isFavorite) Color.Red else Color.Black
                    )
                }
            }
        }
    }
}
