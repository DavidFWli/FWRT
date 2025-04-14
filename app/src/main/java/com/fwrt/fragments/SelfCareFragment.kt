package com.fwrt.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fwrt.adapters.FoodAdapter
import com.fwrt.database.AppDatabase
import com.fwrt.databinding.FragmentSelfCareBinding
import com.fwrt.models.Food
import kotlinx.coroutines.launch

class SelfCareFragment : Fragment() {

    private var _binding: FragmentSelfCareBinding? = null
    private val binding get() = _binding!!

    private lateinit var foodAdapter: FoodAdapter
    private val expiredFoods = mutableListOf<Food>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSelfCareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foodAdapter = FoodAdapter(expiredFoods) {}
        binding.recyclerExpired.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = foodAdapter
        }

        loadExpiredFoods()
    }

    private fun loadExpiredFoods() {
        lifecycleScope.launch {
            val dao = AppDatabase.getDatabase(requireContext()).foodDao()
            val now = System.currentTimeMillis()
            val data = dao.getExpiredFoods(now)
            expiredFoods.clear()
            expiredFoods.addAll(data)
            foodAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
