package com.igli.backend.webservice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WebServiceUtils {

    fun getWebserviceManager(): WebService {
        val retrofit = Retrofit.Builder()
                .baseUrl(WebService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(WebService::class.java)
    }
}