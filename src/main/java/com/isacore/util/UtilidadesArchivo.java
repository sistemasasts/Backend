package com.isacore.util;

import com.isacore.quality.exception.PncErrorException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UtilidadesArchivo {
    public static void compressZip(Map<String, String> archivos, String ruta) throws Exception {
        // crea un buffer temporal para ir poniendo los archivos a comprimir
        ZipOutputStream zous = new ZipOutputStream(new FileOutputStream(ruta));
        archivos.forEach((nombre, path) -> {

            try {
                //nombre con el que se va guardar el archivo dentro del zip
                ZipEntry entrada = new ZipEntry(nombre);
                zous.putNextEntry(entrada);
                System.out.println("Comprimiendo.....");
                //obtiene el archivo para irlo comprimiendo
                FileInputStream fis = new FileInputStream(path);
                int leer;
                byte[] buffer = new byte[1024];
                while (0 < (leer = fis.read(buffer))) {
                    zous.write(buffer, 0, leer);
                }
                fis.close();
                zous.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println("Nombre del Archivo: " + entrada.getName());

        });
        zous.close();
    }
    public static String crearPathArchivo(long secuencial, String nombreArchivo, String rutaBase, String proceso) {
        String path = crearRutaAlmacenamiento(secuencial,rutaBase, proceso).concat(File.separator).concat(nombreArchivo);
        if (PassFileToRepository.fileExists(path))
            path = crearRutaAlmacenamiento(secuencial, rutaBase, proceso).concat(File.separator).concat(PassFileToRepository.generateDateAsId()).concat("_").concat(nombreArchivo);

        return path;
    }

    private static String crearRutaAlmacenamiento(long secuencial, String rutaBase, String proceso) {
        try {
            String carpeta =rutaBase.concat(File.separator).concat(proceso).concat(File.separator).concat(String.valueOf(secuencial));
            Path path = Paths.get(carpeta);
            if (!Files.exists(path))
                Files.createDirectories(path);
            return carpeta;
        } catch (IOException e) {
            throw new PncErrorException("Error al crear el directorio");
        }
    }

}
