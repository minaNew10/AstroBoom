package com.minabeshara.astroboom.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minabeshara.astroboom.databinding.ItemAsteriodBinding
import com.minabeshara.astroboom.model.Asteroid

class AsteroidAdapter(private val clickListener: AsteroidClickListener) : RecyclerView.Adapter<AsteroidAdapter.ViewHolder>() {

    var data = listOf<Asteroid>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], clickListener)
    }

    class ViewHolder private constructor(val binding: ItemAsteriodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding =
                    ItemAsteriodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: Asteroid,clickListener: AsteroidClickListener) {
            binding.asteroid = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class AsteroidClickListener(private val listener: (asteroid : Asteroid) -> Unit){
    fun onClick(asteroid: Asteroid) = listener(asteroid)
}