package com.hayt.devicejudge;

import com.hayt.database.App_log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.*;

/**
 * Author: Haytham Teng
 * Date: 13-9-3
 * Time: 下午1:41
 * hevlhayt@foxmail.com
 * Log Thread
 */
public class LogHandler implements Runnable{

//    private HashMap<String[], Object[]> log = new HashMap<String[], Object[]>();
//
//    public LogHandler(HashMap<String[], Object[]> log) {
//        this.log = log;
//    }

    @Override
    public void run() {
        System.out.println("Thread LogHandler has been created");

        Timer timer = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                SessionFactory sf=DatabaseLink.sessionfactory();
                Session s=sf.openSession();
                Transaction ts=s.beginTransaction();

                HashMap<String[], Object[]> tem = new HashMap<String[], Object[]>(JPacketHandlerCreator.LOG);
                JPacketHandlerCreator.LOG.clear();

                for (Map.Entry<String[], Object[]> entry : tem.entrySet()) {
                    App_log al = new App_log();
                    al.setSource(entry.getKey()[0]);
                    al.setDestination(entry.getKey()[1]);
                    al.setBehavior((String) entry.getValue()[0]);
                    al.setTimestamp((Date) entry.getValue()[1]);
                    al.setRequesturl((String) entry.getValue()[2]);
                    s.save(al);
                }
                ts.commit();
                s.close();
            }
        };

        timer.scheduleAtFixedRate(tt, 500, 1000);

    }
}
