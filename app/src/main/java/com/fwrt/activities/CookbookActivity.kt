package com.fwrt.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fwrt.databinding.ActivityCookbookBinding

class CookbookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCookbookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCookbookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ingredient = intent.getStringExtra("ingredient")
        // TODO: 根据 ingredient 展示推荐菜谱
    }
}
