package com.example.listviewpersonas.Controlador;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listviewpersonas.R;
import com.example.listviewpersonas.Vista.Add_Libro;
import com.example.listviewpersonas.Vista.Detalle;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter<Adaptador.HolderLibros> {

    private Context context;
    private ArrayList<Libros> lista;

    private DatabaseReference mDatabaseReference;


    String obtenerId;
    public Adaptador(Context contexto, ArrayList<Libros> lista) {
    this.context = contexto;
    this.lista = lista;
    }

    @NonNull
    @Override
    public Adaptador.HolderLibros onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.lista, parent, false);
        //Devolvemos el HolderLibros con todas las vistas de la lista inicializadas, es donde pondremos los datos de los libros
        return new HolderLibros(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderLibros holder, final int position) {
        //Con este método obtenemos datos, los establecemos y vemos los clicks

        //Primero obtenemos los datos de cada libro por la posición
        Libros libro = lista.get(position);
        final String id = libro.getId();
        obtenerId = id;
        final String titulo = libro.getTitulo();
        final String auto = libro.getAutor();
        final String imagen = libro.getFoto();
        final String resumen = libro.getResumen();
        final String comentario = libro.getComentario();

        //Estos datos los mostramos en las vistas correspondientes de lista que están recogidas en el holder
        holder.título.setText(libro.getTitulo());
        holder.autor.setText(libro.getAutor());


        //para la imagen, si el usuario no quiere asignar imagen, la uri será nula por lo que configuramos una imagen predeterminada para este caso
        if(imagen == null){
            holder.imagen.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
        }else{
            holder.imagen.setImageURI(Uri.parse(imagen));
        }

       //Si clickamos en un holder (en un item de la lista) nos llevará a la pantalla con los detalles del libro
        //Además, tendremos que pasarle el id de dicho libro
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detalle.class);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });

        //Si clickamos en el botón de opciones
        holder.btn_mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarOpcionesDialogo(""+position, imagen, id, titulo, auto, resumen, comentario);
            }
        });

    }

    //Hacemos un método para mostrar el diálogo del botón de editar y borrar
    public void mostrarOpcionesDialogo(final String posicion, final String imagen, final String id, final String titulo, final String autor, final String resumen, final String comentario){

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        //Creamos un array de Strings con las opciones que van a aparecer en el diálogo

        String[] opciones = {"Editar", "Eliminar"};

        //Creamos el AlertDialog con las opciones
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Selecciona una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //El which a 0 indica la primera opción que es Editar
                if(which == 0){
                    //Si clickamos en Editar vamos a la Actividad de Add_Libro para poder editar los datos
                    //le tenemos que mandar todos los datos que tiene ese libro para que los muestre
                    Intent intent = new Intent(context, Add_Libro.class);
                    intent.putExtra("id", id);
                    intent.putExtra("titulo", titulo);
                    intent.putExtra("autor", autor);
                    intent.putExtra("resumen", resumen);
                    intent.putExtra("comentario", comentario);


                    intent.putExtra("imagen", imagen);
                    //Añadimos otro dato para saber si viene de editar
                    intent.putExtra("REQUEST_EDICION", true);
                    context.startActivity(intent);
                }
                //Si which es 1 ha clickado en eliminar
                else if(which == 1){
                    //Creamos otro diálogo para ver si estamos seguros de borrarlo
                    AlertDialog.Builder eliminarDialogo = new AlertDialog.Builder(context);
                    eliminarDialogo.setTitle("Eliminarás un alumno");
                    eliminarDialogo.setMessage("¿Estás seguro de eliminarlo?");
                    eliminarDialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            eliminar(id);

                        }
                    });
                    eliminarDialogo.setNegativeButton("No", null);
                    eliminarDialogo.create().show();

                }
            }
        });

        //Creamos y mostramos el diálogo
        builder.create().show();

    }

    private void eliminar(String id){
       if(id.equals(obtenerId)){
            mDatabaseReference.child("posts").child(obtenerId).removeValue();
       }
    }

    @Override
    public int getItemCount() {

        return lista.size();
    }

    public static class HolderLibros extends RecyclerView.ViewHolder{
        //En esta clase cogemos todos los elementos de lista para poder utilizarlos en AdapterLibros y rellenar el recyclerview posteriormente
        ImageView imagen;
        TextView título, autor;
        ImageButton btn_mas;

        public HolderLibros(@NonNull View itemView) {
            super(itemView);

            //Inicializamos los elementos de la vista
            imagen = itemView.findViewById(R.id.img);
            título = itemView.findViewById(R.id.Descripcion);
            autor = itemView.findViewById(R.id.Info);
            btn_mas = itemView.findViewById(R.id.btn_opciones);
        }
    }
}
