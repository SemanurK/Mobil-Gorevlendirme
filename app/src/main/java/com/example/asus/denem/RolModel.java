package com.example.asus.denem;

public class RolModel {
    int rolid;

    public RolModel(int rolid, String rol_adi) {
        this.rolid = rolid;
        this.rol_adi = rol_adi;
    }

    public int getRolid() {
        return rolid;
    }

    public void setRolid(int rolid) {
        this.rolid = rolid;
    }

    public String getRol_adi() {
        return rol_adi;
    }

    public void setRol_adi(String rol_adi) {
        this.rol_adi = rol_adi;
    }

    String rol_adi;

    @Override
    public String toString() {
        return  rol_adi ;
    }
}
