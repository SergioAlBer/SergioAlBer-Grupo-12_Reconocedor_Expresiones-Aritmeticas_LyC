package Estructuras;

public class listas {
    
    Nodo cabeza = null;
    int tamanio=0;
    
    public boolean insertarTS(String id, String valor, String tipo){
        if(cabeza == null){
            cabeza = new Nodo();
            cabeza.id = id;
            cabeza.valor = valor;
            cabeza.tipo = tipo;     
        }
        else{
            Nodo nuevo = new Nodo();
            nuevo.id = id;
            nuevo.valor = valor;
            nuevo.tipo = tipo;
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
        }
    
        tamanio++;
        return true;
    }
    
    
    //public void setCabeza
    public void setCabeza(Nodo cabeza){
        this.cabeza = cabeza;
    }
    //Traer le puntero de la cabeza
    public Nodo getCabeza(){
        return cabeza;
    }
    
    //Conocer si la lista esta vacia
    public boolean isEmpity(){
        return tamanio ==0;
    }
    
    //Método para modificar el valor de mis variables
    public boolean modificarValor(String ident, String valor){
        
        Nodo puntero = cabeza;
        
        while(puntero !=null){
            if(puntero.id.equals(ident)){
                puntero.valor = valor;
                return true;
            }
            puntero = puntero.siguiente;
        }
        return false;
    }
    
    //Buscar el valor de la variable, en función de su ID
    public String traerValor(String ident){
        Nodo puntero = cabeza;
        
        while(puntero !=null){
            if(puntero.id.equals(ident)){
                return puntero.valor;
            }
            puntero = puntero.siguiente;
        }
        
        return null;//PARA EL CASO QUE NO SE HAYA ENCONTRADO
    }
    
    //Para evitar la duplicidad de valores
    public boolean existe(String id){        
        Nodo puntero = cabeza;
        
        while(puntero != null){
            if(puntero.id.equals(id)){
                return true;
            }
            puntero = puntero.siguiente;
        }
        return false;
    }
    //Buscar el tipo al que pertenece la variable, en función de su ID
    public String traerTipo(String id){
        Nodo puntero = cabeza;
        
        while(puntero !=null){
            if(puntero.id.equals(id))
                return puntero.tipo;
            
            puntero = puntero.siguiente;
        }
        
        return "NO EXISTE LA VARIABLE";
    }
    
    
    
    public class Nodo{
        String id;
        String valor;//Para los operadores representa su peso o prioridad
        String tipo;
        Nodo siguiente = null;
        
        public String toString(){
            return id;
        }
        
    }
}
