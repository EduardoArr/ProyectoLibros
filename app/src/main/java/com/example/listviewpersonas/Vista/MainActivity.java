package com.example.listviewpersonas.Vista;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.listviewpersonas.Controlador.Adaptador;
import com.example.listviewpersonas.Modelo.ClaseBD;
import com.example.listviewpersonas.Modelo.ConstantesBD;
import com.example.listviewpersonas.R;

public class MainActivity extends AppCompatActivity {
    RecyclerView libro;

    ActionBar actionBar;

    String ordenarPorNombreAsc = ConstantesBD.C_TITULO + "ASC";
    String ordenarPorNombreDesc = ConstantesBD.C_TITULO + "DESC";
    String ordenarPorNuevo = ConstantesBD.C_ADDEDTIME + "Desc";

    String condicionOrdenarActual = ordenarPorNuevo;
    ClaseBD claseBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

       /* try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.AppTheme);*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        libro = findViewById(R.id.listView);
        claseBD = new ClaseBD(this);
        actionBar = getSupportActionBar();
        actionBar.setTitle("LIBROS");

        mostrarLibros(condicionOrdenarActual);

    }

    private void mostrarLibros(String condicionOrdenar){
        condicionOrdenarActual = condicionOrdenar;
        Adaptador adapter = new Adaptador(this, claseBD.mostrarLibros(condicionOrdenar));
        libro.setAdapter(adapter);
    }

    public void onResume(){
        super.onResume();
        mostrarLibros(condicionOrdenarActual);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mainacti, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.crear) {
            lanzarPreferencias(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void lanzarPreferencias(View view) {
        Intent intent = new Intent(MainActivity.this, Add_Libro.class);
        intent.putExtra("REQUEST_EDICION", false);
        startActivity(intent);
    }


}