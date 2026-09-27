package com.example.cookbook.ui.fragments

import android.animation.Animator
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.R
import com.example.cookbook.adapter.recyclerviewadapter.SearchAdapter
import com.example.cookbook.adapter.recyclerviewadapter.WorldAdapter
import com.example.cookbook.api.RetrofitInstance
import com.example.cookbook.database.CookDatabase
import com.example.cookbook.databinding.FragmentHomeBinding
import com.example.cookbook.event.HomeEvent
import com.example.cookbook.model.Country
import com.example.cookbook.model.Meal
import com.example.cookbook.preferences.SessionManager
import com.example.cookbook.repository.FavoriteMealRepository
import com.example.cookbook.repository.MealRepository
import com.example.cookbook.state.FavState
import com.example.cookbook.state.HomeState
import com.example.cookbook.util.DialogUtil
import com.example.cookbook.util.DiscoverType
import com.example.cookbook.viewmodel.FavoriteViewModel
import com.example.cookbook.viewmodel.HomeViewModel
import com.example.cookbook.viewmodelfactory.FavoriteViewModelFactory
import com.example.cookbook.viewmodelfactory.HomeViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var mealRepository: MealRepository
    private lateinit var favoriteMealRepository: FavoriteMealRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var worldAdapter: WorldAdapter
    private lateinit var cuisineAdapter: SearchAdapter
    private lateinit var ingredientAdapter: SearchAdapter

    private var selectedMeal: Meal? = null
    private var isFinished = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val api = RetrofitInstance.api
        mealRepository = MealRepository(api)
        val favDao = CookDatabase.createDatabase(requireContext()).favoriteMealDao()
        favoriteMealRepository = FavoriteMealRepository(favDao)
        val sharedPreferences = requireContext().getSharedPreferences("session", Activity.MODE_PRIVATE)
        sessionManager = SessionManager(sharedPreferences)

        homeViewModel = ViewModelProvider(requireActivity(), HomeViewModelFactory(mealRepository))[HomeViewModel::class.java]
        favoriteViewModel = ViewModelProvider(requireActivity(), FavoriteViewModelFactory(favoriteMealRepository))[FavoriteViewModel::class.java]

        loadCuisineCards()

        cuisineAdapter = SearchAdapter(mutableListOf())
        binding.rvMadeForYou.adapter = cuisineAdapter
        binding.rvMadeForYou.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvMadeForYou.clipToPadding = false

        ingredientAdapter = SearchAdapter(mutableListOf())
        binding.rvFav.adapter = ingredientAdapter
        binding.rvFav.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFav.clipToPadding = false

        binding.rvExplore.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvExplore.clipToPadding = false
        worldAdapter.onItemClick = { item ->
            val action = HomeFragmentDirections.actionHomeFragmentToDiscoverFragment(
                item, DiscoverType.AREA)
            findNavController().navigate(action)
        }

        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                findNavController().navigate(R.id.action_homeFragment_to_gameFragment)
            }
        }

        // Marquee
        loadMarquee()

        // Categories
        binding.tvSeeAll.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_categoriesFragment)
        }

        binding.apply {

            chipChicken.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToDiscoverFragment("Chicken",
                    DiscoverType.CATEGORY)
                findNavController().navigate(action)
            }

            chipBeef.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToDiscoverFragment("Beef",
                    DiscoverType.CATEGORY)
                findNavController().navigate(action)
            }

            chipDessert.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToDiscoverFragment("Dessert",
                    DiscoverType.CATEGORY)
                findNavController().navigate(action)
            }

            chipVegan.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToDiscoverFragment("Vegan",
                    DiscoverType.CATEGORY)
                findNavController().navigate(action)
            }
        }


        binding.lottieAnimation.setOnClickListener {

            selectedMeal = null
            isFinished = false

            binding.lottieAnimation.playAnimation()
            
            homeViewModel.getRandomMeal()
        }

        binding.lottieAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                isFinished = true
                navigateIfReady()
            }

            override fun onAnimationRepeat(p0: Animator) {
            }

            override fun onAnimationStart(p0: Animator) {
            }
        })



        binding.tvSelectCuisine.setOnClickListener {
            DialogUtil.showCuisineDialog(requireContext(), layoutInflater, sessionManager.getCuisine() ?: ""){cuisine ->
                sessionManager.saveCuisine(cuisine)
                homeViewModel.filterBySelectedCuisine(cuisine)
                binding.rvMadeForYou.visibility = View.VISIBLE
                binding.emptyCuisineCard.visibility = View.GONE
            }
        }

        binding.tvSelectIngredient.setOnClickListener {
            DialogUtil.showIngredientsDialog(requireContext(),layoutInflater,sessionManager.getIngredient() ?: ""){ ingredient ->
                sessionManager.saveIngredient(ingredient)
                homeViewModel.filterBySelectedIngredient(ingredient)
                binding.rvFav.visibility = View.VISIBLE
                binding.emptyIngredientCard.visibility = View.GONE
            }
        }




        val selectedCuisine = sessionManager.getCuisine()
        if(selectedCuisine != null){
            homeViewModel.filterBySelectedCuisine(selectedCuisine)
            binding.rvMadeForYou.visibility = View.VISIBLE
            binding.emptyCuisineCard.visibility = View.GONE
        }
        else{
            binding.rvMadeForYou.visibility = View.GONE
            binding.emptyCuisineCard.visibility = View.VISIBLE
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                homeViewModel.filter.collect { selectedCuisine ->
                   selectedCuisine?.meals?.let { meals ->
                       cuisineAdapter.updateList(meals)
                   }
                }
            }
        }

        val selectedIngredient = sessionManager.getIngredient()
        if(selectedIngredient != null){
            homeViewModel.filterBySelectedIngredient(selectedIngredient)
            binding.rvFav.visibility = View.VISIBLE
            binding.emptyIngredientCard.visibility = View.GONE
        }
        else{
            binding.rvFav.visibility = View.GONE
            binding.emptyIngredientCard.visibility = View.VISIBLE
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                homeViewModel.ingredient.collect { selectedIngredient ->
                    selectedIngredient?.meals?.let{ meals ->
                        ingredientAdapter.updateList(meals)
                    }
                }
            }
        }

        cuisineAdapter.onItemClick = { id ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(id)
            findNavController().navigate(action)
        }

        cuisineAdapter.onFavClick = { meal ->
            favoriteViewModel.toggleFavorite(meal)
        }

        ingredientAdapter.onItemClick = { id ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(id)
            findNavController().navigate(action)
        }

        favoriteViewModel.collectFavorites()


        ingredientAdapter.onFavClick = { meal ->
            favoriteViewModel.toggleFavorite(meal)
        }

        // Favorite

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                favoriteViewModel.favState.collect { state ->
                    when(state){
                        FavState.Idle -> {
                        }
                        FavState.Loading -> {}
                        is FavState.Success -> {
                            val ids = state.meals.map { it.idMeal }.toSet()

                            cuisineAdapter.updateFavorites(ids)
                            ingredientAdapter.updateFavorites(ids)
                        }
                        is FavState.Error -> {
                            Toast.makeText(requireContext(), state.message,Toast.LENGTH_SHORT).show()
                        }

                    }

                }
            }
        }



        // Random Recipe
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                homeViewModel.homeState.collect{ state ->
                    when(state){
                        is HomeState.Loading -> {}
                        is HomeState.Success -> {
                            selectedMeal = state.meal
                        }
                        is HomeState.Error -> {Toast.makeText(requireContext(),"Meal not found!", Toast.LENGTH_SHORT).show()}
                        is HomeState.Idle -> Unit
                    }
                }
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                homeViewModel.homeEvent.collect { event ->
                    when(event){
                        is HomeEvent.NavigateToDetail -> {
                            navigateIfReady()
                        }
                    }
                }
            }
        }
    }

    private fun navigateIfReady(){
        if(isFinished && selectedMeal != null){
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(selectedMeal!!.idMeal))
        }
    }

    private fun loadCuisineCards(){

        val list = listOf<Country>(Country("Spain",R.string.spain,R.drawable.ic_flag_es),
            Country("Brazil",R.string.brazil,R.drawable.ic_flag_br),
            Country("China",R.string.china,R.drawable.ic_flag_cn),
            Country("France",R.string.france,R.drawable.ic_flag_fr),
            Country("Turkey",R.string.turkey,R.drawable.ic_flag_tr),
            Country("Netherlands", R.string.netherlands,R.drawable.ic_flag_nl),
            Country("India",R.string.india,R.drawable.ic_flag_in),
            Country("Greece",R.string.greece,R.drawable.ic_flag_gr),
            Country("Italy",R.string.italy, R.drawable.ic_flag_it),
            Country("United States",R.string.united_states, R.drawable.ic_flag_us),
            Country("Japan",R.string.japan, R.drawable.ic_flag_jp),
            Country("Ukraine",R.string.ukraine, R.drawable.ic_flag_ua),
            Country("United Kingdom",R.string.united_kingdom, R.drawable.ic_flag_gb),
            Country("Ireland",R.string.ireland, R.drawable.ic_flag_ie),
            Country("Russia",R.string.russia, R.drawable.ic_flag_ru),
            Country("Thailand",R.string.thailand, R.drawable.ic_flag_th),
            Country("Morocco",R.string.morocco, R.drawable.ic_flag_ma),
            Country("Vietnam",R.string.vietnam, R.drawable.ic_flag_vn),
            Country("Bulgaria", R.string.brazil, R.drawable.ic_flag_bg),
        )

        worldAdapter = WorldAdapter(list)
        binding.rvExplore.adapter = worldAdapter
    }

    private fun loadMarquee(){
        val marquee = listOf(R.string.set1, R.string.set2, R.string.set3, R.string.set4, R.string.set5)
        val randomSet = marquee.random()
        binding.tvMarquee.text = getString(randomSet)
        binding.tvMarquee.isSelected = true
    }

}


