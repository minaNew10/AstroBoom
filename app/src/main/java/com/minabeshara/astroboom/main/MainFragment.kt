package com.minabeshara.astroboom.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.minabeshara.astroboom.R
import com.minabeshara.astroboom.databinding.FragmentMainBinding
import com.minabeshara.astroboom.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {


    private val viewModel: MainViewModel by viewModels {
        val activity = requireNotNull(activity)
        MainViewModel.ViewModelFactory(activity.application)
    }

    private lateinit var adapter: AsteroidAdapter

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
        adapter = AsteroidAdapter(AsteroidClickListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })
        binding.asteroidRecycler.adapter = adapter


        viewModel.newAsteroids.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                viewModel.hideProgressDialog()
                adapter.data = it
                adapter.notifyDataSetChanged()
            }
        }
        viewModel.asteroidsInDay.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                viewModel.hideProgressDialog()
                adapter.data = it
                adapter.notifyDataSetChanged()
            }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.date_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.show_new -> {
                        viewModel.getAsteroidsInDay(getToday())
                        true
                    }
                    R.id.show_old -> {
                            viewModel.newAsteroids.value?.let {
                                adapter.data = it
                                adapter.notifyDataSetChanged()
                            }
                            true
                        }

                    else -> false
                }
            }

        })
    }

    fun getToday() : String{
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }


}