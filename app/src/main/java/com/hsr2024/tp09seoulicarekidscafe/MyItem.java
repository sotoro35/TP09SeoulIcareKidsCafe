package com.hsr2024.tp09seoulicarekidscafe;

public class MyItem {

    String atdrc_nm; //자치구명
    String rntfee_free_at; //무료여부
    String fclty_nm; // 시설이름
    String cttpc; // 연락처
    String bass_adres; //기본주소
    String detail_adres; //상세주소
    String open_week; //운영일
    String close_week; //휴관일
    String posbl_agrde; //이용가능 연령

    String call_iv;



    public MyItem() {
        return;
    }

    public MyItem(String atdrc_nm, String rntfee_free_at, String fclty_nm, String cttpc, String bass_adres, String detail_adres, String open_week, String close_week, String posbl_agrde) {
        this.atdrc_nm = atdrc_nm;
        this.rntfee_free_at = rntfee_free_at;
        this.fclty_nm = fclty_nm;
        this.cttpc = cttpc;
        this.bass_adres = bass_adres;
        this.detail_adres = detail_adres;
        this.open_week = open_week;
        this.close_week = close_week;
        this.posbl_agrde = posbl_agrde;
        this.call_iv = call_iv;
    }

    String getCtppc(){
        return call_iv;
    }
}
