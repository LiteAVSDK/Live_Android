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

        //推流地址格式：rtmp://8888.livepush.myqcloud.com/path/8888_test_12345_test?txSecret=aaaa&txTime=bbbb
        //拉流地址格式：rtmp://8888.liveplay.myqcloud.com/path/8888_test_12345_test
        //            http://8888.liveplay.myqcloud.com/path/8888_test_12345_test.flv
        //            http://8888.liveplay.myqcloud.com/path/8888_test_12345_test.m3u8


        String subString = strStreamUrl;

        {
            //1 截取第一个 ？之前的子串
            int index = subString.indexOf("?");
            if (index != -1) {
                subString = subString.substring(0, index);
            }
            if (subString == null || subString.length() == 0) {
                return null;
            }
        }

        {
            //2 截取最后一个 / 之后的子串
            int index = subString.lastIndexOf("/");
            if (index != -1) {
                subString = subString.substring(index + 1);
            }

            if (subString == null || subString.length() == 0) {
                return null;
            }
        }

        {
            //3 截取第一个 . 之前的子串
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

        //推流地址格式：rtmp://8888.livepush.myqcloud.com/path/8888_test_12345_test?txSecret=aaaa&txTime=bbbb
        //拉流地址格式：rtmp://8888.liveplay.myqcloud.com/path/8888_test_12345_test
        //            http://8888.liveplay.myqcloud.com/path/8888_test_12345_test.flv
        //            http://8888.liveplay.myqcloud.com/path/8888_test_12345_test.m3u8

        String subString = strStreamUrl;

        {
            //1 截取第一个 ？之前的子串
            int index = subString.indexOf("?");
            if (index != -1) {
                subString = subString.substring(0, index);
            }
            if (subString == null || subString.length() == 0) {
                return null;
            }
        }

        {
            //2 截取最后一个 / 之前的子串
            int index = subString.lastIndexOf("/");
            if (index != -1) {
                subString = subString.substring(0, index);
            }

            if (subString == null || subString.length() == 0) {
                return null;
            }
        }

        {
            //3 截取最后一个 / 之后的子串
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
