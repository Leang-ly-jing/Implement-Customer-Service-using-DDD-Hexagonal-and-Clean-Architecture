package kh.edu.istad.iam.restapi.util;

import org.springframework.stereotype.Component;

@Component
public class UserAgentParser {

    public record DeviceInfo(String browserAndOs, String deviceType) {}

    public DeviceInfo parse(String ua) {
        if (ua == null || ua.isBlank()) {
            return new DeviceInfo("Unknown Device", "desktop");
        }

        String os = "Unknown OS";
        String deviceType = "desktop";

        if (ua.contains("iPad")) {
            os = "iPadOS";
            deviceType = "tablet";
        } else if (ua.contains("iPhone") || ua.contains("iPod")) {
            os = "iOS";
            deviceType = "mobile";
        } else if (ua.contains("Android")) {
            os = "Android";
            deviceType = ua.contains("Mobile") ? "mobile" : "tablet";
        } else if (ua.contains("Macintosh") || ua.contains("Mac OS X")) {
            os = "macOS";
            deviceType = "desktop";
        } else if (ua.contains("Windows")) {
            os = "Windows";
            deviceType = "desktop";
        } else if (ua.contains("Linux")) {
            os = "Linux";
            deviceType = "desktop";
        }

        String browser = "Browser";
        if (ua.contains("Edg/")) {
            browser = "Edge";
        } else if (ua.contains("Chrome/") && !ua.contains("Edg/")) {
            browser = "Chrome";
        } else if (ua.contains("Firefox/")) {
            browser = "Firefox";
        } else if (ua.contains("Safari/") && !ua.contains("Chrome/")) {
            browser = "Safari";
        } else if (ua.contains("Opera/") || ua.contains("OPR/")) {
            browser = "Opera";
        }

        return new DeviceInfo(browser + " on " + os, deviceType);
    }
}
