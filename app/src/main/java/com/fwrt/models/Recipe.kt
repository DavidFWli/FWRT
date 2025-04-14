package com.fwrt.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
@Entity
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val ingredients: String   // 示例："番茄:100g, 鸡蛋:1个, 盐:适量"

)
@Dao
interface RecipeDao {
    @Insert
    suspend fun insertRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipe")
    suspend fun getAllRecipes(): List<Recipe>

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)
}

