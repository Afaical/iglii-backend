package com.igli.backend.webservice

import com.igli.backend.models.FertilisationDateModel
import retrofit2.Call
import retrofit2.http.GET

interface WebService {

    @GET("fertilisationDate/getAllFertilisationDate")
    fun getAllFertilisationDate(
    ): Call<List<FertilisationDateModel>>

    companion object {
        var BASE_URL = "http://localhost:8080"
    }
}