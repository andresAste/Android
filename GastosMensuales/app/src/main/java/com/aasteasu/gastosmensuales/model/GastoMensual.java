package com.aasteasu.gastosmensuales.model;

import java.util.Date;

/**
 * Created by aasteasu on 6/25/2015.
 */
public class GastoMensual {

    //region VARIABLES
    /* Fecha de vencimiento del gasto  */
    private Date FechaDeVencimiento;
    public Date getFechaDeVencimiento() { return FechaDeVencimiento; }
    public void setFechaDeVencimiento(Date fechaDeVencimiento) { FechaDeVencimiento = fechaDeVencimiento; }

    /* Importe a pagar */
    private Double Importe;
    public Double getImporte() { return this.Importe; }
    public void setImporte(double importe) { this.Importe = importe; }

    /* Indica si el gasto fue pagado*/
    private boolean Pagado;
    public boolean getPagado() { return this.Pagado; }
    public void setPagado(boolean pagado) { this.Pagado = pagado; }

    /* Descripcion del gasto*/
    private String Concepto;
    public String getConcepto() { return this.Concepto; }
    public void setConcepto(String concepto) { this.Concepto = concepto; }

    /* Tipo de concepto*/
    private TipoConcepto TipoConcepto;
    public TipoConcepto getTipoConcepto() { return TipoConcepto; }
    public void setTipoConcepto(TipoConcepto tipoConcepto) { TipoConcepto = tipoConcepto; }
    //endregion

    /* Mes asociado al Gasto*/
    private Mes Mes;
    public Mes getMes() { return Mes; }
    public void setMes(Mes mes) { Mes = mes; }
    public void setMes(int mes)
    {
        if (mes > 0 && mes < 13)
            Mes = com.aasteasu.gastosmensuales.model.Mes.values()[mes-1];
    }

    //region CONSTRUCTORS
    /* Default constructor*/
    public GastoMensual()
    {
        setPagado(false);
    }
    //endregion
}
