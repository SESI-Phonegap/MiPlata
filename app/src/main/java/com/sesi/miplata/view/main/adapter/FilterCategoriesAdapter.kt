package com.sesi.miplata.view.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sesi.miplata.databinding.ItemRvCategoriaFilterBinding
import com.sesi.miplata.model.OperacionesModel

class FilterCategoriesAdapter(private val categories:  List<OperacionesModel>) :
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
            binding.tvCategory.text = categories[position].catNombre
        }
    }
}