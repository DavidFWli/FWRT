package com.fwrt.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fwrt.database.AppDatabase

import com.fwrt.databinding.ActivityRegisterBinding
import com.fwrt.models.User
import kotlinx.coroutines.launch
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var selectedBirthday: Long = 0L  // 时间戳形式存储

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Register"

        // 1️⃣ 设置性别选择器
        val genderOptions = listOf("Male", "Female", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genderOptions)
        binding.spinnerGender.adapter = adapter

        // 2️⃣ 设置生日选择器
        binding.etBirthday.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedBirthday = calendar.timeInMillis
                    binding.etBirthday.setText("$year-${month + 1}-$dayOfMonth")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // 3️⃣ 注册按钮逻辑
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val gender = binding.spinnerGender.selectedItem.toString()

            if (username.isEmpty() || password.isEmpty() || selectedBirthday == 0L) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val userDao = AppDatabase.getDatabase(this@RegisterActivity).userDao()
                val existingUser = userDao.getUserByUsername(username)
                if (existingUser != null) {
                    Toast.makeText(this@RegisterActivity, "Username already exists", Toast.LENGTH_SHORT).show()
                } else {
                    val user = User(username = username, password = password, gender = gender, birthday = selectedBirthday.toString())
                    userDao.insertUser(user)
                    Toast.makeText(this@RegisterActivity, "Registration successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}
