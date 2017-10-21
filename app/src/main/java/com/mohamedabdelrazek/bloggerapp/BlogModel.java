package com.mohamedabdelrazek.bloggerapp;

/**
 * Created by Mohamed on 10/21/2017.
 */

public class BlogModel {

    private String PostTitle;
    private String PostDesc;
    private String ImageURi;


    public BlogModel() {
    }

    public BlogModel(String postTitle, String postDesc, String imageURi) {
        PostTitle = postTitle;
        PostDesc = postDesc;
        ImageURi = imageURi;
    }

    public String getPostTitle() {
        return PostTitle;
    }

    public void setPostTitle(String postTitle) {
        PostTitle = postTitle;
    }

    public String getPostDesc() {
        return PostDesc;
    }

    public void setPostDesc(String postDesc) {
        PostDesc = postDesc;
    }

    public String getImageURi() {
        return ImageURi;
    }

    public void setImageURi(String imageURi) {
        ImageURi = imageURi;
    }
}
