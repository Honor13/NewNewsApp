package com.example.newnewsapi.presentation.ui.fragments.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.firebasewithmvvm.util.UiState
import com.example.newnewsapi.R
import com.example.newnewsapi.databinding.FragmentLoginBinding
import com.example.newnewsapi.presentation.viewmodels.AuthViewModel
import com.example.newnewsapi.presentation.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var mainViewModel: MainViewModel
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var view: View
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    var  isLogin:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_login, container, false)
        binding.loginFragmentObject = this
        view = binding.buttonLogin
        initMyAuthStateListener()

        authViewModel.loginState()
        authViewModel.isLoginAuthVM.observe(viewLifecycleOwner){ result->

            Log.e("Dante","LoginFragment $result")
            isLogin = result
            if (isLogin){
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_bottomNavHolderFragment)
            }
        }
        authViewModel.login.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    showProgressBar()
                }

                is UiState.Failure -> {
                    hideProgressBar()
                    Toast.makeText(requireContext(),state.error,Toast.LENGTH_SHORT).show()
                }

                is UiState.Success -> {
                    hideProgressBar()
                    Toast.makeText(requireContext(),state.data,Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_bottomNavHolderFragment)
                }
            }

        }


        binding.textRegister.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_registerFragment)
        }


        return binding.root
    }

     fun login(email: String, password: String){
        authViewModel.login(email,password)
    }

    private fun initMyAuthStateListener() {

        mAuthStateListener = FirebaseAuth.AuthStateListener { p0 ->
            Log.e("Dante",p0.currentUser?.uid.toString())
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener)
    }

//    override fun onStop() {
//        super.onStop()
//        FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener)
//    }

}