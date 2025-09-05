package com.example.solitariomagg.Solitaire;
import com.example.solitariomagg.cartas.CartaInglesa;
import com.example.solitariomagg.cartas.Mazo;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Modela un mazo de cartas de solitario.
 * @author Cecilia Curlango
 * @version 2025
 */
public class DrawPile {
    private ArrayList<CartaInglesa> cartas;
    private int cuantasCartasSeEntregan = 3;

    public DrawPile() {
        Mazo mazo = new Mazo();
        cartas = mazo.getCartas();
        setCuantasCartasSeEntregan(3);
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
            retiradas.add(cartas.remove(0));
        }
        return retiradas;
    }

    /**
     * Retira y entrega las cartas del monton. La cantidad que retira
     * depende de cuántas cartas quedan en el montón y serán hasta el máximo
     * que se configuró inicialmente.
     * @return Cartas retiradas.
     */
    public ArrayList<CartaInglesa> retirarCartas() {
        ArrayList<CartaInglesa> retiradas = new ArrayList<>();
        int maximoARetirar = Math.min(cartas.size(), cuantasCartasSeEntregan);

        for (int i = 0; i < maximoARetirar; i++) {
            // 🔹 Sacar siempre del final (tope del mazo)
            CartaInglesa retirada = cartas.remove(cartas.size() - 1);
            retirada.makeFaceUp();
            retiradas.add(retirada);
        }

        // 🔹 Como las sacamos de atrás hacia adelante, las invertimos
        Collections.reverse(retiradas);

        return retiradas;
    }


    /**
     * Indica si aún quedan cartas para entregar.
     * @return true si hay cartas, false si no.
     */
    public boolean hayCartas() {
        return cartas.size() > 0;
    }

    public CartaInglesa verCarta() {
        CartaInglesa regresar = null;
        if (!cartas.isEmpty()) {
            regresar = cartas.getLast();
        }
        return regresar;
    }
    /**
     * Agrega las cartas recibidas al monton y las voltea
     * para que no se vean las caras.
     * @param cartasAgregar cartas que se agregan
     */
    public void recargar(ArrayList<CartaInglesa> cartasAgregar) {
        cartas = cartasAgregar;
        for (CartaInglesa aCarta : cartas) {
            aCarta.makeFaceDown();
        }
    }
    public int getSize(){
        return cartas.size();
    }

    public void voltear(){
        for (CartaInglesa c: cartas){
            if(c.isFaceup()) c.makeFaceDown();
            else c.makeFaceUp();
        }
    }

    @Override
    public String toString() {
        if (cartas.isEmpty()) {
            return "-E-";
        }
        return "@";
    }
}
