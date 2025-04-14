package com.fwrt.activities
import com.fwrt.database.AppDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fwrt.databinding.ActivityAddRecipeBinding

import com.fwrt.models.Recipe
import kotlinx.coroutines.launch

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Add Recipe"

        binding.btnSaveRecipe.setOnClickListener {
            val recipeName = binding.etRecipeName.text.toString().trim()
            val ingredients = binding.etIngredients.text.toString().trim()

            if (recipeName.isEmpty() || ingredients.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val recipe = Recipe(name = recipeName, ingredients = ingredients)

            lifecycleScope.launch {
                val dao = AppDatabase.getDatabase(this@AddRecipeActivity).recipeDao()
                dao.insertRecipe(recipe)
                Toast.makeText(this@AddRecipeActivity, "Recipe added!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
