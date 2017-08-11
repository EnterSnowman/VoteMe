package com.example.android.voteme.model

/**
 * Created by Valentin on 16.07.2017.
 */
data class Vote(var id: String,var title : String,var variants : HashMap<String,Int>,var isOpen:Boolean,var isRevotable:Boolean) {
    constructor() : this("","",HashMap<String,Int>(),true,true)
}