package com.fwrt.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.fwrt.models.Food

@Dao
interface FoodDao {

    @Insert
    suspend fun insertFood(food: Food)
    @Delete
    suspend fun deleteFood(food: Food)

    @Query("SELECT * FROM Food")
    suspend fun getAllFoods(): List<Food>

    @Query("SELECT * FROM Food WHERE expiryDate < :now")
    suspend fun getExpiredFoods(now: Long): List<Food>

    @Query("SELECT * FROM Food WHERE expiryDate >= :today ORDER BY expiryDate ASC")
    suspend fun getAllSortedByExpiry(today: Long): List<Food>

    @Query("DELETE FROM Food WHERE id = :id")
    suspend fun deleteFoodById(id: Int)

}
