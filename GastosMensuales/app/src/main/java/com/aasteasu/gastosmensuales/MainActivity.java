package com.aasteasu.gastosmensuales;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.aasteasu.gastosmensuales.model.GastoMensual;
import com.aasteasu.gastosmensuales.model.Mes;
import com.aasteasu.gastosmensuales.repository.GastoMensualRepository;
import com.aasteasu.gastosmensuales.util.AsyncResult;
import com.aasteasu.gastosmensuales.util.DownloadWebpageTask;
import com.aasteasu.gastosmensuales.util.GastoMensualAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*  https://github.com/telerik/Android-samples/tree/master/Blogs/Json-Reader */
public class MainActivity extends ActionBarActivity {
    private static final String DEBUG_TAG = "HttpExample";
    ListView listview;
    Button btnDownload;
    private GastoMensualRepository gastosRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            gastosRepository = new GastoMensualRepository();
            listview = (ListView) findViewById(R.id.listview);
            btnDownload = (Button) findViewById(R.id.btnDownload);
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                btnDownload.setEnabled(true);
            } else {
                btnDownload.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            handleUncaughtException(e);
        }
    }

    public void buttonClickHandler(View view) {
        String url = getResources().getString(R.string.urlAccesoPlanilla) + getResources().getString(R.string.urlPlanillaGastos);
        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                processJson(object);
            }
        }).execute(url);
        int i = 1;
    }

    private void processJson(JSONObject object) {

        try {
            List<GastoMensual> gastos = gastosRepository.GetGastosMensuales(object);
            List<GastoMensual> gastosRequeridos = gastosRepository.GetGastosMensuales(gastos, new ArrayList<Mes>(Arrays.asList( Mes.Julio )));
            GastoMensualAdapter adapter = new GastoMensualAdapter(this, R.layout.gasto_mensual, gastosRequeridos);
            listview.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleUncaughtException (Exception e)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(e.getMessage());
        builder1.setCancelable(true);
        /*builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });*/

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}