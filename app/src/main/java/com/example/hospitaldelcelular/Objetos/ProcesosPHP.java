package com.example.hospitaldelcelular.Objetos;

import android.content.Context;
import android.util.Log;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;
import java.util.ArrayList;

public class ProcesosPHP implements Response.Listener<JSONObject>,Response.ErrorListener {
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;
    private ArrayList<Citas> citas = new ArrayList<Citas>();
    private String serverip = "https://celularhospital.000webhostapp.com/WebService/";
    public void setContext(Context context){
        request = Volley.newRequestQueue(context);
    }
    public void insertarCitaWebService(Citas c){
        String url = serverip + "wsRegistro.php?nombre="+c.getNombre()
                +"&telefono="+c.getTelefono()+"&correo="+c.getCorreo()+"&dispositivo="+c.getDispositivo()
                +"&falla="+c.getFalla()+"&fecha="+c.getFecha()+"&hora="+c.getHora()+"&idMovil="+c.getIdMovil();
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    public void actualizarCitaWebService(Citas c,int id){
        String url = serverip + "wsActualizar.php?_ID="+id
                +"&nombre="+c.getNombre()+"&telefono="+c.getTelefono()+"&correo="
                +c.getCorreo()+"&dispositivo="+c.getDispositivo()+"&falla="
                +c.getFalla()+"&fecha="+c.getFecha()+"&hora="+c.getHora();
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    public void borrarCitaWebService(int id){
        String url = serverip + "wsEliminar.php?_ID="+id;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.i("ERROR",error.toString());
    }
    @Override
    public void onResponse(JSONObject response) {
    }
}
