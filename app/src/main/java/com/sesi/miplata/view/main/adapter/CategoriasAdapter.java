package com.sesi.miplata.view.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sesi.miplata.R;
import com.sesi.miplata.data.entity.Categorias;
import java.util.Collections;
import java.util.List;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.CategoriasViewHolder> {

    private List<Categorias> categorias;
    private ItemClickListener itemClickListener;

    public CategoriasAdapter() {
        categorias = Collections.emptyList();
    }

    @NonNull
    @Override
    public CategoriasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent, false);
        return new CategoriasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriasViewHolder holder, int position) {
        Categorias categoria = categorias.get(position);
        holder.categorias = categoria;
        holder.tvName.setText(categoria.getNombre());
        holder.view.setOnClickListener( v -> {
            if (null != itemClickListener) {
                itemClickListener.onItemClick(categoria);
            }
        });
    }

    public void setCategorias(List<Categorias> categorias) {
        this.categorias = categorias;
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        void onItemClick(Categorias categoria);
    }

    public static class CategoriasViewHolder extends RecyclerView.ViewHolder {

        final TextView tvName;
        Categorias categorias;
        final View view;

        public CategoriasViewHolder(@NonNull View view) {
            super(view);
            this.tvName = view.findViewById(R.id.tv_item);
            this.view = view;

        }
    }
}
