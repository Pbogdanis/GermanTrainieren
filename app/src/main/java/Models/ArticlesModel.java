package Models;

import com.google.gson.annotations.SerializedName;

public class ArticlesModel {
    private int id;

    private String article;

    private String singular;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getSingular() {
        return singular;
    }

    public void setSingular(String singular) {
        this.singular = singular;
    }

    @SerializedName("body")
    private String text;


}
