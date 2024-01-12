package com.example.newnewsapi.presentation.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.newnewsapi.R
import com.example.newnewsapi.databinding.FragmentDetailsBinding
import com.example.newnewsapi.presentation.ui.news.NewsFragmentArgs
import com.example.newnewsapi.presentation.viewmodels.MainViewModel

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var mainViewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_details, container, false)

        val bundle: DetailsFragmentArgs by navArgs()
        val news = bundle.article
        binding.article = news

        binding.imageViewFav.setOnClickListener{
            mainViewModel.saveFavorites("",news.author,news.content,news.description,news.publishedAt,news.title,news.url,news.urlToImage)
            Toast.makeText(requireContext(),"Eklendi",Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

}