package com.hayt.devicejudge;

import com.hayt.database.App_fp;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Haytham Teng
 * Date: 13-8-20
 * Time: 下午2:50
 * hevlhayt@foxmail.com
 */
public class FpConstants {

    /**
     * Mobile UA List
     */

    public static String[] MOBILE_UA = {"Android","iPad","iPhone","Windows Phone","WebOS","BlackBerry"};

    /**
     * Mobile App fingerprints
     */

    public static String[][] FINGERPRINTS = {

            /************************************
             * Social
             ************************************/

            //Weixin
            {"Host","wx.qlogo.cn","Weixin"},
            {"Host","mmsns.qpic.cn","Weixin"}, //朋友圈
            {"UA","MicroMessenger","Weixin"},

            //QQ2013
            {"Host","scannon.3g.qq.com","QQ2013"},
            {"Host","conf.3g.qq.com","QQ2013"},

            //Qzone
            {"UA","android-qzone","Qzone"},

            //pengyou
            {"Host","py.qlogo.cn","Pengyou"},

            //renren
            {"Host","api.m.renren.com","RenRen"},
            {"Host","fmn.rrimg.com","RenRen"},

            //momo
            {"UA","MomoChat","Momo"},

            //weibo
            {"Host","sinaimg.cn","weibo"},
            {"Host","api.weibo.cn","weibo"},
            {"UA","weibo","weibo"},

            //t weibo
            {"UA","AMB_401","tweibo"},

            //yy
            {"Host","m.yy.com","YY"},

            //kechenggezi
            {"Host","kechenggezi.com","KeChengGeZi"},

            //Line
            {"UA","LA/123","Line"},

            //mitalk
            {"UA","MiTalk","MiTalk"},

            //wangwang
            {"UA","ANDROID_WW","WangWang"},

            //feixin
            {"Host","hdss1ftb.fetion.com.cn","Feixin"},

            //kaixin
            {"Host","kaixin001.com.cn","Kaixin"},

            /************************************
             * Life
             ************************************/

            //taobao
            {"UA","anclient","Taobao series"},

            //moji weather
            {"Host","moji001.com","MojiWeather"},

            //goweather
            {"Host","goweatherex.3g.cn","GoWeather"},

            //jd
            {"Host","gw.m.360buy.com","JD"},
            {"Host","360buyimg.com","JD"},

            //wochacha
            {"Host","android.wochacha.com","Wochacha"},

            //dazhongdianping
            {"UA","MApi","Dazhongdianping"},
            {"Host","m.api.dianping.com","Dazhongdianping"},
//            {"Host","dpfile.com","Dazhongdianping"},

            //tigerknows
            {"Host","tigerknows.net","Tigerknows"},

            //meituan
            {"UA","AiMeiTuan","Meituan"},
            {"Host","api.mobile.meituan.com","Meituan"},

            //tianqitong
            {"Host","forecast.sina.cn","Tianqitong"},

            //zdclock
            {"Host","zdworks.com","ZDClock"},

            //58
            {"Host","58.com","58"},

            //buding
            {"Host","buding.cn","Buding"},

            //doubanmovie
            {"UA","com.douban.amonsul","DoubanMovie"},

            //lashou
            {"Host","lashou.com","Lashou"},

            //tuan800
            {"UA","tuan800","Tuan800"},

            //youdaodic
//            {"Host","dict.youdao.com","YoudaoDict"},

            //Tonghuashun
            {"Host","10jqka.com","Tonghuashun"},

            /************************************
             * Browser
             ************************************/

            //baidu
            {"UA","baidubrowser","BaiduBrowser"},

            //dolphin
            {"UA","Dolphin http client","DolphinBrowser"},

            //firefox
            {"Host","m.g-fox.cn","FireFox"},

            //maxthon
            {"UA","Maxthon","Maxthon"},

            //qqbrowser
            {"UA","MQQBrowser","QQBrowser"},

            //ucbrowser
            {"UA","UCBrowser","UCBrowser"},

            /************************************
             * Media
             ************************************/

            //qvod
            {"UA","QvodPlayerTheater","Qvod"},

            //ttpod
            {"Host","client.api.ttpod.com","TTPod"},

            //baofeng
            {"Host","shouji.baofeng.com","Baofeng"},

            //iqiyi
            {"Host","iqiyi.com","IQIYI"},

            //kugou
            {"Host","mobilecdn.kugou.com","Kugou"},

            //youku
            {"UA","Youku","Youku"},
//            {"Host","youku.com","Youku"},
//            {"Host","ykimg.com","Youku"},

            //qqmusic
            {"Host","3g.music.qq.com","QQMusic"},


            /************************************
             * Communication
             ************************************/

            //qqphonebook
            {"UA","qqphonebook","QQPhoneBook"},

            //Shuqireader
            {"Host","shuqireader.com","ShuqiReader"},

            /************************************
             * News&Community
             ************************************/

            //baidunews
            {"UA","bdnews_android_phone","BaiduNews"},

            //changba
            {"Host","changba.com","Changba"},

            //NetEaseNews
            {"UA","NTES Android","NetEaseNews"},

            //sohunews
            {"Host","k.sohu.com","SohuNews"},

            //tieba
            {"Host","c.tieba.baidu.com","tieba"},

            //qiubai
            {"UA","qiushibalke","Qiubai"},

            //zhangbai
            {"UA","BdMobile","Zhangbai"},

    };

    public static List<String> getMobileUAList() {
        final List<String> list = new ArrayList<String>();
        Collections.addAll(list, MOBILE_UA);
        return list;
    }

    public static void syncFp() {
        //save the fp to database
        SessionFactory sf=DatabaseLink.sessionfactory();
        Session s=sf.openSession();
        Transaction ts=s.beginTransaction();
        for (int i=0; i< FpConstants.FINGERPRINTS.length; i++) {
            App_fp af = new App_fp();
            af.setApp_name(FpConstants.FINGERPRINTS[i][2]);
            if (FpConstants.FINGERPRINTS[i][0].equals("Host")) {
                af.setUA(false);
            }

            af.setFingerprint(FpConstants.FINGERPRINTS[i][1]);
            s.save(af);
        }
        ts.commit();
        s.close();
    }

    public static void main(String[] args) {

        //save the fp to database
        SessionFactory sf=DatabaseLink.sessionfactory();
        Session s=sf.openSession();
        Transaction ts=s.beginTransaction();
        for (int i=0; i< FpConstants.FINGERPRINTS.length; i++) {
            App_fp af = new App_fp();
            af.setApp_name(FpConstants.FINGERPRINTS[i][2]);
            if (FpConstants.FINGERPRINTS[i][0].equals("Host")) {
                af.setUA(false);
            }

            af.setFingerprint(FpConstants.FINGERPRINTS[i][1]);
            s.save(af);
        }
        ts.commit();
        s.close();
    }
}
