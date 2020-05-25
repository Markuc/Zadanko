package eu.emkaware.zadanko.viewmodel.repository;

import android.app.Application;
import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import eu.emkaware.zadanko.network.pojo.Address;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class AddressRepositoryTest {
    private Application app = ApplicationProvider.getApplicationContext();
    private AddressRepository repository = new AddressRepository(app);

    @Test
    public void testCalculateDistance() {
        Assert.assertEquals(-1, repository.calculateDistance(), 0);

        Address firstAddress = repository.getFirstAddress().getValue();
        Assert.assertNull(firstAddress);

        firstAddress = new Address();
        firstAddress.setLat(0);
        repository.setFirstAddress(firstAddress);
        Assert.assertEquals(-1, repository.calculateDistance(), 0);

        firstAddress.setLng(1);
        repository.setFirstAddress(firstAddress);
        Assert.assertEquals(-1, repository.calculateDistance(), 0);

        Address secondAddress = repository.getSecondAddress().getValue();
        Assert.assertNull(secondAddress);

        secondAddress = new Address();
        secondAddress.setLat(1);
        Assert.assertEquals(0, secondAddress.getLng(), 0);

        repository.setSecondAddress(secondAddress);
        Assert.assertEquals(156899, repository.calculateDistance(), 1);
    }
}