package com.df.dianping;

/**
 * Created by asus88 on 2016/6/11.
 */
public class CategoryInfo {
    private String name;
    private int imageId;
    private int catId;

    public CategoryInfo(String name, int imageId, int catId) {
        this.name = name;
        this.imageId = imageId;
        this.catId = catId;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
