package in.ac.spsu.placement.placementspsu.Model;

import com.google.gson.annotations.SerializedName;

public class RequestData {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("title")
    private String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
