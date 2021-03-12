package com.example.listviewpersonas.Registro;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.listviewpersonas.R;
import com.example.listviewpersonas.Vista.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TextView correo;
    ImageView imagen;
    ImageView pers;
    Button cerrarSesion;
    String usuario;
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        actionBar = getSupportActionBar();
        actionBar.setTitle("Perfil Usuario");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        correo = findViewById(R.id.correo);
        imagen = findViewById(R.id.imageView3);
        cerrarSesion = findViewById(R.id.btn_cerrarSesion);
        pers = findViewById(R.id.img_perfil);

        Bundle extras = getIntent().getExtras();
        usuario = extras.getString("correo");

        correo.setText(usuario);
        imagen.setImageResource(R.mipmap.lib);
        pers.setImageResource(R.mipmap.img_perf);


        cerrarSesion();


    }

    private void cerrarSesion(){
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}