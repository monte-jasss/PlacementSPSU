package in.ac.spsu.placement.placementspsu.Model;

import com.google.gson.annotations.SerializedName;

public class CandidateData {
    @SerializedName("id")
    private int id;

    @SerializedName("student_id")
    private int std_id;

    @SerializedName("name")
    private String name;

    @SerializedName("f_name")
    private String fName;

    @SerializedName("status")
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStd_id() {
        return std_id;
    }

    public void setStd_id(int std_id) {
        this.std_id = std_id;
    }

}
