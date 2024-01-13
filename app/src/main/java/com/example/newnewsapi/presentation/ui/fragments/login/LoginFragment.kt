package com.example.newnewsapi.presentation.ui.fragments.login

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
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.newnewsapi.R
import com.example.newnewsapi.data.auth.Resource
import com.example.newnewsapi.databinding.FragmentLoginBinding
import com.example.newnewsapi.presentation.viewmodels.MainViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.reflect.Modifier

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var view:View





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_login,container,false)
        binding.loginFragmentObject = this
        view = binding.buttonLogin


        mainViewModel.loginFlow.observe(viewLifecycleOwner){
            when(it){
                is Resource.Failure -> {
                    hideProgressBar()
                    Toast.makeText(requireContext(),it.exception.message.toString(),Toast.LENGTH_SHORT).show()
                }
                Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_newsFragment)
                }
                null -> {

                }
            }
        }

        binding.textRegister.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_registerFragment)
        }


        return binding.root
    }

    fun login(email: String, password: String){
        mainViewModel.loginUser(email,password)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
    }

}