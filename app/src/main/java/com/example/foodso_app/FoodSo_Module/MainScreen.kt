package com.example.foodso_app.FoodSo_Module

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenUI(nc: NavHostController, vm: TheFoodSoViewModel = viewModel()) {
    val categories by vm.categories.collectAsState(emptyList())
    val meals by vm.meals.collectAsState(emptyList())
    val error by vm.error.collectAsState()
    val isLoadingMeals by vm.isLoadingMeals.collectAsState(false)
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val selectedCategory = remember { mutableStateOf<String?>(null) }
    val areaMeals by vm.areaMeals.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        vm.fetchMealsByArea("French")
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tasty Food",
                            style = TextStyle(
                                fontFamily = FontFamily.Serif, // Use a custom font family if desired
                                fontSize = 24.sp, // Adjust font size as needed
                                color = Color.White
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle navigation click */ }) {
                        val painter = rememberAsyncImagePainter("https://i.pinimg.com/736x/bc/63/e8/bc63e892be4e597a70ec50b825f95978.jpg")
                        Image(
                            painter = painter,
                            contentDescription = "Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp) // Adjust size as needed
                                .clip(CircleShape) // Make the image round
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD64174),
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
        ) {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {

                // Category Buttons
                item {
                    LazyRow(
                        modifier = Modifier
                            .padding(top = 16.dp) // Adjust top padding here
                    ) {
                        items(categories) { category ->
                            val isSelected = category.strCategory == selectedCategory.value
                            Button(
                                onClick = {
                                    selectedCategory.value = category.strCategory
                                    vm.clearMeals()
                                    vm.fetchMeals(category = category.strCategory)
                                },
                                modifier = Modifier.padding(horizontal = 4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFDC3D74),
                                    contentColor = Color.White
                                )
                            ) {
                                AsyncImage(
                                    model = category.strCategoryThumb,
                                    contentDescription = category.strCategory,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = category.strCategory,
                                    style = TextStyle(
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 20.sp,
                                    ),
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }

                // Carousel of meal images and names
                item {
                    Carousel(meals = areaMeals) { meal ->
                        MealCarouselItem(
                            meal = meal,
                            onClick = { selectedMeal ->
                                nc.navigate("MealDetail/${selectedMeal.strMeal}")
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }

                // Error message
                error?.let {
                    item {
                        Text("Error: $it", color = MaterialTheme.colorScheme.error)
                    }
                }

                // Meal list
                items(meals) { meal ->
                    MealItem(
                        meal = meal,
                        isLoading = isLoadingMeals,
                        viewModel = vm,
                        onClick = { selectedMeal ->
                            nc.navigate("MealDetail/${selectedMeal.strMeal}")
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




@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(meals: List<Meal>, itemContent: @Composable (Meal) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { meals.size })

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        if (meals.isEmpty()) {
            CircularProgressIndicator()
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp) // Adjusted height to fit the images and content compactly
                    .background(Color(0xFFFFFFFF))
            ) {
                HorizontalPager(state = pagerState) { page ->
                    itemContent(meals[page])
                }
            }
            CustomDotsIndicator(
                totalDots = meals.size,
                selectedIndex = pagerState.currentPage
            )
        }
    }
}

@Composable
fun CustomDotsIndicator(totalDots: Int, selectedIndex: Int) {
    val selectedColor = Color(0xFFFF80AB)
    val unSelectedColor = Color(0xFFB0B0B0)

    DotsIndicator(
        totalDots = totalDots,
        selectedIndex = selectedIndex,
        selectedColor = selectedColor,
        unSelectedColor = unSelectedColor
    )
}


@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color,
    unSelectedColor: Color
) {
    LazyRow(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(10.dp)
    ) {
        items(totalDots) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (index == selectedIndex) selectedColor else unSelectedColor)
            )

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}


@Composable
fun MealCarouselItem(meal: Meal, onClick: (Meal) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth() // Ensure the item fills the width of the screen
            .clickable { onClick(meal) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFFFFF))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                AsyncImage(
                    model = meal.strMealThumb,
                    contentDescription = meal.strMeal,
                    contentScale = ContentScale.Crop, // Ensures the image scales properly
                    modifier = Modifier
                        .fillMaxWidth() // Makes sure the image fills the width of the screen
                        .aspectRatio(16 / 9f) // Adjusted aspect ratio for better fit
                        .clip(RoundedCornerShape(10.dp))
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
//                    Text(
//                        text = meal.strMeal,
//                        style = TextStyle(
//                            fontFamily = FontFamily.Serif,
//                            fontSize = 16.sp, // Adjusted font size to fit better
//                            color = Color(0xFF1E201E)
//                        ),
//                        modifier = Modifier.align(Alignment.Center)
//                    )
                }
            }
        }
    }
}


@Composable
fun MealItem(
    meal: Meal,
    isLoading: Boolean,
    viewModel: TheFoodSoViewModel,
    onClick: (Meal) -> Unit,
    onFavoriteChanged: (Boolean) -> Unit
) {
    var isFavorite by remember { mutableStateOf(viewModel.isFavorite(meal.idMeal)) }

    val animatedModifier = Modifier
        .padding(2.dp)
        .fillMaxWidth()
        .animateContentSize()

    Card(
        modifier = animatedModifier
            .clickable { onClick(meal) }
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFFFF80AB))
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 16.dp)
                    )
                } else {
                    AsyncImage(
                        model = meal.strMealThumb,
                        contentDescription = meal.strMeal,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = meal.strMeal,
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontSize = 24.sp,

                            ),
                        color = Color(0xFF000000)

                    )
                    Spacer(modifier = Modifier.height(8.dp))
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
                        viewModel.toggleFavorite(meal)
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
