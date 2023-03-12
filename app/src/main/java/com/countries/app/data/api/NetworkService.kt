package com.countries.app.data.api

import com.countries.app.data.api.model.CountriesResponse
import com.countries.app.data.api.model.StatesRequest
import com.countries.app.data.api.model.StatesResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkService {
    @GET("countries")
    suspend fun getCountries(): CountriesResponse

    @POST("countries/states")
    suspend fun getProvinces(@Body request: StatesRequest): StatesResponse
}