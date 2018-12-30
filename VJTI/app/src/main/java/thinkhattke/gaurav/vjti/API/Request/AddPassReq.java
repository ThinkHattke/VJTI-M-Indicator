package thinkhattke.gaurav.vjti.API.Request;

import com.google.gson.annotations.SerializedName;

public class AddPassReq {


    @SerializedName("userId")
    private String userId;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("startLoc")
    private String startLoc;

    @SerializedName("endLoc")
    private String endLoc;

    @SerializedName("goTrain")
    private String goTrain;

    @SerializedName("backTrain")
    private String backTrain;


    public AddPassReq(String userId, String startDate, String endDate, String startLoc, String endLoc, String goTrain, String backTrain) {
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startLoc = startLoc;
        this.endLoc = endLoc;
        this.goTrain = goTrain;
        this.backTrain = backTrain;
    }
}
