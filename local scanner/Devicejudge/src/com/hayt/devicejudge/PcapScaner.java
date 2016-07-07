package com.hayt.devicejudge;

import org.jnetpcap.Pcap;

/**
 * Author: Haytham Teng
 * Date: 13-8-15
 * Time: 下午7:47
 * hevlhayt@foxmail.com
 */
public class PcapScaner {

    public static void main(String[] args) {

        final String FILENAME = "D:\\temp\\win3.pcap";
        final StringBuilder errbuf = new StringBuilder();


        final Pcap pcap = Pcap.openOffline(FILENAME, errbuf);

        if (pcap == null) {
            System.err.println(errbuf);
            return;
        }
        JPacketHandlerCreator jc = new JPacketHandlerCreator();
        pcap.loop(Pcap.LOOP_INFINATE, jc.simpleScan(), errbuf);
    }
}
