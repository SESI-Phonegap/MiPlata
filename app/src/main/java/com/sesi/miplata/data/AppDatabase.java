package com.sesi.miplata.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.sesi.miplata.data.converter.Converters;
import com.sesi.miplata.data.dao.CategoriasDao;
import com.sesi.miplata.data.dao.GastosRecurrentesDao;
import com.sesi.miplata.data.dao.IngresosRecurrentesDao;
import com.sesi.miplata.data.dao.OperacionesDao;
import com.sesi.miplata.data.entity.Categorias;
import com.sesi.miplata.data.entity.GastosRecurrentes;
import com.sesi.miplata.data.entity.GastosRecurrentesV2;
import com.sesi.miplata.data.entity.IngresosRecurrentes;
import com.sesi.miplata.data.entity.Operaciones;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Categorias.class, GastosRecurrentes.class, IngresosRecurrentes.class, Operaciones.class, GastosRecurrentesV2.class},
        version = 2
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
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
                            .addMigrations(MIGRATION_1_2)
                            .createFromAsset("miplata.db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

   /* static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE GASTOS_RECURRENTES ADD COLUMN gr_dia_pago INTEGER");
        }
    };*/

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("INSERT INTO GASTOS_RECURRENTES_2 (gr_id, gr_nombre, gr_nota, gr_monto, gr_id_categoria) SELECT gr_id, gr_nombre, gr_nota, gr_monto, gr_id_categoria FROM GASTOS_RECURRENTES");
            database.execSQL("DROP TABLE GASTOS_RECURRENTES");
            //database.execSQL("ALTER TABLE GASTOS_RECURRENTES ADD COLUMN gr_dia_pago INTEGER");
        }
    };

}
