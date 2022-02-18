package com.sesi.miplata.view.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sesi.miplata.R;
import com.sesi.miplata.model.OperacionesModel;
import com.sesi.miplata.util.Utils;

import java.util.Collections;
import java.util.List;

public class OperacionesAdapter extends RecyclerView.Adapter<OperacionesAdapter.OperacionesViewHolder> {

    private List<OperacionesModel> gastos;
    private ItemClickListener itemClickListener;

    public OperacionesAdapter(){
        gastos = Collections.emptyList();
    }

    @NonNull
    @Override
    public OperacionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_operacion,parent,false);
        return new OperacionesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OperacionesViewHolder holder, int position) {
        OperacionesModel gasto = gastos.get(position);
        holder.gasto = gasto;
        holder.tvName.setText(gasto.getName());
        holder.tvNota.setText(gasto.getNota());
        holder.tvMonto.setText(Utils.getCurrencyFormatter(gasto.getMonto()));
        //holder.imgIcon.setImageResource(gasto.getIcono());
        holder.tvFecha.setText(gasto.getFecha());
        holder.view.setOnClickListener(v -> {
            if (null != itemClickListener){
                itemClickListener.onItemClick(gasto);
            }
        });
    }

    public void setOperaciones(List<OperacionesModel> gastos) {
        this.gastos = gastos;
    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        void onItemClick(OperacionesModel gasto);
    }

    public static class OperacionesViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        ImageView imgIcon;
        TextView tvNota;
        TextView tvFecha;
        TextView tvMonto;
        OperacionesModel gasto;
        View view;

        public OperacionesViewHolder(View view){
            super(view);
            this.tvName = view.findViewById(R.id.tv_nombre);
            this.tvNota = view.findViewById(R.id.tv_nota);
            this.tvFecha = view.findViewById(R.id.tv_fecha);
            this.tvMonto = view.findViewById(R.id.tv_monto);
            this.imgIcon = view.findViewById(R.id.img_icon);
            this.view = view;
        }
    }
}
