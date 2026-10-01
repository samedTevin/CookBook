package com.example.cookbook.adapter.recyclerviewadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookbook.R
import com.example.cookbook.databinding.ItemFavoritesBinding
import com.example.cookbook.model.FavoriteMeal

class FavoritesAdapter(val favoriteList: MutableList<FavoriteMeal>): RecyclerView.Adapter<FavoritesAdapter.ViewHolder>(){

    // Callbacks
    var onItemClick: ((id : String) -> Unit)? = null

    var onFavClick: ((meal: FavoriteMeal) -> Unit)? = null

    inner class ViewHolder(val binding: ItemFavoritesBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(meal: FavoriteMeal){
            binding.ibHeart.frame = 50
            binding.tvFoodName.text = meal.strMeal
            binding.tvCountry.text = meal.strCountry
            binding.tvCategory.text = meal.strCategory
            Glide.with(binding.root)
                .load(meal.strMealThumb)
                .placeholder(R.drawable.bg_skeleton)
                .error(R.drawable.ic_error_image)
                .centerCrop()
                .into(binding.ivFoodPhoto)

            binding.root.setOnClickListener {
                onItemClick?.invoke(meal.idMeal)
            }

            binding.ibHeart.setOnClickListener {
                binding.ibHeart.setMinAndMaxFrame(50,71)
                binding.ibHeart.playAnimation()
                onFavClick?.invoke(meal)
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
        holder.bind(favoriteList[position])

    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    fun updateList(newList: List<FavoriteMeal>) {
        favoriteList.clear()
        favoriteList.addAll(newList)
        notifyDataSetChanged()
    }

}