package com.example.cookbook.adapter.viewpageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(list: ArrayList<Fragment>,fm: FragmentManager, lifeCycle: Lifecycle): FragmentStateAdapter(fm, lifeCycle) {


    // Onboarding Screens
    private val fragmentList = list


    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }
}