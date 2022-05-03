package com.geekydroid.firebaselearn.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.geekydroid.firebaselearn.data.User
import com.geekydroid.firebaselearn.utils.UserPreferences
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

private const val TAG = "SignUpFragmentViewModel"

class SignUpFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val auth = Firebase.auth
    private val database = Firebase.database
    private val signUpListener = MutableLiveData<Boolean>()
    fun getSignUpListener() = signUpListener


    fun verifyEmailAddressAndPassword(emailAddress: String, password: String): Boolean {
        if (emailAddress.isEmpty() || password.isEmpty()) {
            return false
        }
        return true
    }

    fun createUser(emailAddress: String, password: String) {
        auth.createUserWithEmailAndPassword(emailAddress, password)
            .addOnCompleteListener { result ->
                if (!result.isSuccessful) {
                    signUpListener.postValue(false)
                }
            }
        val uniqueKey = database.getReference("users").push().key
        val user = User(
            userId = uniqueKey ?: "",
            emailAddress = emailAddress,
            createdOn = System.currentTimeMillis()
        )

        val ref = database.getReference("users").child(uniqueKey!!)
        ref.setValue(user).addOnCompleteListener { result ->
            Log.d(TAG, "createUser: ${result.exception}")
            if (result.isSuccessful) {
                signUpListener.postValue(true)
            } else {
                signUpListener.postValue(false)
            }
        }

        viewModelScope.launch {
            UserPreferences.updateUserID(
                uniqueKey,
                getApplication<Application>().applicationContext
            )
        }

    }
}

class SignUpFragmentViewModelFactory(private val application: Application) :
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

