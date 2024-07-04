package Analizadores;

import Estructuras.listas;


public class Tokens {
    
    
    private String simbolo;
    private int tipo; 
    
    static Tabla[] tablaPalabrasReservadas = {
                        new Tabla("entero", 0),
                        new Tabla("real", 1),
                        new Tabla("lee", 2),
                        new Tabla("escribe", 3)
                     };
    
    static Tabla[] tablaOperadores = {
                      new Tabla("<<", 4),
                      new Tabla(">>", 5),
                      new Tabla(",", 6),
                      new Tabla(".", 7),
                      new Tabla("*", 8,2),
                      new Tabla("+", 9,1),
                      new Tabla("-", 10,1),
                      new Tabla("/", 11),
                      new Tabla("=", 12),
                      new Tabla("(",13,0)
                     };
    
    //TABLA DE SÍMBOLOS PARA LOS IDENTIFICADORES
    static listas lista = new listas();
    
    //Insertar el simbolo,valor,tipo
    //Ejemplo entero a = 5 → simbolo = a, valor = 5, tipo = entero
    //Ejemplo real b = 1.5 → simbolo = b, valor = 1.5, tipo = real
    public static boolean insertarID(String id, String valor, String tipo){
        
        //Para que no se repitan los identificadores ID
        while(!lista.existe(id)){
            //Si el simbolo no existe en nuestra lista, se inserta en la lista
            return lista.insertarTS(id, valor, tipo);
        }
        return false;
    }
    
    public static boolean existeVariable(String id){
        return lista.existe(id);
    }
    
    public static void modificarVarlo(String id, String valor){
        lista.modificarValor(id, valor);
    }
    public static String traerValor(String id){
        return lista.traerValor(id);
    }
    
    public static String traerTipo(String id){
        return lista.traerTipo(id);
    }
    
    public boolean esReservada(String token){
        for(int i=0;i<this.tablaPalabrasReservadas.length;i++){
            if(token.equals(this.tablaPalabrasReservadas[i].getPr())){
                return true;
            }
        }
        return false;
    }
    
    /*Devuelve la posición de la columna, de la Matriz Transitiva, 
    en la que se encuentra nuestro token*/
    public int traerPosicion(Tokens token){
        
        
        switch (token.getTipo()) {
            case 0:
                for (int j = 0; j < tablaPalabrasReservadas.length; j++) {
                    if(token.getSimbolo().equals(this.tablaPalabrasReservadas[j].getPr()))
                        return this.tablaPalabrasReservadas[j].getPosicion();
                }   break;
            case 1:
                return 14;
            case 2:
                for (int j = 0; j < tablaOperadores.length; j++) {
                    if(token.getSimbolo().equals(this.tablaOperadores[j].getPr()))
                        return this.tablaOperadores[j].getPosicion();
                }   break;
            case 3:
                return 13;
            default:
                break;
        }
        
        return 15;
    }
    
    //CLASE PARA LA CREACIÓN DE TABLAS
    private static class Tabla{
        String pr;
        int posicion;
        int prioridad; // SOLO APLICA PARA LOS OPERADORES
        
        public Tabla(String pr, int posicion){
            this.pr = pr;
            this.posicion = posicion;
        }
        
        public Tabla(String pr, int posicion, int prioridad){
            this.prioridad = prioridad;
            this.pr = pr;
            this.posicion = posicion;
        }

        public String getPr() {
            return pr;
        }

        public void setPr(String pr) {
            this.pr = pr;
        }

        public int getPosicion() {
            return posicion;
        }

        public void setPosicion(int posicion) {
            this.posicion = posicion;
        }
         
    }
    
    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String nombre) {
        this.simbolo = nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
}
