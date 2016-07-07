package com.hayt.devicejudge;

import com.hayt.database.User_IP;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.*;

/**
 * Author: Haytham Teng
 * Date: 13-8-21
 * Time: 下午2:14
 * hevlhayt@foxmail.com
 */
public class DatabaseHandler implements Runnable{

//    private HashMap<String, Object[]> h = new HashMap<String, Object[]>();
//
//    public DatabaseHandler(HashMap<String, Object[]> h) {
//        this.h = h;
//    }

    @Override
    public void run() {
        System.out.println("Thread DatabaseHandler has been created");

        Timer timer = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                SessionFactory sf=DatabaseLink.sessionfactory();
                Session s=sf.openSession();
                Transaction ts=s.beginTransaction();

                HashMap<String, Object[]> tem = new HashMap<String, Object[]>(JPacketHandlerCreator.STAT);

                for (Map.Entry<String, Object[]> entry : tem.entrySet()) {

                    System.out.println(entry.getKey()+" ----- "+entry.getValue()[0]+" , "+entry.getValue()[1]+" , "+entry.getValue()[2]+" , "
                            +entry.getValue()[3]+" ----- "+entry.getValue()[4]);

                    Criteria cr = s.createCriteria(User_IP.class);
                    cr.add(Restrictions.eq("ip", entry.getKey()));

                    List list = cr.list();

                    //ip not in the database insert the data
                    if (list.size() == 0) {
                        User_IP ui = new User_IP();
                        ui.setIp(entry.getKey());
                        ui.setWINDOWS((Boolean) entry.getValue()[0]);
                        ui.setUBUNTU((Boolean) entry.getValue()[1]);
                        ui.setMAC((Boolean) entry.getValue()[2]);
                        ui.setMOBILE((Boolean) entry.getValue()[3]);
                        ui.setBehavior((String) entry.getValue()[4]);
                        s.save(ui);
                    }
                    else if(list.size() != 1) {
                        try {
                            throw new Exception("Over two same data in the database , check it");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //ip in the database update the data
                    else{

                        User_IP old = (User_IP) list.get(0);

                        //ever used a mobile device through this ip
                        if ((Boolean) entry.getValue()[3]) {

                            //if no behaviour
                            if (entry.getValue()[4] == null) {
                                old.setIp(entry.getKey());
                                old.setWINDOWS((Boolean) entry.getValue()[0]|old.getWINDOWS());
                                old.setUBUNTU((Boolean) entry.getValue()[1]|old.getUBUNTU());
                                old.setMAC((Boolean) entry.getValue()[2]|old.getMAC());
                                old.setMOBILE((Boolean) entry.getValue()[3]|old.getMOBILE());
                                s.update(old);
                            }
                            else if (old.getBehavior() == null) {
                                old.setIp(entry.getKey());
                                old.setWINDOWS((Boolean) entry.getValue()[0]|old.getWINDOWS());
                                old.setUBUNTU((Boolean) entry.getValue()[1]|old.getUBUNTU());
                                old.setMAC((Boolean) entry.getValue()[2]|old.getMAC());
                                old.setMOBILE((Boolean) entry.getValue()[3]|old.getMOBILE());
                                old.setBehavior((String) entry.getValue()[4]);
                                s.update(old);
                            }
                            else {
                                String[] old_behaviour_apps = old.getBehavior().split("/");

                                String new_behaviour = (String) entry.getValue()[4];
                                String[] new_behaviour_apps = new_behaviour.split("/");

                                List<String> old_list;
                                old_list = new ArrayList<String>();
                                for (int i = 1; i < old_behaviour_apps.length; i++) {
                                    old_list.add(old_behaviour_apps[i]);
                                }
//                System.out.println(old_list);

                                List<String> new_list;
                                new_list = new ArrayList<String>();
                                for (int i = 1; i < new_behaviour_apps.length; i++) {
                                    new_list.add(new_behaviour_apps[i]);
                                }
//                System.out.println(new_list);

                                for(int i = 0; i < new_list.size(); i++) {
                                    //app not in the old list update it
                                    if (!old_list.contains(new_list.get(i))) {
                                        old_list.add(new_list.get(i));
                                    }
                                }
                                new_behaviour = "";
                                //transfer the oldlist to String
                                for (int i = 0; i < old_list.size(); i++) {
                                    new_behaviour = new_behaviour + "/" + old_list.get(i);
                                }

                                //
                                old.setIp(entry.getKey());
                                old.setWINDOWS((Boolean) entry.getValue()[0]|old.getWINDOWS());
                                old.setUBUNTU((Boolean) entry.getValue()[1]|old.getUBUNTU());
                                old.setMAC((Boolean) entry.getValue()[2]|old.getMAC());
                                old.setMOBILE((Boolean) entry.getValue()[3]|old.getMOBILE());
                                old.setBehavior(new_behaviour);
                                s.update(old);
                            }


                        }
                        //desktop system don't need to update the behaviour
                        else {
                            old.setIp(entry.getKey());
                            old.setWINDOWS((Boolean) entry.getValue()[0]|old.getWINDOWS());
                            old.setUBUNTU((Boolean) entry.getValue()[1]|old.getUBUNTU());
                            old.setMAC((Boolean) entry.getValue()[2]|old.getMAC());
                            old.setMOBILE((Boolean) entry.getValue()[3]|old.getMOBILE());
                            s.update(old);
                        }
                    }
                }
                ts.commit();
                s.close();
            }
        };

        timer.scheduleAtFixedRate(tt, 1000, 2000);

    }

    public static void main(String[] args) {

    }
}
