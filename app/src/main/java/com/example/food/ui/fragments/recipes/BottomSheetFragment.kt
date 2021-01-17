package com.example.food.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.food.databinding.FragmentBottomSheetBinding
import com.example.food.ui.viewmodel.RecipesViewModel
import com.example.food.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.food.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.lang.Exception
import java.util.*


class BottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var mealType = DEFAULT_MEAL_TYPE
    private var mealId = 0
    private var dietType = DEFAULT_DIET_TYPE
    private var dietId = 0

    private lateinit var recipesViewModel: RecipesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetBinding.inflate(inflater,container,false)

        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner){ mealInfo ->
            mealId = mealInfo.mealId
            dietId = mealInfo.dietId
            mealType = mealInfo.mealType
            dietType = mealInfo.dietType

            updateChip(binding.mealTypeChipGroup,mealId)
            updateChip(binding.dietTypeChipGroup,dietId)
        }

        binding.applyButton.setOnClickListener {
            if (!recipesViewModel.networkStatus){
                recipesViewModel.showNetworkStatus()
            }else {
                recipesViewModel.saveMealAndDiet(mealType, mealId, dietType, dietId)
                val action = BottomSheetFragmentDirections
                    .actionBottomSheetFragmentToRecipesFragment(true)
                findNavController().navigate(action)
            }
        }

        binding.mealTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val text = chip.text.toString().toLowerCase(Locale.ROOT)
            mealType = text
            mealId = checkedId
        }

        binding.dietTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val text = chip.text.toString().toLowerCase(Locale.ROOT)
            dietType = text
            dietId = checkedId
        }
        return binding.root
    }

    private fun updateChip(chipGroup: ChipGroup, selectId: Int) {
        if (selectId != 0) {
            try {
                chipGroup.findViewById<Chip>(selectId).isChecked = true
            }catch (e: Exception){
                Log.v("ppp",e.message.toString())
            }

        }
    }

}