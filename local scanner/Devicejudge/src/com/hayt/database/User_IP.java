package com.hayt.database;

/**
 * Author: Haytham Teng
 * Date: 13-8-21
 * Time: 上午10:53
 * hevlhayt@foxmail.com
 */
public class User_IP {

    private int id;
    private String ip;
    private boolean WINDOWS = false;
    private boolean UBUNTU = false;
    private boolean MAC = false;
    private boolean MOBILE = false;
    private String behavior = null;

    public void setId(int id) { this.id = id; }
    public void setIp(String ip) { this.ip = ip; }
    public void setWINDOWS(boolean WINDOWS) { this.WINDOWS = WINDOWS; }
    public void setUBUNTU(boolean UBUNTU) { this.UBUNTU = UBUNTU; }
    public void setMAC(boolean MAC) { this.MAC = MAC; }
    public void setMOBILE(boolean MOBILE) { this.MOBILE = MOBILE; }
    public void setBehavior(String behavior) { this.behavior = behavior; }

    public int getId() { return id; }
    public String getIp() { return ip; }
    public boolean getWINDOWS() { return WINDOWS; }
    public boolean getUBUNTU() { return UBUNTU; }
    public boolean getMAC() { return MAC; }
    public boolean getMOBILE() { return MOBILE; }
    public String getBehavior() { return behavior; }

}
