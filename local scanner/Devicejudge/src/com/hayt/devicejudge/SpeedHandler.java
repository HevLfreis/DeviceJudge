package com.hayt.devicejudge;

import com.hayt.database.App_speed;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Author: Haytham Teng
 * Date: 13-8-29
 * Time: 下午2:57
 * hevlhayt@foxmail.com
 */
public class SpeedHandler implements Runnable {


    @Override
    public void run() {
        System.out.println("Thread SpeedHandler has been created");

        SessionFactory sf=DatabaseLink.sessionfactory();
        Session s=sf.openSession();
        Transaction ts=s.beginTransaction();
        App_speed as = new App_speed();
        as.setSpeed(0);
        s.save(as);
        ts.commit();

        Timer timer = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                SessionFactory sf=DatabaseLink.sessionfactory();
                Session s=sf.openSession();
                Transaction ts=s.beginTransaction();
                App_speed as = (App_speed) s.get(App_speed.class, 1);
                as.setSpeed(JPacketHandlerCreator.PACKET_COUNT/2.0);
                s.update(as);
                ts.commit();
                s.close();
                JPacketHandlerCreator.PACKET_COUNT = 0;
            }
        };

        timer.scheduleAtFixedRate(tt, 10, 2000);
    }
}
