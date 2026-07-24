package com.example.cookbook.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cookbook.R
import com.example.cookbook.model.User
import com.example.cookbook.database.CookDatabase
import com.example.cookbook.databinding.FragmentRegisterBinding
import com.example.cookbook.repository.UserRepository
import com.example.cookbook.state.RegisterState
import com.example.cookbook.viewmodel.RegisterViewModel
import com.example.cookbook.viewmodelfactory.RegisterViewModelFactory
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {


    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegisterViewModel
    private lateinit var userRepository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater,container,false)


        val userDao = CookDatabase.createDatabase(requireContext()).userDao()
        userRepository = UserRepository(userDao)

        viewModel = ViewModelProvider(this, RegisterViewModelFactory(userRepository))[RegisterViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSignUp.setOnClickListener {
            val user = createUser()
            viewModel.registerUser(user)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.registerState.collect { state ->
                when(state){
                    RegisterState.Idle -> {}
                    RegisterState.Success -> {findNavController().navigate(R.id.action_registerFragment_to_loginFragment)}
                    RegisterState.EmailAlreadyExists -> {Toast.makeText(requireContext(),"Email already exists!",
                        Toast.LENGTH_LONG).show()}
                }

            }
        }

        binding.tvAlreadyHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }


    fun createUser(): User{
        val user = User(binding.etMail.text.toString(), "", binding.etUsername.text.toString(), binding.etPassword.text.toString())
        return user
    }


}