package com.hayt.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Date;

/**
 * Author: Haytham Teng
 * Date: 13-8-20
 * Time: 下午1:14
 * hevlhayt@foxmail.com
 */
public class Test {

    public static void main(String[] args)
    {
        Configuration cf=new Configuration();
        cf.configure();
        SessionFactory sf=cf.buildSessionFactory();
        Session s=sf.openSession();
        Transaction ts=s.beginTransaction(); //事务
//        User user=new User();
//        user.setName("jack");
//        User_IP up = new User_IP();
//        up.setIp("192.168.1.2");
//        up.setUBUNTU(true);
//        up.setBehavior("1");
//        User_IP up2 = new User_IP();
//        up2.setIp("192.168.1.1");
//        up2.setWINDOWS(true);
//        up2.setBehavior("2");
//        s.save(up);
//        s.save(up2);
//
//
//        Criteria cr = s.createCriteria(User_IP.class); //生成一个Criteria对象
//
//        cr.add(Restrictions.eq("ip", "192.168.1.2"));//等价于where name=’Bill’
//
//        List list = cr.list();
//        if (list.size() == 0) { System.out.println("None"); }
//        else
//        {User_IP ui = (User_IP) list.get(0);
//            ui.setBehavior("5");
//            s.update(ui);
//
//        System.out.println(ui.getBehavior());}

        App_log al = new App_log();
        al.setSource("192.0.0.0");
        al.setDestination("192.2.2.2");
        al.setBehavior("/None");
        al.setTimestamp(new Date());
        s.save(al);

        ts.commit();
        s.close();
    }
}
