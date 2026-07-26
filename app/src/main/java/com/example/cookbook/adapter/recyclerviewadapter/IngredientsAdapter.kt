package com.example.cookbook.adapter.recyclerviewadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookbook.databinding.ItemIngredientsBinding
import com.example.cookbook.model.Ingredient

class IngredientsAdapter(private val ingredients: MutableList<Ingredient>): RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    class IngredientsViewHolder(val binding: ItemIngredientsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(ingredient: Ingredient){
            Glide.with(binding.root).load(ingredient.imageUrl).into(binding.ivIngredient)
            binding.tvIngredient.text = ingredient.ingredient
            binding.tvMeasure.text = ingredient.measure
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IngredientsAdapter.IngredientsViewHolder {
        val binding = ItemIngredientsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return IngredientsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: IngredientsAdapter.IngredientsViewHolder,
        position: Int
    ) {
        holder.bind(ingredients[position])
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun updateList(newList: List<Ingredient>){
        ingredients.clear()
        ingredients.addAll(newList)
        notifyDataSetChanged()
    }
}