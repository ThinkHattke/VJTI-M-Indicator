package thinkhattke.gaurav.vjti.API.Reponse;

import com.google.gson.annotations.SerializedName;

public class NewReportRes {


    @SerializedName("status")
    private String status;

    @SerializedName("name")
    private String name;

    @SerializedName("phoneNo")
    private String phoneNo;

    @SerializedName("query")
    private String query;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("placeName")
    private String placeName;

    @SerializedName("lat")
    private String lat;

    @SerializedName("lon")
    private String lon;


    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getQuery() {
        return query;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
