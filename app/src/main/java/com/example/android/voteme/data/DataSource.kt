package com.example.android.voteme.data

import java.lang.Exception

/**
 * Created by Valentin on 16.07.2017.
 */
interface DataSource {
    interface SignInCallback{
        fun onSignInCompleted()

        fun onSignInFailure(exception : Exception?)
    }

    interface SignUpCallback{
        fun onSignUpCompleted()

        fun onSignUpFailure(exception : Exception?)
    }
}