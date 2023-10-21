package com.sesi.miplata.view.fragment.categorias;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sesi.miplata.R;
import com.sesi.miplata.data.entity.Categorias;
import com.sesi.miplata.databinding.DialogCategoriaBinding;
import com.sesi.miplata.databinding.FragmentCategoriasBinding;
import com.sesi.miplata.view.main.adapter.CategoriasAdapter;

import java.util.List;

public class CategoriasFragment extends Fragment {

    private FragmentCategoriasBinding binding;
    private CategoriasViewModel viewModel;
    private CategoriasAdapter adapterGastos;
    private CategoriasAdapter adapterIngreso;
    private AlertDialog dialog;
    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CategoriasViewModel.CategoriasViewModelFactory factory = new CategoriasViewModel.CategoriasViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(CategoriasViewModel.class);
        binding = FragmentCategoriasBinding.inflate(inflater, container,false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setCategoriasFragment(this);
        loadAds();
        viewModel.getCategoriasGastos().observe(getViewLifecycleOwner(), categorias -> {
            adapterGastos = new CategoriasAdapter();
            adapterGastos.setCategorias(categorias);
            adapterGastos.setItemClickListener(this::showDialogCategory);
            binding.rvGastos.setLayoutManager(new LinearLayoutManager(requireActivity().getApplication()));
            binding.rvGastos.setAdapter(adapterGastos);
        });
        viewModel.getCategoriasIngresos().observe(getViewLifecycleOwner(), new Observer<List<Categorias>>() {
            @Override
            public void onChanged(List<Categorias> categorias) {
                adapterIngreso = new CategoriasAdapter();
                adapterIngreso.setCategorias(categorias);
                adapterIngreso.setItemClickListener(categoria -> showDialogCategory(categoria));
                binding.rvIngresos.setLayoutManager(new LinearLayoutManager(requireActivity().getApplication()));
                binding.rvIngresos.setAdapter(adapterIngreso);
            }
        });
        binding.btnAgregar.setOnClickListener(v -> showDialogCategory(null));
        return binding.getRoot();
    }

    private void loadAds(){
        adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getContext(), getString(R.string.interesticial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
            }
        });
    }

    private void loadInterestecialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                }

                @Override
                public void onAdShowedFullScreenContent() {
                }
            });

            mInterstitialAd.show(requireActivity());
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
            loadAds();
        }
    }

    public void showDialogCategory(Categorias categoria) {
        BottomSheetDialog bsd =new BottomSheetDialog(getContext(), R.style.BottomSheetDialogStyle_2);
        DialogCategoriaBinding bindingCat = DialogCategoriaBinding.inflate(getLayoutInflater());
        bsd.setContentView(bindingCat.getRoot());
        bsd.show();

        if (categoria != null){
            bindingCat.etNombre.setText(categoria.getNombre());
            if (categoria.getTipoCategoria().equals("Gasto")){
                bindingCat.spinnerCategoriass.setSelection(0);
            } else {
                bindingCat.spinnerCategoriass.setSelection(1);
            }
        }

        bindingCat.btnGuardar.setOnClickListener(v -> {
            String tipoCat = bindingCat.spinnerCategoriass.getSelectedItem().toString();
            String nombre = bindingCat.etNombre.getText().toString();
            if (!nombre.isEmpty()) {
                if (categoria != null) {
                    Categorias categoriaUpdate = new Categorias();
                    categoriaUpdate.setId(categoria.getId());
                    categoriaUpdate.setTipoCategoria(tipoCat);
                    categoriaUpdate.setNombre(nombre);
                    categoriaUpdate.setIcono(0);
                    viewModel.updateCategory(categoriaUpdate);
                } else {
                    viewModel.addCategory(nombre, tipoCat);
                }
                loadInterestecialAd();
                bsd.dismiss();
            } else {
                Toast.makeText(getContext(), "El nombre de la categoria es requerido", Toast.LENGTH_LONG).show();
            }
        });

        bindingCat.btnCancelar.setOnClickListener(v -> bsd.dismiss());
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAds();
    }
}