package example.curserloader.recylerview;

import android.database.Cursor;

/**
 * Created by Developer on 2/27/2017.
 */

public class MyListItem{
    private String name;

    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }

    public static MyListItem fromCursor(Cursor cursor) {
        //TODO return your MyListItem from cursor.
        MyListItem mTemp = new MyListItem();
        mTemp.setName(cursor.getString(cursor.getColumnIndex("name")));

        return mTemp;
    }
}