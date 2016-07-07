package com.hayt.devicejudge;
/**
 * Author: Haytham Teng
 * Date: 13-8-15
 * Time: 下午1:36
 * hevlhayt@foxmail.com
 */
import java.util.ArrayList;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;


public class SpeedScaner {

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

        PcapIf device = alldevs.get(0); // We know we have at least 1 device
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

        /***************************************************************************
         * use JpacketHandlerCreator to create a packet handler which will receive
         * packets from the libpcap loop.
         **************************************************************************/

        SpeedHandler s = new SpeedHandler();
        Thread t = new Thread(s);
        t.start();

        JPacketHandlerCreator jc = new JPacketHandlerCreator();

        pcap.loop(Pcap.LOOP_INFINITE, jc.speedScan(), errbuf);
        pcap.close();
    }
}
