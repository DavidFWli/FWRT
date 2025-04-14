package com.fwrt.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fwrt.activities.CookbookActivity
import com.fwrt.adapters.FoodAdapter
import com.fwrt.database.AppDatabase

import com.fwrt.databinding.FragmentHomeBinding
import com.fwrt.models.Food
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var foodAdapter: FoodAdapter
    private val foodList = mutableListOf<Food>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foodAdapter = FoodAdapter(foodList) { food ->
            val intent = Intent(requireContext(), CookbookActivity::class.java)
            intent.putExtra("ingredient", food.name)
            startActivity(intent)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = foodAdapter
        }

        loadFoodData()
    }

    private fun loadFoodData() {
        lifecycleScope.launch {
            val dao = AppDatabase.getDatabase(requireContext()).foodDao()
            val today = System.currentTimeMillis()
            val sortedFoods = dao.getAllSortedByExpiry(today)
            foodList.clear()
            foodList.addAll(sortedFoods)
            foodAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
