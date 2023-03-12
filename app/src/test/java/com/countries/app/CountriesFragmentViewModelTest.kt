package com.countries.app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.countries.app.data.api.NetworkService
import com.countries.app.data.api.model.CountriesResponse
import com.countries.app.data.api.model.Country
import com.countries.app.data.api.repo.PlacesRepository
import com.countries.app.fragment.countries.CountriesFragmentViewModel
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
class CountriesFragmentViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    lateinit var countriesFragmentViewModel: CountriesFragmentViewModel
    lateinit var placesRepository: PlacesRepository

    @Mock
    lateinit var networkService: NetworkService

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        placesRepository = PlacesRepository(networkService)
        countriesFragmentViewModel = CountriesFragmentViewModel(placesRepository)
    }

    @Test
    fun getAllCountriesLoadingTest() {
        runBlocking {
            val response = CountriesResponse(
                error = false,
                msg = "countries fetched",
                data = listOf(Country(country = "India", iso2 = "IN", iso3 = "IND"))
            )
            Mockito.`when`(placesRepository.getCoutries())
                .thenReturn(response)
            countriesFragmentViewModel.fetchCountries()
            val result: MutableLiveData<ViewState<List<Country>>> = countriesFragmentViewModel.countries
            assertNotNull(result.value)
            assertEquals(ViewState.Loading, result.value)
        }
    }

    @Test
    fun getAllCountriesSuccessTest() {
       runBlocking {
            val response = CountriesResponse(
                error = false,
                msg = "countries fetched",
                data = listOf(Country(country = "India", iso2 = "IN", iso3 = "IND"))
            )
            Mockito.`when`(placesRepository.getCoutries())
                .thenReturn(response)
            countriesFragmentViewModel.fetchCountries()
           countriesFragmentViewModel.countries.captureValues()
           val result: MutableLiveData<ViewState<List<Country>>> = countriesFragmentViewModel.countries
           assertNotNull(result.value)
           assertEquals(result.value, ViewState.Success(response.data))
        }
    }

    @Test
    fun getAllCountriesFailureTest() {
        runBlocking {
            val response = null
            Mockito.`when`(placesRepository.getCoutries())
                .thenReturn(response)
            countriesFragmentViewModel.fetchCountries()
            countriesFragmentViewModel.countries.captureValues()
            val result: MutableLiveData<ViewState<List<Country>>> = countriesFragmentViewModel.countries
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