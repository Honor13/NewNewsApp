package com.example.newnewsapi.presentation.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.newnewsapi.R
import com.example.newnewsapi.databinding.FragmentDetailsBinding
import com.example.newnewsapi.presentation.ui.news.NewsFragmentArgs

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_details, container, false)

        val bundle: DetailsFragmentArgs by navArgs()
        val news = bundle.article
        binding.article = news

        return binding.root
    }

}