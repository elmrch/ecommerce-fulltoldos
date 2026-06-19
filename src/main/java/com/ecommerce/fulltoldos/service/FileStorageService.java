package com.ecommerce.fulltoldos.service;


import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String guardarArchivo(MultipartFile archivo, String subcarpeta);

    void eliminarArchivo(String url);

    boolean esImagenValida(MultipartFile archivo);

    boolean esVideoValido(MultipartFile archivo);
}
