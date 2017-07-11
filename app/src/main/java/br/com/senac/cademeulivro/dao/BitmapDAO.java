package br.com.senac.cademeulivro.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.senac.cademeulivro.model.BitmapCapa;
import br.com.senac.cademeulivro.util.classes.ImageUtils;

/**
 * Created by Didi on 7/10/2017.
 */

public class BitmapDAO {
    private SQLiteDatabase mDatabaseHelper;

    public BitmapDAO(SQLiteDatabase databaseHelper) {
        mDatabaseHelper = databaseHelper;
    }

    public long insert(BitmapCapa b) {
        return mDatabaseHelper.insert("Capa", null, getContentFrom(b));
    }

    public void update(BitmapCapa b) {
        mDatabaseHelper.update("Capa", getContentFrom(b), "_id = ?", new String[]{b.getIdBitmap().toString()});
    }

    public int delete(Integer id) {
        return mDatabaseHelper.delete("Capa", "_id = ?", new String[]{id.toString()});
    }

    public BitmapCapa getById(Integer id) {
        Cursor cursor = mDatabaseHelper.query("Capa", null, "_id = ?", new String[]{id.toString()}, null, null, null);
        cursor.moveToFirst();
        return getBitmap(cursor);
    }

    private BitmapCapa getBitmap(Cursor cursor) {
        BitmapCapa b = new BitmapCapa();
        b.setCapa(ImageUtils.toBitmap(cursor.getBlob(cursor.getColumnIndex("capa"))));
        b.setIdBitmap(cursor.getInt(cursor.getColumnIndex("_id")));
        return b;
    }

    private ContentValues getContentFrom(BitmapCapa b) {
        ContentValues content = new ContentValues();
        content.put("capa", ImageUtils.toByteArray(b.getCapa()));
        content.put("_id", b.getIdBitmap());

        return content;
    }

}
