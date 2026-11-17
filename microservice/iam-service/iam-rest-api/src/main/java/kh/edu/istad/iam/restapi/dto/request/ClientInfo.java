package kh.edu.istad.iam.restapi.dto.request;

import jakarta.servlet.http.HttpServletRequest;

public record ClientInfo(String ip, String userAgent) {

    public static ClientInfo from(HttpServletRequest request) {
        if (request == null) {
            return new ClientInfo(null, null);
        }
        return new ClientInfo(request.getRemoteAddr(), request.getHeader("User-Agent"));
    }
}
