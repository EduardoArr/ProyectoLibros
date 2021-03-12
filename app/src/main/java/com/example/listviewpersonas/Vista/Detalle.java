package com.example.listviewpersonas.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.listviewpersonas.Controlador.Libros;
import com.example.listviewpersonas.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Detalle extends AppCompatActivity {
    ImageView portada;
    TextView titulo;
    TextView autor;
    TextView resumen;
    TextView comentario;

    private DatabaseReference mDatabase;
    ActionBar actionBar;


    Libros libros;

    String id, tit, au, res, come, por;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);


        actionBar = getSupportActionBar();
        actionBar.setTitle("Detalle Libro");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");
        Log.e("hol", "" + id);



        portada = (ImageView) findViewById(R.id.Portada);
        titulo = (TextView) findViewById(R.id.titulo);
        autor = (TextView) findViewById(R.id.autor);
        resumen = (TextView) findViewById(R.id.resumen);
        comentario = (TextView) findViewById(R.id.comentario);

        mostrarDetalle(id);

    }

    public void mostrarDetalle(final String id){

        mDatabase.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Libros lib = snap.getValue(Libros.class);
                    String ID = snap.getKey();

                    if(id.equals(ID)){
                        String tit = lib.getTitulo();
                        String aut = lib.getAutor();
                        String resu = lib.getResumen();
                        String comen = lib.getComentario();

                        titulo.setText(tit);
                        autor.setText(aut);
                        resumen.setText(resu);
                        comentario.setText(comen);
                        portada.setImageResource(R.mipmap.piedrafilosofal);
                    }

                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cambioComent) {
            lanzarPreferencias(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void lanzarPreferencias(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}