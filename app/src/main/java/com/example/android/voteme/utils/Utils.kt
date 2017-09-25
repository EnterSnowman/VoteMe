package com.example.android.voteme.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Valentin on 10.08.2017.
 */
class Utils {
    companion object {
        public fun addKeySuffix(list:ArrayList<String>):ArrayList<String> {
            return list.map { it.plus(Constants.KEY) } as ArrayList<String>
        }

        public fun removeKeySuffix(list:ArrayList<String>):ArrayList<String>{
            return list.map { it.substring(0,it.length-4) } as ArrayList<String>
        }

        public fun removeKeySuffix(list:HashMap<String,Int>):Map<String,Int>{
            return list.mapKeys { it.key.substring(0,it.key.length-4) }
        }

        public fun isEmailValid(email:String):Boolean = if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) false else true

        public fun isPasswordValid(password:String):Boolean = if (password.isEmpty() || password.length<6) false else true

        public fun getReadableDate(mills: Long): String{
            var date = Date(mills)
            var sdf = SimpleDateFormat("dd.MM.yyyy")
            return sdf.format(date)
        }
    }
}