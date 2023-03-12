package com.countries.app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.countries.app.data.api.NetworkService
import com.countries.app.data.api.model.*
import com.countries.app.data.api.repo.PlacesRepository
import com.countries.app.fragment.provinces.ProvincesFragmentViewModel
import com.countries.app.utils.ViewState
import io.mockk.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ProvincesFragmentViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    lateinit var provincesFragmentViewModel: ProvincesFragmentViewModel
    lateinit var placesRepository: PlacesRepository
    lateinit var savedStateHandle: SavedStateHandle

    @Mock
    lateinit var networkService: NetworkService

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle().apply { set("country", "India") }
        placesRepository = PlacesRepository(networkService)
        provincesFragmentViewModel = ProvincesFragmentViewModel(placesRepository, savedStateHandle)
    }

    @Test
    fun getAllStatesLoadingTest() {
        runBlocking {
            val request = StatesRequest(country = "India")
            val response = StatesResponse(
                error = false,
                msg = "countries fetched",
                data = Data(name = "India", iso2 = "IN", iso3 = "IND", states = listOf(State(name = "Kerala", state_code = "KL")))
            )
            Mockito.`when`(placesRepository.getStates(request))
                .thenReturn(response)
            provincesFragmentViewModel.fetchStates()
            val result: MutableLiveData<ViewState<List<State>>> = provincesFragmentViewModel.states
            assertNotNull(result.value)
            assertEquals(ViewState.Loading, result.value)
        }
    }

    @Test
    fun getAllStatesSuccessTest() {
        runBlocking {
            val request = StatesRequest(country = "India")
            val response = StatesResponse(
                error = false,
                msg = "countries fetched",
                data = Data(name = "India", iso2 = "IN", iso3 = "IND", states = listOf(State(name = "Kerala", state_code = "KL")))
            )
            Mockito.`when`(placesRepository.getStates(request))
                .thenReturn(response)
            provincesFragmentViewModel.fetchStates()
            provincesFragmentViewModel.states.captureValues()
            val result: MutableLiveData<ViewState<List<State>>> = provincesFragmentViewModel.states
            assertNotNull(result.value)
            assertEquals(ViewState.Success(response.data.states), result.value)
        }
    }

    @Test
    fun getAllStatesFailureTest() {
        runBlocking {
            val request = StatesRequest(country = "India")
            val response = null
            Mockito.`when`(placesRepository.getStates(request))
                .thenReturn(response)
            provincesFragmentViewModel.fetchStates()
            provincesFragmentViewModel.states.captureValues()
            val result: MutableLiveData<ViewState<List<State>>> = provincesFragmentViewModel.states
            assertNotNull(result.value)
            assertEquals(result.value, ViewState.Failure("null"))
        }
    }

    private inline fun <reified T > LiveData<T>.captureValues(): List<T?> {
        val mockObserver = mockk<Observer<T>>()
        val list = mutableListOf<T?>()
        every { mockObserver.onChanged(captureNullable(list))} just runs
        this.observeForever(mockObserver)
        return list
    }

}