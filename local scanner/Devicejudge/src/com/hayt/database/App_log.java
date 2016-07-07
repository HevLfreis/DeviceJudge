package com.hayt.database;

import java.util.Date;

/**
 * Author: Haytham Teng
 * Date: 13-9-3
 * Time: 上午10:12
 * hevlhayt@foxmail.com
 */
public class App_log {
    private int id;
    private String source;
    private String destination;
    private String behavior;
    private Date timestamp;
    private String requesturl;

    public void setId(int id) { this.id = id; }
    public void setSource(String source) { this.source = source; }
    public void setDestination(String destination) {this.destination = destination; }
    public void setBehavior(String behavior) {this.behavior = behavior; }
    public void setTimestamp(Date timestamp) {this.timestamp = timestamp; }
    public void setRequesturl(String requesturl) { this.requesturl = requesturl; }

    public int getId() { return id; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public String getBehavior() {return behavior; }
    public Date getTimestamp() { return timestamp; }
    public String getRequesturl() { return requesturl; }

}
