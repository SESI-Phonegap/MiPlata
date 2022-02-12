package com.sesi.miplata.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sesi.miplata.data.converter.Converters;
import com.sesi.miplata.data.dao.CategoriasDao;
import com.sesi.miplata.data.dao.GastosRecurrentesDao;
import com.sesi.miplata.data.dao.IngresosRecurrentesDao;
import com.sesi.miplata.data.dao.OperacionesDao;
import com.sesi.miplata.data.entity.Categorias;
import com.sesi.miplata.data.entity.GastosRecurrentes;
import com.sesi.miplata.data.entity.IngresosRecurrentes;
import com.sesi.miplata.data.entity.Operaciones;
import com.sesi.miplata.data.worker.SeedDataBaseWorker;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Categorias.class, GastosRecurrentes.class, IngresosRecurrentes.class, Operaciones.class},
        version = 1,exportSchema = true)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
    private static final String KEY_FILENAME = "CATEGORIES_LIST";
    private static final String CATEGORIES_DATA_FILENAME = "categories.json";
    private static final String DATA_BASE_NAME = "MiPlata.db";
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract CategoriasDao getCategoriasDao();
    public abstract GastosRecurrentesDao getGastosRecurrentesDao();
    public abstract IngresosRecurrentesDao getIngresosRecurrentesDao();
    public abstract OperacionesDao getOperacionesDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATA_BASE_NAME)
                            .createFromAsset("miplata.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
