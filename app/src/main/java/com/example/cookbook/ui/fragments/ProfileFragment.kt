package com.example.cookbook.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentProfileBinding
import com.example.cookbook.util.DialogUtil


class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.languageCard.setOnClickListener {
            DialogUtil.showLanguageDialog(requireContext(),layoutInflater) { language ->

                Toast.makeText(context, "Selected language: $language", Toast.LENGTH_LONG).show()
                binding.selectedLanguage.text = language
            }
        }

        binding.cuisineCard.setOnClickListener {
            DialogUtil.showCuisineDialog(requireContext(),layoutInflater) { cuisine ->
                Toast.makeText(context,"Selected cuisine: $cuisine",Toast.LENGTH_LONG).show()
                binding.selectedCuisine.text = cuisine
            }
        }

        binding.aboutCard.setOnClickListener {
            DialogUtil.showAboutDialog(requireContext(),layoutInflater)
        }
    }


}