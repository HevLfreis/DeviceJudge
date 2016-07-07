package com.hayt.devicejudge;
/**
 * Author: Haytham Teng
 * Date: 13-8-15
 * Time: 下午1:36
 * hevlhayt@foxmail.com
 */

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

import java.util.ArrayList;
import java.util.List;


public class DynamicTrafficScanner {

    /**
     * Main startup method
     */

    public static void main(String[] args) {
        List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
        StringBuilder errbuf = new StringBuilder(); // For any error msgs

        /***************************************************************************
         * get a list of devices on this system
         **************************************************************************/

        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s", errbuf
                    .toString());
            return;
        }

        System.out.println("Network devices found:");

        int i = 0;
        for (PcapIf device : alldevs) {
            String description =
                    (device.getDescription() != null) ? device.getDescription()
                            : "No description available";
            System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
        }

        PcapIf device = alldevs.get(1); // We know we have atleast 1 device
        System.out
                .printf("\nChoosing '%s' on your behalf:\n",
                        (device.getDescription() != null) ? device.getDescription()
                                : device.getName());

        /***************************************************************************
         * open up the selected device
         **************************************************************************/

        int snaplen = 64 * 1024;           // Capture all packets, no trucation
        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
        int timeout = 10 * 1000;           // 10 seconds in millis
        Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);

        if (pcap == null) {
            System.err.printf("Error while opening device for capture: "
                    + errbuf.toString());
            return;
        }

        /**
         * Sync the fp into database, read the fingerprint from database
         */

        FpConstants.syncFp();
        FingerPrints.resetFingerprints();

        /**
         * thread
         */

        Thread t;
        SpeedHandler s = new SpeedHandler();
        t = new Thread(s);
        t.start();

        LogHandler l = new LogHandler();
        t = new Thread(l);
        t.start();

        DatabaseHandler d = new DatabaseHandler();
        t = new Thread(d);
        t.start();


        /***************************************************************************
         * use JpacketHandlerCreator to create a packet handler which will receive
         * packets from the libpcap loop.
         *
         **************************************************************************/

        JPacketHandlerCreator jc = new JPacketHandlerCreator();

        pcap.loop(Pcap.LOOP_INFINITE, jc.deviceScan(), errbuf);
        pcap.close();

    }
}
