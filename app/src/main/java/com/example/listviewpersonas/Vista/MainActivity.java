package com.example.listviewpersonas.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.listviewpersonas.Controlador.Adaptador;
import com.example.listviewpersonas.Controlador.Libros;
import com.example.listviewpersonas.R;
import com.example.listviewpersonas.Registro.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    RecyclerView rv;

    ActionBar actionBar;

    private ArrayList<Libros> libros = new ArrayList<>();
    private Adaptador adapter;
    private DatabaseReference mDatabase;


    String usuarios;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rv = findViewById(R.id.listView);
        actionBar = getSupportActionBar();
        actionBar.setTitle("LIBROS");

        //Recogemos el email
        Bundle extras = getIntent().getExtras();
        usuarios = extras.getString("email");
        Log.e("email", "" + usuarios);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mostrarLibros();

        Log.e("mensaje", "fuunc");

    }

    private void mostrarLibros() {
        //Recorremos los datos de la base de datos, y los metemos dentro del contructor de la clase libros, y luego lo añadimos a un ArrayList, así puede usar los datos el adaptador.
        mDatabase.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                libros.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Libros lib = snap.getValue(Libros.class);

                    String id = snap.getKey();
                    String titulo = lib.getTitulo();
                    String autor = lib.getAutor();
                    String resumen = lib.getResumen();
                    String comentario = lib.getComentario();


                    Log.e("Titulo", "" + titulo);
                    Log.e("id", "" + id);
                    Log.e("autor", "" + autor);
                    Log.e("resumen", "" + resumen);
                    Log.e("comentario", "" + comentario);
                    Log.e("Datos", "" + snapshot.getValue());

                    lib = new Libros(id, titulo, autor, resumen, comentario);
                    libros.add(lib);

                }
                //seteamos el adaptador para mostrar los libros.s
                adapter = new Adaptador(MainActivity.this, libros);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
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
            añadir(null);
            return true;
        }
        if(id == R.id.perfil){
            perfil(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void añadir(View view) {
        Intent intent = new Intent(MainActivity.this, Add_Libro.class);
        intent.putExtra("REQUEST_EDICION", false);
        startActivity(intent);
    }

    //Con este metodo cambiamos de ventana a la de el perfil de usuario y le pasamos el usuario para luego mostrarlo en la otra clase
    public void perfil(View view){
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("correo", usuarios);
        startActivity(intent);
    }


}