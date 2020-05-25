package eu.emkaware.zadanko.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import eu.emkaware.zadanko.network.pojo.Address;

@Dao
public interface AddressDao {
    @Query("SELECT * FROM Address ORDER BY timestamp DESC")
    List<Address> getHistoryAddresses();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAddress(Address address);

    @Query("SELECT id FROM Address WHERE display_name = :displayName")
    long getLocalAddressId(String displayName);

    @Query("SELECT * FROM Address WHERE id NOT IN (SELECT id FROM Address ORDER BY timestamp DESC LIMIT 20)")
    List<Address> getOldestAddresses();

    @Delete
    void deleteAddress(Address address);
}
