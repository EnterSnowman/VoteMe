package com.example.android.voteme.utils

import android.content.Context
import android.util.Log
import com.example.android.voteme.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import java.lang.Exception
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

        fun getErrorText(errorCode:String,context: Context): String{
            when(errorCode){
                "ERROR_WRONG_PASSWORD"->return context.getString(R.string.error_wrong_password)
                "ERROR_USER_NOT_FOUND" -> return context.getString(R.string.error_user_not_found)
                "NETWORK_ERROR" -> return context.getString(R.string.error_network)
                "ERROR_EMAIL_ALREADY_IN_USE" ->return context.getString(R.string.error_email_already_in_use)
                else -> return "unknown error"
            }
        }

        fun handleException(exception:Exception):String{
            var errorCode = ""
            Log.d("Exception",exception.message)

            Log.d("Exception",exception.javaClass.simpleName)
            when(exception){
                is FirebaseNetworkException ->{
                    Log.d("Network",exception.message)
                    errorCode = "NETWORK_ERROR"
                }
                is FirebaseAuthException ->{
                    Log.d("Login",exception.errorCode)
                    Log.d("Login",exception.message)
                    errorCode = exception.errorCode
                }
                is FirebaseException ->{
                    if (exception.message!!.contains("Network Error"))
                    errorCode = "NETWORK_ERROR"
                    Log.d("Login",exception.message)
                    //errorCode = exception.errorCode
                }
            }
            return errorCode
        }
    }
}