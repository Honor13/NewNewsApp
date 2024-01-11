package com.example.newnewsapi.presentation.ui.news

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newnewsapi.R
import com.example.newnewsapi.databinding.FragmentNewsBinding
import com.example.newnewsapi.presentation.ui.adapters.NewsAdapter
import com.example.newnewsapi.presentation.viewmodels.MainViewModel
import com.example.newnewsapi.util.Constants.Companion.API_KEY
import com.example.newnewsapi.util.Constants.Companion.COUNTRY
import com.example.newnewsapi.util.Constants.Companion.QUERY_API_KEY
import com.example.newnewsapi.util.Constants.Companion.QUERY_COUNTRY
import com.example.newnewsapi.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding : FragmentNewsBinding
    private val mAdapter by lazy { NewsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_news,container,false)

        setupRV()
        requesApiData()

        return binding.root


    }

    private fun setupRV(){
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmer()
    }

    private fun requesApiData(){
        mainViewModel.getNews(mainViewModel.applyQueries())
        mainViewModel.newsApiResponse.observe(viewLifecycleOwner){response ->
            when(response) {

                is NetworkResult.Success -> {
                    hideShimmer()
                    response.data?.let { mAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
                    hideShimmer()
                    Toast.makeText(requireContext(),response.message.toString(),Toast.LENGTH_SHORT).show()
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

    }

    private fun showShimmer(){
        binding.shimmerRV.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.INVISIBLE
        binding.shimmerRV.showShimmer(true)
    }
    private fun hideShimmer(){
        binding.shimmerRV.visibility = View.INVISIBLE
        binding.recyclerView.visibility = View.VISIBLE
        binding.shimmerRV.hideShimmer()
    }

}