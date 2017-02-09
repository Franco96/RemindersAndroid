package com.apress.gerber.reminders;

/**
 * Created by Lautaro on 07/02/2017.
 */

public class Reminder {
  //attribute
    private int mId;
    private String mContent;
    private int mImportant;

   //constructor
    public Reminder(int id, String content, int important) {
        mId = id;
        mImportant = important;
        mContent = content;    }

    //methods setters and getters
    public int getId(){return mId;}
    public void setId(int id){mId = id;}

    public int getImportant(){return mImportant;}
    public void setImportant(int important){mImportant = important;}

    public String getContent(){return mContent;}
    public void setContent(String content){mContent = content;}

}
