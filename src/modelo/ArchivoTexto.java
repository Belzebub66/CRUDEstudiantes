//Representa al CRUD de un archivo de texto
package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArchivoTexto {
    File fichero;
    FileWriter writer;
    PrintWriter pw;
    FileReader reader;
    BufferedReader buffer;
    int totReg;

    public ArchivoTexto() {
        this.fichero = null;
        this.writer = null;
        this.pw = null;
        this.reader = null;
        this.buffer = null;
        this.totReg = 0;
    }

    private boolean establecerFlujo(String nombreArchivo) {
        fichero = new File(nombreArchivo);
        return fichero.exists();
    }

    public boolean abrirArchivoTexto(char modo, String nombreArchivo) {
        try {
            boolean existe = this.establecerFlujo(nombreArchivo);
            switch (modo) {
                case 'w':
                    this.writer = new FileWriter(this.fichero, true); // este archivo sirve para leer un archivo
                                                                      // existente
                    this.pw = new PrintWriter(this.writer); // esta sirve para dar enter. No lee, no escribe, solo busca
                                                            // la ruta
                    break;
                case 'r':
                    if (!existe)
                        return false;
                    this.reader = new FileReader(this.fichero); // este es como un envoltorio
                    this.buffer = new BufferedReader(this.reader); // esta es forma de escritura mas facil para no
                                                                   // agregar saltos de linea
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(ArchivoTexto.class.getName()).log(Level.SEVERE, null, ex);

        }
        return true;
    }

    public void crearLinea(String linea) {
        this.pw.println(linea); // esta si escribe la información
    }

    public void cerrarArchivo(char modo) { // este metodo cierra el flujo de arriba
        try {
            switch (modo) {
                case 'w':
                    this.pw.close();
                    this.writer.close();
                    break;
                case 'r':
                    this.buffer.close();
                    this.reader.close();
                    break;
            }

        } catch (IOException ex) {
            Logger.getLogger(ArchivoTexto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    String obtenerLineas() { // devoolver todo los archivos de estudiantes.txt
        String lineas = "";
        try {
            String linea = null;// evita que haya informacion previa
            linea = buffer.readLine(); // lee la primera linea del archivo, al fnla del archivo hay null
            while (linea != null) { // lee y va almacenando
                this.totReg++;
                lineas += linea + "\n";
                linea = buffer.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(ArchivoTexto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lineas;
    }

    public String[][] obtenerMatrizRegistros(int numCol) {

        String lineas = this.obtenerLineas(); // nos devuelve todos los registros
        String lista[] = lineas.split("\n"); // cada que haya enter separa las lineas
        String registros[][] = new String[this.totReg][numCol]; // esta matriz se crea del total de registros, guarda
                                                                // numcontrol

        for (int f = 0; f < this.totReg; f++) {
            String datos[] = lista[f].split(","); // como devuelve todos los datos de ncontrol, este los separa por
                                                  // comasuna vez los separa los guarda en un array
            for (int c = 0; c < numCol; c++) { // así hace por cada registro
                registros[f][c] = datos[c]; // devuelve 123, juan, perez, b, informatica
            }
        }
        // System.out.println("Lineas:"+ lineas);
        return registros;
    }

    /*
     * public String[] buscarDato(String nc) {
     * try {
     * String linea=null;
     * linea = buffer.readLine();
     * int numReg=0;
     * linea = buffer.readLine();
     * linea = linea + "," + numReg;
     * while(linea!=null){
     * String datos[] = linea.split(",");
     * if (datos[0].equals(nc)){
     * //datos[6]+= numReg;
     * return datos;
     * }
     * numReg++;
     * linea = buffer.readLine();
     * linea = linea + "," + numReg;
     * }
     * } catch (IOException ex) {
     * Logger.getLogger(ArchivoTexto.class.getName()).log(Level.SEVERE, null, ex);
     * }
     * return null;
     * }
     */
    public String[] buscarDato(String nc) { // devuelve registro en donde coincidan los datos
        try {
            String linea = null;
            linea = buffer.readLine();

            while (linea != null) {
                String datos[] = linea.split(",");
                if (datos[0].equals(nc))
                    return datos;
                linea = buffer.readLine();

            }
        } catch (IOException ex) {
            Logger.getLogger(ArchivoTexto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void eliminarLinea(String numControl) {
        File ficheroTemporal = new File("temporal.txt"); // crea un archivo temporal
        // Abrir el archivo que se va a utilizar como temporal, se abre para escritura
        // fer
        try {
            this.writer = new FileWriter(ficheroTemporal);
            this.pw = new PrintWriter(this.writer);
        } catch (IOException ex) {
            Logger.getLogger(ArchivoTexto.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            String linea = null;
            linea = buffer.readLine();
            while (linea != null) {
                String datos[] = linea.split(",");
                // Paso a temporal todas las lineas distintas
                if (!(datos[0].equals(numControl))) // verifica que el nc sea igual, si es asi los pasa a temp, si no
                                                    // los deja en estudiantes
                    this.crearLinea(linea);
                linea = buffer.readLine();
            }
            // Cierro el archivo temporal.txt
            this.pw.close();
            this.writer.close();
        } catch (IOException ex) {
            Logger.getLogger(ArchivoTexto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modificarLinea(String nc, String nom, String ape, String sem, String gpo,
            String carrera) {
        File ficheroTemporal = new File("temporal.txt"); // crea un archivo temporal
        // Abrir el archivo que se va a utilizar como temporal, se abre para escritura
        // fer
        try {
            this.writer = new FileWriter(ficheroTemporal);
            this.pw = new PrintWriter(this.writer);
        } catch (IOException ex) {
            Logger.getLogger(ArchivoTexto.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            String linea = null;
            linea = buffer.readLine();
            while (linea != null) {
                String datos[] = linea.split(",");
                // Paso a temporal todas las lineas distintas
                if (!(datos[0].equals(nc))) // verifica que el nc sea igual, si es asi los pasa a temp, si no los deja
                                            // en
                                            // estudiantes
                    this.crearLinea(linea);
                else {
                    this.crearLinea(nc + "," + nom + "," + ape + "," + sem + "," + gpo + "," + carrera); // si es igual
                                                                                                         // lo modifica
                }
                linea = buffer.readLine();
            }
            // Cierro el archivo temporal.txt
            this.pw.close();
            this.writer.close();
        } catch (IOException ex) {
            Logger.getLogger(ArchivoTexto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarArchivo(String archivoEliminar) {
        if (this.establecerFlujo(archivoEliminar)) {
            this.fichero.delete(); // aqui borra el archivo que teniamos antes
        }
    }

    public void renombrarArchivo(String archivoRenombrar, String nuevoNombre) {
        if (this.establecerFlujo(archivoRenombrar)) {
            File fichero2 = new File(nuevoNombre);
            this.fichero.renameTo(fichero2); // pasa de temp a estudiantes
        }
    }
    // aqui debemos de crear el de actualizar, sebe ser parecido al de eliminar
    // linea al crear un archivo temporal y recibir un nc
    // validar al momento de pasar los registros que datos no sean los mismos que el
    // anterior

    // logica: buscar, modificar, guardar, confirmación
    // en el archivo binario la forma en la que escribes es la misma en la que lees,
    // pero el flujo es exactamente el mismo
}
