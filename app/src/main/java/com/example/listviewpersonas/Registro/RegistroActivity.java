package com.example.listviewpersonas.Registro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.listviewpersonas.R;
import com.example.listviewpersonas.Vista.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroActivity extends AppCompatActivity {
    Button registrar;
    EditText email;
    EditText contraseña;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mAuth = FirebaseAuth.getInstance();

        registrar = findViewById(R.id.btn_cerrarSesion);
        email = findViewById(R.id.emailRegistro);
        contraseña = findViewById(R.id.passwordRegistro);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), contraseña.getText().toString())
                        .addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("tag", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegistroActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                            }
                        });
            }
        });

    }




         public void updateUI(FirebaseUser account) {

                if (account != null) {
                    Toast.makeText(this, "U Signed In successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, MainActivity.class));

                } else {
                    Toast.makeText(this, "U Didnt signed in", Toast.LENGTH_LONG).show();
                }

            }
        }