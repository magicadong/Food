package com.example.food.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailPageAdapter(
    private val resultBundle: Bundle,
    private val fragments: List<Fragment>,
    fragmentActivity: FragmentActivity
):FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = fragments[position]
        fragment.arguments = resultBundle
        return fragment
    }
}