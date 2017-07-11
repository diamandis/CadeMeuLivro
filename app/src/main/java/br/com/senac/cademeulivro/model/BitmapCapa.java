package br.com.senac.cademeulivro.model;

import android.graphics.Bitmap;

/**
 * Created by Didi on 7/10/2017.
 */

public class BitmapCapa {
    private Bitmap capa;
    private Integer idBitmap;

    public BitmapCapa() {

    }

    public BitmapCapa(Bitmap capa) {
        this.capa = capa;
    }

    public Bitmap getCapa() {
        return capa;
    }

    public void setCapa(Bitmap capa) {
        this.capa = capa;
    }

    public Integer getIdBitmap() {
        return idBitmap;
    }

    public void setIdBitmap(Integer idBitmap) {
        this.idBitmap = idBitmap;
    }
}
