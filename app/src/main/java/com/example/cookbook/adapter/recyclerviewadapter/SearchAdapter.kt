package com.example.cookbook.adapter.recyclerviewadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookbook.databinding.ItemFavoritesBinding
import com.example.cookbook.model.Meal
import com.example.cookbook.model.MealResponse

class SearchAdapter(private var meals: MutableList<Meal>): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    var onItemClick: ((id: String) -> Unit)? = null
    inner class SearchViewHolder(val binding: ItemFavoritesBinding): RecyclerView.ViewHolder(binding.root){
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
    ): SearchAdapter.SearchViewHolder {
        val binding = ItemFavoritesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchAdapter.SearchViewHolder, position: Int) {
        holder.bind(meals[position])
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    fun updateList(newList: List<Meal>){
        meals.clear()
        meals.addAll(newList)
        notifyDataSetChanged()
    }
}