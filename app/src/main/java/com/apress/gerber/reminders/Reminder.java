package com.apress.gerber.reminders;
/**
 * Created by Lautaro on 07/02/2017.
 */

public class Reminder {
   //attribute
      private int mId;
      private String mContent;
      private float mImportant;

   //constructor
    public Reminder(int id, String content, float important) {
        mId = id;
        mImportant = important;
        mContent = content;    }

    //methods setters and getters
    public int getId(){
        return mId;
    }

    public void setId(int id){
        mId = id;
    }

    public float getImportant(){
        return mImportant;
    }

    public void setImportant(float important){
        mImportant = important;
    }

    public String getContent(){
        return mContent;
    }

    public void setContent(String content){
        mContent = content;
    }

}
