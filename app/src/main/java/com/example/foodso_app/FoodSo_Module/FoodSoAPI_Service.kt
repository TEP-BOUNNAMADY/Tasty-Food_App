package com.example.foodso_app.FoodSo_Module

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val Base_Url = "https://www.themealdb.com/api/json/v1/1/"
interface TheFoodSoService {
    @GET("search.php?s=")
    suspend fun searchMeals(@Query("s") query: String): MealsResponse

    @GET("filter.php?c=")
    suspend fun searchMealsByCategory(@Query("c") category: String): MealsResponse

    @GET("categories.php")
    suspend fun getCategories(): CategoriesResponse

    @GET("filter.php?a=")
    suspend fun searchMealsByArea(@Query("a") area: String): MealsResponse


    companion object {
        private var apiService: TheFoodSoService? = null
        fun getInstance(): TheFoodSoService {
            if (apiService == null) {
                val gson = GsonBuilder().setLenient().create()
                apiService = Retrofit.Builder()
                    .baseUrl(Base_Url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(TheFoodSoService::class.java)
            }
            return apiService!!
        }
    }
}

