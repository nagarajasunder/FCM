package com.geekydroid.firebaselearn.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geekydroid.firebaselearn.R
import com.geekydroid.firebaselearn.adapter.HomeFragmentAdapter
import com.geekydroid.firebaselearn.viewmodels.HomeFragmentViewModelFactory
import com.geekydroid.firebaselearn.viewmodels.HomeFragmentViewmodel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var fragmentView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeFragmentAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModelFactory: HomeFragmentViewModelFactory
    private lateinit var viewModel: HomeFragmentViewmodel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentView = view
        auth = Firebase.auth
        viewModelFactory = HomeFragmentViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this)[HomeFragmentViewmodel::class.java]
        setHasOptionsMenu(true)
        setUI(fragmentView)

        viewModel.getUserList().observe(viewLifecycleOwner) { response ->
            if (response.isNotEmpty()) {
                adapter.submitList(response)
            }
        }



    }

    private fun setUI(fragmentView: View) {
        recyclerView = fragmentView.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = HomeFragmentAdapter()
        recyclerView.adapter = adapter

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_fragment_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> logout()
        }
        return true

    }

    private fun logout() {
        if (auth.currentUser != null) {
            auth.signOut()
        }
    }
}