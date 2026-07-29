package com.example.cookbook.ui.fragments

import android.animation.Animator
import android.app.Activity
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
import com.example.cookbook.adapter.recyclerviewadapter.SearchAdapter
import com.example.cookbook.adapter.recyclerviewadapter.WorldAdapter
import com.example.cookbook.api.RetrofitInstance
import com.example.cookbook.databinding.FragmentHomeBinding
import com.example.cookbook.event.HomeEvent
import com.example.cookbook.model.Country
import com.example.cookbook.model.Meal
import com.example.cookbook.preferences.SessionManager
import com.example.cookbook.repository.MealRepository
import com.example.cookbook.state.HomeState
import com.example.cookbook.util.DiscoverType
import com.example.cookbook.viewmodel.HomeViewModel
import com.example.cookbook.viewmodelfactory.HomeViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mealRepository: MealRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var worldAdapter: WorldAdapter
    private lateinit var searchAdapter: SearchAdapter
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
        val sharedPreferences = requireContext().getSharedPreferences("session", Activity.MODE_PRIVATE)
        sessionManager = SessionManager(sharedPreferences)

        homeViewModel = ViewModelProvider(requireActivity(), HomeViewModelFactory(mealRepository))[HomeViewModel::class.java]

        loadCuisineCards()

        searchAdapter = SearchAdapter(mutableListOf())
        binding.rvMadeForYou.adapter = searchAdapter
        binding.rvMadeForYou.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvMadeForYou.clipToPadding = false

        binding.rvExplore.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvExplore.clipToPadding = false
        worldAdapter.onItemClick = { item ->
            val action = HomeFragmentDirections.actionHomeFragmentToDiscoverFragment(
                item, DiscoverType.AREA)
            findNavController().navigate(action)
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
                       searchAdapter.updateList(meals)
                   }
                }
            }
        }

        searchAdapter.onItemClick = { id ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(id)
            findNavController().navigate(action)
        }


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
                            findNavController()
                        }
                    }
                }
            }
        }
    }

    fun navigateIfReady(){
        if(isFinished && selectedMeal != null){
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(selectedMeal!!.idMeal))
        }
    }

    fun loadCuisineCards(){

        val list = listOf<Country>(Country("Spain",R.drawable.ic_flag_es),
            Country("Brazil",R.drawable.ic_flag_br),
            Country("China",R.drawable.ic_flag_cn),
            Country("France",R.drawable.ic_flag_fr),
            Country("Turkey",R.drawable.ic_flag_tr),
            Country("Netherlands", R.drawable.ic_flag_nl),
            Country("India",R.drawable.ic_flag_in),
            Country("Greece",R.drawable.ic_flag_gr),
            Country("Italy",R.drawable.ic_flag_it),
            Country("United States",R.drawable.ic_flag_us),
            Country("Japan",R.drawable.ic_flag_jp),
            Country("Ukraine",R.drawable.ic_flag_ua),
            Country("United Kingdom",R.drawable.ic_flag_gb),
            Country("Ireland",R.drawable.ic_flag_ie),
            Country("Russia",R.drawable.ic_flag_ru),
            Country("Thailand",R.drawable.ic_flag_th),
            Country("Morocco",R.drawable.ic_flag_ma),
            Country("Vietnam",R.drawable.ic_flag_vn),
            Country("Bulgaria", R.drawable.ic_flag_bg),
            Country("Malesia",R.drawable.ic_flag_my)
        )

        worldAdapter = WorldAdapter(list)
        binding.rvExplore.adapter = worldAdapter
    }


}


