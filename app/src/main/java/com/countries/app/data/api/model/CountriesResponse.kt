package com.countries.app.data.api.model

data class CountriesResponse(
    val data: List<Country>,
    val error: Boolean,
    val msg: String
)

data class Country(
    val country: String,
    val iso2: String,
    val iso3: String
)