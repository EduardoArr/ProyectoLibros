package com.example.listviewpersonas.Modelo;

public class ConstantesBD {
    public static final String BD_NAME = "LIBROS";

    public static final int BD_VERSION = 1;

    public static final String TABLE_NAME = "LIBRO";

    public static final String C_ID = "ID";
    public static final String C_TITULO = "TITULO";
    public static final String C_AUTOR = "AUTOR";
    public static final String C_RESUMEN = "RESUMEN";
    public static final String C_COMENTARIO = "COMENTARIO";
    public static final String C_ADDEDTIME = "FECHA_ALTA";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_TITULO + " TEXT, "
            + C_AUTOR + " TEXT, "
            + C_RESUMEN + " TEXT, "
            + C_COMENTARIO + " TEXT, "
            + C_ADDEDTIME + " TEXT"
            + ")";
}
