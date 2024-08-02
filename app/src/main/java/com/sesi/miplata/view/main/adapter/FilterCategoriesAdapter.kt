package com.sesi.miplata.view.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sesi.miplata.R
import com.sesi.miplata.data.dto.CategoryDayDto
import com.sesi.miplata.databinding.ItemRvCategoriaFilterBinding
import com.sesi.miplata.view.fragment.bmensual.detalle.tab.CategoryAction

class FilterCategoriesAdapter(private val categories:  List<CategoryDayDto>, private val action: CategoryAction) :
    RecyclerView.Adapter<FilterCategoriesAdapter.FilterCategoriesViewHolder>() {
    inner class FilterCategoriesViewHolder(val binding: ItemRvCategoriaFilterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterCategoriesViewHolder {
        return FilterCategoriesViewHolder(
            ItemRvCategoriaFilterBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: FilterCategoriesViewHolder, position: Int) {
        with(holder) {
            val category = categories[position]
            binding.tvCategory.text = category.categoryName
            if (category.isSelected){
                binding.cardView.strokeColor = binding.root.context.getColor(R.color.primaryColor)
                binding.tvCategory.setTextColor(binding.root.context.getColor(R.color.primaryColor))
            } else {
                binding.cardView.strokeColor = binding.root.context.getColor(R.color.transparent)
                binding.tvCategory.setTextColor(binding.root.context.getColor(R.color.primaryColor))
            }
            binding.root.setOnClickListener {
                if (category.isSelected) {
                    deselectAllCategories()
                    action.onDeselectCategory()
                } else {
                    deselectAllCategories()
                    category.isSelected = true
                    action.onClickCategory(category)
                }

            }
        }
    }

    private fun deselectAllCategories() {
        categories.forEach { it.isSelected = false }
        notifyDataSetChanged()
    }
}