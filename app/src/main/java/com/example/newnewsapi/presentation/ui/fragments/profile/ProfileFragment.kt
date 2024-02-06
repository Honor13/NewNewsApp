package com.example.newnewsapi.presentation.ui.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.newnewsapi.R
import com.example.newnewsapi.databinding.FragmentProfileBinding
import com.example.newnewsapi.presentation.viewmodels.AuthViewModel
import com.example.newnewsapi.presentation.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var view: View
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_profile, container, false)
        binding.profileFragmentObject = this
        view = binding.progressBarProfile
        authViewModel.loginState()

        binding.imgToolbarBtnBackLogout.setOnClickListener{
            authViewModel.logOut {
                Navigation.findNavController(view).navigate(R.id.action_bottomNavHolderFragment_to_loginFragment)
            }
        }

        return binding.root
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