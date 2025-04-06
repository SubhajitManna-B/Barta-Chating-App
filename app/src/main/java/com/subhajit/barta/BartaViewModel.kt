package com.subhajit.barta

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.subhajit.barta.data.Event
import com.subhajit.barta.data.USER_NODE
import com.subhajit.barta.data.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BartaViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore

) : ViewModel() {
    val inProgress = mutableStateOf(false)
    private val eventMutableState = mutableStateOf<Event<String>?>(null)
    val signIn = mutableStateOf(false)
    private var userData = mutableStateOf<UserData?>(null)

    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

    //Function used for sign in the user
    fun signup(name: String, number: String, email: String, password: String){
        //set progress value true because a process is running
        inProgress.value = true
        //Check all the field are fill or not
        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()){
            inProgress.value = false
            handleException(customMessage = "Please fill all the fields.")
            return
        }
        inProgress.value = true
        db.collection(USER_NODE).whereEqualTo("userNumber", number).get().addOnSuccessListener { it ->
            if (it.isEmpty){
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    //Check the sign up process is successful or not
                    if (it.isSuccessful){
                        signIn.value = true
                        //Save user data in database
                        createOrUpdateProfile(name,number)
                        Log.d("signup","User Sign up Successfully")
                        //navigateTo(navController = NavController(), DestinationScreen.ChatList.route)
                    }
                    else{
                        handleException(it.exception)
                    }
                }
            } else{
                handleException(customMessage = "Number already exist")
            }
        }
    }

    //Fun to save/update user profile data in database
    private fun createOrUpdateProfile(name: String?=null, number: String?=null, profileImageUrl: String ?= null){
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            userName = name?:userData.value?.userName,
            userNumber = number?:userData.value?.userNumber,
            userImage = profileImageUrl?:userData.value?.userImage
        )
        uid?.let {
            inProgress.value = true
            db.collection(USER_NODE).document(uid).get().addOnSuccessListener{
                if (it.exists()){
                    //update user data
                }else{
                    //Add user data to database
                    db.collection(USER_NODE).document(uid).set(userData)
                    inProgress.value = false
                    getUserData(uid)
                }
            }.addOnFailureListener {
                handleException(it, "Can't Retrieve user data")
            }
        }
    }

    //Fetch the user data from the database
    private fun getUserData(uid: String) {
        inProgress.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener{value, error ->
            if (error != null){
                handleException(error, "Can't Retrieve user data")
            }

            if (value != null) {
                val user = value.toObject<UserData>()
                userData.value = user
                inProgress.value = false
            }
        }
    }

    //Function to handle the exception
    private fun handleException(exception: Exception?=null, customMessage: String=""){
        Log.e("LiveChatApp", "Live chat exception: ",exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage?:""
        val message = customMessage.ifEmpty { errorMsg }
        eventMutableState.value = Event(message)
        inProgress.value = false
    }
}