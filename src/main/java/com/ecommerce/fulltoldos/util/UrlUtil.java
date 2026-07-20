package com.ecommerce.fulltoldos.util;

import jakarta.servlet.http.HttpServletRequest;

public class UrlUtil {

    private UrlUtil() {}

    /** URL base del sitio (esquema + host + puerto si no es el estándar), sin slash final. */
    public static String baseUrl(HttpServletRequest request) {
        int port = request.getServerPort();
        boolean puertoEstandar = ("http".equals(request.getScheme()) && port == 80)
                || ("https".equals(request.getScheme()) && port == 443);
        return request.getScheme() + "://" + request.getServerName()
                + (puertoEstandar ? "" : ":" + port);
    }
}
