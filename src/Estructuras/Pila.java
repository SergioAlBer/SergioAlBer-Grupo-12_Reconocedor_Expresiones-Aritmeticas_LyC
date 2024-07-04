package Estructuras;

public class Pila{
    
    NodoPila cabeza = null;
    int tamanio =0;
    
    public void setCabeza(NodoPila cabeza){
        this.cabeza = cabeza;
    }
    public NodoPila getCabeza(){
        return cabeza;
    }
    
    public void insertar(String simbolo, String tipo) {
        if(cabeza == null){
            cabeza = new NodoPila(simbolo, tipo);
        }
        else{
            NodoPila nuevo = new NodoPila(simbolo, tipo);
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
        }
        tamanio++;
    }
    
    
    //Solo se considera la prioridad dentro de la pila
    public void insertar(String id, int prioridad) {
        if(cabeza == null){
            cabeza = new NodoPila(id, prioridad);
        }
        else{
            NodoPila nuevo = new NodoPila(id,prioridad);
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
        }
        tamanio++;
    }
    
    public NodoPila pop(){
        NodoPila puntero = null;
        
        if(!this.isEmpity()){
            puntero = cabeza;
            cabeza = cabeza.siguiente;
            puntero.siguiente = null;
            tamanio--;
        }
        
        return puntero;

    }
    
    public boolean isEmpity(){
        return tamanio==0;
    }
    
    public class NodoPila{
        String simbolo;
        int prioridad;
        String tipo = "OP"; //valor por defecto
        /* 
           ID = para variables
           F  = para valores flotantes
           E  = para enteros
           OP = para operadores
        */
        NodoPila siguiente;
        
        //Aplicable para los ID's
        public NodoPila(String simbolo, String tipo){
            this.simbolo = simbolo;
            this.tipo = tipo;
        }
        //Aplicable para los Operadores
        public NodoPila(String simbolo, int prioridad){
            this.simbolo = simbolo;
            this.prioridad = prioridad;
        }
        
        public String toString(){
            return this.simbolo +"   Con tipo"+this.tipo;
        }
    }
}
