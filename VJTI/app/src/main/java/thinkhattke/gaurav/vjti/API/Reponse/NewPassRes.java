package thinkhattke.gaurav.vjti.API.Reponse;

import com.google.gson.annotations.SerializedName;

public class NewPassRes {


    @SerializedName("status")
    private String status;

    @SerializedName("id")
    private String id;


    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }
}
