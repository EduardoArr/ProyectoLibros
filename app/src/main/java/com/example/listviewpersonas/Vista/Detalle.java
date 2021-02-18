package com.example.listviewpersonas.Vista;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.listviewpersonas.Modelo.ClaseBD;
import com.example.listviewpersonas.Controlador.Libros;
import com.example.listviewpersonas.R;

public class Detalle extends AppCompatActivity {
    ImageView portada;
    TextView titulo;
    TextView autor;
    TextView resumen;
    TextView comentario;

    ActionBar actionBar;
    ClaseBD claseBD;

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

        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");

        claseBD = new ClaseBD(this);

        portada = (ImageView) findViewById(R.id.Portada);
        titulo = (TextView) findViewById(R.id.titulo);
        autor = (TextView) findViewById(R.id.autor);
        resumen = (TextView) findViewById(R.id.resumen);
        comentario = (TextView) findViewById(R.id.comentario);

        mostrarDetalle(id);

    }

    public void mostrarDetalle(String id){
        libros = claseBD.getLibro(id);

        tit = libros.getTitulo();
        au = libros.getAutor();
        res = libros.getResumen();
        come = libros.getComentario();


        titulo.setText(tit);
        autor.setText(au);
        resumen.setText(res);
        comentario.setText(come);
        portada.setImageResource(R.mipmap.piedrafilosofal);
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