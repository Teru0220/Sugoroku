package com.example.sugoroku.menu;

import android.widget.ImageView;

public class MenuData {
    private ImageView imageView;
    private String tableName;
    private int masuTotal;


    public MenuData(String tableName,int masuTotal){
        setTableName(tableName);
        setMasuTotal(masuTotal);
    }

    public ImageView getImageView(){return imageView;}
    public void setImageView(ImageView imageView){this.imageView = imageView;}
    public String getTableName(){ return this.tableName; }
    public void setTableName(String tableName){ this.tableName = tableName; }
    public int getMasuTotal(){return this.masuTotal; }
    public void setMasuTotal(int masuTotal){this.masuTotal = masuTotal; }
}
