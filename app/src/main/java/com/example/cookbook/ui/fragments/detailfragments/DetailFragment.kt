package com.example.cookbook.ui.fragments.detailfragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cookbook.R
import com.example.cookbook.adapter.recyclerviewadapter.IngredientsAdapter
import com.example.cookbook.api.RetrofitInstance
import com.example.cookbook.databinding.FragmentDetailBinding
import com.example.cookbook.mapper.formatInstructionsToHtml
import com.example.cookbook.mapper.toIngredients
import com.example.cookbook.repository.MealRepository
import com.example.cookbook.viewmodel.CategoryViewModel
import com.example.cookbook.viewmodel.DetailViewModel
import com.example.cookbook.viewmodelfactory.DetailViewModelFactory
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.Lottie
import com.airbnb.lottie.LottieDrawable
import com.example.cookbook.state.DetailState


class DetailFragment : Fragment() {

    private var _binding : FragmentDetailBinding? = null
    val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var mealRepository: MealRepository
    private lateinit var ingredientsAdapter: IngredientsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = args.mealId

        val api = RetrofitInstance.api
        mealRepository = MealRepository(api)

        ingredientsAdapter = IngredientsAdapter(mutableListOf())

        detailViewModel = ViewModelProvider(requireActivity(), DetailViewModelFactory(mealRepository))[DetailViewModel::class.java]

        detailViewModel.getDetails(id)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            detailViewModel.state.collect { state ->
                when(state){
                    DetailState.Idle -> {}
                    DetailState.Loading -> {
                        binding.loadingAnimation.visibility = View.VISIBLE
                        binding.layoutDetail.visibility = View.GONE

                        binding.loadingAnimation.repeatCount = LottieDrawable.INFINITE

                        if(!binding.loadingAnimation.isAnimating){
                            binding.loadingAnimation.playAnimation()
                        }
                    }
                    is DetailState.Success -> {
                        state.meal?.let { meal ->
                            Glide.with(binding.root)
                                .load(meal.strMealThumb)
                                .placeholder(R.drawable.bg_skeleton)
                                .error(R.drawable.ic_error_image)
                                .into(binding.ivDetailPhoto)

                            val tags = meal.strTags?.split(",") ?: emptyList()

                            binding.apply {
                                tvTitle.text = meal.strMeal
                                chipCategory.text = if (meal.strCategory.isNullOrEmpty()) "Not Defined" else meal.strCategory
                                chipArea.text = if (meal.strArea.isNullOrEmpty()) "N/A" else meal.strArea
                                chipCountry.text = if (meal.strCountry.isNullOrEmpty()) "N/A" else meal.strCountry

                                chipTag1.text = tags.getOrNull(0) ?: "N/A"
                                chipTag2.text = tags.getOrNull(1) ?: "N/A"

                                rvIngredients.adapter = ingredientsAdapter
                                rvIngredients.isNestedScrollingEnabled = false
                                rvIngredients.setHasFixedSize(true)
                                rvIngredients.layoutManager = LinearLayoutManager(requireContext())
                                ingredientsAdapter.updateList(meal.toIngredients())

                                binding.tvInstructions.text = meal.strInstructions.formatInstructionsToHtml()

                                buttonYoutube.setOnClickListener {
                                    if(!meal.strYoutube.isNullOrBlank()){
                                        val intent = Intent(Intent.ACTION_VIEW, meal.strYoutube.toUri())
                                        startActivity(intent)
                                    }
                                    else{
                                        Toast.makeText(requireContext(),"No video found!", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                binding.loadingAnimation.cancelAnimation()
                                binding.loadingAnimation.visibility = View.GONE
                                binding.layoutDetail.visibility = View.VISIBLE
                                binding.scrollViewDetail.scrollTo(0, 0)
                        }
                    }

                }

                is DetailState.Error -> {
                    binding.loadingAnimation.cancelAnimation()
                    binding.loadingAnimation.visibility = View.GONE
                    binding.layoutDetail.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }

                }

            }
        }





    }


}