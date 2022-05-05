package com.geekydroid.firebaselearn.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.geekydroid.firebaselearn.data.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragmentViewmodel(application: Application, private val userId: String) :
    AndroidViewModel(application) {

    private val database = Firebase.database
    private val userLiveData: MutableLiveData<List<User>> = MutableLiveData()
    private val userListener = object : ValueEventListener {
        /**
         * This method will be called with a snapshot of the data at this location. It will also be called
         * each time that data changes.
         *
         * @param snapshot The current data at the location
         */
        override fun onDataChange(snapshot: DataSnapshot) {
            val userList: MutableList<User> = mutableListOf()
            for (data in snapshot.children) {
                val user = data.getValue(User::class.java)
                if (user != null) {
                    if (user.userId != userId) {
                        userList.add(user)
                    }
                }
            }
            userLiveData.postValue(userList)
        }

        /**
         * This method will be triggered in the event that this listener either failed at the server, or
         * is removed as a result of the security and Firebase Database rules. For more information on
         * securing your data, see: [ Security
 * Quickstart](https://firebase.google.com/docs/database/security/quickstart)
         *
         * @param error A description of the error that occurred
         */
        override fun onCancelled(error: DatabaseError) {

        }
    }


    fun getUserList() = userLiveData


    init {
        database.reference.child("users").addValueEventListener(userListener)
    }
}

class HomeFragmentViewModelFactory(
    private val application: Application,
    private val userId: String
) :
    ViewModelProvider.Factory {
    /**
     * Creates a new instance of the given `Class`.
     *
     * @param modelClass a `Class` whose instance is requested
     * @return a newly created ViewModel
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Application::class.java, String::class.java)
            .newInstance(application, userId)
    }

}