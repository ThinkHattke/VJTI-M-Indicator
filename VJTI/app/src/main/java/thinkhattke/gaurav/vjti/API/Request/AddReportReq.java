package thinkhattke.gaurav.vjti.API.Request;

import com.google.gson.annotations.SerializedName;

public class AddReportReq {

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

    @SerializedName("token")
    private String token;

    public AddReportReq(String name, String phoneNo, String query, String imageUrl, String placeName, String lat, String lon, String token) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.query = query;
        this.imageUrl = imageUrl;
        this.placeName = placeName;
        this.lat = lat;
        this.lon = lon;
        this.token = token;
    }

}

