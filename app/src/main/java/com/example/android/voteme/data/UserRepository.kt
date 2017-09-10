package com.example.android.voteme.data

import com.google.firebase.auth.*

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

    fun getCurrentUser() : FirebaseUser? = mAuth.currentUser

    fun signIn(email: String, password: String, callback: DataSource.SignInCallback){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            task -> if (task.isSuccessful) callback.onSignInCompleted() else callback.onSignInFailure(task.exception)
            }
        }

    fun signUp(email: String, password: String, callback: DataSource.SignUpCallback){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task -> if (task.isSuccessful) callback.onSignUpCompleted() else callback.onSignUpFailure(task.exception) }
    }

    fun sendVerificationEmail(callback: DataSource.EmailVerificationCallback){
        if (mAuth.currentUser!=null)
            mAuth.currentUser!!.sendEmailVerification()
                    .addOnCompleteListener { task -> if(task.isSuccessful)  callback.onSended() else callback.onSendedFailure(task.exception)  }
    }

    fun isUserVerified(callback : DataSource.IsVerifiedCallback)  {
        if (!mAuth.currentUser!!.isEmailVerified)
        mAuth.currentUser!!.reload().addOnCompleteListener { task ->         callback.onResult( mAuth.currentUser!!.isEmailVerified) }
        else
            callback.onResult(true)

    }

    fun changePassword(oldPass:String,newPass:String,callback:DataSource.ChangePasswordCallback){
        var cred = EmailAuthProvider.getCredential(mAuth.currentUser!!.email!!,oldPass)
        mAuth.currentUser!!.reauthenticate(cred).addOnCompleteListener { task ->
            if (task.isSuccessful)
                mAuth.currentUser!!.updatePassword(newPass).addOnCompleteListener{task -> if (task.isSuccessful) callback.onSuccess() else callback.onFailure() }
            else
                callback.onWrongOldPassword()
        }
    }
    }


