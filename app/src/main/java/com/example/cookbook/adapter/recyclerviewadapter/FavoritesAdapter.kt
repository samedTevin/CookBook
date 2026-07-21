package com.example.cookbook.adapter.recyclerviewadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookbook.data.Meal
import com.example.cookbook.data.Meals
import com.example.cookbook.databinding.ItemFavoritesBinding

class FavoritesAdapter(val list: Meals): RecyclerView.Adapter<FavoritesAdapter.ViewHolder>(){

    var onItemClick: ((id : String) -> Unit)? = null

    inner class ViewHolder(val binding: ItemFavoritesBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(meal: Meal){
            binding.tvFoodName.text = meal.strMeal
            binding.tvCountry.text = meal.strCountry
            binding.tvCategory.text = meal.strCategory
            Glide.with(binding.root).load(meal.strMealThumb).into(binding.ivFoodPhoto)

            binding.root.setOnClickListener {
                onItemClick?.invoke(meal.idMeal)
            }

        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesAdapter.ViewHolder {
        val binding = ItemFavoritesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: FavoritesAdapter.ViewHolder, position: Int) {
        holder.bind(list.mealList[position])

    }

    override fun getItemCount(): Int {
        return list.mealList.size
    }
}