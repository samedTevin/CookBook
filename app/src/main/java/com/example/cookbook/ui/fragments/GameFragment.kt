package com.example.cookbook.ui.fragments

import android.graphics.Color
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
import com.example.cookbook.adapter.recyclerviewadapter.SearchAdapter
import com.example.cookbook.api.ApiService
import com.example.cookbook.api.RetrofitInstance
import com.example.cookbook.dao.FavoriteMealDao
import com.example.cookbook.database.CookDatabase
import com.example.cookbook.databinding.FragmentGameBinding
import com.example.cookbook.repository.FavoriteMealRepository
import com.example.cookbook.repository.MealRepository
import com.example.cookbook.state.FavState
import com.example.cookbook.viewmodel.FavoriteViewModel
import com.example.cookbook.viewmodel.GameViewModel
import com.example.cookbook.viewmodelfactory.FavoriteViewModelFactory
import com.example.cookbook.viewmodelfactory.GameViewModelFactory
import com.vungn.luckywheel.OnLuckyWheelReachTheTarget
import com.vungn.luckywheel.SpinTime
import com.vungn.luckywheel.WheelItem
import com.vungn.luckywheel.WheelMode
import com.vungn.luckywheel.WheelUtils
import kotlinx.coroutines.launch

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: GameViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var mealRepository: MealRepository
    private lateinit var favoriteMealRepository: FavoriteMealRepository
    private lateinit var api: ApiService
    private lateinit var dao: FavoriteMealDao
    private lateinit var adapter: SearchAdapter
    private var isAutoSpinning = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = RetrofitInstance.api
        dao = CookDatabase.createDatabase(requireContext()).favoriteMealDao()
        mealRepository = MealRepository(api)
        favoriteMealRepository = FavoriteMealRepository(dao)
        adapter = SearchAdapter(mutableListOf())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGameBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteViewModel = ViewModelProvider(requireActivity(), FavoriteViewModelFactory(favoriteMealRepository))[FavoriteViewModel::class.java]
        viewModel = ViewModelProvider(requireActivity(), GameViewModelFactory(mealRepository))[GameViewModel::class.java]
        binding.rvLetter.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLetter.isNestedScrollingEnabled = false
        binding.rvLetter.adapter = adapter

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        createWheel()
        collectMeal()
        collectLetter()
        collectFavorites()

        favoriteViewModel.collectFavorites()

        adapter.onFavClick = { meal ->
            favoriteViewModel.toggleFavorite(meal)
        }

        adapter.onItemClick = { id ->
            val action = GameFragmentDirections.actionGameFragmentToDetailFragment(id)
            findNavController().navigate(action)
        }
    }

    private fun createWheel(){
        val lw = binding.lwv
        val wheelItems: List<WheelItem> = listOf(
            WheelItem(0, Color.RED, Color.WHITE, "A", 1),
            WheelItem(1, Color.BLUE, Color.WHITE, "B", 1),
            WheelItem(2, Color.GREEN, Color.WHITE, "C", 1),
            WheelItem(3, Color.YELLOW, Color.WHITE, "D", 1),
            WheelItem(4, Color.MAGENTA, Color.WHITE, "E", 1),
            WheelItem(5, Color.CYAN, Color.WHITE, "F", 1),
            WheelItem(6, Color.DKGRAY, Color.WHITE, "G", 1),
            WheelItem(7, Color.rgb(255, 128, 0), Color.WHITE, "H", 1),
            WheelItem(8, Color.rgb(128, 0, 255), Color.WHITE, "I", 1),
            WheelItem(9, Color.rgb(0, 128, 128), Color.WHITE, "J", 1),
            WheelItem(10, Color.rgb(255, 0, 128), Color.WHITE, "K", 1),
            WheelItem(11, Color.rgb(128, 128, 0), Color.WHITE, "L", 1),
            WheelItem(12, Color.rgb(0, 128, 255), Color.WHITE, "M", 1),
            WheelItem(13, Color.rgb(255, 64, 64), Color.WHITE, "N", 1),
            WheelItem(14, Color.rgb(64, 64, 255), Color.WHITE, "O", 1),
            WheelItem(15, Color.rgb(64, 200, 64), Color.WHITE, "P", 1),
            WheelItem(16, Color.rgb(200, 64, 200), Color.WHITE, "Q", 1),
            WheelItem(17, Color.rgb(200, 128, 64), Color.WHITE, "R", 1),
            WheelItem(18, Color.rgb(64, 160, 160), Color.WHITE, "S", 1),
            WheelItem(19, Color.rgb(160, 64, 160), Color.WHITE, "T", 1),
            WheelItem(20, Color.rgb(64, 100, 200), Color.WHITE, "U", 1),
            WheelItem(21, Color.rgb(200, 64, 100), Color.WHITE, "V", 1),
            WheelItem(22, Color.rgb(100, 200, 64), Color.WHITE, "W", 1),
            WheelItem(23, Color.rgb(100, 64, 200), Color.WHITE, "Y", 1),
            WheelItem(24, Color.rgb(200, 100, 64), Color.WHITE, "Z", 1)
        )

        lw.setWheelMode(WheelMode.NORMAL)

        lw.addWheelItems(wheelItems)

        lw.setLuckyWheelReachTheTarget(object: OnLuckyWheelReachTheTarget{

            override fun onReachFinalTarget(item: WheelItem?) {
                val selectedItem = item?.text ?: ""
                viewModel.listByFirstLetter(selectedItem)
                viewModel.saveLetter(selectedItem)
            }


            override fun onTargetChanged(item: WheelItem?) {
                binding.chipLetter.text = item?.text ?: ""
            }

        })

        binding.btnSpin.setOnClickListener {
            val randomNum = WheelUtils.getRandomIndex(wheelItems)
            lw.rotateWheelTo(randomNum)
        }

    }

    private fun collectMeal() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.list.collect { selectedMeal ->
                    selectedMeal?.meals?.let {
                        adapter.updateList(it)
                        binding.tvCount.text = adapter.itemCount.toString()
                        binding.linearTitle.visibility = View.VISIBLE
                        binding.divider.visibility = View.VISIBLE
                        binding.rvLetter.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun collectLetter(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.selectedLetter.collect { letter ->
                    binding.chipLetter.text = letter
                    binding.tvSelectedLetter.text = " " + letter
                }
            }
        }
    }

    private fun collectFavorites(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                favoriteViewModel.favState.collect { state ->
                    when(state){
                        FavState.Idle -> {
                        }
                        FavState.Loading -> {}
                        is FavState.Success -> {
                            val favorites = state.meals.map{ it.idMeal }.toSet()
                            adapter.updateFavorites(favorites)
                        }
                        is FavState.Error -> {
                            Toast.makeText(requireContext(), state.message,Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }
    }
    
}