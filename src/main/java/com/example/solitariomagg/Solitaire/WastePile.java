package com.example.solitariomagg.Solitaire;
import com.example.solitariomagg.cartas.CartaInglesa;
import Pilas.Pila;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Modela el montículo donde se colocan las cartas
 * que se extraen de Draw pile.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
public class WastePile {
    private Pila<CartaInglesa> cartas;

    public WastePile() {

        cartas = new Pila<>(52);
    }

    public void addCartas(ArrayList<CartaInglesa> nuevas) {
        for (CartaInglesa c : nuevas) {
            cartas.push(c);
        }
    }

    public ArrayList<CartaInglesa> emptyPile() {
        ArrayList<CartaInglesa> pile = new ArrayList<>();
        while (!cartas.pila_vacia()) {
            pile.add(cartas.pop());
        }
        // invertimos porque el pop las saca de arriba hacia abajo
        Collections.reverse(pile);
        return pile;
    }

    /**
     * Obtener la última carta sin removerla.
     * @return Carta que está encima. Si está vacía, es null.
     */
    public CartaInglesa verCarta() {
        return cartas.peek();
    }

    public CartaInglesa getCarta() {
        return cartas.pop();
    }

    public int getSize(){
        return cartas.size();
    }

    public ArrayList<CartaInglesa> getUltimasCartas(int n) {
        ArrayList<CartaInglesa> lista = new ArrayList<>();
        Pila<CartaInglesa> aux = new Pila<>(cartas.size());

        // Sacamos hasta n cartas o hasta vaciar
        int contador = 0;
        while (!cartas.pila_vacia() && contador < n) {
            CartaInglesa c = cartas.pop();
            aux.push(c);
            lista.add(c);
            contador++;
        }

        // Restaurar
        while (!aux.pila_vacia()) {
            cartas.push(aux.pop());
        }

        // Invertimos para que queden de base → tope en la lista
        Collections.reverse(lista);

        return lista;
    }

    @Override
    public String toString() {
        if (cartas.pila_vacia()) {
            return "---";
        } else {
            CartaInglesa top = cartas.peek();
            top.makeFaceUp();
            return top.toString();
        }
    }

    public boolean hayCartas() {
        return !cartas.pila_vacia();
    }
}
