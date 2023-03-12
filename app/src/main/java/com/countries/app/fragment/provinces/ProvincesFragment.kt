package com.countries.app.fragment.provinces

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.countries.app.R
import com.countries.app.data.api.model.State
import com.countries.app.databinding.FragmentProvincesBinding
import com.countries.app.utils.adapter.StatesAdapter
import com.countries.app.utils.ViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProvincesFragment : Fragment() {
    private val args: ProvincesFragmentArgs by navArgs()
    private val provincesFragmentViewModel: ProvincesFragmentViewModel by viewModels()
    private lateinit var binding: FragmentProvincesBinding
    private var provincesList = ArrayList<State>()
    private var adapter : StatesAdapter = StatesAdapter(provincesList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProvincesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.title = args.country
        binding.rvCountries.layoutManager = LinearLayoutManager(view.context)
        binding.rvCountries.adapter = adapter

        provincesFragmentViewModel.states.observe(viewLifecycleOwner){
            render(it)
        }
    }

    private fun render(state: ViewState<List<State>>){
        when(state){
            is ViewState.Success -> {
                binding.loader.visibility = View.GONE
                if(state.data.isNotEmpty()){
                    provincesList.clear()
                    provincesList.addAll(state.data as ArrayList<State>)
                    adapter.notifyDataSetChanged()
                }else{
                    binding.rvCountries.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                }
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