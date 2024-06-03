package com.tencent.mlvb.timeshift;

public class TimeShiftHelper {

    private String  mDomain     = "";

    public TimeShiftHelper(String domain) {
        mDomain = domain;
    }

    public String getTimeShiftUrl(String liveUrl, long delay) {
        String appName = getAppNameByStreamUrl(liveUrl);
        if (appName == null) {
            return "";
        }
        String streamId = getStreamIDByStreamUrl(liveUrl);
        if (streamId == null) {
            return "";
        }
        // http://[Domain]/timeshift/[AppName]/[StreamName]/timeshift.m3u8?delay=xxx
        String url = String.format("http://%s/timeshift/%s/%s/timeshift.m3u8?delay=%d", mDomain, appName, streamId, Math.max(delay, 90));
        return url;
    }

    public static String getStreamIDByStreamUrl(String strStreamUrl) {
        if (strStreamUrl == null || strStreamUrl.length() == 0) {
            return null;
        }

        //Push address format: rtmp://8888.livepush.myqcloud.com/path/8888_test_12345_test?txSecret=aaaa&txTime=bbbb
        //Pulling address format: rtmp://8888.liveplay.myqcloud.com/path/8888_test_12345_test
        // http://8888.liveplay.myqcloud.com/path/8888_test_12345_test.flv
        // http://8888.liveplay.myqcloud.com/path/8888_test_12345_test.m3u8

        String subString = strStreamUrl;

        {
            //1 intercept the first one? previous substring
            int index = subString.indexOf("?");
            if (index != -1) {
                subString = subString.substring(0, index);
            }
            if (subString == null || subString.length() == 0) {
                return null;
            }
        }

        {
            //2 intercept the substring after the last
            int index = subString.lastIndexOf("/");
            if (index != -1) {
                subString = subString.substring(index + 1);
            }

            if (subString == null || subString.length() == 0) {
                return null;
            }
        }

        {
            //3 Intercept the substring before the first
            int index = subString.indexOf(".");
            if (index != -1) {
                subString = subString.substring(0, index);
            }
            if (subString == null || subString.length() == 0) {
                return null;
            }
        }

        return subString;
    }

    public static String getAppNameByStreamUrl(String strStreamUrl) {
        if (strStreamUrl == null || strStreamUrl.length() == 0) {
            return null;
        }

        //Push address format: rtmp://8888.livepush.myqcloud.com/path/8888_test_12345_test?txSecret=aaaa&txTime=bbbb
        //Pulling address format: rtmp://8888.liveplay.myqcloud.com/path/8888_test_12345_test
        // http://8888.liveplay.myqcloud.com/path/8888_test_12345_test.flv
        // http://8888.liveplay.myqcloud.com/path/8888_test_12345_test.m3u8

        String subString = strStreamUrl;

        {
            //1 intercept the first one? previous substring
            int index = subString.indexOf("?");
            if (index != -1) {
                subString = subString.substring(0, index);
            }
            if (subString == null || subString.length() == 0) {
                return null;
            }
        }

        {
            //2 intercept the substring before the last
            int index = subString.lastIndexOf("/");
            if (index != -1) {
                subString = subString.substring(0, index);
            }

            if (subString == null || subString.length() == 0) {
                return null;
            }
        }

        {
            //3 intercept the substring after the last
            int index = subString.lastIndexOf("/");
            if (index != -1) {
                subString = subString.substring(index + 1);
            }

            if (subString == null || subString.length() == 0) {
                return null;
            }
        }

        return subString;
    }

}
