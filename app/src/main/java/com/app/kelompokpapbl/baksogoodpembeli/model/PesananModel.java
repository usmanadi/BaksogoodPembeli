package com.app.kelompokpapbl.baksogoodpembeli.model;

/**
 * Created by ASUS on 12/05/2018.
 */

public class PesananModel {
    private String nama;
    private String lat;
    private String lon;

    private String status_pesanan;

    public PesananModel() {

    }

    public PesananModel(String nama, String lat, String lon) {
        this.nama = nama;
        this.lat = lat;
        this.lon = lon;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String  getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String alamat) { this.lon = lon; }

}
