package com.example.food.ui.fragments.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food.R
import com.example.food.adapter.IngredientAdapter
import com.example.food.data.model.Result
import com.example.food.databinding.FragmentIngredientBinding


class IngredientFragment : Fragment() {
    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding!!

    private val mAdapter: IngredientAdapter by lazy {
        IngredientAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientBinding.inflate(inflater,container,false)

        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {

        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        arguments?.let {
            val result = it.getParcelable<Result>("result")
            mAdapter.setData(result!!.extendedIngredients)
        }

    }
}