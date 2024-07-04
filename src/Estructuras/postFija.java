package Estructuras;

import Analizadores.Tokens;
import Estructuras.Pila.NodoPila;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class postFija {

    public listas lista = new listas();
    public Pila pila = new Pila();
    public Pila PilapostFija = new Pila();
    public Pila pilaInversa = new Pila();
    
    
    public void aPostFijo(ArrayList<Tokens> token, String tipoID) {

        int pesoFpila; //Valor descartable
        
        //Realiza el paso a postfijo sin contar el primer ID y el =
        for (int i = 2; i < token.size() - 1; i++) {
            
            Tokens simb = token.get(i);
            
            //Peso nicial de todo token
            pesoFpila = -1;
            
            //Para los operadores
            if (simb.getTipo() == 2) {

                //Se le da la prioridad por operador
                if (simb.getSimbolo().equals("*") || simb.getSimbolo().equals("/")) {
                    pesoFpila = 2;
                } else if (simb.getSimbolo().equals("+") || simb.getSimbolo().equals("-")) {
                    pesoFpila = 1;
                } else if (simb.getSimbolo().equals("(")) { //PARA EL CASO DEL PARENTESIS IZQUIERDO
                    pesoFpila = 5;
                }

                //EVALUANDO LA PRIORIDAD DE OPERADORES PARA LA PILA
                if (pila.isEmpity()) {
                    if (pesoFpila == 5) {
                        pesoFpila = 0; //Solo el parentesis izquierdo "(" tiene prioridad 0 dentro de la pila
                    }
                    pila.insertar(simb.getSimbolo(), pesoFpila);
                    
                } else {
                    
                    NodoPila auxiliar = pila.pop();
                    
                    if (pesoFpila == -1) { //Indica que ingresó un parentesis derecho 

                        while (!auxiliar.simbolo.equals("(")) {
                            PilapostFija.insertar(auxiliar.simbolo, auxiliar.tipo);
                            auxiliar = pila.pop();
                        }
                        
                    } else if (pesoFpila <= auxiliar.prioridad) {
                        
                        if (pesoFpila == 5) {
                            pesoFpila = 0; //Solo el parentesis izquierdo tiene prioridad 0 dentro de la pila
                        }
                        pila.insertar(simb.getSimbolo(), pesoFpila);

                        PilapostFija.insertar(auxiliar.simbolo, auxiliar.tipo);

                    } else {
                        if (pesoFpila == 5) {
                            pesoFpila = 0; //Solo el parentesis izquierdo tiene prioridad 0 dentro de la pila
                        }
                        pila.insertar(auxiliar.simbolo, auxiliar.prioridad);

                        pila.insertar(simb.getSimbolo(), pesoFpila);

                    }
                }
                
                //Para los numeros
            } else if (simb.getTipo() == 3) {
                
                if (token.get(i + 1).getSimbolo().equals(".") && tipoID.equals("real")) {
                    
                    //Para los numeros reales
                    String numero = token.get(i).getSimbolo() + token.get(i + 1).getSimbolo() + token.get(i + 2).getSimbolo();
                    i += 2;
                    PilapostFija.insertar(numero, "F");
                    
                } else if( tipoID.equals("entero") && !token.get(i + 1).getSimbolo().equals(".")){
                    PilapostFija.insertar(simb.getSimbolo(), "E");
                }
                else {
                    JOptionPane.showMessageDialog(null, "valor numérico no corresponde al tipo de la variable");
                    System.exit(2);
                }
                
                //
            } else {
                
                if(Tokens.traerTipo(simb.getSimbolo()).equals(tipoID)){
                    PilapostFija.insertar(simb.getSimbolo(), "ID");
                }
                else{
                    JOptionPane.showMessageDialog(null, "La variable '"+simb.getSimbolo()+"' No ha sido declarada");
                    System.exit(2);
                }
                
            }

        }

        //PASANDO LOS ÚLTIMOS OPERADORES A NUESTRO POSFIJO
        while (!pila.isEmpity()) {
            NodoPila extractor = pila.pop();
            PilapostFija.insertar(extractor.simbolo, "OP");
        }
                
    }
    
    
    public String calcularValor (ArrayList<Tokens> token, String tipoID){
        
        //Comvertimos la cadena de tokens en una notación posfija
        this.aPostFijo(token, tipoID );
        
        //Invirtiendo el orden de la pila PostFija
        while(!PilapostFija.isEmpity()){
            NodoPila temporal = PilapostFija.pop(); //Sacamos los valores de la lista y lo vamos metiendo a una pila.
            pilaInversa.insertar(temporal.simbolo, temporal.tipo); //se crea una nueva pila pero se le inserta al revés los elementos
        }
        
        //Identificando el tipo de dato a operar
        if(tipoID.equals("entero")){
            //Cuando la variable a guardar es entero
            this.calcularEntero();
        }
        else{
            //Cuando la variable a guardar en real
            this.calcularReal();
        }
        
        
        //Se guarda el resultado 
        NodoPila Resultado = pila.pop();
        return Resultado.simbolo;
        
    }
    
    public void calcularEntero(){
        
        NodoPila valor;
        NodoPila op1;
        NodoPila op2;
        
        //Para los enteros
        Integer num3;
        Integer num4;
        Integer resultado;
        
        while(!pilaInversa.isEmpity()){
            
            valor = pilaInversa.pop();

            if(valor.tipo.equals("OP")){
                op2 = pila.pop();
                op1 = pila.pop();
                
                /* 
                ID = para variables
                F  = para valores flotantes
                E  = para enteros
                OP = para operadores
                */
                if (op1.tipo.equals("E")) {
                    num3 = Integer.parseInt(op1.simbolo);//Se cambia de String a entero
                } else {
                    num3 = Integer.parseInt(Tokens.traerValor(op1.simbolo));//Para los float
                }
                
                //lo mismo para el segundo operador
                if (op2.tipo.equals("E")) {
                    num4 = Integer.parseInt(op2.simbolo);
                } else {
                    num4 = Integer.parseInt(Tokens.traerValor(op2.simbolo));
                }
                
                //Se realizan las operaciones por prioridad
                if(valor.simbolo.equals("*"))
                    resultado = num3 * num4;
                else if (valor.simbolo.equals("/"))
                    resultado = num3 / num4;
                else if (valor.simbolo.equals("-"))
                    resultado = num3 - num4;
                else
                    resultado = num3 + num4;
                    
                pila.insertar(String.valueOf(resultado), "E");
            }
            else{
                pila.insertar(valor.simbolo, valor.tipo);
            }
        }

    }
    
    public void calcularReal(){
        
        NodoPila valor;
        NodoPila op1;
        NodoPila op2;
        
        //Para los flotantes
        Float num1;
        Float num2;
        Float resultado;
         while(!pilaInversa.isEmpity()){
            
            valor = pilaInversa.pop();
            System.out.println("Valor actual: "+valor.simbolo);
            if(valor.tipo.equals("OP")){
                System.out.println("Sección OP");
                op2 = pila.pop();
                op1 = pila.pop();
                
                System.out.println("OP1: "+op1.toString());
                System.out.println("OP2: "+op2.toString());
                
                if (op1.tipo.equals("F")) {
                    num1 = Float.parseFloat(op1.simbolo);
                    System.out.println(num1);
                } else {
                    String data2 = Tokens.traerValor(op1.simbolo);
                    num1 = Float.parseFloat(data2);
                }

                if (op2.tipo.equals("F")) {
                    num2 = Float.parseFloat(op2.simbolo);
                } else {
                    String data1 = Tokens.traerValor(op2.simbolo);
                    num2 = Float.parseFloat(data1);
                }
                
                if(valor.simbolo.equals("*"))
                    resultado = num1 * num2;
                else if (valor.simbolo.equals("/"))
                    resultado = num1 / num2;
                else if (valor.simbolo.equals("-"))
                    resultado = num1 - num2;
                else
                    resultado = num1 + num2;
                    
                System.out.println("Insertando el resultado de la operación: "+resultado);
                
                pila.insertar(String.valueOf(resultado), "F");
            }
            else{
                pila.insertar(valor.simbolo, valor.tipo);
                System.out.println("Insertando en la pila de operación: "+valor.simbolo);
            }
        }
    }
    
}
    
    



        

