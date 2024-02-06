package com.example.newnewsapi.presentation.ui.fragments.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newnewsapi.R
import com.example.newnewsapi.databinding.FragmentFavoritesBinding
import com.example.newnewsapi.presentation.ui.adapters.FavoritesAdapter
import com.example.newnewsapi.presentation.viewmodels.MainViewModel


class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: FavoritesAdapter
//    private val mAdapter by lazy { FavoritesAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_favorites, container, false)

        showShimmer()

        mainViewModel.loadFavorites()
        adapter = FavoritesAdapter(onItemClickListener = {
            mainViewModel.deleteFavorites(it)
        })



        setupRV()
        mainViewModel.listFav.observe(viewLifecycleOwner) {
            adapter.setData(it)
            hideShimmer()
        }


        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    private fun setupRV() {
        binding.recyclerViewFavorites.adapter = adapter
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        showShimmer()
    }

    private fun showShimmer() {
        binding.shimmerRVFavorites.visibility = View.VISIBLE
        binding.recyclerViewFavorites.visibility = View.INVISIBLE
        binding.shimmerRVFavorites.showShimmer(true)
    }

    private fun hideShimmer() {
        binding.shimmerRVFavorites.visibility = View.INVISIBLE
        binding.recyclerViewFavorites.visibility = View.VISIBLE
        binding.shimmerRVFavorites.hideShimmer()
    }

}