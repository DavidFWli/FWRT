package com.fwrt.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fwrt.activities.AddRecipeActivity
import com.fwrt.adapters.RecipeAdapter
import com.fwrt.database.AppDatabase
import com.fwrt.databinding.FragmentCookbookBinding
import com.fwrt.models.Recipe
import kotlinx.coroutines.launch

class CookbookFragment : Fragment() {

    private var _binding: FragmentCookbookBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeAdapter: RecipeAdapter
    private val recipeList = mutableListOf<Recipe>()
    private var keyword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyword = activity?.intent?.getStringExtra("ingredient")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCookbookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeAdapter = RecipeAdapter(recipeList)
        binding.recyclerCookbook.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recipeAdapter
        }

        binding.btnAddRecipe.setOnClickListener {
            startActivity(Intent(requireContext(), AddRecipeActivity::class.java))
        }

        loadRecipeData()
    }

    private fun loadRecipeData() {
        lifecycleScope.launch {
            val dao = AppDatabase.getDatabase(requireContext()).recipeDao()
            val allRecipes = dao.getAllRecipes()

            // 若有传入关键词，则过滤显示
            val filtered = keyword?.let { key ->
                allRecipes.filter { it.ingredients.contains(key, ignoreCase = true) }
            } ?: allRecipes

            recipeList.clear()
            recipeList.addAll(filtered)
            recipeAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
