package eu.emkaware.zadanko.network.pojo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Address {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private double lat;
    @SerializedName("lon")
    private double lng;
    @SerializedName("display_name")
    @ColumnInfo(name = "display_name")
    private String displayName;
    private long timestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean hasValidData() {
        return !(Double.isNaN(lat) && Double.isNaN(lng));
    }
}
