package com.example.cookbook.ui.fragments.detailfragments

import android.animation.Animator
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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.example.cookbook.adapter.recyclerviewadapter.FavoritesAdapter
import com.example.cookbook.adapter.recyclerviewadapter.SearchAdapter
import com.example.cookbook.api.RetrofitInstance
import com.example.cookbook.database.CookDatabase
import com.example.cookbook.databinding.FragmentDiscoverBinding
import com.example.cookbook.model.Meal
import com.example.cookbook.repository.FavoriteMealRepository
import com.example.cookbook.repository.MealRepository
import com.example.cookbook.state.DiscoverState
import com.example.cookbook.state.FavState
import com.example.cookbook.util.DiscoverType
import com.example.cookbook.viewmodel.DiscoverViewModel
import com.example.cookbook.viewmodel.FavoriteViewModel
import com.example.cookbook.viewmodelfactory.DiscoverViewModelFactory
import com.example.cookbook.viewmodelfactory.FavoriteViewModelFactory
import kotlinx.coroutines.launch


class DiscoverFragment : Fragment() {

    private var _binding : FragmentDiscoverBinding? = null
    val binding get() = _binding!!
    private lateinit var discoverViewModel: DiscoverViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var mealRepository: MealRepository
    private lateinit var favoriteMealRepository: FavoriteMealRepository
    private lateinit var searchAdapter: SearchAdapter
    private val args: DiscoverFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val api = RetrofitInstance.api
        mealRepository = MealRepository(api)
        val favDao = CookDatabase.createDatabase(requireContext()).favoriteMealDao()
        favoriteMealRepository = FavoriteMealRepository(favDao)
        favoriteViewModel = ViewModelProvider(this, FavoriteViewModelFactory(favoriteMealRepository))[FavoriteViewModel::class.java]
        discoverViewModel = ViewModelProvider(this, DiscoverViewModelFactory(mealRepository))[DiscoverViewModel::class.java]
        searchAdapter = SearchAdapter(mutableListOf())

        binding.apply {
            rvFilter.layoutManager = LinearLayoutManager(requireContext())
            rvFilter.adapter = searchAdapter
            rvFilter.clipToPadding = false
            rvFilter.isNestedScrollingEnabled = false
            tvSubTitle.text = args.name
            chipTag1.text = args.name
        }



        val name = args.name.replace(" ","_")

        when(args.type){
            DiscoverType.CATEGORY -> {discoverViewModel.filterByCategory(name)}
            DiscoverType.AREA -> {discoverViewModel.filterByArea(name)}
        }
        


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                discoverViewModel.state.collect { state ->
                    when(state){
                        DiscoverState.Idle -> {}
                        DiscoverState.Loading -> {
                            binding.loadingAnimation.visibility = View.VISIBLE
                            binding.layoutDiscover.visibility = View.GONE

                            binding.loadingAnimation.repeatCount = LottieDrawable.INFINITE

                            if (!binding.loadingAnimation.isAnimating) {
                                binding.loadingAnimation.playAnimation()
                            }
                        }
                        is DiscoverState.Success ->{
                            state.meal.meals?.let { meals ->

                                searchAdapter.updateList(meals)
                                binding.chipTag2.text = "${meals.size} recipes"

                                binding.loadingAnimation.cancelAnimation()
                                binding.loadingAnimation.visibility = View.GONE
                                binding.layoutDiscover.visibility = View.VISIBLE

                            }
                        }
                        is DiscoverState.Error -> {
                            binding.loadingAnimation.cancelAnimation()
                            binding.loadingAnimation.visibility = View.GONE
                            binding.layoutDiscover.visibility = View.VISIBLE
                            Toast.makeText(requireContext(),state.message,Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }

                }
            }
        }

        favoriteViewModel.collectFavorites()


        // Handle favorites
        searchAdapter.onFavClick = {meal ->
            favoriteViewModel.toggleFavorite(meal)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                favoriteViewModel.favState.collect { state ->
                    when(state){
                        FavState.Idle -> {
                        }
                        FavState.Loading -> {}
                        is FavState.Success -> {
                            val favIds = state.meals.map{it.idMeal}.toSet()
                            searchAdapter.updateFavorites(favIds)
                        }
                        is FavState.Error -> {
                            Toast.makeText(requireContext(), state.message,Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }

        searchAdapter.onItemClick = { it ->
            val action = DiscoverFragmentDirections.actionDiscoverFragmentToDetailFragment(it)
            findNavController().navigate(action)
        }

    }


}