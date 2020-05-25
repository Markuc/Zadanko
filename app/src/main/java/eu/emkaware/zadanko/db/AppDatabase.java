package eu.emkaware.zadanko.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.emkaware.zadanko.db.dao.AddressDao;
import eu.emkaware.zadanko.network.pojo.Address;

@Database(entities = {Address.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "appdb").build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract AddressDao addressDao();
}
