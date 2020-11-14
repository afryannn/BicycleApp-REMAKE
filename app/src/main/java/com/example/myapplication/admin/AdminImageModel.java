package com.example.myapplication.admin;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class AdminImageModel implements Parcelable {
    private int iditem;
    private String kodesepeda;
    private String merk;
    private String warna;
    private String gambar;
    private String url;
    private String harga;
    public AdminImageModel(Parcel in) {
        iditem = in.readInt();
        kodesepeda = in.readString();
        merk = in.readString();
        warna = in.readString();
        gambar = in.readString();
        url = in.readString();
        harga = in.readString();
    }

    public static final Creator<AdminImageModel> CREATOR = new Creator<AdminImageModel>() {
        @Override
        public AdminImageModel createFromParcel(Parcel in) {
            return new AdminImageModel(in);
        }

        @Override
        public AdminImageModel[] newArray(int size) {
            return new AdminImageModel[size];
        }
    };

    public AdminImageModel(JSONObject dataUser) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getIditem() {
        return iditem;
    }

    public void setIditem(int iditem) {
        this.iditem = iditem;
    }

    public String getKodesepeda() {
        return kodesepeda;
    }

    public void setKodesepeda(String kodesepeda) {
        this.kodesepeda = kodesepeda;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getWarna() {
        return warna;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public static Creator<AdminImageModel> getCREATOR() {
        return CREATOR;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(iditem);
        parcel.writeString(kodesepeda);
        parcel.writeString(merk);
        parcel.writeString(warna);
        parcel.writeString(gambar);
        parcel.writeString(url);
        parcel.writeString(harga);
    }
}
