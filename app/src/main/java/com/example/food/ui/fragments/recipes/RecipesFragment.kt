package com.example.food.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food.adapter.RecipesRowAdapter
import com.example.food.databinding.FragmentRecipesBinding
import com.example.food.ui.viewmodel.MainViewModel
import com.example.food.ui.viewmodel.RecipesViewModel
import com.example.food.util.NetworkListener
import com.example.food.util.NetworkResult
import com.example.food.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() =  _binding!!

    private val mAdapter = RecipesRowAdapter()
    private val mainViewModel by viewModels<MainViewModel>()
    private val recipesViewModel by viewModels<RecipesViewModel>()

    private val args by navArgs<RecipesFragmentArgs>()

    private lateinit var networkListener: NetworkListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        initRecyclerView()

        recipesViewModel.readBackOnline.observe(viewLifecycleOwner){
            recipesViewModel.backOnline = it
        }

        //实时监听网络状态
        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    recipesViewModel.networkStatus = status
                    readDatabase()
                }
        }

        binding.floatingActionButton.setOnClickListener {
            val action = RecipesFragmentDirections.actionRecipesFragmentToBottomSheetFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun readDatabase(){
        mainViewModel.readRecipes.observeOnce(viewLifecycleOwner){
            if (it.isNotEmpty() && !args.backFromBottomSheet){
                mAdapter.setData(it[0].foodRecipes)
                binding.recyclerView.hideShimmer()
            }else{
                requestApiData()
            }
        }
    }

    private fun requestApiData() {
        mainViewModel.getRecipies(recipesViewModel.applyQueries())
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

    private fun initRecyclerView(){
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}