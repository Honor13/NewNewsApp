package com.example.newnewsapi.presentation.ui.fragments.news.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.newnewsapi.R
import com.example.newnewsapi.databinding.FragmentNewsBottomSheetBinding
import com.example.newnewsapi.presentation.viewmodels.NewsViewModel
import com.example.newnewsapi.util.Constants.Companion.DEFAULT_CATEGORY_TYPE
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.Locale

class NewsBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding:FragmentNewsBottomSheetBinding
    private lateinit var newsViewModel: NewsViewModel

    private var categoryTypeChip = DEFAULT_CATEGORY_TYPE
    private var categoryTypeChipId = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_news_bottom_sheet,container,false)

        newsViewModel.readCategoryType.asLiveData().observe(viewLifecycleOwner) {value ->
            categoryTypeChip = value.selectedCategoryType
            updateChip(value.selectedCategoryTypeId,binding.chipGroup)
        }

        binding.chipGroup.setOnCheckedChangeListener{group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedCategoryType = chip.text.toString().lowercase(Locale.ROOT)
            categoryTypeChip = selectedCategoryType
            categoryTypeChipId = selectedChipId
        }

        binding.applyBtn.setOnClickListener {
            newsViewModel.saveCategory(
                categoryTypeChip,
                categoryTypeChipId
            )

            val action = NewsBottomSheetDirections.actionNewsBottomSheetToNewsFragment()
                .apply {
                setBackFromBottomSheet(true)
            }
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsViewModel = ViewModelProvider(requireActivity()).get(NewsViewModel::class.java)
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {

        if (chipId != 0){
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
                Log.e("dasdsad","asfasfasf")
            }catch (e: Exception){
                Log.d("BottomSheet",e.message.toString())
            }
        }

    }
}