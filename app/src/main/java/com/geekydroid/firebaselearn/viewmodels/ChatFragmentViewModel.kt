package com.geekydroid.firebaselearn.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.geekydroid.firebaselearn.data.Chat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val TAG = "ChatFragmentViewModel"

class ChatFragmentViewModel(private val senderID: String, private val receiverId: String) :
    ViewModel() {

    private val chatData: MutableLiveData<List<Chat>> = MutableLiveData()
    private val database = Firebase.database
    private val chatListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val chatList: MutableList<Chat> = mutableListOf()
            for (data in snapshot.children) {
                val chat: Chat? = data.getValue(Chat::class.java)
                chat?.let {
                    if ((it.senderId == senderID && it.receiverId == receiverId) || (it.senderId == receiverId && it.receiverId == senderID)) {
                        chatList.add(chat)
                    }
                }
            }
            if (chatList.isNotEmpty()) {
                chatData.postValue(chatList)
            }

        }

        override fun onCancelled(error: DatabaseError) {

        }

    }

    init {
        database.reference.child("chats").addValueEventListener(chatListener)
    }

    fun newChat(senderId: String, receiverId: String, message: String) {
        val chatId = database.reference.child("chats").push().key
        val chat = Chat(
            chatId = chatId ?: "",
            senderId = senderId,
            receiverId = receiverId,
            message = message,
            timeStamp = System.currentTimeMillis()
        )
        database.reference.child("chats").child(chatId!!).setValue(chat)
    }

    fun getChatData() = chatData

}

class ChatFragmentViewModelFactory(private val senderId: String, private val receiverId: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(String::class.java, String::class.java)
            .newInstance(senderId, receiverId)
    }

}
