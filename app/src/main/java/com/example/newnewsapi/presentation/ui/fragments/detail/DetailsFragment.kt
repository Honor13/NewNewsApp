package com.example.newnewsapi.presentation.ui.fragments.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.newnewsapi.R
import com.example.newnewsapi.databinding.FragmentDetailsBinding
import com.example.newnewsapi.presentation.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var authKey: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_details, container, false)
        authKey = mainViewModel.authKey.toString()

        val bundle: DetailsFragmentArgs by navArgs()
        val news = bundle.article
        mainViewModel.checkFavorites(news.title.toString())
        binding.article = news

        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(news.url.toString())
            binding.progressBarWebView.visibility=View.INVISIBLE
        }
        mainViewModel.favState.observe(viewLifecycleOwner){
            if (it == false)
                binding.imageViewFav.setImageResource(R.drawable.ic_unfill_fav)
            else
                binding.imageViewFav.setImageResource(R.drawable.ic_fill_fav)
        }




        mainViewModel.hidePrgorressBar.observe(viewLifecycleOwner) {
            hideProgressBar(it)
        }

        binding.imageViewFav.setOnClickListener {
            showProgressBar()
            mainViewModel.checkIfNewsExistsAndSaveFavorites(
                it,
                "",
                authKey,
                news.author,
                news.content,
                news.description,
                news.publishedAt,
                news.source!!,
                news.title,
                news.url,
                news.urlToImage
            )
            binding.imageViewFav.setImageResource(R.drawable.ic_fill_fav)
        }



        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    private fun showProgressBar() {
        binding.progressBarDetail.visibility = View.VISIBLE
    }

    private fun hideProgressBar(bool: Boolean) {
        if (bool) {
            binding.progressBarDetail.visibility = View.INVISIBLE
        }

    }

}