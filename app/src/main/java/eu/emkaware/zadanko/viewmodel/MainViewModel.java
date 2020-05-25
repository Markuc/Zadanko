package eu.emkaware.zadanko.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import eu.emkaware.zadanko.network.pojo.Address;
import eu.emkaware.zadanko.viewmodel.repository.AddressRepository;

public class MainViewModel extends AndroidViewModel {
    private AddressRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new AddressRepository(application);
    }

    public LiveData<List<Address>> getAddresses() {
        return repository.getAddresses();
    }

    public LiveData<Address> getFirstAddress() {
        return repository.getFirstAddress();
    }

    public void setFirstAddress(Address selectedAddress) {
        repository.setFirstAddress(selectedAddress);
    }

    public LiveData<Address> getSecondAddress() {
        return repository.getSecondAddress();
    }

    public void setSecondAddress(Address selectedAddress) {
        repository.setSecondAddress(selectedAddress);
    }

    public boolean loadAddresses(String query) {
        return repository.loadAddresses(query);
    }

    public float calculateDistance() {
        return repository.calculateDistance();
    }

    public void addAddressToHistory(Address address) {
        repository.insertAndDropOldEntries(address);
    }
}
