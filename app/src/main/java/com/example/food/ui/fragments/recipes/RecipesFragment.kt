package com.example.food.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food.R
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
class RecipesFragment : Fragment(),SearchView.OnQueryTextListener {
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

        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search,menu)

        val searchItem = menu.findItem(R.id.menuSearch)
        val searchView = searchItem.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

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
                    Log.v("ppp", networkResult.message!!)
                    binding.recyclerView.hideShimmer()
                }
            }
        }
    }

    private fun searchApiData(search: String){
        binding.recyclerView.showShimmer()
        mainViewModel.searchRecipes(recipesViewModel.applySearchQueries(search))
        mainViewModel.searchRecipesResponse.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Success ->{
                    mAdapter.setData(it.data!!)
                    binding.recyclerView.hideShimmer()
                }
                is NetworkResult.Error ->{
                    binding.recyclerView.hideShimmer()
                    Toast.makeText(
                            requireContext(),
                            "Food Not Found",
                            Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading ->{
                    binding.recyclerView.showShimmer()
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}