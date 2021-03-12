package com.example.listviewpersonas.Vista;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.listviewpersonas.Controlador.Libros;
import com.example.listviewpersonas.R;

public class SettingsActivity extends AppCompatActivity {

    EditText edt1;
    Button guardar;

    Libros libros;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambio_de_comentario);
        edt1 = (EditText) findViewById(R.id.comment);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Modificar Comentario");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}