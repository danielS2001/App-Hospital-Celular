package com.example.hospitaldelcelular;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.hospitaldelcelular.Objetos.Citas;
import com.example.hospitaldelcelular.Objetos.Device;
import com.example.hospitaldelcelular.Objetos.ProcesosPHP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CitasActivity extends ListActivity implements Response.Listener<JSONObject>,Response.ErrorListener{

    private final Context context = this;
    private ProcesosPHP php = new ProcesosPHP();;
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;
    private ArrayList<Citas> listaCitas;
    private String serverip ="https://celularhospital.000webhostapp.com/WebService/";
    private Button btnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);
        listaCitas = new ArrayList<Citas>();
        request = Volley.newRequestQueue(context);
        consultarTodosWebService();
        btnSalir = (Button) findViewById(R.id.btnRegresar);
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
    public void consultarTodosWebService(){
        String url = serverip +"wsConsultarTodos.php?idMovil="+ Device.getSecureId(this);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
    }

    @Override
    public void onResponse(JSONObject response) {
        Citas cita = null;
        JSONArray json = response.optJSONArray("celular");
        try {
            for(int i=0;i<json.length();i++){
                cita = new Citas();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);
                cita.set_ID(jsonObject.optInt("_ID"));
                cita.setNombre(jsonObject.optString("nombre"));
                cita.setTelefono(jsonObject.optString("telefono"));
                cita.setCorreo(jsonObject.optString("correo"));
                cita.setDispositivo(jsonObject.optString("dispositivo"));
                cita.setFalla(jsonObject.optString("falla"));
                cita.setFecha(jsonObject.optString("fecha"));
                cita.setHora(jsonObject.optString("hora"));
                cita.setIdMovil(jsonObject.optString("idMovil"));
                listaCitas.add(cita);
            }
            MyArrayAdapter adapter = new MyArrayAdapter(context,R.layout.layout_appointment,listaCitas);
            setListAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class MyArrayAdapter extends ArrayAdapter<Citas> { Context context;
        int textViewRecursoId;
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
                            php.setContext(context);
                            Log.i("id", String.valueOf(objects.get(position).get_ID()));
                            php.borrarCitaWebService(objects.get(position).get_ID());
                            objects.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(),"Cita eliminado con exito",
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


}
