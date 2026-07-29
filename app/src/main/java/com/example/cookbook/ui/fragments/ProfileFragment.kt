package com.example.cookbook.ui.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.cookbook.R
import com.example.cookbook.dao.UserDao
import com.example.cookbook.database.CookDatabase
import com.example.cookbook.databinding.FragmentProfileBinding
import com.example.cookbook.model.User
import com.example.cookbook.preferences.SessionManager
import com.example.cookbook.repository.UserRepository
import com.example.cookbook.util.DialogUtil
import com.example.cookbook.util.LogoutBottomSheet
import com.example.cookbook.viewmodel.ProfileViewModel
import com.example.cookbook.viewmodelfactory.ProfileViewModelFactory


class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var sessionManager: SessionManager
    private lateinit var userRepository: UserRepository


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

        val dao = CookDatabase.createDatabase(requireContext()).userDao()
        userRepository = UserRepository(dao)
        val sharedPreferences = requireActivity().getSharedPreferences("session", Activity.MODE_PRIVATE)
        sessionManager = SessionManager(sharedPreferences)
        profileViewModel = ViewModelProvider(this, ProfileViewModelFactory(userRepository,sessionManager))[ProfileViewModel::class.java]

        binding.languageCard.setOnClickListener {
            DialogUtil.showLanguageDialog(requireContext(),layoutInflater) { language ->
                sessionManager.saveLanguage(language)
                Toast.makeText(context, "Selected language: $language", Toast.LENGTH_LONG).show()
                binding.selectedLanguage.text = language
            }
        }

        binding.switchDarkMode.isChecked = sessionManager.getDarkMode()
        binding.selectedCuisine.text = sessionManager.getCuisine()
        binding.selectedLanguage.text = sessionManager.getLanguage()
        binding.selectedIngredient.text = sessionManager.getIngredient()
        

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            sessionManager.saveDarkMode(isChecked)
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO
            )
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
        }


        val currentCuisine = sessionManager.getCuisine() ?: ""
        binding.cuisineCard.setOnClickListener {
            DialogUtil.showCuisineDialog(requireContext(),layoutInflater,currentCuisine) { cuisine ->
                sessionManager.saveCuisine(cuisine)
                Toast.makeText(context,"Selected cuisine: $cuisine",Toast.LENGTH_LONG).show()
                binding.selectedCuisine.text = cuisine
            }
        }

        binding.aboutCard.setOnClickListener {
            DialogUtil.showAboutDialog(requireContext(),layoutInflater)
        }

        binding.ibLogout.setOnClickListener {
            LogoutBottomSheet{
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                profileViewModel.logOut()
            }.show(parentFragmentManager,"logout")
        }
    }


}