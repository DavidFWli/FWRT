package com.fwrt.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.fwrt.models.Recipe

@Dao
interface RecipeDao {
    @Insert
    suspend fun insertRecipe(recipe: Recipe)

    @Query("SELECT * FROM Recipe")
    suspend fun getAllRecipes(): List<Recipe>

    @Query("SELECT * FROM Recipe WHERE id = :id")
    suspend fun getRecipeById(id: Int): Recipe?

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

}
