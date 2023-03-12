package com.countries.app.fragment.countries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.countries.app.R
import com.countries.app.data.api.model.Country
import com.countries.app.databinding.FragmentCountriesBinding
import com.countries.app.utils.adapter.CountriesAdapter
import com.countries.app.utils.ViewState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CountriesFragment : Fragment() {

    private val countriesFragmentViewModel: CountriesFragmentViewModel by viewModels()
    private lateinit var binding: FragmentCountriesBinding
    private var countryList = ArrayList<Country>()
    private var adapter : CountriesAdapter = CountriesAdapter(countryList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCountriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCountries.layoutManager = LinearLayoutManager(view.context)
        binding.rvCountries.adapter = adapter
        adapter.setOnItemClickListener(object : CountriesAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                findNavController().navigate(CountriesFragmentDirections.actionCountriesFragmentToProvincesFragment(countryList[position].country))
            }
        })
        countriesFragmentViewModel.countries.observe(viewLifecycleOwner){
            render(it)
        }
    }

    private fun render(state: ViewState<List<Country>>){
        when(state){
            is ViewState.Success -> {
                binding.loader.visibility = View.GONE
                countryList.clear()
                countryList.addAll(state.data as ArrayList<Country>)
                adapter.notifyDataSetChanged()
            }
            is ViewState.Failure -> {
                binding.loader.visibility = View.GONE
                Toast.makeText(requireContext(), getText(R.string.error), Toast.LENGTH_SHORT).show()
            }
            is ViewState.Loading -> {
                binding.loader.visibility = View.VISIBLE
            }
        }
    }
}