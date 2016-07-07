package com.hayt.devicejudge;

import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Http;

import java.util.Date;
import java.util.HashMap;

/**
 * Author: Haytham Teng
 * Date: 13-8-21
 * Time: 下午12:33
 * hevlhayt@foxmail.com
 * create a obj of JpacketHandler for pcap.loop
 */
public class JPacketHandlerCreator {

    private JPacketHandler<StringBuilder> jpackethandler = null;
//    private final HashMap<String, Object[]> h = new HashMap<String, Object[]>();
//    private final HashMap<String[], Object[]> log = new HashMap<String[], Object[]>();

    public static HashMap<String, Object[]> STAT = new HashMap<String, Object[]>();
    public static HashMap<String[], Object[]> LOG = new HashMap<String[], Object[]>();
    public static int PACKET_COUNT = 0;

    public JPacketHandler<StringBuilder> simpleScan() {
        jpackethandler = new JPacketHandler<StringBuilder>() {

            final Http http = new Http();
            final Ip4 ip4 = new Ip4();

            @Override
            public void nextPacket(JPacket packet, StringBuilder errbuf) {
                if (packet.hasHeader(http) && packet.hasHeader(ip4) && !http.isResponse()) {
                    System.out.println("URL: "+http.fieldValue(Http.Request.RequestUrl));
                }
            }
        };
        return jpackethandler;
    }

    public JPacketHandler<StringBuilder> deviceScan() {
        jpackethandler = new JPacketHandler<StringBuilder>() {

            final Http http = new Http();
            final Ip4 ip4 = new Ip4();
            final Icmp icmp = new Icmp();
//            final FingerPrints fp = new FingerPrints();
//            int count = 0;

            @Override
            public void nextPacket(JPacket packet, StringBuilder errbuf) {
                PACKET_COUNT++;

                /**
                 *  we only concerned about the http get or post
                 */

                if (packet.hasHeader(http) && packet.hasHeader(ip4) && !http.isResponse() &&
                        !packet.hasHeader(icmp)) {

                    //stat
                    Object[] info ={false,false,false,false,null};
                    //log
                    String[] log_ip = {null, null};
                    Object[] log_act = {null, new Date(), null};

                    boolean isSuccess = false;

                    byte[] sIP = packet.getHeader(ip4).source();
                    byte[] dIP = packet.getHeader(ip4).destination();
                    String sourceIP = org.jnetpcap.packet.format.FormatUtils.ip(sIP);
                    String destinationIP = org.jnetpcap.packet.format.FormatUtils.ip(dIP);

                    String UA = http.fieldValue(Http.Request.User_Agent);
                    if (UA == null) { UA = "null"; }

//                    System.out.println(UA);

                    String requesturl;
                    if (http.fieldValue(Http.Request.Host) != null){
                        requesturl = http.fieldValue(Http.Request.Host)+http.fieldValue(Http.Request.RequestUrl);
                    }
                    else {
                        requesturl = http.fieldValue(Http.Request.Host)+http.fieldValue(Http.Request.RequestUrl);
                    }


                    String app_name = null;

                    /**
                     *   this part is used to judge the device
                     */

                    if (UA.contains("Windows NT")) {
                        info[0] = true;
                        isSuccess = true;
                        System.out.println("Ip : " + sourceIP+ " ------- " + "Desktop System : Windows");

                        //save to log hashmap
                        log_ip[0] = sourceIP;
                        log_ip[1] = destinationIP;
//                        log_act[0] = app_name;
                        log_act[2] = requesturl;
                        LOG.put(log_ip, log_act);
                    }
                    else if (UA.contains("Ubuntu")){
                        info[1] = true;
                        isSuccess = true;
                        System.out.println("Ip : " + sourceIP+ " ------- " + "Desktop System : Ubuntu");
                    }
                    else if (UA.contains("Macintosh")){
                        info[2] = true;
                        isSuccess = true;
                        System.out.println("Ip : " + sourceIP+ " ------- " + "Desktop System : Mac OS");
                    }

                    //if not the desktop system , maybe a mobile device , maybe nothing
                    else {
                        app_name = FingerPrints.judge(http.fieldValue(Http.Request.Host), UA);
                        if (app_name != null) {
                            info[3] = true;
                            info[4] = "/" + app_name;
                            isSuccess = true;
                            System.out.println("Ip : " + sourceIP+ " -------" + "Mobile App : " + app_name);

                            //save to log hashmap
                            log_ip[0] = sourceIP;
                            log_ip[1] = destinationIP;
                            log_act[0] = app_name;
                            log_act[2] = requesturl;
                            LOG.put(log_ip, log_act);
                        }
                        else {
                            for (String i : FpConstants.MOBILE_UA) {
                                if (UA.contains(i)) {
                                    info[3] = true;
                                    isSuccess = true;
                                    System.out.println("Ip : " + sourceIP+ " -------" + "Mobile Devices : " + i);
                                    break;
                                }
                            }
                        }
                    }

                    /**
                     *   if  the packet is analyzed successfully , add or update it to the hashmap
                     */

                    if (isSuccess) {
                        if (!STAT.containsKey(sourceIP)) { STAT.put(sourceIP, info); }
                        else {
                            Object[] old = STAT.get(sourceIP);
                            String old_behaviour = (String) old[4];

                            if (old_behaviour != null && app_name != null) {
                                if (!old_behaviour.contains(app_name)) {
                                    old[4] = old_behaviour + info[4];
                                }
                            }
                            else if (old_behaviour == null && app_name != null){
                                old[4] = info[4];
                            }

                            for (int i = 0; i < 4; i++) {
                                old[i] = (Boolean) info[i] | (Boolean) old[i];
                            }

                            STAT.put(sourceIP, old);

                        }

//                        count ++;
                        // save hashmap to database
//                        if (count == 1000) {
//
//                            //solve the ConcurrentModificationException
//                            HashMap<String, Object[]> tem = new HashMap<String, Object[]>(h);
//
//                            DatabaseHandler dh = new DatabaseHandler(tem);
//                            Thread t = new Thread(dh);
//                            t.start();
//                            count = 0;
//                            for (Map.Entry<String, Object[]> entry : tem.entrySet()) {
//                                System.out.println(entry.getKey()+" ----- "+entry.getValue()[0]+" , "+entry.getValue()[1]+" , "+entry.getValue()[2]+" , "
//                                        +entry.getValue()[3]+" ----- "+entry.getValue()[4]);
//
//                            }
//                            //sync the fp hashmap
//                            .resetFingerprints();
//                        }
//                        System.out.println();
//
//                        //save log to database
//                        if (log.size() == 100) {
//
//                            //solve the ConcurrentModificationException
//                            HashMap<String[], Object[]> tem = new HashMap<String[], Object[]>(log);
//                            LogHandler lh = new LogHandler(tem);
//                            Thread t = new Thread(lh);
//                            t.start();
//                            log.clear();
//                        }
                    }

                }

            }
        };
        return jpackethandler;
    }

    public JPacketHandler<StringBuilder> speedScan() {
        jpackethandler = new JPacketHandler<StringBuilder>() {

            @Override
            public void nextPacket(JPacket packet, StringBuilder errbuf) {

                PACKET_COUNT++;

            }
        };
        return jpackethandler;
    }

    public HashMap<String, Object[]> getHashMap() { return STAT; }
}
