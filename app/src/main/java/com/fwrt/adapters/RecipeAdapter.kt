package com.fwrt.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fwrt.database.AppDatabase
import com.fwrt.databinding.ItemRecipeBinding
import com.fwrt.models.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeAdapter(
    private val recipeList: MutableList<Recipe>,
    private val onClick: ((Recipe) -> Unit)? = null
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]

        holder.binding.tvRecipeName.text = recipe.name
        holder.binding.tvIngredients.text = "Ingredients: ${recipe.ingredients}"

        holder.binding.root.setOnClickListener {
            onClick?.invoke(recipe)
        }

        // 点击图片删除
        holder.binding.imgRecipe.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Delete Recipe")
            builder.setMessage("Are you sure you want to delete this recipe?")
            builder.setPositiveButton("Yes") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    AppDatabase.getDatabase(holder.itemView.context)
                        .recipeDao()
                        .deleteRecipe(recipe)

                    withContext(Dispatchers.Main) {
                        recipeList.removeAt(position)
                        notifyItemRemoved(position)
                    }
                }
            }
            builder.setNegativeButton("No", null)
            builder.show()
        }
    }

    override fun getItemCount(): Int = recipeList.size
}
