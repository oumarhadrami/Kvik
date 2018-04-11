package com.example.abdelkerim.kvik;

/**
 * Created by abdelkerim on 1/25/2018.
 */

public class Course {
    String title;
    String description;
    /*int Image;
    int Fees;
    */

    public Course(){}

    public Course(String title, String description /*, int Image, int Fees*/){
        this.title = title;
        this.description = description;
        //this.Image = Image;
        //this.Fees=Fees;
    }


    public String getTitle() {
        return title;
    }


    public String getDesccription() {
        return description;
    }
}
