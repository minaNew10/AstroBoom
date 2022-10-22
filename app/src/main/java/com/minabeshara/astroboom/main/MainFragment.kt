package com.minabeshara.astroboom.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.minabeshara.astroboom.R
import com.minabeshara.astroboom.databinding.FragmentMainBinding
import com.minabeshara.astroboom.model.Asteroid

class MainFragment : Fragment() {


    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMainBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_main, container, false)
        val adapter = AsteroidAdapter()
        adapter.data = listOf(
            Asteroid(
                11, "name", "date", 2.3, 3.4, 3.4, 5.6, false
            ),
            Asteroid(
                11, "name1", "date", 2.3, 3.4, 3.4, 5.6, false
            ),
            Asteroid(
                11, "name2", "date", 2.3, 3.4, 3.4, 5.6, false
            ),
            Asteroid(
                11, "name3", "date", 2.3, 3.4, 3.4, 5.6, false
            ),
            Asteroid(
                11, "name4", "date", 2.3, 3.4, 3.4, 5.6, false
            ),
            Asteroid(
                11, "name5", "date", 2.3, 3.4, 3.4, 5.6, false
            )
        )
        binding.asteroidRecycler.adapter = adapter
        return binding.root
    }


}