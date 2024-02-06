package com.example.newnewsapi.presentation.ui.fragments.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.newnewsapi.R
import com.example.newnewsapi.data.auth.Resource
import com.example.newnewsapi.databinding.FragmentRegisterBinding
import com.example.newnewsapi.presentation.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var view: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_register, container, false)
        binding.registerFragmentObject = this
        view = binding.buttonSignUp

        mainViewModel.signupLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Failure -> {
                    hideProgressBar()
                    Toast.makeText(
                        requireContext(),
                        it.exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                Resource.Loading -> {
                    showProgressBar()
                }

                is Resource.Success -> {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_registerFragment_to_bottomNavHolderFragment)
                }

                null -> {

                }
            }
        }


        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

    }

    fun signUp(username: String, email: String, password1: String, password2: String) {
        showProgressBar()
        if (password1 == password2)
            mainViewModel.signupUser(username, email, password1)
        else {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            hideProgressBar()
        }


    }

    private fun showProgressBar() {
        binding.progressBarRegisterScreen.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBarRegisterScreen.visibility = View.INVISIBLE
    }


}