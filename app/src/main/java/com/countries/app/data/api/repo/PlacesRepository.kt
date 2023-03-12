package com.countries.app.data.api.repo

import com.countries.app.data.api.NetworkService
import com.countries.app.data.api.model.CountriesResponse
import com.countries.app.data.api.model.StatesRequest
import com.countries.app.data.api.model.StatesResponse
import javax.inject.Inject

class PlacesRepository @Inject constructor(private val networkService: NetworkService) {

    suspend fun getCoutries(): CountriesResponse{
        return networkService.getCountries()
    }

    suspend fun getStates(statesRequest: StatesRequest): StatesResponse{
        return networkService.getProvinces(statesRequest)
    }
}