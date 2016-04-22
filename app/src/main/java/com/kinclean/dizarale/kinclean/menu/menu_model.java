package com.kinclean.dizarale.kinclean.menu;

import org.parceler.Parcel;

/**
 * Created by dizar on 2/1/2016.
 */

@Parcel
public class menu_model {
    private String title;
    private String title_thai;
    private String thumbnail;
    private String price;
    private String id;
    private String des = "";
    private String num;

    public menu_model(){}
    public String getTitle() {
        return title;
    }
    public String getTitle_thai() {
        return title_thai;
    }
    public void setTitle_thai(String title) {
        this.title_thai = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price){ this.price = price;}
    public void setDes(String des){ this.des = des;}
    public String getDes(){
        return des;
    }

    public String getPrice(){
        return price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public void setid(String id){
        this.id = id;
    }
    public String getid(){
        return id;
    }
    public void setNum(String num){
        this.num = num;
    }
    public String getNum(){
        return this.num;
    }

}
