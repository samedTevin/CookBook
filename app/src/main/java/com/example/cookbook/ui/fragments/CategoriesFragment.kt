package com.example.cookbook.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cookbook.adapter.recyclerviewadapter.CategoriesAdapter
import com.example.cookbook.api.RetrofitInstance
import com.example.cookbook.databinding.FragmentCategoriesBinding
import com.example.cookbook.repository.CategoryRepository
import com.example.cookbook.util.DiscoverType
import com.example.cookbook.viewmodel.CategoryViewModel
import com.example.cookbook.viewmodelfactory.CategoryViewModelFactory
import kotlinx.coroutines.launch


class CategoriesFragment : Fragment() {

    private var _binding : FragmentCategoriesBinding? = null
    val binding get() = _binding!!
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var adapter: CategoriesAdapter
    private lateinit var categoryRepository: CategoryRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val api = RetrofitInstance.api
        categoryRepository = CategoryRepository(api)
        adapter = CategoriesAdapter(mutableListOf())
        categoryViewModel = ViewModelProvider(requireActivity(), CategoryViewModelFactory(categoryRepository))[CategoryViewModel::class.java]
        binding.favoriteRecyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        binding.favoriteRecyclerView.adapter = adapter

        categoryViewModel.getCategories()

        viewLifecycleOwner.lifecycleScope.launch {
            categoryViewModel.category.collect { categories ->
                categories?.let{
                    adapter.updateList(it.categories)
                }
            }
        }

        adapter.onItemClick = { name ->
            val action = CategoriesFragmentDirections.actionCategoriesFragmentToDiscoverFragment(name,
                DiscoverType.CATEGORY)
            findNavController().navigate(action)
        }
    }

}