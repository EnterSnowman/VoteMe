package com.example.android.voteme.data

import com.example.android.voteme.model.Vote
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

    interface VoteAddedCallback{
        fun onComplete()

        fun onFailure(exception : Exception?)
    }

    interface VotesCallback{
        fun onComplete(votes : ArrayList<Vote>?)

        fun onFailure(exception : Exception?)
    }

    interface SingleVoteLoadCallback{
        fun onLoad(vote:Vote)

        fun onFailure(exception: Exception)
    }

    interface ElectCallback{
        fun onElected()

        fun onFailure(exception: Exception)
    }

    interface RefreshVoteCallback{
        fun onVoteUpdated(varinat: String,newCount : Int)
    }
}