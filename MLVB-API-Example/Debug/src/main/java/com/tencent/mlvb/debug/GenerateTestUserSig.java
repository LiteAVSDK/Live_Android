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
 * Module:   GenerateTestUserSig
 * Function: 用于生成测试用的 UserSig，UserSig 是腾讯云为其云服务设计的一种安全保护签名。
 * 其计算方法是对 SDKAppID、UserID 和 EXPIRETIME 进行加密，加密算法为 HMAC-SHA256。
 * Attention: 请不要将如下代码发布到您的线上正式版本的 App 中，原因如下：
 * 本文件中的代码虽然能够正确计算出 UserSig，但仅适合快速调通 SDK 的基本功能，不适合线上产品，
 * 这是因为客户端代码中的 SECRETKEY 很容易被反编译逆向破解，尤其是 Web 端的代码被破解的难度几乎为零。
 * 一旦您的密钥泄露，攻击者就可以计算出正确的 UserSig 来盗用您的腾讯云流量。
 * 正确的做法是将 UserSig 的计算代码和加密密钥放在您的业务服务器上，然后由 App 按需向您的服务器获取实时算出的 UserSig。
 * 由于破解服务器的成本要高于破解客户端 App，所以服务器计算的方案能够更好地保护您的加密密钥。
 * Reference：https://cloud.tencent.com/document/product/647/17275#Server
 * Module: GenerateTestUserSig
 * Description: generates UserSig for testing. UserSig is a security signature designed by Tencent Cloud for its
 * cloud services.
 * It is calculated based on `SDKAppID`, `UserID`, and `EXPIRETIME` using the HMAC-SHA256 encryption
 * algorithm.
 * Attention: do not use the code below in your commercial application. This is because:
 * The code may be able to calculate UserSig correctly, but it is only for quick testing of the SDK’s
 * basic features, not for commercial applications.
 * `SECRETKEY` in client code can be easily decompiled and reversed, especially on web.
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
     * 腾讯云License管理页面(https://console.cloud.tencent.com/live/license)
     * 当前应用的License LicenseUrl
     * License Management View (https://console.cloud.tencent.com/live/license)
     * License URL of your application
     */
    public static final String LICENSEURL = "PLACEHOLDER";


    /**
     * 腾讯云License管理页面(https://console.cloud.tencent.com/live/license)
     * 当前应用的License Key
     * License Management View (https://console.cloud.tencent.com/live/license)
     * License key of your application
     */
    public static final String LICENSEURLKEY = "PLACEHOLDER";

    /**
     * 腾讯云 SDKAppId，需要替换为您自己账号下的 SDKAppId。
     * 进入腾讯云实时音视频[控制台](https://console.cloud.tencent.com/rav ) 创建应用，即可看到 SDKAppId，
     * 它是腾讯云用于区分客户的唯一标识。
     * Tencent Cloud `SDKAppID`. Set it to the `SDKAppID` of your account.
     * You can view your `SDKAppID` after creating an application in the [TRTC console](https://console.cloud.tencent
     * .com/rav).
     * `SDKAppID` uniquely identifies a Tencent Cloud account.
     */
    public static final int SDKAPPID = PLACEHOLDER;


    /**
     * 签名过期时间，建议不要设置的过短
     * 时间单位：秒
     * 默认时间：7 x 24 x 60 x 60 = 604800 = 7 天
     * Signature validity period, which should not be set too short
     * Unit: second
     * Default value: 604800 (7 days)
     */
    private static final int EXPIRETIME = 604800;


    /**
     * 计算签名用的加密密钥，获取步骤如下：
     * step1. 进入腾讯云实时音视频[控制台](https://console.cloud.tencent.com/rav )，如果还没有应用就创建一个，
     * step2. 单击您的应用，并进一步找到“快速上手”部分。
     * step3. 点击“查看密钥”按钮，就可以看到计算 UserSig 使用的加密的密钥了，请将其拷贝并复制到如下的变量中
     * 注意：该方案仅适用于调试Demo，正式上线前请将 UserSig 计算代码和密钥迁移到您的后台服务器上，以避免加密密钥泄露导致的流量盗用。
     * 文档：https://cloud.tencent.com/document/product/647/17275#Server
     * Follow the steps below to obtain the key required for UserSig calculation.
     * Step 1. Log in to the [TRTC console](https://console.cloud.tencent.com/rav), and create an application if you
     * don’t have one.
     * Step 2. Find your application, click “Application Info”, and click the “Quick Start” tab.
     * Step 3. Copy and paste the key to the code, as shown below.
     * Note: this method is for testing only. Before commercial launch, please migrate the UserSig calculation code
     * and key to your backend server to prevent key disclosure and traffic stealing.
     * Documentation: https://cloud.tencent.com/document/product/647/17275#Server
     */
    public static final String SECRETKEY = "PLACEHOLDER";

    /**
     * 配置的推流地址
     * 腾讯云域名管理页面：https://console.cloud.tencent.com/live/domainmanage
     */
    public static final String PUSH_DOMAIN = "PLACEHOLDER";

    /**
     * 配置的拉流地址
     * 腾讯云域名管理页面：https://console.cloud.tencent.com/live/domainmanage
     */
    public static final String PLAY_DOMAIN = "PLACEHOLDER";

    /**
     * 如果开通鉴权配置的鉴权Key
     * 注意：该方案仅适用于调试Demo，正式上线前请将 安全地址生成逻辑迁移到您的后台服务器上，以避免信息泄漏
     * 详细可参考 https://console.cloud.tencent.com/live/domainmanage 页面 -》 推流配置 -》 鉴权配置
     */
    public static final String LIVE_URL_KEY = "PLACEHOLDER";


    /**
     * 计算 UserSig 签名
     * 函数内部使用 HMAC-SHA256 非对称加密算法，对 SDKAPPID、userId 和 EXPIRETIME 进行加密。
     *
     * 请不要将如下代码发布到您的线上正式版本的 App 中，原因如下：
     * 本文件中的代码虽然能够正确计算出 UserSig，但仅适合快速调通 SDK 的基本功能，不适合线上产品，
     * 这是因为客户端代码中的 SECRETKEY 很容易被反编译逆向破解，尤其是 Web 端的代码被破解的难度几乎为零。
     * 一旦您的密钥泄露，攻击者就可以计算出正确的 UserSig 来盗用您的腾讯云流量。
     * 正确的做法是将 UserSig 的计算代码和加密密钥放在您的业务服务器上，然后由 App 按需向您的服务器获取实时算出的 UserSig。
     * 由于破解服务器的成本要高于破解客户端 App，所以服务器计算的方案能够更好地保护您的加密密钥。
     * 文档：https://cloud.tencent.com/document/product/647/17275#Server
     *
     * Calculating UserSig
     * The asymmetric encryption algorithm HMAC-SHA256 is used in the function to calculate UserSig based on
     * `SDKAppID`, `UserID`, and `EXPIRETIME`.
     *
     * do not use the code below in your commercial application. This is because:
     * The code may be able to calculate UserSig correctly, but it is only for quick testing of the SDK’s basic
     * features, not for commercial applications.
     * `SECRETKEY` in client code can be easily decompiled and reversed, especially on web.
     * Once your key is disclosed, attackers will be able to steal your Tencent Cloud traffic.
     * The correct method is to deploy the `UserSig` calculation code on your project server so that your application
     * can request from your server a `UserSig` that is calculated whenever one is needed.
     * Given that it is more difficult to hack a server than a client application, server-end calculation can better
     * protect your key.
     * Documentation: https://cloud.tencent.com/document/product/647/17275#Server
     */
    public static String genTestUserSig(String userId) {
        return genTLSSignature(SDKAPPID, userId, EXPIRETIME, null, SECRETKEY);
    }

    /**
     * 生成 tls 票据
     *
     * @param sdkAppId      应用的 appid
     * @param userId        用户 id
     * @param expire        有效期，单位是秒
     * @param userBuf       默认填写null
     * @param priKeyContent 生成 tls 票据使用的私钥内容
     * @param sdkAppId      `appid` of your application
     * @param userId        User ID
     * @param expire        Validity period, in seconds
     * @param userBuf       `null` by default
     * @param priKeyContent Private key required for generating a TLS ticket
     * @return 如果出错，会返回为空，或者有异常打印，成功返回有效的票据
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
