package com.fwrt.adapters

import android.app.AlertDialog
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fwrt.database.AppDatabase
import com.fwrt.databinding.ItemFoodBinding
import com.fwrt.models.Food
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoodAdapter(
    private val foodList: MutableList<Food>,
    private val onDeleteClick: (Food) -> Unit
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    inner class FoodViewHolder(val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foodList[position]
        holder.binding.imgFood.setImageURI(Uri.parse(food.imageUri))
        holder.binding.tvFoodName.text = food.name
        holder.binding.tvNutrition.text = "Nutrition: ${food.nutrition}"
        holder.binding.tvPurchaseDate.text = "Purchased: ${formatDate(food.purchaseDate)}"
        holder.binding.tvExpiryDate.text = "Expires: ${formatDate(food.expiryDate)}"
        holder.binding.tvDaysLeft.text = getDaysLeftText(food.expiryDate)

        holder.binding.imgFood.setOnClickListener {
            val context = holder.itemView.context
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val food = foodList[position]
                AlertDialog.Builder(context)
                    .setTitle("Delete Food")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes") { _, _ ->
                        CoroutineScope(Dispatchers.IO).launch {
                            AppDatabase.getDatabase(context).foodDao().deleteFood(food)
                            withContext(Dispatchers.Main) {
                                foodList.removeAt(position)
                                notifyItemRemoved(position)
                            }
                        }
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        }

        holder.itemView.setOnClickListener {
            // Handle item click if needed
        }
    }

    override fun getItemCount(): Int = foodList.size

    fun submitList(newList: List<Food>) {
        foodList.clear()
        foodList.addAll(newList)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        foodList.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(java.util.Date(timestamp))
    }

    private fun getDaysLeftText(expiryDate: Long): String {
        val daysLeft = ((expiryDate - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toInt()
        return "Expires in ${daysLeft} days"
    }
}
