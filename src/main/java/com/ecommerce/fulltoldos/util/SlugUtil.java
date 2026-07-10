package com.ecommerce.fulltoldos.util;

import java.text.Normalizer;

/** Normaliza texto a slug de URL, igual que toSlug() en scrip.js (minúsculas, sin tildes, espacios/símbolos → guiones). */
public final class SlugUtil {

    private SlugUtil() { }

    public static String toSlug(String s) {
        if (s == null) return "";
        String n = Normalizer.normalize(s.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        n = n.replaceAll("[^a-z0-9]+", "-");
        return n.replaceAll("^-+|-+$", "");
    }
}
