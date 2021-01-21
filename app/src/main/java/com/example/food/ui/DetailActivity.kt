package com.example.food.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.example.food.adapter.DetailPageAdapter
import com.example.food.databinding.ActivityDetailBinding
import com.example.food.ui.fragments.detail.IngredientFragment
import com.example.food.ui.fragments.detail.InstructionFragment
import com.example.food.ui.fragments.detail.OverviewFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private var _binding:ActivityDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = LayoutInflater.from(this)
        _binding = ActivityDetailBinding.inflate(inflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViewPager2()
    }

    private fun initViewPager2() {
        val fragments = listOf(
            OverviewFragment(),
            IngredientFragment(),
            InstructionFragment()
        )

        val titles = arrayOf(
            "Overview",
            "Ingredients",
            "Instructions"
        )

        val resultBundle = Bundle()
        resultBundle.putParcelable("result",args.result)
        binding.viewPager2.adapter = DetailPageAdapter(
            resultBundle,
            fragments,
            this
        )

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager2
        ) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}