package com.example.abdelkerim.kvik;

/**
 * Created by abdelkerim on 2/28/2018.
 */

public class Events {
    String title;
    String description;
    String event;


    public Events(){}

    public Events(String title, String description, String event ){
        this.title = title;
        this.description = description;
        this.event=event;

    }


    public String getTitle() {
        return title;
    }


    public String getDesccription() {
        return description;
    }
    public String getEvent(){
        return event;
    }
}
