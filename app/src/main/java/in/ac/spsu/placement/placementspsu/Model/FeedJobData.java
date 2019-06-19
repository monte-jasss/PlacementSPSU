package in.ac.spsu.placement.placementspsu.Model;

import com.google.gson.annotations.SerializedName;

public class FeedJobData {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("requirement")
    private String requirement;

    @SerializedName("opening")
    private String opening;

    @SerializedName("lastdate")
    private String lastdate;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public String getLastdate() {
        return lastdate;
    }

    public void setLastdate(String lastdate) {
        this.lastdate = lastdate;
    }
}
