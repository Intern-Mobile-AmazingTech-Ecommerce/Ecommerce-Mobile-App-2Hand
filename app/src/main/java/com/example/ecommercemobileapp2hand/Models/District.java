package com.example.ecommercemobileapp2hand.Models;

import java.util.List;

public class District {
    private String name;
    private int code;
    private String divisionType;
    private String codename;
    private int provinceCode;



    public District(String name, int code, String divisionType, String codename, int provinceCode) {
        this.name = name;
        this.code = code;
        this.divisionType = divisionType;
        this.codename = codename;
        this.provinceCode = provinceCode;

    }
    public District(){

    }
    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDivisionType() {
        return divisionType;
    }

    public void setDivisionType(String divisionType) {
        this.divisionType = divisionType;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }




}