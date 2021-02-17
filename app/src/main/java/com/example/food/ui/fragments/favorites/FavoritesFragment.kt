package com.example.food.ui.fragments.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food.R
import com.example.food.adapter.FavoriteRecipesAdapter
import com.example.food.databinding.FragmentFavoritesBinding
import com.example.food.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding:FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val mAdapter: FavoriteRecipesAdapter by lazy {
        FavoriteRecipesAdapter()
    }
    private val mainViewModel:MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter
        binding.executePendingBindings()
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        binding.favoritesRecipesRecyclerView.adapter = mAdapter
        binding.favoritesRecipesRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}