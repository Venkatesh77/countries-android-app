package com.countries.app.fragment.countries

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.countries.app.data.api.model.Country
import com.countries.app.data.api.repo.PlacesRepository
import com.countries.app.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesFragmentViewModel @Inject constructor(private val placesRepository: PlacesRepository): ViewModel() {

    val countries = MutableLiveData<ViewState<List<Country>>>()
    init {
        fetchCountries()
    }

    fun fetchCountries(){
        countries.postValue(ViewState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = placesRepository.getCoutries()
                countries.postValue(ViewState.Success(result.data))
            }catch (ex: Exception){
                countries.postValue(ViewState.Failure(ex.message.toString()))
            }
        }
    }

}