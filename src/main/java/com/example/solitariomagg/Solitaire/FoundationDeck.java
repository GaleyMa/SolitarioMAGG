package com.example.solitariomagg.Solitaire;
import com.example.solitariomagg.cartas.CartaInglesa;
import com.example.solitariomagg.cartas.Palo;
import Pilas.Pila;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Modela un montículo donde se ponen las cartas
 * de un solo palo, usando Pila.
 *
 * @author Mayra García.
 * @version 2025
 */
public class FoundationDeck {
    private Palo palo;
    private Pila<CartaInglesa> cartas;

    public FoundationDeck(Palo palo) {
        this.palo = palo;
        this.cartas = new Pila<>(13);
    }

    public FoundationDeck(ArrayList<CartaInglesa> cartas) {
        this.cartas = new Pila<>(13);
        for (CartaInglesa carta : cartas) {
            this.cartas.push(carta);
        }
        this.palo=this.cartas.peek().getPalo();
    }

    public FoundationDeck(CartaInglesa carta) {
        this.cartas = new Pila<>();
        if (carta.getValorBajo() == 1) {
            this.palo = carta.getPalo();
            cartas.push(carta);
        }
    }

    /**
     * Agrega una carta al montículo. Sólo la agrega si
     * la carta es del palo del montículo y el la siguiente
     * carta en la secuencia.
     *
     * @param carta que se intenta almancenar
     * @return true si se pudo guardar la carta, false si no
     */
    public boolean agregarCarta(CartaInglesa carta) {
        if (carta == null) return false;

        if (cartas.pila_vacia()) {
            if (carta.getValorBajo() == 1) {
                palo = carta.getPalo();
                cartas.push(carta);
                return true;
            }
            return false;
        } else {
            CartaInglesa ultima = cartas.peek();
            if (carta.tieneElMismoPalo(palo) &&  carta.getValorBajo() == ultima.getValorBajo() + 1) {
                cartas.push(carta);
                return true;
            }
            return false;
        }
    }


    /**
     * Remover la última carta del montículo.
     *
     * @return la carta que removió, null si estaba vacio
     */
   public CartaInglesa removerUltimaCarta() {
       if (!cartas.pila_vacia()) {
           return cartas.pop();
       }
       return null;
   }

    @Override
    public String toString() {
        if (cartas.pila_vacia()) {
            return "---";
        } else {
            return getUltimaCarta().toString();
        }
    }

    /**
     * Determina si hay cartas en el Foundation.
     * @return true hay al menos una carta, false no hay cartas
     */
    public boolean estaVacio() {
        return cartas.pila_vacia();
    }
    /**
     * Obtiene la última carta del Foundation sin removerla.
     * @return última carta, null si no hay cartas
     */
    public CartaInglesa getUltimaCarta() {
        if (!cartas.pila_vacia()) {
            return cartas.peek();
        }
        return null;
    }

    public ArrayList<CartaInglesa> getCartas() {
        ArrayList<CartaInglesa> lista = new ArrayList<>();
        Pila<CartaInglesa> aux = new Pila<>(13);
        while (!estaVacio()) {
            aux.push(cartas.peek());
            lista.add(cartas.pop());
        }
        while (!aux.pila_vacia()){
            cartas.push(aux.pop());
        }
        Collections.reverse(lista);
        return lista;
    }
}
