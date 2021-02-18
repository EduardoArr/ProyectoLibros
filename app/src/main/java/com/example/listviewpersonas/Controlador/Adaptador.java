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

import com.example.listviewpersonas.Modelo.ClaseBD;
import com.example.listviewpersonas.R;
import com.example.listviewpersonas.Vista.Add_Libro;
import com.example.listviewpersonas.Vista.Detalle;
import com.example.listviewpersonas.Vista.MainActivity;

import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter<Adaptador.HolderAlumno> {

    private Context context;
    private ArrayList<Libros> lista;

    ClaseBD claseBD;

    public Adaptador(Context context, ArrayList<Libros> lista) {
        this.context = context;
        this.lista = lista;
        claseBD = new ClaseBD(context);
    }

    @NonNull
    @Override
    public Adaptador.HolderAlumno onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflamos el layout con el de lista_alumno
        View view = LayoutInflater.from(context).inflate(R.layout.lista, parent, false);

        //Devolvemos el HolderAlumno con todas las vistas de lista_alumno inicializadas, es donde pondremos los datos de los alumnos
        return new HolderAlumno(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAlumno holder, final int position) {
        //Con este método obtenemos datos, los establecemos y vemos los clicks

        //Primero obtenemos los datos de cada alumno por la posición
        Libros libro = lista.get(position);
        final String id = libro.getId();

        final String titulo = libro.getTitulo();
        final String autor = libro.getAutor();
        final String imagen = libro.getFoto();
        final String resumen = libro.getResumen();
        final String comentario = libro.getComentario();


        //Estos datos los mostramos en las vistas correspondientes de lista_alumno que están recogidas en el holder
        holder.título.setText(titulo);
        holder.autor.setText(autor);


        //para la imagen, si el usuario no quiere asignar imagen, la uri será nula por lo que configuramos una imagen predeterminada para este caso
        if(imagen == null){
            //Toast.makeText(contexto, nombre + " " + imagen, Toast.LENGTH_SHORT).show();
            holder.imagen.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
        }else{
            holder.imagen.setImageURI(Uri.parse(imagen));
        }

        //Si clickamos en un holder (en un item de la lista) nos llevará a la pantalla con los detalles del alumno
        //Además, tendremos que pasarle el id de dicho alumno
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detalle.class);
                //Toast.makeText(contexto, nombre + " " + id, Toast.LENGTH_SHORT).show();
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });

        //Si clickamos en el botón de opciones
        holder.btn_mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarOpcionesDialogo(""+position, imagen, id, titulo, autor, resumen, comentario);
            }
        });

    }

    //Hacemos un método para mostrar el diálogo del botón de editar y borrar
    public void mostrarOpcionesDialogo(final String posicion, final String imagen, final String id, final String titulo, final String autor, final String resumen, final String comentario){
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
                    //Si clickamos en Editar vamos a la Actividad de AddAlumnoActivity para poder editar los datos
                    //le tenemos que mandar todos los datos que tiene ese alumno para que los muestre
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
                            //en este caso borramos al alumno que queremos
                            claseBD.eliminarPersonaById(id);
                            //y actualizamos la lista de alumnos otra vez llamando al método onResume del MainActivity
                            ((MainActivity)context).onResume();
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

    @Override
    public int getItemCount() {
        //Devuelve el número de alumnos almacenados
        return lista.size();
    }

    public class HolderAlumno extends RecyclerView.ViewHolder{
        //En esta clase cogemos todos los elementos de lista_alumnos para poder utilizarlos en AdapterAlumno y rellenar el recyclerview posteriormente
        ImageView imagen;
        TextView título, autor;
        ImageButton btn_mas;

        public HolderAlumno(@NonNull View itemView) {
            super(itemView);

            //Inicializamos los elementos de la vista
            imagen = itemView.findViewById(R.id.img);
            título = itemView.findViewById(R.id.Descripcion);
            autor = itemView.findViewById(R.id.Info);
            btn_mas = itemView.findViewById(R.id.btn_opciones);
        }
    }
}
