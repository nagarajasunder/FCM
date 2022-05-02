package com.geekydroid.firebaselearn.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.geekydroid.firebaselearn.R
import com.geekydroid.firebaselearn.viewmodels.SignUpFragmentViewModel
import com.geekydroid.firebaselearn.viewmodels.SignUpFragmentViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var fragmentView: View
    private lateinit var etEmailAddress: TextInputLayout
    private lateinit var etPassword: TextInputLayout
    private lateinit var signup: Button
    private lateinit var btnLogin: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModelFactory:SignUpFragmentViewModelFactory
    private lateinit var viewModel: SignUpFragmentViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = SignUpFragmentViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this,viewModelFactory)[SignUpFragmentViewModel::class.java]
        auth = Firebase.auth




        fragmentView = view
        setUI()
        viewModel.getSignUpListener().observe(viewLifecycleOwner) {
            if (it) {
                showToast("Successfully Signed up")
                navigateToHome()
            } else {
                showToast("Error in signing up. Please try again")
            }
        }

        signup.setOnClickListener {
            signupUser()
        }

        btnLogin.setOnClickListener {
            authenticateUser()
        }
    }

    private fun authenticateUser() {
        val emailAddress = etEmailAddress.editText!!.text.toString()
        val password = etPassword.editText!!.text.toString()
        if (viewModel.verifyEmailAddressAndPassword(emailAddress, password)) {
            auth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        showToast("Login successful!")
                        navigateToHome()
                    } else {
                        result.exception?.message?.let {
                            showToast(it)
                        }
                    }
                }
        }
    }


    private fun signupUser() {

        val emailAddress = etEmailAddress.editText!!.text.toString()
        val password = etPassword.editText!!.text.toString()

        if (viewModel.verifyEmailAddressAndPassword(emailAddress, password)) {
            viewModel.createUser(emailAddress, password)
        } else {
            showToast("Please fill all the details")
        }
    }

    private fun navigateToHome() {

        val action = SignUpFragmentDirections.actionSignUpFragmentToHomeFragment2()
        findNavController().navigate(action)
//        mecpv90536@svcache.com
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setUI() {
        etEmailAddress = fragmentView.findViewById(R.id.ed_emailAddress)
        etPassword = fragmentView.findViewById(R.id.ed_password)
        signup = fragmentView.findViewById(R.id.btn_signup)
        btnLogin = fragmentView.findViewById(R.id.btn_login)
    }
}