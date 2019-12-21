package com.pradita.arya.appconnectmysql;

import android.content.DialogInterface;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pradita.arya.appconnectmysql.app.AppController;
import com.pradita.arya.appconnectmysql.data.Data;
import com.pradita.arya.appconnectmysql.adapter.Adapter;
import com.pradita.arya.appconnectmysql.util.Server;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Toolbar toolbar;
    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<Data> itemList = new ArrayList<Data>();
    Adapter adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_nim, txt_nama, txt_alamat;
    String nim, nama, alamat;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String url_select = Server.URL + "select.php";
    private static String url_insert = Server.URL + "insert.php";
    private static String url_edit = Server.URL + "edit.php";
    private static String url_update = Server.URL + "update.php";
    private static String url_delete = Server.URL + "delete.php";
    public static final String TAG_NIM = "nim";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_ALAMAT = "alamat";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        swipe = findViewById(R.id.swipe_refresh_layout);
        list = findViewById(R.id.list);

        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new Adapter(MainActivity.this, itemList);
        list.setAdapter(adapter);

        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override

                       public void run() {

                           swipe.setRefreshing(true);
                           itemList.clear();

                           adapter.notifyDataSetChanged();
                           callVolley();

                       }
                   }
        );

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm("", "", "", "SIMPAN");
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                final String nimx = itemList.get(position).getNim();
                final CharSequence[] dialogitem = {"Edit", "Delete"};
                dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                edit(nimx);
                                break;
                            case 1:
                                delete(nimx);
                                break;

                        }
                    }
                }).show();
                return false;
            }
        });
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();
    }

    // untuk mengosongi edittext pada form
    private void kosong() {
        txt_nim.setText(null);
        txt_nama.setText(null);
        txt_alamat.setText(null);
    }

    private void DialogForm(String nimx, String namax, String alamatx, String button) {
        dialog = new AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_biodata, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form Biodata");
        txt_nim = dialogView.findViewById(R.id.txt_nim);

        txt_nama = dialogView.findViewById(R.id.txt_nama);
        txt_alamat = dialogView.findViewById(R.id.txt_alamat);
        if (!nimx.isEmpty()) {
            txt_nim.setText(nimx);
            txt_nama.setText(namax);
            txt_alamat.setText(alamatx);
        } else {
            kosong();
        }
        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nim = txt_nim.getText().toString();
                nama = txt_nama.getText().toString();
                alamat = txt_alamat.getText().toString();
                simpan_update();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                kosong();
            }
        });
        dialog.show();
    }

    // untuk menampilkan semua data pada listview
    private void callVolley() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);
// membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new
                Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
// Parsing json

                        for (int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Data item = new Data();
                                item.setNim(obj.getString(TAG_NIM));
                                item.setNama(obj.getString(TAG_NAMA));
                                item.setAlamat(obj.getString(TAG_ALAMAT));

                                // menambah item ke array
                                itemList.add(item);
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                        // notifikasi adanya perubahan data pada adapter
                        adapter.notifyDataSetChanged();
                        swipe.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });
// menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    // fungsi untuk menyimpan atau update
    private void simpan_update() {
        String url;
// jika id kosong maka simpan, jika id ada nilainya maka update
        if (nim.isEmpty()) {
            url = url_insert;
        } else {
            url = url_update;
        }
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);
// Cek error node pada json
                            if (success == 1) {
                                Log.d("Add/update", jObj.toString());
                                callVolley();
                                kosong();

                                Toast.makeText(MainActivity.this, jObj.getString(TAG_MESSAGE),
                                        Toast.LENGTH_LONG).show();
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MainActivity.this, jObj.getString(TAG_MESSAGE),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
// JSON error
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url

                Map<String, String> params = new HashMap<String, String>();

                // jika id kosong maka simpan, jika id ada nilainya maka update
                if (nim.isEmpty()) {

                    params.put("nama", nama);
                    params.put("alamat", alamat);
                } else {
                    params.put("nim", nim);
                    params.put("nama", nama);
                    params.put("alamat", alamat);
                }
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq);
    }

    // fungsi untuk get edit data
    private void edit(final String nimx){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);
// Cek error node pada json
                            if (success == 1) {

                                Log.d("get edit data", jObj.toString());
                                String nimx = jObj.getString(TAG_NIM);
                                String namax = jObj.getString(TAG_NAMA);
                                String alamatx = jObj.getString(TAG_ALAMAT);
                                DialogForm(nimx, namax, alamatx, "UPDATE");
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MainActivity.this, jObj.getString(TAG_MESSAGE),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
// JSON error

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
// Posting parameters ke post url

                Map<String, String> params = new HashMap<String, String>();

                params.put("nim", nimx);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq);
    }

    // fungsi untuk menghapus
    private void delete(final String nimx){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_delete, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);
// Cek error node pada json
                            if (success == 1) {

                                Log.d("delete", jObj.toString());
                                callVolley();
                                Toast.makeText(MainActivity.this, jObj.getString(TAG_MESSAGE),
                                        Toast.LENGTH_LONG).show();
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MainActivity.this, jObj.getString(TAG_MESSAGE),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
// JSON error
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(MainActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
// Posting parameters ke post url

                Map<String, String> params = new HashMap<String, String>();
                params.put("nim", nimx);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
