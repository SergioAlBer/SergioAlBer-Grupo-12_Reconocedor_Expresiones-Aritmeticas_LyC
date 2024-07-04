package Analizadores;

public class Lexico {

    private final int PR = 0;
    private final int ID = 1;
    private final int OP = 2;
    private final int NUM = 3;
    private final int LF = 4;
    private final int $ = 5;
    private int puntero =0;
    private char comparador;
    
    public Tokens scanner(String cadena){
        
        Tokens token = new Tokens();
        
        //Iniciar el token
        token.setSimbolo("");
        token.setTipo(-1);
        
        while(cadena.charAt(puntero)==' ')
            puntero++;
        
        comparador = cadena.charAt(puntero);
        
        if(comparador>='a' && comparador<='z'){
            
            while((comparador>='a' && comparador<='z')||( comparador>='0' && comparador<='9')){
                
                token.setSimbolo(token.getSimbolo()+comparador);
                puntero++;
                comparador = cadena.charAt(puntero);
                
            }
            
            if(token.esReservada(token.getSimbolo()))
                token.setTipo(PR); // TIPO: Palabra Reservada
            else
                token.setTipo(ID); // TIPO: Identificador
            
        }//Analizando si es el token es un número
        else if(comparador>='0' && comparador<='9'){
            
            while(comparador>='0' && comparador<='9'){
                
                token.setSimbolo(token.getSimbolo()+comparador);
                puntero++;
                comparador = cadena.charAt(puntero);
            }
            token.setTipo(NUM); // TIPO: NÚMERO
            
        }else if(comparador == ',' || comparador == '=' || comparador == '*' 
                || comparador == '/' || comparador == '-' || comparador == '+'
                || comparador == '<' || comparador == '>' || comparador == '.'
                || comparador == '(' || comparador == ')'){
            
            token.setSimbolo(token.getSimbolo()+comparador);

            if ((comparador == '/' && cadena.charAt(puntero + 1) == '/')) {//Para ignorar comentarios en una línea
                    while (cadena.charAt(puntero) != '\n') {
                        puntero++;
                    }
                   token.setSimbolo("#");
                    
            }
            else if ((comparador == '<' && cadena.charAt(puntero + 1) == '<')
                        || (comparador == '>' && cadena.charAt(puntero + 1) == '>')
                        || (comparador == '>' && cadena.charAt(puntero + 1) == '=')
                        || (comparador == '<' && cadena.charAt(puntero + 1) == '=')){
                    token.setSimbolo(token.getSimbolo()+ cadena.charAt(puntero + 1));
                    
                    puntero++;
            }
            
            if((comparador == '/' && cadena.charAt(puntero + 1) == '*')){
                puntero++;
                while (cadena.charAt(puntero+1) !='*' && cadena.charAt(puntero+2) !='/' ) {
                        puntero++;
                }
                System.out.println(cadena.charAt(puntero));
                puntero+=2;
                System.out.println(cadena.charAt(puntero));
                token.setSimbolo("##");//Representa el simbolo de un comentario
            }
            /****/
            if(comparador != '\n'){
                token.setTipo(OP); // TIPO: OPERADOR
                puntero++;
            }
                
            
        }
        else if(comparador == '\r' && cadena.charAt(puntero + 1) == '\n'){
            
            token.setSimbolo("Fin de Linea");
            token.setTipo(LF);
            puntero +=2;
        }
        //Carácter que representa el fin de cadena
        else if(comparador == '$'){
            token.setSimbolo("Fin de Cadena");
            token.setTipo($);
        }
        
        return token;
    }
    
    public void setPuntero(int puntero){
        this.puntero=puntero;
    }
}
