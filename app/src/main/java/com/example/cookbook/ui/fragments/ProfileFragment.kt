package com.example.cookbook.ui.fragments

import android.animation.Animator
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.example.cookbook.R
import com.example.cookbook.database.CookDatabase
import com.example.cookbook.databinding.FragmentProfileBinding
import com.example.cookbook.model.User
import com.example.cookbook.preferences.SessionManager
import com.example.cookbook.repository.FavoriteMealRepository
import com.example.cookbook.repository.UserRepository
import com.example.cookbook.util.DialogUtil
import com.example.cookbook.util.LanguageManager
import com.example.cookbook.util.LogoutBottomSheet
import com.example.cookbook.viewmodel.FavoriteViewModel
import com.example.cookbook.viewmodel.ProfileViewModel
import com.example.cookbook.viewmodelfactory.FavoriteViewModelFactory
import com.example.cookbook.viewmodelfactory.ProfileViewModelFactory
import java.io.File


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

        val db = CookDatabase.createDatabase(requireContext().applicationContext)
        val dao = db.userDao()
        val favDao = db.favoriteMealDao()
        userRepository = UserRepository(dao)
        val favoriteMealRepository = FavoriteMealRepository(favDao)
        val sharedPreferences = requireActivity().getSharedPreferences("session", Activity.MODE_PRIVATE)
        sessionManager = SessionManager(sharedPreferences)
        profileViewModel = ViewModelProvider(this, ProfileViewModelFactory(userRepository,sessionManager))[ProfileViewModel::class.java]
        val favoriteViewModel = ViewModelProvider(requireActivity(), FavoriteViewModelFactory(favoriteMealRepository))[FavoriteViewModel::class.java]

        observeUser()

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
                profileViewModel.logOut()
                findNavController().navigate(
                    R.id.action_profileFragment_to_welcomeFragment
                )
            }.show(parentFragmentManager,"logout")
        }

        binding.ibDelete.setOnClickListener {
            DialogUtil.showDeleteDialog(requireContext(),layoutInflater){

                val currentEmail = sessionManager.getCurrentUserEmail()

                currentEmail?.let { email ->
                    profileViewModel.getUserByEmail(email).observe(viewLifecycleOwner){ user ->
                        user?.let{
                            profileViewModel.deleteUser(user)
                        }
                    }
                }

                favoriteViewModel.clearAllFavorites()
                sessionManager.clearSession()

                binding.ibDelete.visibility = View.GONE
                binding.ibLogout.visibility = View.GONE
                binding.tvTitle.visibility = View.GONE
                binding.imgProfilePhoto.visibility = View.GONE
                binding.lLayoutInformation.visibility = View.GONE
                binding.tvOptions.visibility = View.GONE
                binding.optionsContainer.visibility = View.GONE

                binding.lottieDeleteAnimation.visibility = View.VISIBLE
                binding.lottieDeleteAnimation.repeatCount = 0
                binding.lottieDeleteAnimation.playAnimation()

                binding.lottieDeleteAnimation.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) {
                        AppCompatDelegate.setApplicationLocales(
                            LocaleListCompat.forLanguageTags("en")
                        )
                        AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO
                        )

                        findNavController().navigate(
                            R.id.action_profileFragment_to_welcomeFragment,
                            null,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.homeFragment, true)
                                .build()
                        )
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })
            }
        }
    }


    private fun observeUser(){
        val currentEmail = sessionManager.getCurrentUserEmail()

        currentEmail?.let{
            profileViewModel.getUserByEmail(currentEmail).observe(viewLifecycleOwner){ user ->
                user?.let{
                    displayUserPhoto(user)
                    if(!user.fullName.isNullOrBlank()){
                        binding.tvFullName.visibility = View.VISIBLE
                        binding.tvFullName.text = user.fullName
                    }
                    binding.tvUsername.text = "@${user.username}" ?: "@johnDoe"
                    binding.tvEmail.text = user.email ?: "johndoe@gmail.com"
                }
            }
        }
    }

    private fun displayUserPhoto(user: User){
        val savedPath = user.imagePath
        if(savedPath != null){
            val file = File (savedPath)
            if(file.exists()){
                Glide.with(this).load(file).signature(ObjectKey(file.lastModified().toString())).placeholder(R.drawable.baseline_person_24).into(binding.imgProfilePhoto)
                return
            }
        }
        Glide.with(this).load(R.drawable.baseline_person_24).into(binding.imgProfilePhoto)
    }



}