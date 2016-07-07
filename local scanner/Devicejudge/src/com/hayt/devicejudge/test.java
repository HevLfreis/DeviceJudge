package com.hayt.devicejudge;

import java.util.*;

/**
 * Author: Haytham Teng
 * Date: 13-8-21
 * Time: 上午9:13
 * hevlhayt@foxmail.com
 */
public class test {
    public static void main (String[] args) {
////        StringBuilder errbuf = new StringBuilder();
////
////        final Pcap pcap = Pcap.openOffline("D:\\packets\\life\\taobao.pcap", errbuf);
////
////        try {
////            JFlowMap map = new JFlowMap();
////
////            pcap.loop(Pcap.LOOP_INFINATE, map, null);
////
////
////
////            System.out.println(map.toString());
////
////        } finally {
////            pcap.close();
////        }
//        String old = "/1/2/3/4/5/6";
////        String[] a = as.split("/");
//        String _new = "/5/6/7/8/9/0";
////        String[] _new = bs.split("/");
//
//        String[] old_behaviour_apps = old.split("/");
//
////        String new_behaviour = (String) entry.getValue()[4];
//        String[] new_behaviour_apps = _new.split("/");
//
//        List<String> old_list;
//        old_list = new ArrayList<String>();
//        for (int i = 1; i < old_behaviour_apps.length; i++) {
//            old_list.add(old_behaviour_apps[i]);
//        }
////                System.out.println(old_list);
//
//        List<String> new_list;
//        new_list = new ArrayList<String>();
//        for (int i = 1; i < new_behaviour_apps.length; i++) {
//            new_list.add(new_behaviour_apps[i]);
//        }
////                System.out.println(new_list);
//
//        for(int i = 0; i < new_list.size(); i++) {
//            //app not in the old list update it
//            if (!old_list.contains(new_list.get(i))) {
//                old_list.add(new_list.get(i));
//            }
//        }
//
//        //transfer the oldlist to String
//        _new ="";
//        for (int i = 0; i < old_list.size(); i++) {
//            _new = _new + "/" + old_list.get(i);
//        }
//
//        System.out.println(_new);
//        SessionFactory sf=DatabaseLink.sessionfactory();
//        Session s=sf.openSession();
//        Transaction ts=s.beginTransaction();
//        Criteria cr = s.createCriteria(App_fp.class);
//        List list = cr.list();
//
//        for (int i = 0; i<list.size(); i++){
//            App_fp a = (App_fp) list.get(i);
//            System.out.println(a.getApp_name());
//        }
//      Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//                System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
//            }
//        }, 1000, 1000);

        HashMap<String[], Object[]> log = new HashMap<String[], Object[]>();
        String[] a = {"1","2"};
        Object[] b = {new Date(), "3"};
        log.put(a, b);

        System.out.println(log);
        String[] c = {"1","2"};
        System.out.println(log.containsKey(c));


        for (int i = 0; i < 9; i++) {
            
            
        }
        for (Map.Entry<String[], Object[]> entry : log.entrySet()) {
            System.out.println(entry.getKey()[0].equals(c[0]));
            System.out.println(entry.getKey()[1].equals(c[1]));
        }

    }
}
