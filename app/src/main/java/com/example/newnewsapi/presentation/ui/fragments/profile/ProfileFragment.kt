package com.example.newnewsapi.presentation.ui.fragments.profile

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.newnewsapi.MainActivity
import com.example.newnewsapi.R
import com.example.newnewsapi.databinding.FragmentProfileBinding
import com.example.newnewsapi.presentation.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar

class ProfileFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var view: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_profile, container, false)
        binding.profileFragmentObject = this
        view = binding.progressBarProfile

        mainViewModel.loginFlow.observe(viewLifecycleOwner) {
            if (it == null) {
                hideProgressBar()
                val mainNavController =
                    (requireActivity() as MainActivity).findNavController(R.id.navHostFragment)

                // popUpTo kullanarak back stack temizleme işlemi
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.bottomNavHolderFragment, true)
                    .build()

                mainNavController.navigate(
                    R.id.action_bottomNavHolderFragment_to_loginFragment,
                    null,
                    navOptions
                )
            }
        }


        return binding.root
    }

    fun logout(view: View) {
        Snackbar.make(view,"Are you sure you want to log out of this account?", Snackbar.LENGTH_SHORT)
            .setBackgroundTint(Color.GRAY)
            .setTextColor(Color.RED)
            .setActionTextColor(Color.BLUE)
            .setAction("EVET") {
                showProgressBar()
                mainViewModel.logout()
            }

    }

    private fun showProgressBar() {
        binding.progressBarProfile.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBarProfile.visibility = View.INVISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

}