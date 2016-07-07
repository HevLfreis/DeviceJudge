package com.hayt.database;

/**
 * Author: Haytham Teng
 * Date: 13-8-26
 * Time: 上午9:08
 * hevlhayt@foxmail.com
 */
public class App_fp {

    private int id;
    private String app_name;
    private Boolean UA = true;  //true = UA
    private String fingerprint = "null";

    public void setId(int id) { this.id = id; }
    public void setApp_name(String app_name) { this.app_name = app_name; }
    public void setUA(boolean UA) { this.UA = UA; }
    public void setFingerprint(String fingerprint) { this.fingerprint = fingerprint; }

    public int getId() { return id; }
    public String getApp_name() { return app_name; }
    public boolean getUA() { return UA; }
    public String getFingerprint() { return fingerprint; }
}
