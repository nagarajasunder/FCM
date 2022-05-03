package com.geekydroid.firebaselearn.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geekydroid.firebaselearn.R
import com.geekydroid.firebaselearn.adapter.ChatAdapter
import com.geekydroid.firebaselearn.viewmodels.ChatFragmentViewModel
import com.geekydroid.firebaselearn.viewmodels.ChatFragmentViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var fragmentView: View
    private lateinit var etMessage: TextInputEditText
    private lateinit var btnSend: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModelFactory: ChatFragmentViewModelFactory
    private lateinit var viewModel: ChatFragmentViewModel
    private val args: ChatFragmentArgs by navArgs()
    private lateinit var senderId: String
    private lateinit var receiverId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentView = view
        senderId = args.senderId
        receiverId = args.receiverId
        setUI()
        viewModelFactory = ChatFragmentViewModelFactory(senderId, receiverId)
        viewModel = ViewModelProvider(this, viewModelFactory)[ChatFragmentViewModel::class.java]

        btnSend.setOnClickListener {
            sendMessage()
        }

        viewModel.getChatData().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                chatAdapter.submitList(it)
            }
        }


    }

    private fun sendMessage() {
        val message = etMessage.text.toString()
        if (message.trim().isNotEmpty()) {
            viewModel.newChat(senderId, receiverId, message.trim())
        }
        etMessage.text?.clear()
    }

    private fun setUI() {
        etMessage = fragmentView.findViewById(R.id.et_message)
        btnSend = fragmentView.findViewById(R.id.fab_send)
        recyclerView = fragmentView.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        chatAdapter = ChatAdapter(senderId)
        recyclerView.adapter = chatAdapter
    }

}