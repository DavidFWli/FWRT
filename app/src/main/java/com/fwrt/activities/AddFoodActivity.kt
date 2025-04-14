package com.fwrt.activities

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.fwrt.database.AppDatabase
import com.fwrt.databinding.ActivityAddFoodBinding
import com.fwrt.models.Food
import kotlinx.coroutines.launch
import java.util.*

class AddFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFoodBinding
    private var imageUri: String = ""
    private var purchaseDate: Long = 0L
    private var expiryDate: Long = 0L

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Add Food"

        // 可预填条码内容
        val barcode = intent.getStringExtra("barcode")
        barcode?.let {
            binding.etName.setText(it) // 假设条码名作食物名
        }

        // 日期选择器
        binding.etPurchaseDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                cal.set(y, m, d)
                purchaseDate = cal.timeInMillis
                binding.etPurchaseDate.setText("$y-${m + 1}-$d")
            }, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH]).show()
        }

        binding.etExpiryDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                cal.set(y, m, d)
                expiryDate = cal.timeInMillis
                binding.etExpiryDate.setText("$y-${m + 1}-$d")
            }, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH]).show()
        }

        binding.btnSaveFood.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val nutrition = binding.etNutrition.text.toString().trim()

            if (name.isEmpty() || nutrition.isEmpty() || purchaseDate == 0L || expiryDate == 0L) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val food = Food(
                name = name,
                nutrition = nutrition,
                purchaseDate = purchaseDate,
                expiryDate = expiryDate,
                imageUri = imageUri // 还可以添加图片上传
            )

            lifecycleScope.launch {
                AppDatabase.getDatabase(this@AddFoodActivity).foodDao().insertFood(food)
                Toast.makeText(this@AddFoodActivity, "Food added", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
