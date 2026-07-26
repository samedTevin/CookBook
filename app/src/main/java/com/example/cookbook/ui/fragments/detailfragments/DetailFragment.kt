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
import com.example.cookbook.mapper.toIngredients
import com.example.cookbook.repository.MealRepository
import com.example.cookbook.viewmodel.CategoryViewModel
import com.example.cookbook.viewmodel.DetailViewModel
import com.example.cookbook.viewmodelfactory.DetailViewModelFactory
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager


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

        viewLifecycleOwner.lifecycleScope.launch {
            detailViewModel.detail.collect { response ->

                response?.let { meal ->

                    Glide.with(binding.root)
                        .load(meal.strMealThumb)
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

                        val formatted = meal.strInstructions
                            .replace(Regex("""(?m)^\d+\s*$"""), "")
                            .split("\r\n")
                            .filter { it.isNotBlank() }
                            .mapIndexed { index, step ->
                                "${index + 1}. $step"
                            }
                            .joinToString("\n\n")

                        binding.tvInstructions.text = formatted

                        buttonYoutube.setOnClickListener {
                            if(!meal.strYoutube.isNullOrBlank()){
                                val intent = Intent(Intent.ACTION_VIEW, meal.strYoutube.toUri())
                                startActivity(intent)
                            }
                            else{
                                Toast.makeText(requireContext(),"No video found!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }



            }
        }





    }


}