package com.geekydroid.firebaselearn.viewmodels

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.geekydroid.firebaselearn.data.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService

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
                if (result.isSuccessful) {
                    val currentUer = auth.currentUser
                    val uniqueKey = currentUer?.uid
                    val user = User(
                        userId = uniqueKey!!,
                        emailAddress = emailAddress,
                        createdOn = System.currentTimeMillis()
                    )
                    val ref = database.getReference("users").child(uniqueKey)
                    ref.setValue(user).addOnCompleteListener { result2 ->
                        if (result2.isSuccessful) {
                            val prefs: SharedPreferences? =
                                getApplication<Application>().applicationContext.getSharedPreferences(
                                    "token",
                                    FirebaseMessagingService.MODE_PRIVATE
                                )
                            val token = prefs?.getString("token", "")
                            database.getReference("Tokens").child(uniqueKey)
                                .setValue(token).addOnCompleteListener {
                                    signUpListener.postValue(true)
                                }

                        } else {
                            signUpListener.postValue(false)
                        }
                    }
                } else {
                    signUpListener.postValue(false)
                }
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

