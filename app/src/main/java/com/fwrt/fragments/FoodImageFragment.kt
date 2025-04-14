package com.fwrt.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fwrt.activities.AddFoodActivity
import com.fwrt.adapters.FoodAdapter
import com.fwrt.database.AppDatabase
import com.fwrt.databinding.FragmentFoodImageBinding
import com.fwrt.models.Food
import kotlinx.coroutines.launch

class FoodImageFragment : Fragment() {

    private var _binding: FragmentFoodImageBinding? = null
    private val binding get() = _binding!!

    private lateinit var foodAdapter: FoodAdapter
    private val foodList = mutableListOf<Food>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFoodImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foodAdapter = FoodAdapter(foodList) {
            // 可拓展点击逻辑
        }

        binding.recyclerFood.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = foodAdapter
        }

        binding.btnAddFood.setOnClickListener {
            startActivity(Intent(requireContext(), AddFoodActivity::class.java))
        }

        loadFoodItems()
    }

    private fun loadFoodItems() {
        lifecycleScope.launch {
            val dao = AppDatabase.getDatabase(requireContext()).foodDao()
            val data = dao.getAllFoods()
            foodList.clear()
            foodList.addAll(data)
            foodAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
