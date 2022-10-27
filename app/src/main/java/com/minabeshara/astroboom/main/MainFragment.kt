package com.minabeshara.astroboom.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.minabeshara.astroboom.R
import com.minabeshara.astroboom.databinding.FragmentMainBinding
import com.minabeshara.astroboom.utils.parseAsteroidsJsonResult
import org.json.JSONObject

class MainFragment : Fragment() {


    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMainBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_main, container, false)


        viewModel.imageOfDay.observe(viewLifecycleOwner) {
            binding.image = it
        }

        viewModel.navigateToAsteroidDetails.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToDetailsFragment(it)
                )
                viewModel.onAsteroidDetailsNavigated()
            }
        }
        val adapter = AsteroidAdapter(AsteroidClickListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })
        binding.asteroidRecycler.adapter = adapter
        viewModel.response.observe(viewLifecycleOwner) {
            Log.i("TAG", "onCreateView: $it")
            val list = parseAsteroidsJsonResult(JSONObject(it))
            viewModel.hideProgressDialog()
            adapter.data = list
        }
        viewModel.progressDialogVisibilty.observe(viewLifecycleOwner) { visible ->
            if (visible) {
                binding.statusLoadingWheel.visibility = View.VISIBLE
            } else {
                binding.statusLoadingWheel.visibility = View.GONE
            }
        }
        return binding.root
    }


}