package com.example.listviewpersonas.Vista;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listviewpersonas.Controlador.Libros;
import com.example.listviewpersonas.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class Add_Libro extends AppCompatActivity {

    ImageView portada;
    TextView tituEdit;
    EditText título;
    EditText autor;
    EditText resumen;
    EditText comentario;
    FloatingActionButton aceptar;

    ActionBar actionBar;

    private DatabaseReference mDatabaseReference;

    private static final int REQUEST_CAMERA_CODE = 1;
    private static final int REQUEST_STORAGE_CODE = 2;

    //Declaramos códigos que nos servirán para comprobar si la imagen viene de galería o de cámara
    private static final int PICK_CAMERA_CODE = 3;
    private static final int PICK_GALLERY_CODE = 4;

    //Creamos arrays de permisos (el primero para cámara y almacenamiento y el segundo sólo para almacenamiento)
    private String[] cameraPermissions;
    private String[] storagePermissions;

    Uri uri;
    String txt_titulo, txt_autor, txt_resumen, txt_comentario, txt_imagen, txt_id;



    boolean editar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__libro);


        tituEdit = findViewById(R.id.titEdit);
        portada = findViewById(R.id.btn_add_photo);
        título = findViewById(R.id.título);
        autor = findViewById(R.id.autor);
        resumen = findViewById(R.id.comment);
        comentario = findViewById(R.id.comentario);
        aceptar = findViewById(R.id.save);



        mDatabaseReference = FirebaseDatabase.getInstance().getReference();



        actionBar = getSupportActionBar();
        actionBar.setTitle("Agregar Libro");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        Bundle bundle = getIntent().getExtras();
        editar = bundle.getBoolean("REQUEST_EDICION");
        Log.i("EDITAR", "" + editar);

        if (editar) {
            //cambiamos el título del ActionBar
            actionBar.setTitle("Editar Registro");

            //si viene de editar cojo los datos del libro
            txt_id = bundle.getString("id");
            Log.e("pruebaID", "" + txt_id);
            txt_titulo = bundle.getString("titulo");
            txt_autor = bundle.getString("autor");
            txt_resumen = bundle.getString("resumen");
            Log.i("NOMBRE", txt_titulo);
            txt_comentario = bundle.getString("comentario");
            txt_imagen = bundle.getString("imagen");

            //y los muestro
            tituEdit.setText("EDITA UN LIBRO");
            título.setText(txt_titulo);
            autor.setText(txt_autor);
            resumen.setText(txt_resumen);
            comentario.setText(txt_comentario);


            if (txt_imagen == null) {
                portada.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
                //Cuando pinchamos en agregar imagen de libro

            } else {
                portada.setImageURI(uri);
            }
        } else {
            //cambiamos el título dek ActionBar
            actionBar.setTitle("Agregar Registro");
        }


        portada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarOpcionesImagen();
            }
        });
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarLibro();
                onBackPressed();
            }
        });
    }

    private void guardarLibro(){

        //Recogemos los datos de los EditText y los pasamos a String.
        txt_titulo = título.getText().toString().trim();
        txt_autor = autor.getText().toString().trim();
        txt_resumen = resumen.getText().toString().trim();
        txt_comentario = comentario.getText().toString().trim();

        //Si es distinto de editar creará el nuevo libro con los datos que hemos escrito.
        if(!editar) {
            Map<String, Object> postref = new HashMap<>();
            postref.put("titulo", txt_titulo);
            postref.put("autor", txt_autor);
            postref.put("resumen", txt_resumen);
            postref.put("comentario", txt_comentario);
            mDatabaseReference.child("posts").push().setValue(postref);

        }else{

            //Si ha entrado en la opción de editar modificaremos los datos de la base de datos con los nuevos que escriba, para ello tenemos que pasarle una id y el campo que queremos cambiar.
            mDatabaseReference.child("posts").child(txt_id).child("autor").setValue(txt_autor);
            mDatabaseReference.child("posts").child(txt_id).child("comentario").setValue(txt_comentario);
            mDatabaseReference.child("posts").child(txt_id).child("resumen").setValue(txt_resumen);
            mDatabaseReference.child("posts").child(txt_id).child("titulo").setValue(txt_titulo);

        }


    }

    private void mostrarOpcionesImagen() {
        //Tenemos dos opciones, sacar una foto con la cámara o seleccionarla de galería
        String[] opciones = {"Cámara", "Galería"};

        //Creamos el AlertDialog con el texto de Selecccionar imagen y las dos opciones
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar imagen");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //manejamos los click dependiendo de si el usuario elige galería o cámara
                //el entero wich será cero si se elige la primera opción y 1 si se elige la segundaç

                //Si hace click en cámara
                if(which==0){
                    //comprobamos si tiene los permisos de cámara
                    //si no los tiene se los pedimos
                    if(!comprobarPermisosCamara()){
                        pedirPermisosCamara();
                    }
                    //si los tiene, vamos al método irCamara() para sacar la foto
                    else{
                        irCamara();
                    }
                }
                //Si hace click en galería
                else if(which==1){
                    //comprobamos si tiene los permisos de almacenamiento
                    //si no los tiene se los pedimos
                    if(!comprobarPermisosAlmacenamiento()){
                        pedirPermisosAlmacenamiento();
                    }
                    //si los tiene, vamos al método irGaleria() para seleccionar la foto de galería
                    else{
                        irGaleria();
                    }
                }
            }
        });
        //Creamos y mostramos el diálogo con lo establecido
        builder.create().show();

    }

    private boolean comprobarPermisosCamara() {

        boolean permisosCamara = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean permisosAlmacenamiento = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return permisosCamara && permisosAlmacenamiento;

    }


    private void pedirPermisosCamara() {
        //Solicitamos el permiso de cámara
        ActivityCompat.requestPermissions(this, cameraPermissions, REQUEST_CAMERA_CODE);
    }

    private boolean comprobarPermisosAlmacenamiento() {

        boolean permisos = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return permisos;
    }
    private void pedirPermisosAlmacenamiento() {
        //Solicitamos el permiso de almacenamiento
        ActivityCompat.requestPermissions(this, storagePermissions, REQUEST_STORAGE_CODE);
    }

    private void irCamara() {
        //Creamos un ContentValues para almacenar el título de la imagen escogida y su descripción
        ContentValues valores = new ContentValues();
        valores.put(MediaStore.Images.Media.TITLE, "Título de la imagen");
        valores.put(MediaStore.Images.Media.DESCRIPTION, "Descripción de la imagen");

        //En la uri correpsondiente a la imagen escogida insertamos los valores de título y descripción
        uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, valores);

        //lanzamosel intent para sacar foto desde la cámara con la uri y el códifo correspondiente a la cámara para que desde
        //onActivityResult se sepa que es desde aquí
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, PICK_CAMERA_CODE);
    }

    private void irGaleria() {
        //lanzamos un intent para seleccionar una imagen de la galería con el código correspondiente
        // para que sepa que proviene de galería y será escuchado por el método onActivityResult
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_GALLERY_CODE);
    }

    //Con este método pedimos los permisos, el requestCode será el REQUEST_CAMERA_CODE o el REQUEST_STORAGE_CODE
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CAMERA_CODE){
            //Si el usuario permite esos permisos, vamos a la cámara
            if(permissions.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                irCamara();
            }
            //si no le mostramos un mensaje para que habilite los permisos
            else{
                Toast.makeText(this, "Es necesario que habilites los permisos", Toast.LENGTH_SHORT).show();
            }

        }
        if(requestCode == REQUEST_STORAGE_CODE){
            //Si el usuario permite esos permisos, vamos a la galería
            if(permissions.length>0 && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                irGaleria();
            }
            //si no le mostramos un mensaje para que habilite los permisos
            else{
                Toast.makeText(this, "Es necesario que habilites los permisos", Toast.LENGTH_SHORT).show();
            }

        }

    }

    //Con el siguiente método vemos si se lanza el intent de la cámara con su correspondiente código requestCode de PICK_CAMERA_CODE
    //o el de galería con PICK_GALLERY_CODE
    //resultCode puede ser RESULT_OK o RESULT_CANCEL. El OK será cuando se toma la foto y se recibe bien
    //data será el tipo de foto cuando se toma
    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Si se ha sacado la foto correctamente o se ha seleccionado de galería correctamente comprobamos
        //si viene de cámara o galería
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_CAMERA_CODE) {
                //si viene de cámara utilizamos la librería de CropImage para recortar la imagen y ponerla en el CircleImageView
                CropImage.activity(uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);


            } else if (requestCode == PICK_GALLERY_CODE) {
                //si viene de galería utilizamos la librería de CropImage para recortar la imagen y ponerla en el CircleImageView

                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);


            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                //si se ha recibido correctamente la imagen crop, cambiamos el CircleImageView por ésta
                CropImage.ActivityResult resultado = CropImage.getActivityResult(data);
                if (resultCode == Activity.RESULT_OK) {
                    Uri imagenUri = resultado.getUri();
                    uri = imagenUri;
                    portada.setImageURI(imagenUri);
                } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    //Si hay un error lo mostramos
                    Exception error = resultado.getError();
                    Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();

                }
            }

        }
    }

    //Para que desde el ActionBar te vaya a la actividad anterior
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}