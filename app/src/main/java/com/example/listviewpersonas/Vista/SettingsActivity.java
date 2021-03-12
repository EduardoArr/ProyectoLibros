package com.example.listviewpersonas.Vista;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.listviewpersonas.Controlador.Libros;
import com.example.listviewpersonas.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    EditText edt1;
    FloatingActionButton guardar;

    private DatabaseReference mDatabaseReference;

    ActionBar actionBar;


    String txt_id, txt_comentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambio_de_comentario);

        edt1 = (EditText) findViewById(R.id.comment);
        guardar = findViewById(R.id.guardar);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        Bundle extras = getIntent().getExtras();
        txt_id = extras.getString("ID");
        Log.e("probably not", "" + txt_id);



        actionBar = getSupportActionBar();
        actionBar.setTitle("Modificar Comentario");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarComentario();
                onBackPressed();
            }
        });


    }

    //Con esto podemos actualizar el comentario nuevo del Libro
    private void guardarComentario(){

        txt_comentario = edt1.getText().toString().trim();
        Log.e("probably yes", "" + txt_comentario);
        mDatabaseReference.child("posts").child(txt_id).child("comentario").setValue(txt_comentario);


    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}