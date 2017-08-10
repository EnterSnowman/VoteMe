package com.example.android.voteme.utils

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
    }
}