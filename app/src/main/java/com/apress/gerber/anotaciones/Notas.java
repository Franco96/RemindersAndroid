package com.apress.gerber.anotaciones;

public class Notas {
   //attribute
      private int mId;
      private String mContent;
      private float mImportant;

   //constructor
  public Notas(int id, String content, float important)
    {
        mId = id;
        mImportant = important;
        mContent = content;
    }

    //methods setters and getters
    public float getImportant()
    {
        return mImportant;
    }

    public int getId()
    {
        return mId;
    }

    public String getContent()
     {
         return mContent;
     }

    public void setId(int id)
    {
        mId = id;
    }

}


