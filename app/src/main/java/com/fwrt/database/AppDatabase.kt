package com.fwrt.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.fwrt.models.Food
import com.fwrt.models.Recipe
import com.fwrt.models.User

@Database(entities = [Food::class, Recipe::class, User::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun recipeDao(): RecipeDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fwrt_database"
                ).fallbackToDestructiveMigration()  // 可以清空旧数据库
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
