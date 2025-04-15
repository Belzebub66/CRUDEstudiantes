package controlador;

import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.ArchivoTexto;
import vista.VistaCRUDEstudiantes;

public class ControladorCRUDEstudiantes {
    
    public VistaCRUDEstudiantes objVistaCRUDEst;
    ArchivoTexto objArchivo;
    DefaultTableModel modelo;
    public ControladorCRUDEstudiantes(VistaCRUDEstudiantes aThis, ArchivoTexto objArchivo) {
        this.objVistaCRUDEst=aThis;
        this.objArchivo=objArchivo;
        this.modelo = null;
    }
    public void llenarTabla(){
        String [] columnas = {"Num.Control", "Nombre", "Apellidos","Semestre", "Grupo", "Carrera"};        
        ArchivoTexto objArchivo = new ArchivoTexto();
        //Abrir Archivo
        objArchivo.abrirArchivoTexto('r', "estudiantes.txt");        
        //Lectura de datos desde el archivo (extraer los registros)
        String [][] filas = objArchivo.obtenerMatrizRegistros(6);        
        //Cerrar el archivo        
        objArchivo.cerrarArchivo('r');
        //DefaultTableModel modelo;
        modelo= new DefaultTableModel(filas,columnas);        
        this.objVistaCRUDEst.jtblEstudiantes.setModel(modelo);                        
    } 
    
    public void guardarRegistro(String nc, String nom, String ape, String sem, String gpo, String carrera){
        String linea= nc + "," + nom + ","+ ape + ","+ sem + ","+ gpo + ","+ carrera;
        this.objArchivo = new ArchivoTexto();
        this.objArchivo.abrirArchivoTexto('w', "estudiantes.txt");
        this.objArchivo.crearLinea(linea);
        this.objArchivo.cerrarArchivo('w');
        this.llenarTabla();
    }
    
    public String[] buscarRegistro(String nc){
        String[] datos=null;
        this.objArchivo = new ArchivoTexto();
        this.objArchivo.abrirArchivoTexto('r', "estudiantes.txt");
        datos=this.objArchivo.buscarDato(nc);
        this.objArchivo.cerrarArchivo('r');  
        return datos;
    }
    
    public void buscarSeleccionarRegistro(String nc){
        boolean encontrado=false;
        for(int i=0;i<modelo.getRowCount(); i++){
            String nControl = this.modelo.getValueAt(1,0).toString();
            if(nControl.equals(nc)){
                this.objVistaCRUDEst.jtblEstudiantes.setRowSelectionInterval(i,i);
                this.objVistaCRUDEst.jtblEstudiantes.setSelectionBackground(Color.blue);
                encontrado = true;
                break;
            }   
        }
        if(encontrado ==false)
            JOptionPane.showMessageDialog(objVistaCRUDEst,"No se encontro el registro");
    }
    
    public void eliminarRegistro(String numControl) {        
        this.objArchivo = new ArchivoTexto();
        this.objArchivo.abrirArchivoTexto('r', "estudiantes.txt");
        this.objArchivo.eliminarLinea(numControl);
        this.objArchivo.cerrarArchivo('r');   
        
        this.objArchivo.eliminarArchivo("estudiantes.txt");
        this.objArchivo.renombrarArchivo("temporal.txt", "estudiantes.txt");
        
        this.llenarTabla();
    }
}
