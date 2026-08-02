package com.example.cookbook.ui.fragments.detailfragments

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.example.cookbook.R
import com.example.cookbook.database.CookDatabase
import com.example.cookbook.databinding.FragmentEditProfileBinding
import com.example.cookbook.model.User
import com.example.cookbook.preferences.SessionManager
import com.example.cookbook.repository.UserRepository
import com.example.cookbook.viewmodel.ProfileViewModel
import com.example.cookbook.viewmodelfactory.ProfileViewModelFactory
import java.io.File
import java.io.FileOutputStream


class EditProfileFragment : Fragment() {


    private var _binding : FragmentEditProfileBinding? = null
    val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var userRepository: UserRepository
    private var activeUser: User? = null


    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){  uri : Uri? ->
        uri?.let{ selectedUri ->

            val currentUserEmail = profileViewModel.getUserEmail()
            Glide.with(this).load(uri).into(binding.imgProfilePhoto)

            if(currentUserEmail != null){
                saveImageToAppFolder(selectedUri, currentUserEmail)
            }
            else{
                Toast.makeText(requireContext(),"Saving process cancelled. User email not found!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userDao = CookDatabase.createDatabase(requireContext()).userDao()
        userRepository = UserRepository(userDao)
        val sharedPreferences = requireActivity().getSharedPreferences("session", Activity.MODE_PRIVATE)
        sessionManager = SessionManager(sharedPreferences)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(this, ProfileViewModelFactory(userRepository,sessionManager))[ProfileViewModel::class.java]


        val email = profileViewModel.getUserEmail()

        if(email != null){
            profileViewModel.getUserByEmail(email).observe(viewLifecycleOwner) {user ->
                user?.let {
                    displayUserPhoto(it)
                    activeUser = it
                }
            }
        }

        binding.imgProfilePhoto.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.resetButton.setOnClickListener {
           activeUser?.let {
               resetProfileImage(it)
           }
        }

    }

    private fun saveImageToAppFolder(uri: Uri, currentUserEmail : String){
        try{
            val context = requireContext()
            val inputStream = context.contentResolver.openInputStream(uri) ?: return

            // Create unique file using user's email
            val localFile = File (context.filesDir, "user_photo_$currentUserEmail.jpg")
            val outputStream = FileOutputStream(localFile)

            // Copy photo bytes to local file
            inputStream.use{ input ->
                outputStream.use{ output ->
                    input.copyTo(output)
                }
            }

            val pathString = localFile.absolutePath
            profileViewModel.updateUserPhoto(currentUserEmail, pathString)

        }
        catch(e: Exception){ e.printStackTrace() }
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

    private fun resetProfileImage(user: User){
        val savedPath =user.imagePath

        if(savedPath != null){
            val file = File(savedPath)
            if(file.exists()) file.delete()
        }

        profileViewModel.updateUserPhoto(user.email, null)
        Glide.with(this).load(R.drawable.baseline_person_24).into(binding.imgProfilePhoto)
    }
}