package com.example.cookbook.adapter.recyclerviewadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookbook.databinding.ItemCuisinesBinding
import com.example.cookbook.model.Country
import com.example.cookbook.model.Meal

class WorldAdapter(private val countries: List<Country>): RecyclerView.Adapter<WorldAdapter.WorldViewHolder>() {


    var onItemClick: ((country: String) -> Unit)? = null

    inner class WorldViewHolder(val binding: ItemCuisinesBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(country: Country){
            binding.apply {
                Glide.with(root).load(country.imageId).into(ivFlagPhoto)
                tvFlagName.text = country.name

                root.setOnClickListener {
                    onItemClick?.invoke(country.name)
                }

            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorldAdapter.WorldViewHolder {
        val binding = ItemCuisinesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WorldViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorldAdapter.WorldViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    override fun getItemCount(): Int {
        return countries.size
    }
}