package com.example.cookbook.adapter.recyclerviewadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookbook.R
import com.example.cookbook.model.Category
import com.example.cookbook.databinding.ItemCategoriesBinding

class CategoriesAdapter(private val categoryList: MutableList<Category>): RecyclerView.Adapter< CategoriesAdapter.CategoriesViewHolder>() {

    var onItemClick : ((id: String)-> Unit )? = null
    inner class CategoriesViewHolder(val binding: ItemCategoriesBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(category : Category){
            binding.tvCategory.text = when(category.strCategory){
                "Beef" -> itemView.context.getString(R.string.beef)
                "Breakfast" -> itemView.context.getString(R.string.breakfast)
                "Chicken" -> itemView.context.getString(R.string.chicken)
                "Dessert" -> itemView.context.getString(R.string.dessert)
                "Goat" ->  itemView.context.getString(R.string.goat)
                "Lamb" -> itemView.context.getString(R.string.lamb)
                "Miscellaneous" -> itemView.context.getString(R.string.miscellaneous)
                "Pasta" -> itemView.context.getString(R.string.pasta)
                "Pork" -> itemView.context.getString(R.string.pork)
                "Seafood" -> itemView.context.getString(R.string.seafood)
                "Side" -> itemView.context.getString(R.string.side)
                "Starter" -> itemView.context.getString(R.string.starter)
                "Vegan" -> itemView.context.getString(R.string.vegan)
                "Vegetarian" -> itemView.context.getString(R.string.vegetarian)
                else -> ""
            }
            binding.chipInfo.text = category.idCategory
            Glide.with(binding.root).load(category.strCategoryThumb).placeholder(R.drawable.bg_skeleton).error(R.drawable.ic_error_image).into(binding.ivFoodPhoto)

            binding.root.setOnClickListener {
                onItemClick?.invoke(category.strCategory)
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesAdapter.CategoriesViewHolder {
        val binding = ItemCategoriesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoriesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CategoriesAdapter.CategoriesViewHolder,
        position: Int
    ) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun updateList(newList: List<Category>){
        categoryList.clear()
        categoryList.addAll(newList)
        notifyDataSetChanged()
    }
}