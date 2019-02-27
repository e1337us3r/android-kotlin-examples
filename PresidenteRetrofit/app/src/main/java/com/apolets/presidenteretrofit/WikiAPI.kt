package com.apolets.presidenteretrofit

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object WikiAPI{
    const val URL = "https://en.wikipedia.org/w/"

    object Model{
        data class Query(val query: QueryResult)
        data class QueryResult(val searchinfo: SearchInfo)
        data class SearchInfo(val totalhits: Int)
    }

    interface Service{
        @GET("api.php?action=query&prop=links&list=search&format=json&formatversion=2")
        fun search(@Query("srsearch") searchTerm: String): Call<Model.Query>
    }

    private val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val service = retrofit.create(Service::class.java)





}