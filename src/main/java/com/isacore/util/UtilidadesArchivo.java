package com.isacore.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
}
