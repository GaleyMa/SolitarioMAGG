package com.example.solitariomagg.Solitaire;
import com.example.solitariomagg.cartas.CartaInglesa;
import com.example.solitariomagg.cartas.Mazo;
import Pilas.Pila;
import java.util.ArrayList;

/**
 * Modela un mazo de cartas de solitario.
 * modificado para usar Pilas
 * @author Cecilia Curlango
 * modificado por Mayra García
 * @version 2025
 */
public class DrawPile {
    private Pila <CartaInglesa> cartas;
    private int cuantasCartasSeEntregan = 3;

    public DrawPile() {
        Mazo mazo = new Mazo();
        cartas = new Pila<>(52);
        setCuantasCartasSeEntregan(3);

        for( CartaInglesa c: mazo.getCartas() ) {
            cartas.push(c);
        }

    }

    /**
     * Establece cuantas cartas se sacan cada vez.
     * Puede ser 1 o 3 normalmente.
     * @param cuantasCartasSeEntregan
     */
    public void setCuantasCartasSeEntregan(int cuantasCartasSeEntregan) {
        this.cuantasCartasSeEntregan = cuantasCartasSeEntregan;
    }

    /**
     * Regresa la cantidad de cartas que se sacan cada vez.
     * @return cantidad de cartas que se entregan
     */
    public int getCuantasCartasSeEntregan() {
        return cuantasCartasSeEntregan;
    }

    /**
     * Retirar una cantidad de cartas. Este método se utiliza al inicio
     * de una partida para cargar las cartas de los tableaus.
     * Si se tratan de remover más cartas de las que hay,
     * se provocará un error.
     * @param cantidad de cartas que se quieren a retirar
     * @return cartas retiradas
     */
    public ArrayList<CartaInglesa> getCartas(int cantidad) {
        ArrayList<CartaInglesa> retiradas = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            retiradas.add(cartas.pop());
        }
        return retiradas;
    }

    /**
     * Retira y entrega las cartas del monton. La cantidad que retira
     * depende de cuántas cartas quedan en el montón y serán hasta el máximo
     * que se configuró inicialmente.
     * @return Cartas retiradas.
     */
    public Pila<CartaInglesa> retirarCartas() {
        Pila<CartaInglesa> retiradas = new Pila<>(cuantasCartasSeEntregan);
        int maximoARetirar = Math.min(cartas.size(), cuantasCartasSeEntregan);

        for (int i = 0; i < maximoARetirar; i++) {
            CartaInglesa retirada = cartas.pop();
            retirada.makeFaceUp();
            retiradas.push(retirada);
        }
        System.out.println("Cartas retiradas de DrawPile");
        return retiradas;
    }


    /**
     * Indica si aún quedan cartas para entregar.
     * @return true si hay cartas, false si no.
     */
    public boolean hayCartas() {
        System.out.println("----HAY CARTAS DRAW PILE");
        return !cartas.pila_vacia();
    }

    public CartaInglesa verCarta() {
        return cartas.peek();
    }
    /**
     * Agrega las cartas recibidas al monton y las voltea
     * para que no se vean las caras.
     * @param cartasAgregar cartas que se agregan
     */
    public void recargar(Pila<CartaInglesa> cartasAgregar) {
        this.cartas = new Pila<>(52);
        int contador=0;

        while (!cartasAgregar.pila_vacia()) {
            CartaInglesa c = cartasAgregar.pop();
            c.makeFaceDown();
            cartas.push(c);
            contador++;
        }
        System.out.println("Cartas recibidas a DrawPile: " + contador);
    }

    public int getSize(){
        return cartas.size();
    }

    public void voltear(){
        Pila<CartaInglesa> temp = new Pila<>(52);
        while (!cartas.pila_vacia()){
            CartaInglesa c = cartas.pop();
            if(c.isFaceup()) c.makeFaceDown();
            else c.makeFaceUp();
            temp.push(c);
        }
        while (!temp.pila_vacia()){
            CartaInglesa c = temp.pop();
            cartas.push(c);
        }
    }

    @Override
    public String toString() {
        if (cartas.pila_vacia()) {
            return "-E-";
        }
        return "@";
    }
}
