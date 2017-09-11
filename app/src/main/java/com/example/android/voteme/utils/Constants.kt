package com.example.android.voteme.utils

/**
 * Created by Valentin on 16.07.2017.
 */
class Constants {
    companion object {
        val VOTES = "votes"
        val TITLE = "title"
        val VARIANTS = "variants"
        val USERS = "users"
        val CREATED = "created"
        val VOTE_ID = "vote_id"
        val VOTED = "voted"
        val JOINED = "joined"
        val IS_OPEN = "isOpen"
        val IS_REVOTABLE = "isRevotable"
        val VOTES_URL = "https://vote-me-80aab.firebaseio.com/votes/"
        //error types
        val NON_UNIQUE_VARIANT  = 1
        val EMPTY_TITLE = 2
        val NOT_FULL_LIST  = 3
        val  KEY = "_key"


    }

}
