package Analizadores;

import Estructuras.Main;
import Estructuras.postFija;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Semantico extends Sintactico {
    Lexico lexico= new Lexico();
    Tokens adicionar = new Tokens();
    postFija calculador = new postFija();
    
    public static String resultado = "";
        
    public boolean analisisSintactico(String cadena){
        
        lexico.setPuntero(0);
        Tokens tokenUnico = new Tokens();
        ArrayList<Tokens> tokens = new ArrayList<>();
        
        int result;
        do{
            tokenUnico = lexico.scanner(cadena);
            tokens.add(tokenUnico);  
            
            if(tokenUnico.getSimbolo().equals("Fin de Cadena") || tokenUnico.getSimbolo().equals("Fin de Linea")|| tokenUnico.getSimbolo().equals("&")){
                
                result = this.procesoSintactico(tokens);
                
                if(result == -1){
                    tokens.clear();
                    System.out.println("Lenguaje rechazado");
                    return false;
                }    
                tokens.clear();
            }
           
        }while(!tokenUnico.getSimbolo().equals("Fin de Cadena"));//Representa el "Fin de Cadena"
        
        System.out.println("lenguaje aceptado");
        return true;
        
    }
    
    public String analisisSemantico(String cadena){
        
        //Primero es pasarlo por el analizador sintactico para que corrobore que no haya error
        boolean respuesta = this.analisisSintactico(cadena);
        
        if(!respuesta)
            return null;
        
        lexico.setPuntero(0);
        Tokens tokenUnico = new Tokens();
        
        ArrayList<Tokens> tokens = new ArrayList<>();
        
        do{
            //Guardamos el token con su simbolo y tipo, en un array list de tipo Token
            tokenUnico = lexico.scanner(cadena);
            tokens.add(tokenUnico);  
            
            if(tokenUnico.getSimbolo().equals("Fin de Cadena") || tokenUnico.getSimbolo().equals("Fin de Linea")|| tokenUnico.getSimbolo().equals("&")){
                
                //SECCIÓN INTERACTUAR CON LA TABLA DE SÍMBOLOS
                resultado = tablaSimbolos(tokens);
                
                tokens.clear();
            }
           
        }while(!tokenUnico.getSimbolo().equals("Fin de Cadena"));//Representa el "Fin de Cadena"
        
        return resultado;

    }
    
    public String tablaSimbolos(ArrayList<Tokens> tokens){
        
        //Para los simbolos de tipo entero
        if(tokens.get(0).getSimbolo().equals("entero")){
            int i=1;
            while(i <tokens.size()-1){ 
                
                //Para saber si es un ID, tipo 1 = id
                if(tokens.get(i).getTipo() == 1){
                    
                    //Guardamos el simbolo del token
                    String id = tokens.get(i).getSimbolo();
                    
                    //Si le sigue un igual es decir si tiene la siguiente forma → "entero a = 5";
                    if(tokens.get(i+1).getSimbolo().equals("=")){
                        
                        i += 2; //Nos posicionamos en el valor que se le va asiganar a un identificador
                        
                        //Guardamos el valor numerio entero a = 5 → guardamos el 5
                        String valor = tokens.get(i).getSimbolo();
                        
                        //Se pregunta si ese identificador ya existe o no
                        if(!Tokens.insertarID(id,valor,"entero")){
                            JOptionPane.showMessageDialog(null,"Se detecto una variable duplicada: "+id);
                            System.exit(1);
                        }
                            
                    }
                    //Si es de la forma → "entero a";
                    //Se le asigna como valor por default el 0
                    else if(!Tokens.insertarID(id,"0","entero")){
                        JOptionPane.showMessageDialog(null, "Se detecto una variable duplicada: "+id);
                        System.exit(1);
                    }     
                    System.out.println("ID: "+Tokens.traerValor(id));
                    
                }
                i++;
            }
        }
        
        //Lo mismo para los reales
        else if(tokens.get(0).getSimbolo().equals("real")){
            
            int i=1;
            while(i <tokens.size()-1){ 
                if(tokens.get(i).getTipo() == 1){
                    String id = tokens.get(i).getSimbolo();
                    if(tokens.get(i+1).getSimbolo().equals("=")){
                        i +=2;
                        
                        //Añade para el caso de float por el punto
                        String valor = tokens.get(i).getSimbolo() + tokens.get(i+1).getSimbolo()+tokens.get(i+2).getSimbolo();
                        if(!Tokens.insertarID(id,valor,"real")){
                            System.out.println("Duplicidad de valores");
                        }
                        
                        //por si hay una coma por ejemplo entenro a = 5, b
                        i +=2;   
                    }
                    else if(!Tokens.insertarID(id,"0.0","real")){
                        System.out.println("Se detecto una variable duplicada");
                    }     
                    System.out.println("ID: "+Tokens.traerValor(id));
                    
                }
                i++;
            }
            
        }
        
        else if(tokens.get(0).getSimbolo().equals("escribe")){
            
            int i=2; //Para omitir los simbolos "<<"
            
            while(i <tokens.size()-1){
                
                //Obtiene el simbolo del Token
                String id = tokens.get(i).getSimbolo();
                //Se le envia el id y se busca en la lista el nodo al que pertence el id y se recupera su valor
                //Por ejemplo un nodo va a tener el id y el valor → a y 5, entonces se obtiene el 5
                
                //Se manda el valor para imprimirse
                resultado = resultado + "\n" +Tokens.traerValor(id);
                
                i+=2; //Por si hay valores para imprimir. Ejemplo escribre>>a>>b
            }
        }
        //Para el caso cuándo un identificador ya haya sido declarado
        //Y se le quiera cambiar el valor
        else if(tokens.get(0).getTipo() == 1){
            
            //Traemos el ID del que vamos a modificar su valor
            String vModificar = tokens.get(0).getSimbolo();
            
            //Corroboramos si esa variable previamente a sido declarada
            if(Tokens.existeVariable(vModificar)){
                
                //Guardamos el tipo del simbolo entero o float
                String tipoID= Tokens.traerTipo(vModificar);
                
                String nuevoValor = calculador.calcularValor(tokens, tipoID);
                
                //Se hace el cambio del nuevo valor calculado
                Tokens.modificarVarlo(vModificar, nuevoValor);
            }
            //Si se hace un cambio y la variable no se ha declarado
            else{
                
                JOptionPane.showMessageDialog(null, "Variable No Declarada");
                System.exit(1);
            }
            
        }
        return resultado;
    }
}
