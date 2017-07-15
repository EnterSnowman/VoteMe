package com.example.android.voteme.data

/**
 * Created by Valentin on 16.07.2017.
 */
interface DataSource {
    interface SignInCallback{
        fun onSignInCompleted()

        fun onSignInFailure()
    }
}