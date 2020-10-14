package com.example.pokeapp;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Pokey implements Runnable{
    RequestQueue queue;// = Volley.newRequestQueue(this);
    PokeyMaker source;
    String url;// ="https://www.google.com";
    String[] args;

    /*
     * Jake, writing documentation? Surely not.
     * args[0] represents the type of request, denoted by its endpoint. The rest depend. Here's a list.
     * args[0] = register, args[1] = name
     * args[0] = friends/add, args[1] = user_UUID, args[2] = target_UUID
     * The arguments are organized so that addXRequest takes arguments in order.
     */
    public Pokey(RequestQueue queue, PokeyMaker source, String url, String[] args) {
        this.queue = queue;
        this.source = source;
        this.url = url;
        this.args = args;
    }

    //@Override
    public void run() {
        Log.d("POKEY", "Starting pokey with endpoint " + args[0]);
        switch(args[0]) {
            case "register":
                addRegiRequest(args[1]);
                break;
            case "friends/add":
                //unimplemented
                break;
            default:
                Log.d("POKEY", "endpoint not found");
        }
    }

    void addRegiRequest(String name) {
        final String regiName = name;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                source.setResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name", regiName);
                return params;
            }
        };
        queue.add(stringRequest);
    }
};
