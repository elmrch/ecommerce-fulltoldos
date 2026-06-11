package com.ecommerce.fulltoldos.service;


import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    // Guardar archivo y retornar la URL pública
    String guardarArchivo(MultipartFile archivo, String subcarpeta);

    // Eliminar archivo por su URL
    void eliminarArchivo(String url);

    // Validar que el archivo sea imagen permitida
    boolean esImagenValida(MultipartFile archivo);

    // Validar que el archivo sea video permitido
    boolean esVideoValido(MultipartFile archivo);
}