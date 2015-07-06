package com.aasteasu.gastosmensuales.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aasteasu.gastosmensuales.R;
import com.aasteasu.gastosmensuales.model.GastoMensual;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by aasteasu on 7/3/2015.
 */
public class GastoMensualAdapter extends ArrayAdapter<GastoMensual> {
    Context context;
    private List<GastoMensual> gastos;

    public GastoMensualAdapter(Context context, int textViewResourceId, List<GastoMensual> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.gastos = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.gasto_mensual, null);
        }
        GastoMensual gasto = gastos.get(position);

        try
        {
            if (gasto != null) {
                TextView gastoDesc = (TextView) v.findViewById(R.id.concepto);
                TextView mes = (TextView) v.findViewById(R.id.mes);
                TextView fechaVencimiento = (TextView) v.findViewById(R.id.fechaVencimiento);
                TextView importe = (TextView) v.findViewById(R.id.importe);

                gastoDesc.setText(gasto.getConcepto());
                mes.setText(gasto.getMes().toString());
                if (gasto.getFechaDeVencimiento() != null)
                {
                    SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
                    fechaVencimiento.setText(String.valueOf(fmtOut.format(gasto.getFechaDeVencimiento())));
                }
                importe.setText(String.valueOf(gasto.getImporte()));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return v;
    }
}
