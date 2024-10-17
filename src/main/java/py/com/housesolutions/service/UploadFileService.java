package py.com.housesolutions.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadFileService {
    private String folder = "images//";

    /*
    * Este método tiene como objetivo guardar una imagen (o archivo) en un sistema de archivos
    * y devolver el nombre del archivo guardado.
    * ¿Qué es?: MultipartFile: Es una interfaz de Spring que representa un archivo cargado
    * a través de un formulario HTML. Este archivo podría ser cualquier cosa, pero
    * generalmente en este contexto, se trata de una imagen.
    *
    * throws IOException: ¿Qué hace?: Este método declara que puede lanzar una excepción de tipo IOException.
    * Propósito: El manejo de archivos puede producir errores (por ejemplo, si la ruta de destino no existe,
    * o si el archivo no se puede escribir), y IOException es la excepción que manejaría estos problemas.
    * Al declararla con throws, indicas que este método podría lanzar esta excepción y que quien lo llame
    * debe estar preparado para manejarla.
    * */
    public String saveImage(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            /*
            * ¿Qué hace?: Este if verifica si el archivo no está vacío, es decir, si
            * realmente se ha cargado un archivo.
            * Propósito: Se asegura de que sólo se procesen archivos que contengan datos. Si no
            * se ha subido ningún archivo, la condición es falsa, y el código no intenta guardar nada.
            * */

            //¿Qué hace?: Esta línea convierte el archivo cargado (file) en un array de bytes.
            //Propósito: Los archivos se manejan como secuencias de bytes para poder ser escritos
            // en el sistema de archivos.
            byte [] bytes = file.getBytes();

            //¿Qué hace?: Esta línea construye una ruta (Path) en el sistema de archivos donde se guardará el archivo.
            //Componentes:
            // folder: Se espera que folder sea una variable que contiene la ruta del directorio donde se guardarán las imágenes.
            // file.getOriginalFilename(): Devuelve el nombre original del archivo que se ha cargado, incluyendo su extensión (por ejemplo, imagen.jpg).
            // Propósito: Se construye la ruta completa donde se guardará la imagen, combinando el directorio (folder) con el nombre original del archivo.
            Path path = Paths.get(folder + file.getOriginalFilename());

            //¿Qué hace?: Escribe el array de bytes (bytes) en la ubicación especificada por path.
            //Propósito: Guardar físicamente el archivo en el sistema de archivos.
            Files.write(path, bytes);

            //¿Qué hace?: Retorna el nombre original del archivo cargado.
            //Propósito: Después de guardar el archivo, se retorna su nombre original, posiblemente para almacenarlo en una base de datos o para otro uso posterior.
            return file.getOriginalFilename(); //retorna la imagen existente.
        }

        //¿Qué hace?: Si el archivo está vacío (es decir, si no se subió ningún archivo), retorna "default.jpg".
        //Propósito: Este es un valor por defecto. Si el usuario no carga una imagen, el sistema utilizará una imagen predeterminada (probablemente almacenada en el servidor) llamada "default.jpg".
        return "default.jpg"; //retorna el nombre de la imagen por defecto.
    }

    public void deleteImage(String nombre) {
        String ruta = "images//";
        File file = new File(ruta + nombre);
        file.delete();
    }

}
