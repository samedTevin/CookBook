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
import com.example.cookbook.database.CookDatabase
import com.example.cookbook.databinding.FragmentLoginBinding
import com.example.cookbook.repository.UserRepository
import com.example.cookbook.state.LoginState
import com.example.cookbook.viewmodel.LoginViewModel
import com.example.cookbook.viewmodelfactory.LoginViewModelFactory
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel
    private lateinit var userRepository: UserRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val userDao = CookDatabase.createDatabase(requireContext()).userDao()
        userRepository = UserRepository(userDao)
        viewModel = ViewModelProvider(this, LoginViewModelFactory(userRepository))[LoginViewModel::class.java]

        _binding = FragmentLoginBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignIn.setOnClickListener {
            viewModel.login(binding.etMail.text.toString())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collect { state ->
               when(state){
                   LoginState.Idle -> {}
                   LoginState.Success -> {findNavController().navigate(R.id.action_loginFragment_to_homeFragment) }
                   LoginState.UserNotFound -> {Toast.makeText(requireContext(),"User not found!", Toast.LENGTH_LONG).show()}
               }
            }

        }

        binding.tvCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }


}