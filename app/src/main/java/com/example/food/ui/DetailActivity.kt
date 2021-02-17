package com.example.food.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import com.example.food.R
import com.example.food.adapter.DetailPageAdapter
import com.example.food.data.local.entity.FavoritesEntity
import com.example.food.data.model.Result
import com.example.food.databinding.ActivityDetailBinding
import com.example.food.ui.fragments.detail.IngredientFragment
import com.example.food.ui.fragments.detail.InstructionFragment
import com.example.food.ui.fragments.detail.OverviewFragment
import com.example.food.ui.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<DetailActivityArgs>()
    private val mainViewModel by viewModels<MainViewModel>()
    private var recipeSaved = false
    private var recipeId = 0

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_star_menu,menu)
        val menuItem = menu?.findItem(R.id.menu_save)
        checkSavedRecipes(menuItem!!)
        return true
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavorites.observe(this){ favoritesEntity ->
            for (savedRecipe in favoritesEntity){
                if (args.result.id == savedRecipe.result.id){
                    changeMenuItemColor(menuItem,R.color.yellow)
                    recipeSaved = true
                    recipeId = savedRecipe.id
                }else{
                    changeMenuItemColor(menuItem,R.color.white)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }else {
            if(item.itemId == R.id.menu_save) {
                if (!recipeSaved) {
                    saveToFavorites(item)
                }else{
                    removeFromFavorites(item)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun removeFromFavorites(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(recipeId,args.result)
        mainViewModel.deleteFavorite(favoritesEntity)

        changeMenuItemColor(item, R.color.white)
        showSnackBar("Favorite Recipe Removed!")
        recipeSaved = false
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(0,args.result)
        mainViewModel.insertFavorite(favoritesEntity)

        changeMenuItemColor(item,R.color.yellow)
        showSnackBar("Recipe saved!")
        recipeSaved = true
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
                binding.detailLayout,
                message,
                Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
                .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this,color))
    }
}