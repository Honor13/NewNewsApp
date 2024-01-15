package com.example.newnewsapi.presentation.ui.fragments.bottomnavholder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.newnewsapi.R
import com.example.newnewsapi.databinding.FragmentBottomNavHolderBinding
import com.example.newnewsapi.presentation.ui.fragments.favorite.FavoritesFragment
import com.example.newnewsapi.presentation.ui.fragments.news.NewsFragment
import com.example.newnewsapi.presentation.ui.fragments.profile.ProfileFragment


class BottomNavHolderFragment : Fragment() {

    private lateinit var binding: FragmentBottomNavHolderBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_bottom_nav_holder,
            container,
            false
        )


        binding.bottomNav.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.action_home -> openFragment(NewsFragment())
                R.id.action_fav -> openFragment(FavoritesFragment())
                R.id.action_profile -> openFragment(ProfileFragment())

            }
            true
        }
        openFragment(NewsFragment())
        return binding.root
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.navHostFragment, fragment)
        fragmentTransaction.commit()
    }
}