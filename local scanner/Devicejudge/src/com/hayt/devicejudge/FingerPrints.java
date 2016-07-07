package com.hayt.devicejudge;

import com.hayt.database.App_fp;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Haytham Teng
 * Date: 13-8-18
 * Time: 上午9:12
 * hevlhayt@foxmail.com
 * Identify the Mobile app
 */
public class FingerPrints {

    private static HashMap<String,String> HOST = new HashMap<String, String>();
    private static HashMap<String,String> USERAGENT =  new HashMap<String, String>();

    public FingerPrints(){

//        HOST = new HashMap<String, String>();
//        USERAGENT = new HashMap<String, String>();

        //read the fp info from fp static
//        System.out.println("Total " + FpConstants.FINGERPRINTS.length + " fingerprints ");
//
//        for (int i=0; i<FpConstants.FINGERPRINTS.length; i++) {
//
//            if (FpConstants.FINGERPRINTS[i][0].equals("Host")) {
//               HOST.put(FpConstants.FINGERPRINTS[i][1], FpConstants.FINGERPRINTS[i][2]);
//            }
//            else if (FpConstants.FINGERPRINTS[i][0].equals("UA")) {
//               USERAGENT.put(FpConstants.FINGERPRINTS[i][1], FpConstants.FINGERPRINTS[i][2]);
//            }
//
//        }

        //read the fp info from database
        resetFingerprints();
    }

    /**
     * according to the Host & UA in the packet
     * distinguish the app
     * @param Host the Host from the packet
     * @param UA   the UA from the packet
     */

    public static String judge(String Host, String UA) {

        String name_host = null, name_ua = null;
        boolean isBrowser = false;

        if (Host != null) {
            for (Map.Entry<String, String> entry : HOST.entrySet()) {
                String key = entry.getKey();
                if (Host.contains(key)) { name_host = entry.getValue(); }
            }
        }

        if (UA != null) {
            isBrowser = UA.contains("Chrome")||UA.contains("AppleWebkit")||UA.contains("Mozilla")||UA.contains("Mobile Safari");
            for (Map.Entry<String, String> entry : USERAGENT.entrySet()) {
                String key = entry.getKey();
                if (UA.contains(key)) { name_ua = entry.getValue(); }
            }
        }

        if (isBrowser) {
           return name_ua;
        }
        else {
            if ( name_ua != null) { return name_ua; }
            else { return name_host; }
        }
    }

    public static HashMap<String,String> getHOST() {
        return HOST;
    }

    public HashMap<String,String> getUSERAGENT() {
        return USERAGENT;
    }

    public static void resetFingerprints() {
        SessionFactory sf=DatabaseLink.sessionfactory();
        Session s=sf.openSession();
        Criteria cr = s.createCriteria(App_fp.class);
        List list = cr.list();

        for (Object aList : list) {
            App_fp a = (App_fp) aList;
            if (a.getUA()) {
                USERAGENT.put(a.getFingerprint(), a.getApp_name());
            } else {
                HOST.put(a.getFingerprint(), a.getApp_name());
            }
        }
    }


    public static void main(String[] args) {
        FingerPrints.resetFingerprints();
        System.out.println(FingerPrints.getHOST());
        System.out.println(FingerPrints.judge("client.api.ttpod.com", "adsfkjdgakj"));
    }

}
