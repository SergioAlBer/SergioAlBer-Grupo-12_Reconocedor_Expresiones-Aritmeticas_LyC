package Analizadores;

import java.util.ArrayList;

public class Sintactico {

    Lexico scan = new Lexico();
    public ArrayList<String> auxiliar = new ArrayList<>();
    int marcador;
    String text = "";
    Tokens tok = new Tokens();
    Tokens simb = new Tokens();

    int cont;
    
    //Mejora del metodo CadeanaAUX
    public void crearCadena(String s) {
        if (s.isEmpty()) {
            auxiliar.add("Salto de Linea");
        } else {
            auxiliar.add(s);
        }
    }

    //Métodos que apoyarán al analizador semantico
    public int procesoSintactico(ArrayList<Tokens> tokens) {

        if (tokens.get(0).getTipo() == 1) {
            return this.mainLL1(tokens);
        } else {
            return this.estados(tokens);
        }

        /*Devuelve el valor de lo que representa la línea candidata para el programa
            1: ACEPTADA //Reconce
            0: IGNORADA
           -1: RECHAZADA //No reconce
         */
    }

    //Métodos que apoyarán al analizador sintactico
    public void reconocedor(String cadena, ArrayList<Integer> aceptada) {
        scan.setPuntero(0);
        Tokens tokenUnico = new Tokens();
        ArrayList<Tokens> tokens = new ArrayList<>();
        int lineaAceptada;
        do {

            tokenUnico = scan.scanner(cadena);
            tokens.add(tokenUnico);

            if (tokenUnico.getSimbolo().equals("Fin de Cadena") || tokenUnico.getSimbolo().equals("Fin de Linea") || tokenUnico.getSimbolo().equals("&")) {
                if (tokens.get(0).getTipo() == 1) {
                    lineaAceptada = this.mainLL1(tokens);
                    aceptada.add(lineaAceptada);
                } else {
                    lineaAceptada = this.estados(tokens);
                    aceptada.add(lineaAceptada);
                }
                tokens.clear();
            }

        } while (!tokenUnico.getSimbolo().equals("Fin de Cadena"));//Representa el "Fin de Cadena"

    }

    //MODIFICAR
    public int mainLL1(ArrayList<Tokens> token) {

        cont = 0;
        if (token.get(cont).getTipo() == 1) {
            cont++;
            if (token.get(cont).getSimbolo().equals("=")) {
                cont++;
                return S(token);
            }
        }
        return -1;
    }

    public int S(ArrayList<Tokens> token) {

        K(token);
        E(token);
        System.out.println(token.get(cont).getSimbolo());
        if (token.get(cont).getSimbolo().equals("Fin de Cadena") || token.get(cont).getSimbolo().equals("Fin de Linea")) {
            System.out.println("Es aceptada");
            return 1;
        }
        System.out.println("No es aceptada");
        return -1;

    }

    public void K(ArrayList<Tokens> token) {
        if (token.get(cont).getSimbolo().equals("+")) {
            cont++;
        } else if (token.get(cont).getSimbolo().equals("(") || token.get(cont).getSimbolo().equals("-")
                || token.get(cont).getTipo() == 1 || token.get(cont).getTipo() == 3) {
            System.out.println("Comentario K");
        } else {
            System.out.println("Error en K");
        }

    }

    public void E(ArrayList<Tokens> token) {
        T(token);
        W(token);
    }

    public void T(ArrayList<Tokens> token) {
        F(token);
        R(token);
    }

    //OPCION PARA EL ERROR
    public void W(ArrayList<Tokens> token) {
        if (token.get(cont).getSimbolo().equals("+")
                || token.get(cont).getSimbolo().equals("-")) {
            X(token);
            T(token);
            W(token);
        } else if (token.get(cont).getSimbolo().equals(")") || token.get(cont).getSimbolo().equals("Fin de Linea")
                || token.get(cont).getSimbolo().equals("Fin de Cadena")) {
            System.out.println("Comentario W");
        } else {
            System.out.println("Error en w");
        }

    }

    public void X(ArrayList<Tokens> token) {
        if (token.get(cont).getSimbolo().equals("+") || token.get(cont).getSimbolo().equals("-")) {
            cont++;
            System.out.println(token.get(cont).getSimbolo());
        } else {
            System.out.println("ERROR en X");
        }
    }

    public void R(ArrayList<Tokens> token) {
        if (token.get(cont).getSimbolo().equals("*") || token.get(cont).getSimbolo().equals("/")) {
            Y(token);
            F(token);
            R(token);
        } else if (token.get(cont).getSimbolo().equals("+") || token.get(cont).getSimbolo().equals("-")
                || token.get(cont).getSimbolo().equals(")") || token.get(cont).getSimbolo().equals("Fin de Linea")
                || token.get(cont).getSimbolo().equals("Fin de Cadena")) {
            System.out.println("Comentario de R");
        } else {
            System.out.println("ERROR en R");
        }
    }

    public void Y(ArrayList<Tokens> token) {
        if (token.get(cont).getSimbolo().equals("*") || token.get(cont).getSimbolo().equals("/")) {
            cont++;
            System.out.println(token.get(cont).getSimbolo());
        } else {
            System.out.println("ERROR en Y");
        }
    }

    public void F(ArrayList<Tokens> token) {
        if (token.get(cont).getSimbolo().equals("(")) {
            cont++;
            E(token);
            cont++;
        } else if (token.get(cont).getSimbolo().equals("-")) {
            cont++;
            F(token);
        } else if (token.get(cont).getTipo() == 1) { // PARA LA SECCIÓN DE IDENTIFICADORES
            cont++;
            System.out.println(token.get(cont).getSimbolo());
        } else if (token.get(cont).getTipo() == 3) { // PARA LA SECCIÓN DE NÚMEROS
            cont++;
            M(token);
        } else {
            System.out.println("ERROR en F");
        }
    }

    public void M(ArrayList<Tokens> token) {
        if (token.get(cont).getSimbolo().equals(".")) {
            cont++;
            if (token.get(cont).getTipo() == 3) {
                cont++;
            } else {
                System.out.println("ERROR en F");
            }
        } else if (token.get(cont).getSimbolo().equals("*") || token.get(cont).getSimbolo().equals("/")
                || token.get(cont).getSimbolo().equals("+") || token.get(cont).getSimbolo().equals("-")
                || token.get(cont).getSimbolo().equals(")") || token.get(cont).getSimbolo().equals("Fin de Cadena")
                || token.get(cont).getSimbolo().equals("Fin de Linea")) {
            System.out.println("Comentario");
        } else {
            System.out.println("ERROR en M");
        }
    }

    public int estados(ArrayList<Tokens> token) {
        int i = 0;
        simb = token.get(0);
        int estado_actual = 0;

        while (estado_actual != QF && estado_actual != QE && estado_actual != QI) {

            estado_actual = matrizDeTransicion(estado_actual, simb);

            if (i < token.size() - 1) {
                i++;
                simb = token.get(i);
            } else {
                break;
            }

        }

        switch (estado_actual) {
            case QF:
                System.out.println("Se reconoce la cadena");
                return 1;
            case QI:
                System.out.println("Se ignora");
                return 0;
            default:
                System.out.println("NO SE RECONOCE");
                return -1;
        }

    }

    public int matrizDeTransicion(int q, Tokens token) {
        int columna = tok.traerPosicion(token);
        int fila = q;

        final int[][] mtGeneral = {
        {Q1, Q5, Q11, Q14, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, QI},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, Q2, -1},
        {-1, -1, -1, -1, -1, -1, Q1, -1, -1, -1, -1, -1, Q3, -1, -1, QF},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, Q4, -1, -1},
        {-1, -1, -1, -1, -1, -1, Q1, -1, -1, -1, -1, -1, -1, -1, -1, QF},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, Q6, -1},
        {-1, -1, -1, -1, -1, -1, Q5, -1, -1, -1, -1, -1, Q7, -1, -1, QF},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, Q8, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1, Q9, -1, -1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, Q10, -1, -1},
        {-1, -1, -1, -1, -1, -1, Q5, -1, -1, -1, -1, -1, -1, -1, -1, QF},
        {-1, -1, -1, -1, -1, Q12, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, Q13, -1},
        {-1, -1, -1, -1, -1, Q12, -1, -1, -1, -1, -1, -1, -1, -1, -1, QF},
        {-1, -1, -1, -1, Q15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, Q16, Q16, -1},
        {-1, -1, -1, -1, Q15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, QF},};

        return mtGeneral[fila][columna];
    }

    final private int QF = 100;
    final private int QE = -1;
    final private int QI = -2;

    final private int Q1 = 1;
    final private int Q2 = 2;
    final private int Q3 = 3;
    final private int Q4 = 4;
    final private int Q5 = 5;
    final private int Q6 = 6;
    final private int Q7 = 7;
    final private int Q8 = 8;
    final private int Q9 = 9;
    final private int Q10 = 10;
    final private int Q11 = 11;
    final private int Q12 = 12;
    final private int Q13 = 13;
    final private int Q14 = 14;
    final private int Q15 = 15;
    final private int Q16 = 16;
    
    
}
