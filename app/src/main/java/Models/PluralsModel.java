package Models;

import com.google.gson.annotations.SerializedName;

public class PluralsModel {
    private int id;

    private String singular;

    private String plural;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSingular() {
        return singular;
    }

    public void setSingular(String singular) {
        this.singular = singular;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }
    @SerializedName("body")
    private String text;
}
