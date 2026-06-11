package com.ecommerce.fulltoldos.service.impl;

import com.ecommerce.fulltoldos.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    // Ruta base definida en application.properties
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private static final List<String> IMAGENES_PERMITIDAS =
            Arrays.asList("image/jpeg", "image/png", "image/webp", "image/gif", "image/jfif");

    private static final List<String> VIDEOS_PERMITIDOS =
            Arrays.asList("video/mp4", "video/webm", "video/ogg", "video/quicktime",
                          "video/x-msvideo", "video/x-matroska");

    @Override
    public String guardarArchivo(MultipartFile archivo, String subcarpeta) {
        try {
            Path carpeta = Paths.get(uploadDir).toAbsolutePath().resolve(subcarpeta);
            Files.createDirectories(carpeta);

            // Nombre único para evitar colisiones
            String extension  = obtenerExtension(archivo.getOriginalFilename());
            String nombreFile = UUID.randomUUID().toString() + "." + extension;

            // Guardar en disco
            Path destino = carpeta.resolve(nombreFile);
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            // Retornar URL pública
            return "/uploads/" + subcarpeta + "/" + nombreFile;

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo: " + e.getMessage());
        }
    }

    @Override
    public void eliminarArchivo(String url) {
        if (url == null || url.isEmpty()) return;
        try {
            // url es "/uploads/productos/uuid.jpg" → quitar el prefijo "/uploads/"
            String subpath = url.startsWith("/uploads/") ? url.substring("/uploads/".length()) : url;
            Path archivo = Paths.get(uploadDir).toAbsolutePath().resolve(subpath);
            Files.deleteIfExists(archivo);
        } catch (IOException e) {
            System.err.println("No se pudo eliminar: " + url);
        }
    }

    @Override
    public boolean esImagenValida(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) return false;
        return IMAGENES_PERMITIDAS.contains(archivo.getContentType());
    }

    @Override
    public boolean esVideoValido(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) return false;
        return VIDEOS_PERMITIDOS.contains(archivo.getContentType());
    }

    // ── Privado ──
    private String obtenerExtension(String nombreOriginal) {
        if (nombreOriginal == null || !nombreOriginal.contains(".")) return "jpg";
        return nombreOriginal.substring(nombreOriginal.lastIndexOf('.') + 1).toLowerCase();
    }
}
