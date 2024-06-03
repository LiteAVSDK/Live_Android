package com.tencent.mlvb.debug;


import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.Deflater;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Module: GenerateTestUserSig
 * Description: generates UserSig for testing. UserSig is a security signature designed by Tencent Cloud for its
 * cloud services.
 * It is calculated based on `SDKAppID`, `UserID`, and `EXPIRETIME` using the HMAC-SHA256 encryption
 * algorithm.
 * Attention: do not use the code below in your commercial application. This is because:
 * The code may be able to calculate UserSig correctly, but it is only for quick testing of the SDK’s
 * basic features, not for commercial applications.
 * `SDKSECRETKEY` in client code can be easily decompiled and reversed, especially on web.
 * Once your key is disclosed, attackers will be able to steal your Tencent Cloud traffic.
 * The correct method is to deploy the `UserSig` calculation code and encryption key on your project
 * server so that your application can request from your server a `UserSig` that is calculated whenever
 * one is needed.
 * Given that it is more difficult to hack a server than a client application, server-end calculation can
 * better protect your key.
 * Reference: https://cloud.tencent.com/document/product/647/17275#Server
 */
public class GenerateTestUserSig {


    /**
     * License Management View (https://console.cloud.tencent.com/live/license)
     * License URL of your application
     */
    public static final String LICENSEURL =
            "PLACEHOLDER";


    /**
     * License Management View (https://console.cloud.tencent.com/live/license)
     * License key of your application
     */
    public static final String LICENSEURLKEY = "PLACEHOLDER";

    /**
     * Tencent Cloud `SDKAppID`. Set it to the `SDKAppID` of your account.
     * You can view your `SDKAppID` after creating an application in the [TRTC console](https://console.cloud.tencent
     * .com/rav).
     * `SDKAppID` uniquely identifies a Tencent Cloud account.
     */
    public static final int SDKAPPID = PLACEHOLDER;


    /**
     * Signature validity period, which should not be set too short
     * Unit: second
     * Default value: 604800 (7 days)
     */
    private static final int EXPIRETIME = 604800;


    /**
     * Follow the steps below to obtain the key required for UserSig calculation.
     * Step 1. Log in to the [TRTC console](https://console.cloud.tencent.com/rav), and create an application if you
     * don’t have one.
     * Step 2. Find your application, click “Application Info”, and click the “Quick Start” tab.
     * Step 3. Copy and paste the key to the code, as shown below.
     * Note: this method is for testing only. Before commercial launch, please migrate the UserSig calculation code
     * and key to your backend server to prevent key disclosure and traffic stealing.
     * Documentation: https://cloud.tencent.com/document/product/647/17275#Server
     */
    public static final String SDKSECRETKEY = "PLACEHOLDER";

    /**
     * Configured push address
     * Tencent Cloud domain name management page: https://console.cloud.tencent.com/live/domainmanage
     */
    public static final String PUSH_DOMAIN = "PLACEHOLDER";

    /**
     * Configured streaming address
     * Tencent Cloud domain name management page: https://console.cloud.tencent.com/live/domainmanage
     */
    public static final String PLAY_DOMAIN = "PLACEHOLDER";

    /**
     * If the authentication key of the authentication configuration is enabled
     * Note: This solution is only suitable for debugging Demo.
     * Please migrate the secure address generation logic to your backend server
     * before official launch to avoid information leakage.
     * For details, please refer to the
     * https://console.cloud.tencent.com/live/domainmanage page -》 Push configuration -》 Authentication configuration
     */
    public static final String LIVE_URL_KEY = "PLACEHOLDER";


    /**
     * Calculating UserSig
     * The asymmetric encryption algorithm HMAC-SHA256 is used in the function to calculate UserSig based on
     * `SDKAppID`, `UserID`, and `EXPIRETIME`.
     *
     * do not use the code below in your commercial application. This is because:
     * The code may be able to calculate UserSig correctly, but it is only for quick testing of the SDK’s basic
     * features, not for commercial applications.
     * `SDKSECRETKEY` in client code can be easily decompiled and reversed, especially on web.
     * Once your key is disclosed, attackers will be able to steal your Tencent Cloud traffic.
     * The correct method is to deploy the `UserSig` calculation code on your project server so that your application
     * can request from your server a `UserSig` that is calculated whenever one is needed.
     * Given that it is more difficult to hack a server than a client application, server-end calculation can better
     * protect your key.
     * Documentation: https://cloud.tencent.com/document/product/647/17275#Server
     */
    public static String genTestUserSig(String userId) {
        return genTLSSignature(SDKAPPID, userId, EXPIRETIME, null, SDKSECRETKEY);
    }

    /**
     * Generate tls ticket
     *
     * @param sdkAppId      appid of application
     * @param userId        user id
     * @param expire        Validity period, unit is seconds
     * @param userBuf       Fill in null by default
     * @param priKeyContent Contents of the private key used to generate tls tickets
     * @param sdkAppId      `appid` of your application
     * @param userId        User ID
     * @param expire        Validity period, in seconds
     * @param userBuf       `null` by default
     * @param priKeyContent Private key required for generating a TLS ticket
     * @return If an error occurs, it will return empty, or an exception will be printed,
     * and a valid ticket will be returned successfully.
     *     Generating a TLS Ticket
     * @return If an error occurs, an empty string will be returned or exceptions printed. If the operation succeeds,
     *     a valid ticket will be returned.
     */
    private static String genTLSSignature(long sdkAppId, String userId, long expire, byte[] userBuf,
                                          String priKeyContent) {
        long currTime = System.currentTimeMillis() / 1000;
        JSONObject sigDoc = new JSONObject();
        try {
            sigDoc.put("TLS.ver", "2.0");
            sigDoc.put("TLS.identifier", userId);
            sigDoc.put("TLS.sdkappid", sdkAppId);
            sigDoc.put("TLS.expire", expire);
            sigDoc.put("TLS.time", currTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String base64UserBuf = null;
        if (null != userBuf) {
            base64UserBuf = Base64.encodeToString(userBuf, Base64.NO_WRAP);
            try {
                sigDoc.put("TLS.userbuf", base64UserBuf);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String sig = hmacsha256(sdkAppId, userId, currTime, expire, priKeyContent, base64UserBuf);
        if (sig.length() == 0) {
            return "";
        }
        try {
            sigDoc.put("TLS.sig", sig);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Deflater compressor = new Deflater();
        compressor.setInput(sigDoc.toString().getBytes(Charset.forName("UTF-8")));
        compressor.finish();
        byte[] compressedBytes = new byte[2048];
        int compressedBytesLength = compressor.deflate(compressedBytes);
        compressor.end();
        return new String(base64EncodeUrl(Arrays.copyOfRange(compressedBytes, 0, compressedBytesLength)));
    }


    private static String hmacsha256(long sdkappid, String userId, long currTime, long expire, String priKeyContent,
                                     String base64Userbuf) {
        String contentToBeSigned =
                "TLS.identifier:" + userId + "\n" + "TLS.sdkappid:" + sdkappid + "\n" + "TLS.time:" + currTime + "\n"
                        + "TLS.expire:" + expire + "\n";
        if (null != base64Userbuf) {
            contentToBeSigned += "TLS.userbuf:" + base64Userbuf + "\n";
        }
        try {
            byte[] byteKey = priKeyContent.getBytes("UTF-8");
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, "HmacSHA256");
            hmac.init(keySpec);
            byte[] byteSig = hmac.doFinal(contentToBeSigned.getBytes("UTF-8"));
            return new String(Base64.encode(byteSig, Base64.NO_WRAP));
        } catch (UnsupportedEncodingException e) {
            return "";
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (InvalidKeyException e) {
            return "";
        }
    }

    private static byte[] base64EncodeUrl(byte[] input) {
        byte[] base64 = new String(Base64.encode(input, Base64.NO_WRAP)).getBytes();
        for (int i = 0; i < base64.length; ++i) {
            switch (base64[i]) {
                case '+':
                    base64[i] = '*';
                    break;
                case '/':
                    base64[i] = '-';
                    break;
                case '=':
                    base64[i] = '_';
                    break;
                default:
                    break;
            }
        }
        return base64;
    }


    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String getSafeUrl(String streamName) {
        long txTime = System.currentTimeMillis() / 1000 + 60 * 60;
        String input = new StringBuilder().append(LIVE_URL_KEY).append(streamName)
                .append(Long.toHexString(txTime).toUpperCase()).toString();
        String txSecret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            txSecret = byteArrayToHexString(messageDigest.digest(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new StringBuilder().append("?txSecret=").append(txSecret).append("&").append("txTime=")
                .append(Long.toHexString(txTime).toUpperCase()).toString();
    }

    private static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];
        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }
}
