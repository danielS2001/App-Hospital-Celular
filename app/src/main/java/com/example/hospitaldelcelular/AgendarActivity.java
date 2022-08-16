package com.example.hospitaldelcelular;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitaldelcelular.Objetos.Citas;
import com.example.hospitaldelcelular.Objetos.ReferenciasFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AgendarActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnAgendar;
    private Button btnRegresar;
    private Button btnLimpiar;
    private TextView txtNombre;
    private TextView txtCorreo;
    private TextView txtTelefono;
    private TextView txtDispositivo;
    private TextView txtFalla;
    private TextView txtFecha;
    private TextView txtHora;
    private FirebaseDatabase basedatabase;
    private DatabaseReference referencia;
    private Citas savedCita;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar);
        initComponents();
        setEvents();
    }
    public void initComponents() {
//se obtiene una instancia de la base de datos y se obtiene la referencia que apunta a la tabla citas
        this.basedatabase = FirebaseDatabase.getInstance();
        this.referencia = this.basedatabase.getReferenceFromUrl(ReferenciasFirebase.URL_DATABASE
                + ReferenciasFirebase.DATABASE_NAME + "/" +ReferenciasFirebase.TABLE_NAME);

        this.txtNombre = findViewById(R.id.txtNombre);
        this.txtTelefono = findViewById(R.id.txtCelular);
        this.txtCorreo = findViewById(R.id.txtEmail);
        this.txtDispositivo = findViewById(R.id.txtDispositivo);
        this.txtFalla = findViewById(R.id.txtFalla);
        this.txtFecha = findViewById(R.id.txtDate);
        this.txtHora = findViewById(R.id.txtHora);
        this.btnAgendar = findViewById(R.id.btnAgendar);
        this.btnRegresar = findViewById(R.id.btnRegresar);
        this.btnLimpiar = findViewById(R.id.btnLimpiar);
        savedCita = null;
    }

    public void setEvents() {
        this.btnAgendar.setOnClickListener(this);
        this.btnRegresar.setOnClickListener(this);
        this.btnLimpiar.setOnClickListener(this);
    }
    @Override
    public void onClick(View view)
    {
        if(isNetworkAvailable()){
            switch (view.getId()) {
                case R.id.btnAgendar:
                    boolean completo = true;
                    if(txtNombre.getText().toString().equals("")){
                        txtNombre.setError("Introduce el nombre completo");
                        completo=false;
                    }
                    if(txtTelefono.getText().toString().equals("")){
                        txtTelefono.setError("Introduce el número telefónico");
                        completo=false;
                    }
                    if(txtCorreo.getText().toString().equals("")){
                        txtCorreo.setError("Introduce el correo electrónico");
                        completo=false;
                    }
                    if(txtDispositivo.getText().toString().equals("")){
                        txtDispositivo.setError("Introduce el nombre del dispositivo");
                        completo=false;
                    }
                    if(txtFalla.getText().toString().equals("")){
                        txtFalla.setError("Introduce la descripción de la falla");
                        completo=false;
                    }
                    if(txtFecha.getText().toString().equals("")){
                        txtFecha.setError("Introduce la fecha");
                        completo=false;
                    }
                    if(txtHora.getText().toString().equals("")){
                        txtHora.setError("Introduce la hora");
                        completo=false;
                    }
                    if (completo) {
                        Citas nCita = new Citas();
                        nCita.setNombre(txtNombre.getText().toString());
                        nCita.setTelefono(txtTelefono.getText().toString());
                        nCita.setCorreo(txtCorreo.getText().toString());
                        nCita.setDispositivo(txtDispositivo.getText().toString());
                        nCita.setFalla(txtFalla.getText().toString());
                        nCita.setFecha(txtFecha.getText().toString());
                        nCita.setHora(txtHora.getText().toString());
                        if(savedCita == null){
                            agregarCita(nCita);
                            Toast.makeText(getApplicationContext(),"Cita guardada con exito",
                                    Toast.LENGTH_SHORT).show(); limpiar();
                        }else{
                            actualizarCita(id, nCita);
                            Toast.makeText(getApplicationContext(),"Cita actualizada con exito",
                                    Toast.LENGTH_SHORT).show(); limpiar();
                        }
                    } break;
                case R.id.btnLimpiar:
                    limpiar();
                    break;
                case R.id.btnRegresar:
                    AlertDialog.Builder confirmar = new AlertDialog.Builder(AgendarActivity.this);
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
                    limpiar();
                    break;
            }
        }else{
            Toast.makeText(getApplicationContext(),"Se necesita tener conexion a internet",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void agregarCita(Citas c) {
        DatabaseReference newCitaReference = referencia.push();
        //obtener el id del registro y setearlo
        String id = newCitaReference.getKey();
        c.set_ID(id);
        newCitaReference.setValue(c);
    }
    public void actualizarCita(String id, Citas p) {
        //actualizar un objeto al nodo referencia
        p.set_ID(id);
        referencia.child(String.valueOf(id)).setValue(p);
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }
    public void limpiar(){
        savedCita = null;
        txtNombre.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDispositivo.setText("");
        txtFalla.setText("");
        txtFecha.setText("");
        txtHora.setText("");
        id="";
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        if(intent != null){
            Bundle oBundle = intent.getExtras();
            if(Activity.RESULT_OK == resultCode){
                Citas cita = (Citas) oBundle.getSerializable("cita");
                savedCita = cita;
                id = cita.get_ID();
                txtNombre.setText(cita.getNombre());
                txtTelefono.setText(cita.getTelefono());
                txtCorreo.setText(cita.getCorreo());
                txtDispositivo.setText(cita.getDispositivo());
                txtFalla.setText(cita.getFalla());
                txtFecha.setText(cita.getFecha());
                txtHora.setText(cita.getHora());
            }else{
                limpiar(); } } }}