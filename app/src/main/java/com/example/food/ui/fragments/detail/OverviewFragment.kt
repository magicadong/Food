package com.example.food.ui.fragments.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.food.R
import com.example.food.data.model.Result
import com.example.food.databinding.FragmentDetailBinding


class OverviewFragment : Fragment() {

    private var _binding:FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private var result:Result? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            result = it.getParcelable("result")
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailBinding.inflate(inflater,container,false)
        binding.result = result

        return binding.root
    }
}