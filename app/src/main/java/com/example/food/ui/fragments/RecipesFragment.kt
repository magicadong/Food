package com.example.food.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.food.databinding.FragmentRecipesBinding
import com.example.food.ui.viewmodel.MainViewModel
import com.example.food.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding = _binding!!

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesBinding.inflate(inflater,container,false)

        mainViewModel.getRecipies(mainViewModel.applyQueries())

        mainViewModel.recipesResponse.observe(viewLifecycleOwner){networkResult ->
            when(networkResult){
                is NetworkResult.Loading -> binding.recyclerView.showShimmer()
                is NetworkResult.Success -> {
                    Log.v("ppp","result = ${networkResult.data}")
                    binding.recyclerView.hideShimmer()
                }
                is NetworkResult.Error -> {
                    Log.v("ppp","error = ${networkResult.message}")
                    binding.recyclerView.hideShimmer()
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}