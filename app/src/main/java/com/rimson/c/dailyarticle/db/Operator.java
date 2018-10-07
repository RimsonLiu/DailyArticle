package com.rimson.c.dailyarticle.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.rimson.c.dailyarticle.bean.Collection;

import java.util.ArrayList;

public class Operator {
    private OpenHelper dbHelper;
    private SQLiteDatabase database;

    public Operator(Context context){
        dbHelper=new OpenHelper(context,"Collection.db",null,2);
        database=dbHelper.getWritableDatabase();
        //database.execSQL("delete from collection");
        //context.deleteDatabase("Collection.db");
    }

    //添加
    public void add(Collection collection){
        database.execSQL("insert into collection values(?,?,?,?,?)",
                new Object[]{collection.type,collection.title,collection.author,collection.content,collection.url});
    }

    //删除
    public void delete(Collection collection){
        database.execSQL("delete from collection where type=? and title=? and author=?",
                new String[]{collection.type,collection.title,collection.author});
    }

    //查询单个
    public Collection queryOne(Collection collection){
        Collection result = null;
        Cursor cursor=database.rawQuery("select * from collection where type=? and title=? and author=?",
                new String[]{collection.type,collection.title,collection.author});
        while (cursor.moveToNext()){
            result.type=cursor.getString(0);
            result.title=cursor.getString(1);
            result.author=cursor.getString(2);
        }
        cursor.close();
        return result;
    }

    //查询所有
    public ArrayList<Collection> queryAll(){
        ArrayList<Collection> collections=new ArrayList<Collection>();
        Cursor cursor=database.rawQuery("select * from collection",null);
        while (cursor.moveToNext()){
            Collection collection=new Collection(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));
            collections.add(collection);
        }
        cursor.close();
        return collections;
    }

    //检查是否已经存在
    public boolean dataExists(Collection collection){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        String query="Select * from collection where type=? and title=? and author=?";
        Cursor cursor=db.rawQuery(query,new String[]{collection.type,collection.title,collection.author});
        if (cursor.getCount()>0){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

}
