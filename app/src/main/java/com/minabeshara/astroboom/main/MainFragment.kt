package com.minabeshara.astroboom.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.minabeshara.astroboom.R
import com.minabeshara.astroboom.databinding.FragmentMainBinding

class MainFragment : Fragment() {


    private val viewModel: MainViewModel by viewModels {
        val activity = requireNotNull(activity)
        MainViewModel.ViewModelFactory(activity.application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMainBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_main, container, false)


        viewModel.pictureOfDay.observe(viewLifecycleOwner) {
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


        viewModel.asteroids.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                viewModel.hideProgressDialog()
                adapter.data = it
            }
            adapter.notifyDataSetChanged()
        }
        viewModel.progressDialogVisibility.observe(viewLifecycleOwner) { visible ->
            if (visible) {
                binding.statusLoadingWheel.visibility = View.VISIBLE
            } else {
                binding.statusLoadingWheel.visibility = View.GONE
            }
        }
        return binding.root
    }


}