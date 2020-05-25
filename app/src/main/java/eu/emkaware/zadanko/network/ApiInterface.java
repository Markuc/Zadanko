package eu.emkaware.zadanko.network;

import java.util.List;

import eu.emkaware.zadanko.network.pojo.Address;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("search.php?format=json")
    Call<List<Address>> getSearchResults(@Query("q") String query);
}
