package com.app.kelompokpapbl.baksogoodpembeli.model;

/**
 * Created by ASUS on 06/05/2018.
 */

public class    PembeliModel {
    private String nama;
    private String alamat;
    private String posisi_pesan;
    private String email;

    private String status_pesanan;

    public PembeliModel() {

    }

    public PembeliModel(String nama, String alamat, String email) {
        this.nama = nama;
        this.email = email;
        this.alamat = alamat;
        posisi_pesan = "";
        status_pesanan = "false";
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getPosisi_pesan() {
        return posisi_pesan;
    }

    public void setStatus_pesanan(String status_pesanan) {
        this.status_pesanan = status_pesanan;
    }

    public String getStatus_pesanan() {
        return status_pesanan;
    }

    public void setPosisi_pesan(String posisi_pesan) { this.posisi_pesan = posisi_pesan; }
}
