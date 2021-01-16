package com.example.food.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food.adapter.RecipesRowAdapter
import com.example.food.databinding.FragmentRecipesBinding
import com.example.food.ui.viewmodel.MainViewModel
import com.example.food.util.NetworkResult
import com.example.food.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() =  _binding!!

    private val mAdapter = RecipesRowAdapter()
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        initRecyclerView()
        readDatabase()

        return binding.root
    }

    private fun requestApiData() {
        mainViewModel.getRecipies(mainViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner){networkResult ->
            when(networkResult){
                is NetworkResult.Loading -> {
                    binding.recyclerView.showShimmer()
                }
                is NetworkResult.Success -> {
                    mAdapter.setData(networkResult.data!!)
                    binding.recyclerView.hideShimmer()
                }
                is NetworkResult.Error -> {
                    binding.recyclerView.hideShimmer()
                }
            }
        }
    }

    private fun readDatabase(){
        mainViewModel.readRecipes.observeOnce(viewLifecycleOwner){
            if (it.isNotEmpty()){
                mAdapter.setData(it[0].foodRecipes)
                binding.recyclerView.hideShimmer()
            }else{
                requestApiData()
            }
        }
    }

    private fun initRecyclerView(){
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}