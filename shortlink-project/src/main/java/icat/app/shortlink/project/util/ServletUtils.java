package icat.app.shortlink.project.util;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;

public class ServletUtils {

    /**
     * 获取用户真实IP
     * @param request 用户请求
     * @return 用户真实IP
     */
    public static String getClientIp(HttpServletRequest request) {
        String[] headers = new String[]{"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        String ip;
        for (String header : headers) {
            ip = request.getHeader(header);
            if (!NetUtil.isUnknown(ip)) {
                return NetUtil.getMultistageReverseProxyIp(ip);
            }
        }
        ip = request.getRemoteAddr();
        return NetUtil.getMultistageReverseProxyIp(ip);
    }

    /**
     * 获取用户访问浏览器
     * @param request 请求
     * @return 访问浏览器
     */
    public static String getBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.toLowerCase().contains("edg")) {
            return "Microsoft Edge";
        } else if (userAgent.toLowerCase().contains("chrome")) {
            return "Google Chrome";
        } else if (userAgent.toLowerCase().contains("firefox")) {
            return "Mozilla Firefox";
        } else if (userAgent.toLowerCase().contains("safari")) {
            return "Apple Safari";
        } else if (userAgent.toLowerCase().contains("opera")) {
            return "Opera";
        } else if (userAgent.toLowerCase().contains("msie") || userAgent.toLowerCase().contains("trident")) {
            return "Internet Explorer";
        } else {
            return "Unknown";
        }
    }

    /**
     * 获取用户访问设备
     *
     * @param request 请求
     * @return 访问设备
     */
    public static String getDevice(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.toLowerCase().contains("mobile")) {
            return "Mobile";
        }
        return "PC";
    }

    public static String getSystemInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");

        String osName = "Unknown";

        if (StrUtil.isNotBlank(userAgent)) {
            userAgent = userAgent.toLowerCase();

            if (userAgent.contains("windows")) {
                osName = "Windows";
            } else if (userAgent.contains("mac")) {
                osName = "Mac";
            } else if (userAgent.contains("linux")) {
                osName = "Linux";
            } else if (userAgent.contains("android")) {
                osName = "Android";
            } else if (userAgent.contains("iphone")) {
                osName = "IOS";
            }
        }
        return osName;
    }
}
