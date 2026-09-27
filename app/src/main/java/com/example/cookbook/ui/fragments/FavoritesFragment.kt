package com.example.cookbook.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.R
import com.example.cookbook.adapter.recyclerviewadapter.FavoritesAdapter
import com.example.cookbook.database.CookDatabase
import com.example.cookbook.databinding.FragmentFavoritesBinding
import com.example.cookbook.repository.FavoriteMealRepository
import com.example.cookbook.state.FavState
import com.example.cookbook.viewmodel.FavoriteViewModel
import com.example.cookbook.viewmodelfactory.FavoriteViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FavoritesFragment : Fragment() {

    private var _binding : FragmentFavoritesBinding? = null
    val binding get() = _binding!!
    private lateinit  var adapter : FavoritesAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favDao = CookDatabase.createDatabase(requireContext()).favoriteMealDao()
        val favMealRepository = FavoriteMealRepository(favDao)
        favoriteViewModel = ViewModelProvider(this, FavoriteViewModelFactory(favMealRepository))[FavoriteViewModel::class.java]
        adapter = FavoritesAdapter(mutableListOf())
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favoriteRecyclerView.adapter = adapter

        favoriteViewModel.collectFavorites()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                favoriteViewModel.favState.collect { state ->
                    when(state){
                        FavState.Idle -> {
                            adapter.updateList(emptyList())
                            binding.layoutBegin.visibility = View.VISIBLE
                            binding.favoriteRecyclerView.visibility = View.GONE
                        }
                        FavState.Loading -> {}
                        is FavState.Success -> {
                            binding.tvCount.text = state.meals.size.toString()
                            adapter.updateList(state.meals)

                            if(state.meals.isEmpty()){
                                binding.layoutBegin.visibility = View.VISIBLE
                                binding.favoriteRecyclerView.visibility = View.GONE
                            }
                            else{
                                binding.layoutBegin.visibility = View.GONE
                                binding.favoriteRecyclerView.visibility = View.VISIBLE
                            }
                        }
                        is FavState.Error -> {
                            adapter.updateList(emptyList())
                            Toast.makeText(requireContext(), state.message,Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }

        }

        binding.btnExplore.setOnClickListener {
            findNavController().navigate(R.id.action_favoritesFragment_to_searchFragment)
        }

        adapter.onFavClick = { meal ->
            favoriteViewModel.deleteFavorite(meal)
        }

        adapter.onItemClick = { id ->
            val action = FavoritesFragmentDirections.actionFavoritesFragmentToDetailFragment(id)
            findNavController().navigate(action)
        }


    }


}