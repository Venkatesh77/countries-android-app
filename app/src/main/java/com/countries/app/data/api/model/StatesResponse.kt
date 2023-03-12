package com.countries.app.data.api.model

data class StatesResponse(
    val data: Data,
    val error: Boolean,
    val msg: String
)

data class Data(
    val iso2: String,
    val iso3: String,
    val name: String,
    val states: List<State>
)

data class State(
    val name: String,
    val state_code: String
)