package com.example.hospitaldelcelular;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitaldelcelular.Objetos.Citas;
import com.example.hospitaldelcelular.Objetos.ReferenciasFirebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CitasActivity extends ListActivity {
    private FirebaseDatabase basedatabase;
    private DatabaseReference referencia;
    //private Button btnNuevo;
    private Button btnSalir;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);
        basedatabase = FirebaseDatabase.getInstance();
        referencia = basedatabase.getReferenceFromUrl(ReferenciasFirebase.URL_DATABASE
                + ReferenciasFirebase.DATABASE_NAME + "/" +ReferenciasFirebase.TABLE_NAME);
        btnSalir = (Button) findViewById(R.id.btnRegresar);
        obtenerCitas();
        //btnNuevo.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        setResult(Activity.RESULT_CANCELED);
        //        finish();
        //    }
        //});

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmar = new AlertDialog.Builder(CitasActivity.this);
                confirmar.setTitle("¿Salir al menú principal?");
                confirmar.setMessage("¿Está seguro de que salir al menú principal?");
                confirmar.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                confirmar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                confirmar.show();
            }
        });
    }
    public void obtenerCitas(){
        final ArrayList<Citas> citas = new ArrayList<Citas>();
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Citas cita = dataSnapshot.getValue(Citas.class);
                citas.add(cita);
                final MyArrayAdapter adapter = new MyArrayAdapter(context,R.layout.layout_appointment,citas);
                setListAdapter(adapter);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        referencia.addChildEventListener(listener);
    }

    class MyArrayAdapter extends ArrayAdapter<Citas>
    { Context context; int textViewRecursoId;
        ArrayList<Citas> objects;
        public MyArrayAdapter(Context context, int textViewResourceId,ArrayList<Citas>
                objects){
            super(context, textViewResourceId, objects);
            this.context = context;
            this.textViewRecursoId = textViewResourceId;
            this.objects = objects;
        }
        public View getView(final int position, View convertView, ViewGroup viewGroup){
            LayoutInflater layoutInflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(this.textViewRecursoId, null);
            TextView lblNombre = (TextView)view.findViewById(R.id.lblName);
            TextView lblTelefono = (TextView)view.findViewById(R.id.lblPhone);
            TextView lblDevice = (TextView)view.findViewById(R.id.lblDevice);
            //Button btnModificar = (Button)view.findViewById(R.id.btnModificar);
            Button btnBorrar = (Button)view.findViewById(R.id.btnBorrar);
            lblNombre.setText(objects.get(position).getNombre());
            lblTelefono.setText(objects.get(position).getTelefono());
            lblDevice.setText(objects.get(position).getDispositivo());

            btnBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder confirmar = new AlertDialog.Builder(CitasActivity.this);
                    confirmar.setTitle("¿Borrar cita?");
                    confirmar.setMessage("¿Está seguro de que desea borrar la cita?");
                    confirmar.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            borrarCita(objects.get(position).get_ID());
                            objects.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "Cita eliminada con exito",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    confirmar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    confirmar.show();

                }
            });
            return view;
        }
    }
    public void borrarCita(String childIndex){
        referencia.child(String.valueOf(childIndex)).removeValue();
    }


}
