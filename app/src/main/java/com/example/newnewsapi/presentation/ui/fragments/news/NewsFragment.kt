package com.example.newnewsapi.presentation.ui.fragments.news

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newnewsapi.R
import com.example.newnewsapi.databinding.FragmentNewsBinding
import com.example.newnewsapi.presentation.ui.adapters.NewsAdapter
import com.example.newnewsapi.presentation.viewmodels.MainViewModel
import com.example.newnewsapi.presentation.viewmodels.NewsViewModel
import com.example.newnewsapi.util.NetworkListener
import com.example.newnewsapi.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class NewsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var binding: FragmentNewsBinding
    private val mAdapter by lazy { NewsAdapter() }

    private lateinit var networkListener: NetworkListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_news, container, false)

        setupRV()
        requesApiData()
        newsViewModel.readBackOnline.observe(viewLifecycleOwner){
            newsViewModel.backOnline = it
        }

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    newsViewModel.networkStatus = status
                    newsViewModel.showNetworkStatus()

                }
        }

        binding.fabCategory.setOnClickListener() {
            if (newsViewModel.networkStatus) {
                findNavController().navigate(R.id.action_bottomNavHolderFragment_to_newsBottomSheet)
            } else {

                newsViewModel.showNetworkStatus()
            }

        }

        return binding.root


    }

    private fun setupRV() {
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmer()
    }

    private fun requesApiData() {
        mainViewModel.getNews(newsViewModel.applyQueries())
        mainViewModel.newsApiResponse.observe(viewLifecycleOwner) { response ->
            when (response) {

                is NetworkResult.Success -> {
                    hideShimmer()
                    response.data?.let { mAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
                    hideShimmer()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    showShimmer()
                }

            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        newsViewModel = ViewModelProvider(requireActivity()).get(NewsViewModel::class.java)

    }

    private fun showShimmer() {
        binding.shimmerRV.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.INVISIBLE
        binding.shimmerRV.showShimmer(true)
    }

    private fun hideShimmer() {
        binding.shimmerRV.visibility = View.INVISIBLE
        binding.recyclerView.visibility = View.VISIBLE
        binding.shimmerRV.hideShimmer()
    }

}