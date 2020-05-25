package eu.emkaware.zadanko.viewmodel.repository;

import android.app.Application;
import android.location.Location;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import eu.emkaware.zadanko.db.AppDatabase;
import eu.emkaware.zadanko.db.dao.AddressDao;
import eu.emkaware.zadanko.network.ApiInterface;
import eu.emkaware.zadanko.network.Network;
import eu.emkaware.zadanko.network.pojo.Address;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressRepository {
    private AddressDao addressDao;
    private ApiInterface apiInterface = Network.getClient();
    private MutableLiveData<List<Address>> addresses;
    private MutableLiveData<Address> firstAddress;
    private MutableLiveData<Address> secondAddress;

    private Handler handler = new Handler();
    private volatile String searchQuery;
    private Runnable fetchAddresses = new Runnable() {
        @Override
        public void run() {
            apiInterface.getSearchResults(searchQuery).enqueue(new Callback<List<Address>>() {
                @Override
                public void onResponse(@NonNull Call<List<Address>> call, @NonNull Response<List<Address>> response) {
                    addresses.setValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<List<Address>> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    };

    public AddressRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        addressDao = db.addressDao();
    }

    public LiveData<List<Address>> getAddresses() {
        if (addresses == null) {
            addresses = new MutableLiveData<>();
            getHistoryAddresses();
        }
        return addresses;
    }

    private void getHistoryAddresses() {
        AppDatabase.databaseExecutor.execute(() -> addresses.postValue(addressDao.getHistoryAddresses()));
    }

    public LiveData<Address> getFirstAddress() {
        if (firstAddress == null) {
            firstAddress = new MutableLiveData<>();
        }
        return firstAddress;
    }

    public void setFirstAddress(Address selectedAddress) {
        firstAddress.setValue(selectedAddress);
    }

    public LiveData<Address> getSecondAddress() {
        if (secondAddress == null) {
            secondAddress = new MutableLiveData<>();
        }
        return secondAddress;
    }

    public void setSecondAddress(Address selectedAddress) {
        secondAddress.setValue(selectedAddress);
    }

    public boolean loadAddresses(String query) {
        boolean isNewQuery = !query.equals(searchQuery);
        if (query.isEmpty()) {
            handler.removeCallbacks(fetchAddresses);
            searchQuery = query;
            getHistoryAddresses();
            return false;
        } else if (isNewQuery) {
            handler.removeCallbacks(fetchAddresses);
            searchQuery = query;
            handler.postDelayed(fetchAddresses, 1500);
        }
        return isNewQuery;
    }

    public float calculateDistance() {
        float[] results = new float[1];
        Address firstValue = getFirstAddress().getValue();
        Address secondValue = getSecondAddress().getValue();
        if (firstValue != null && firstValue.hasValidData() && secondValue != null && secondValue.hasValidData()) {
            Location.distanceBetween(firstValue.getLat(), firstValue.getLng(), secondValue.getLat(), secondValue.getLng(), results);
            return results[0];
        } else {
            return -1f;
        }
    }

    public void insertAndDropOldEntries(Address address) {
        AppDatabase.databaseExecutor.execute(() -> {
            address.setId(addressDao.getLocalAddressId(address.getDisplayName()));
            addressDao.insertAddress(address);
            for (Address oldAddress : addressDao.getOldestAddresses()) {
                addressDao.deleteAddress(oldAddress);
            }
        });
    }
}
