package com.countries.app.fragment.provinces

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.countries.app.data.api.model.State
import com.countries.app.data.api.model.StatesRequest
import com.countries.app.data.api.repo.PlacesRepository
import com.countries.app.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProvincesFragmentViewModel @Inject constructor(private val placesRepository: PlacesRepository, private val state: SavedStateHandle): ViewModel() {
    private var statesRequest: StatesRequest
    val states = MutableLiveData<ViewState<List<State>>>()
    init {
        val country = state.get<String>("country")
        statesRequest = StatesRequest(country!!)
        fetchStates()
    }

    fun fetchStates(){
        states.postValue(ViewState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = placesRepository.getStates(statesRequest)
                states.postValue(ViewState.Success(result.data.states))
            }catch (ex: Exception){
                states.postValue(ViewState.Failure(ex.message.toString()))
            }
        }
    }

}