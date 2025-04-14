package com.fwrt.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fwrt.R
import com.fwrt.adapters.FoodAdapter
import com.fwrt.database.AppDatabase
import com.fwrt.databinding.ActivityMainBinding
import com.fwrt.fragments.CookbookFragment
import com.fwrt.fragments.FoodImageFragment
import com.fwrt.fragments.HomeFragment
import com.fwrt.fragments.SelfCareFragment
import com.fwrt.fragments.SettingFragment
import com.fwrt.models.Food
import com.fwrt.utils.BarcodeScannerUtil
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var foodAdapter: FoodAdapter
    private val foodList = mutableListOf<Food>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Expiring Foods"

        setupRecyclerView()
        loadFoodData()

        // 底部导航栏切换
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()

                    true
                }
                R.id.nav_food -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FoodImageFragment())
                        .commit()
                    true
                }
                R.id.nav_cookbook -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CookbookFragment())
                        .commit()
                    true
                }
                R.id.nav_selfcare -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SelfCareFragment())
                        .commit()
                    true
                }
                R.id.nav_settings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SettingFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }

    }

    private fun setupRecyclerView() {
        foodAdapter = FoodAdapter(foodList) { food ->
            // 点击跳转推荐菜谱
            val intent = Intent(this, CookbookFragment::class.java)
            intent.putExtra("ingredient", food.name)
            startActivity(intent)
        }
        binding.recyclerView.apply {
            adapter = foodAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun loadFoodData() {
        lifecycleScope.launch {
            val dao = AppDatabase.getDatabase(this@MainActivity).foodDao()
            val today = System.currentTimeMillis()
            val sortedFoods = dao.getAllSortedByExpiry(today)
            foodList.clear()
            foodList.addAll(sortedFoods)
            foodAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_scan -> {
                BarcodeScannerUtil.scanBarcode(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        BarcodeScannerUtil.handleScanResult(this, requestCode, resultCode, data) { barcode ->
            // 在此处理扫码结果，例如跳转到添加食物页面并填入条码内容
            Toast.makeText(this, "Scanned: $barcode", Toast.LENGTH_SHORT).show()
        }
    }

}
