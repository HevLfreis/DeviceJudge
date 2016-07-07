package com.hayt.devicejudge;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Author: Haytham Teng
 * Date: 13-8-21
 * Time: 下午2:42
 * hevlhayt@foxmail.com
 */
public class DatabaseLink {

    private static SessionFactory sf;

    static {
        Configuration cf = new Configuration();
        cf.configure();
        sf = cf.buildSessionFactory();
    }

    public static SessionFactory sessionfactory() {
        return sf;
    }

    public static Session session() {
        return sf.openSession();
    }
}
