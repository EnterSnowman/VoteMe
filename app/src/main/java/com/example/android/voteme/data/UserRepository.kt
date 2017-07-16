package com.example.android.voteme.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Valentin on 15.07.2017.
 */
class UserRepository private constructor(){

    var mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    companion object {
        private var repository : UserRepository? = null
        fun getInstance(): UserRepository {
            if (repository==null)
                repository = UserRepository()
            return repository!!

        }
    }

    fun getCurrentUser() : FirebaseUser? = mAuth?.currentUser

    fun signIn(email: String, password: String, callback: DataSource.SignInCallback){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            task -> if (task.isSuccessful) callback.onSignInCompleted() else callback.onSignInFailure(task.exception)
            }
        }

    fun signUp(email: String, password: String, callback: DataSource.SignUpCallback){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task -> if (task.isSuccessful) callback.onSignUpCompleted() else callback.onSignUpFailure(task.exception) }
    }
    }


