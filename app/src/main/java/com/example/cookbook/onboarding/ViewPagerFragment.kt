package com.example.cookbook.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cookbook.adapter.viewpageradapter.ViewPagerAdapter
import com.example.cookbook.databinding.FragmentViewPagerBinding
import com.example.cookbook.onboarding.screens.FirstScreen
import com.example.cookbook.onboarding.screens.SecondScreen
import com.example.cookbook.onboarding.screens.ThirdScreen
import androidx.viewpager2.widget.ViewPager2
import com.example.cookbook.R


class ViewPagerFragment : Fragment() {


    private var _binding : FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentViewPagerBinding.inflate(inflater,container,false)
        val view = binding.root

        // Represents Onboarding
        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )




        // Initializing the adapter
        val adapter = ViewPagerAdapter(fragmentList,requireActivity().supportFragmentManager,lifecycle)
        binding.viewPager2.adapter = adapter

        // Attach to viewPager2
        binding.wormDots.attachTo(viewPager2 = binding.viewPager2)

        binding.btnNext.setOnClickListener {
            if(binding.viewPager2.currentItem < 2){
                binding.viewPager2.currentItem += 1
            }
            else{
                // Finishes the onboarding and navigates the home screen
                findNavController().navigate(R.id.action_viewPagerFragment_to_homeFragment)
                onBoardingFinished()
            }
        }

        binding.viewPager2.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.btnNext.text =
                        if (position == 2) "Get Started"
                        else "Next"
                }
            }
        )

        return view
    }

    private fun onBoardingFinished() {
        val sharedPreferences = requireActivity().getSharedPreferences("onboarding", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }


}