package com.example.listviewpersonas.Modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.listviewpersonas.Controlador.Libros;

import java.util.ArrayList;

public class ClaseBD extends SQLiteOpenHelper {
    public ClaseBD(@Nullable Context context) {
        super(context, ConstantesBD.BD_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ConstantesBD.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + ConstantesBD.TABLE_NAME);
        onCreate(db);
    }

    public long insertarLibros(String titulo, String autor, String resumen, String comentario){
        SQLiteDatabase db = getWritableDatabase();

        String time = "" + System.currentTimeMillis();

        ContentValues valores = new ContentValues();
        valores.put(ConstantesBD.C_TITULO, titulo);
        valores.put(ConstantesBD.C_AUTOR, autor);
        valores.put(ConstantesBD.C_RESUMEN, resumen);
        valores.put(ConstantesBD.C_COMENTARIO, comentario);
        valores.put(ConstantesBD.C_ADDEDTIME, time);
        long id = db.insert(ConstantesBD.TABLE_NAME, null, valores);

        return id;
    }


    //Devolver todas las personas registradas en la app
    public ArrayList<Libros> mostrarLibros(String cAddedtime){
        ArrayList<Libros> libros;
        libros = new ArrayList<>();

        String query = " SELECT * FROM " + ConstantesBD.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{

                Libros lib = new Libros(
                        cursor.getInt(cursor.getColumnIndex(ConstantesBD.C_ID)) + "",
                        cursor.getString(cursor.getColumnIndex(ConstantesBD.C_TITULO)),
                        cursor.getString(cursor.getColumnIndex(ConstantesBD.C_AUTOR)),
                        cursor.getString(cursor.getColumnIndex(ConstantesBD.C_RESUMEN)),
                        cursor.getString(cursor.getColumnIndex(ConstantesBD.C_COMENTARIO))
                );
                libros.add(lib);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return libros;

    }

    public Libros getLibro(String id){
        Libros l = null;
        String query = " SELECT * FROM " + ConstantesBD.TABLE_NAME + " WHERE " + ConstantesBD.C_ID + "=\"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{

                l = new Libros(
                        cursor.getInt(cursor.getColumnIndex(ConstantesBD.C_ID)) + "",
                        cursor.getString(cursor.getColumnIndex(ConstantesBD.C_TITULO)),
                        cursor.getString(cursor.getColumnIndex(ConstantesBD.C_AUTOR)),
                        cursor.getString(cursor.getColumnIndex(ConstantesBD.C_RESUMEN)),
                        cursor.getString(cursor.getColumnIndex(ConstantesBD.C_COMENTARIO))
                );

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return l;
    }

    public void eliminarPersonaById(String id){

        SQLiteDatabase db = getWritableDatabase();
        db.delete(ConstantesBD.TABLE_NAME, ConstantesBD.C_ID + " = ? ", new String[]{id});

        db.close();

    }

    public void actualizarLibros(String id, String titulo, String autor, String resumen, String comentario) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        //valores.put(ConstantesBD.C_PHOTO, foto);
        valores.put(ConstantesBD.C_TITULO, titulo);
        valores.put(ConstantesBD.C_AUTOR, autor);
        valores.put(ConstantesBD.C_RESUMEN, resumen);
        valores.put(ConstantesBD.C_COMENTARIO, comentario);


        db.update(ConstantesBD.TABLE_NAME,valores,""+ConstantesBD.C_ID+"="+id,null);
        db.close();

    }
}
