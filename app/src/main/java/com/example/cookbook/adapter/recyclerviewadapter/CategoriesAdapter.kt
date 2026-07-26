package com.example.cookbook.adapter.recyclerviewadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookbook.model.Category
import com.example.cookbook.databinding.ItemCategoriesBinding

class CategoriesAdapter(private val categoryList: MutableList<Category>): RecyclerView.Adapter< CategoriesAdapter.CategoriesViewHolder>() {

    var onItemClick : ((id: String)-> Unit )? = null
    inner class CategoriesViewHolder(val binding: ItemCategoriesBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(category : Category){
            binding.tvCategory.text = category.strCategory
            binding.chipInfo.text = category.idCategory
            Glide.with(binding.root).load(category.strCategoryThumb).into(binding.ivFoodPhoto)

            binding.root.setOnClickListener {
                onItemClick?.invoke(category.idCategory)
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