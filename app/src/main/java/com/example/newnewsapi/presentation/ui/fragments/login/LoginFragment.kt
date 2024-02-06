package com.example.newnewsapi.presentation.ui.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.newnewsapi.R
import com.example.newnewsapi.data.auth.Resource
import com.example.newnewsapi.databinding.FragmentLoginBinding
import com.example.newnewsapi.presentation.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var view: View
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_login, container, false)
        binding.loginFragmentObject = this
        view = binding.buttonLogin
        initMyAuthStateListener()

        mainViewModel.loginLiveData.observe(viewLifecycleOwner) {
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
                    hideProgressBar()
                    Navigation.findNavController(view)
                        .navigate(R.id.action_loginFragment_to_bottomNavHolderFragment)
                }

                null -> {

                }
            }
        }

        binding.textRegister.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_registerFragment)
        }


        return binding.root
    }

    fun login(email: String, password: String) {
        mainViewModel.loginUser(email, password)
    }

    private fun initMyAuthStateListener() {

        mAuthStateListener = FirebaseAuth.AuthStateListener { p0 ->
            var user = p0.currentUser
            if (user != null){
                Navigation.findNavController(view)
                    .navigate(R.id.action_loginFragment_to_bottomNavHolderFragment)
            }
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