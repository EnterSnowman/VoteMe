package com.example.android.voteme.data

import com.example.android.voteme.model.Vote
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.*

/**
 * Created by Valentin on 16.09.2017.
 */
interface CloudFunctionsInterface {

    @POST("createVote")
    fun createVote(@Body vote : HashMap<String,Any>,@Header("Authorization") token: String) : Call<ResponseBody>


    companion object Factory{
        fun create(): CloudFunctionsInterface{
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://us-central1-vote-me-80aab.cloudfunctions.net/")
                    .build()
            return retrofit.create(CloudFunctionsInterface::class.java)
        }
    }
}