package com.example.cookbook.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.R
import com.example.cookbook.adapter.recyclerviewadapter.SearchAdapter
import com.example.cookbook.api.ApiService
import com.example.cookbook.api.RetrofitInstance
import com.example.cookbook.databinding.FragmentSearchBinding
import com.example.cookbook.model.MealResponse
import com.example.cookbook.repository.MealRepository
import com.example.cookbook.state.SearchState
import com.example.cookbook.viewmodel.SearchViewModel
import com.example.cookbook.viewmodelfactory.SearchViewModelFactory
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    val binding get() = _binding!!

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var mealRepository: MealRepository
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val api = RetrofitInstance.api
        mealRepository = MealRepository(api)
        searchViewModel = ViewModelProvider(requireActivity(), SearchViewModelFactory(mealRepository))[SearchViewModel::class.java]
        searchAdapter = SearchAdapter(mutableListOf())
        binding.rvSearchResult.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchResult.adapter = searchAdapter
        binding.rvSearchResult.isNestedScrollingEnabled = false

        binding.searchView.setQuery(searchViewModel.query.value,false)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String?): Boolean {

                searchViewModel.updateQuery(query.orEmpty())

                if(query.isNullOrEmpty()){
                    searchViewModel.setIdle()
                    return true
                }

                searchViewModel.searchMeal(query)
                return true
            }

            override fun onQueryTextSubmit(newText: String?): Boolean {
                return false
            }
        })

        searchAdapter.onItemClick = {item ->
            val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(item)
            findNavController().navigate(action)
        }



        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.searchStateFlow.collect { state ->

                when(state){
                    SearchState.Idle ->{
                        searchAdapter.updateList(emptyList())
                        binding.layoutBegin.visibility = View.VISIBLE
                        binding.layoutResult.visibility  = View.GONE
                        binding.layoutNotFound.visibility = View.GONE
                    }
                    SearchState.Loading -> {}

                    is SearchState.Success -> {
                        searchAdapter.updateList(state.meal)
                        binding.layoutBegin.visibility = View.GONE
                        binding.layoutResult.visibility  = View.VISIBLE
                        binding.layoutNotFound.visibility = View.GONE
                    }

                    SearchState.NotFound -> {
                        searchAdapter.updateList(emptyList())
                        binding.layoutBegin.visibility = View.GONE
                        binding.layoutResult.visibility  = View.GONE
                        binding.layoutNotFound.visibility = View.VISIBLE
                    }
                }
                
            }
        }

    }

}