package com.geekydroid.firebaselearn.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.geekydroid.firebaselearn.data.User
import com.geekydroid.firebaselearn.utils.UserPreferences
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeFragmentViewmodel(application: Application) : AndroidViewModel(application) {

    private lateinit var userId: String
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

    init {
        fetchUserId()

    }


    private fun fetchUserId() {
        viewModelScope.launch {
            userId =
                UserPreferences.getUserId(getApplication<Application>().applicationContext).first()
            database.reference.child("users").addValueEventListener(userListener)
            Log.d("homeFrag", "getUserId: $userId")
        }
    }

    fun getUserList() = userLiveData

    fun getUserId() = userId
}

class HomeFragmentViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    /**
     * Creates a new instance of the given `Class`.
     *
     * @param modelClass a `Class` whose instance is requested
     * @return a newly created ViewModel
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Application::class.java).newInstance(application)
    }

}