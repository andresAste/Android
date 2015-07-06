package com.aasteasu.gastosmensuales.repository;

import android.content.res.Resources;
import android.util.Pair;

import com.aasteasu.gastosmensuales.R;
import com.aasteasu.gastosmensuales.model.GastoMensual;
import com.aasteasu.gastosmensuales.model.Mes;
import com.aasteasu.gastosmensuales.model.TipoConcepto;
import com.aasteasu.gastosmensuales.util.AsyncResult;
import com.aasteasu.gastosmensuales.util.DownloadWebpageTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by aasteasu on 6/25/2015.
 */
public class GastoMensualRepository {

    // region INTERFACE METHODS

    public List<GastoMensual> GetGastosMensuales(List<GastoMensual> gastos, List<Mes> mesesRequeridos) {
        ArrayList<GastoMensual> result = new ArrayList<GastoMensual>();

        for (GastoMensual gasto : gastos) {
            if (mesesRequeridos.contains(gasto.getMes()) && gasto.getImporte() != 0)
            {
                result.add(gasto);
            }
        }
        return result;
    }

    /* Retorna los gastos mensuales sin pagar*/
    public List<GastoMensual> GetGastosMensuales(JSONObject object) {
        ArrayList<GastoMensual> result = new ArrayList<GastoMensual>();

        JSONObject currentColFecha = null;
        JSONObject currentColMonto = null;
        try {
            JSONArray rows = object.getJSONArray("rows");

            for (int r = 2; r < rows.length(); r = r + 2) {
                JSONObject rowFecha = rows.getJSONObject(r);
                JSONObject rowMonto = rows.getJSONObject(r+1);

                JSONArray columnsFecha = rowFecha.getJSONArray("c");
                JSONArray columnsMonto = rowMonto.getJSONArray("c");

                String gastoDesc = columnsFecha.getJSONObject(0).getString("v");
                if (gastoDesc.equalsIgnoreCase("***"))
                    break;
                else
                {
                    for (int col = 2; col <=13; col++) {
                        currentColFecha = columnsFecha.get(col).toString() != "null" ?  columnsFecha.getJSONObject(col) : null;
                        currentColMonto = columnsMonto.get(col).toString() != "null" ?  columnsMonto.getJSONObject(col) : null;

                        String  dia =  currentColFecha != null ? currentColFecha.getString("v") : "0";
                        int mes = col - 2;
                        int anio = 2015;
                        String monto =  currentColMonto != null ? currentColMonto.getString("v") : "0";

                        GastoMensual gasto = new GastoMensual();
                        try{ gasto.setImporte(Double.valueOf(monto)); } catch (NumberFormatException ex){ }
                        gasto.setConcepto(gastoDesc);
                        gasto.setMes(mes+1);
                        Pair<Date, Boolean> fechaInfo = ParseDate(dia, mes, anio);
                        if (fechaInfo != null)
                        {
                            gasto.setFechaDeVencimiento(fechaInfo.first);
                            gasto.setPagado(fechaInfo.second);
                        }

                        result.add(gasto);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    // endregion

    // region PRIVATE METHODS
    private Pair<Date, Boolean> ParseDate(String dia, int mes, int anio)
    {
        Pair<Date, Boolean> result = null;
        try
        {
            if (dia == null || dia.isEmpty() || dia == "0")
            {
                result = null;
            }
            else if (dia.startsWith("pf:") || dia.startsWith("p:") )
            {
                Pair<Date, Boolean> date2 = ParseDate(dia.replace("pf", "f").replace("p:", ""), mes, anio);
                result = new Pair<Date, Boolean>(date2.first, true);
            }
            else if (dia.startsWith("f:"))
            {
                dia = dia.replace("f:","");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mmd/yyyy");
                result = new Pair<Date, Boolean>(dateFormat.parse(dia), false);
            }
            else if (dia.endsWith("?") )
            {
                result = ParseDate(dia.replace("?", ""), mes, anio);
            }
            else
            {
                Calendar date = Calendar.getInstance();
                Double diaDouble =Double.parseDouble(dia);
                date.set(anio, mes, diaDouble.intValue());
                result = new Pair<Date, Boolean>(date.getTime(), false);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    // endregion
}
