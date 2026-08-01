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

        binding.switchDarkMode.isChecked = sessionManager.getDarkMode()
        binding.selectedCuisine.text = when(sessionManager.getCuisine()){
            "Italian" -> getString(R.string.italian)
            "Turkish" -> getString(R.string.turkish)
            "Japanese" -> getString(R.string.japanese)
            "Moroccan" -> getString(R.string.moroccan)
            "Chinese" -> getString(R.string.chinese)
            "Russian" -> getString(R.string.russian)
            "Thai" -> getString(R.string.thai)
            "Irish" -> getString(R.string.irish)
            "Greek" -> getString(R.string.greek)
            "Vietnamese" -> getString(R.string.vietnamese)
            else -> ""
        }
        binding.selectedLanguage.text = when(sessionManager.getLanguage()){
            "en" -> getString(R.string.english)
            "de" -> getString(R.string.german)
            "tr" -> getString(R.string.turkish_lang)
            "fr" -> getString(R.string.french)
            "zh" -> getString(R.string.chinese_lang)
            "ru" -> getString(R.string.russian_lang)
            else -> ""
        }
        binding.selectedIngredient.text = when(sessionManager.getIngredient()){
            "Egg" -> getString(R.string.egg)
            "Beef" -> getString(R.string.beef)
            "Bread" -> getString(R.string.bread)
            "Sugar" -> getString(R.string.sugar)
            "Cheese" -> getString(R.string.cheese)
            "Cocoa" -> getString(R.string.cocoa)
            "Milk" -> getString(R.string.milk)
            "Honey" -> getString(R.string.honey)
            "Carrots" -> getString(R.string.carrots)
            "Cucumber" -> getString(R.string.cucumber)
            else -> ""
        }


        binding.editProfileCard.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfile)
        }

        binding.languageCard.setOnClickListener {
            DialogUtil.showLanguageDialog(requireContext(),layoutInflater, sessionManager.getLanguage() ?: "") { tag->
                sessionManager.saveLanguage(tag)
                profileViewModel.setLanguage(tag)
                findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
                binding.selectedLanguage.text = when(tag){
                    "en" -> getString(R.string.english)
                    "de" -> getString(R.string.german)
                    "tr" -> getString(R.string.turkish_lang)
                    "fr" -> getString(R.string.french)
                    "zh" -> getString(R.string.chinese_lang)
                    "ru" -> getString(R.string.russian_lang)
                    else -> ""
                }
            }
        }

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



        binding.cuisineCard.setOnClickListener {
            DialogUtil.showCuisineDialog(requireContext(),layoutInflater,sessionManager.getCuisine() ?: "") { tag ->
                sessionManager.saveCuisine(tag)
                binding.selectedCuisine.text = when(tag){
                    "Italian" -> getString(R.string.italian)
                    "Turkish" -> getString(R.string.turkish)
                    "Japanese" -> getString(R.string.japanese)
                    "Moroccan" -> getString(R.string.moroccan)
                    "Chinese" -> getString(R.string.chinese)
                    "Russian" -> getString(R.string.russian)
                    "Thai" -> getString(R.string.thai)
                    "Irish" -> getString(R.string.irish)
                    "Greek" -> getString(R.string.greek)
                    "Vietnamese" -> getString(R.string.vietnamese)
                    else -> ""
                }
            }
        }

        binding.ingredientCard.setOnClickListener {
            DialogUtil.showIngredientsDialog(requireContext(),layoutInflater,sessionManager.getIngredient() ?: ""){ tag ->
                sessionManager.saveIngredient(tag)
                binding.selectedIngredient.text = when(tag){
                    "Egg" -> getString(R.string.egg)
                    "Beef" -> getString(R.string.beef)
                    "Bread" -> getString(R.string.bread)
                    "Sugar" -> getString(R.string.sugar)
                    "Cheese" -> getString(R.string.cheese)
                    "Cocoa" -> getString(R.string.cocoa)
                    "Milk" -> getString(R.string.milk)
                    "Honey" -> getString(R.string.honey)
                    "Carrots" -> getString(R.string.carrots)
                    "Cucumber" -> getString(R.string.cucumber)
                    else -> ""}
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