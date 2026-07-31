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
    var onFavClick: ((meal: Meal) -> Unit)? = null

    private var favoriteIds = emptySet<String>()


    inner class SearchViewHolder(val binding: ItemFavoritesBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(meal: Meal){

            if(favoriteIds.contains(meal.idMeal)){
                binding.ibHeart.frame = 50
            }
            else{
                binding.ibHeart.frame = 0
            }

            binding.tvFoodName.text = meal.strMeal
            binding.tvCountry.text = meal.strCountry
            binding.tvCategory.text = meal.strCategory
            Glide.with(binding.root).load(meal.strMealThumb).into(binding.ivFoodPhoto)

            binding.root.setOnClickListener {
                onItemClick?.invoke(meal.idMeal)
            }

            binding.ibHeart.setOnClickListener {

                val isFav = favoriteIds.contains(meal.idMeal)

                if (isFav) {
                    binding.ibHeart.setMinAndMaxFrame(50,71)
                } else {
                    binding.ibHeart.setMinAndMaxFrame(18,49)
                }

                binding.ibHeart.playAnimation()
                onFavClick?.invoke(meal)
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

    fun updateFavorites(favIds: Set<String>){
        favoriteIds = favIds
        notifyDataSetChanged()
    }
}