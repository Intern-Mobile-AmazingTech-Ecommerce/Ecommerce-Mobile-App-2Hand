package com.example.ecommercemobileapp2hand.Models;

import java.util.List;

public class Province {
    private String name;
    private int code;
    private String divisionType;
    private String codename;
    private int phoneCode;
    private List<District> districts;

    // Constructor
    public Province(String name, int code, String divisionType, String codename, int phoneCode, List<District> districts) {
        this.name = name;
        this.code = code;
        this.divisionType = divisionType;
        this.codename = codename;
        this.phoneCode = phoneCode;
        this.districts = districts;
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

    public int getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(int phoneCode) {
        this.phoneCode = phoneCode;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }





}