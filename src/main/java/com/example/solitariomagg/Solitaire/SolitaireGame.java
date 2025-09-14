package com.example.solitariomagg.Solitaire;

import com.example.solitariomagg.cartas.CartaInglesa;
import com.example.solitariomagg.cartas.Palo;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SolitaireGame {
    private ArrayList<TableauDeck> tableau = new ArrayList<>();
    private ArrayList<FoundationDeck> foundation = new ArrayList<>();
    private DrawPile drawPile;
    private WastePile wastePile;
    private FoundationDeck lastFoundationUpdated;

    public SolitaireGame() {
        drawPile = new DrawPile();
        wastePile = new WastePile();
        createTableaux();
        createFoundations();
        wastePile.addCartas(drawPile.retirarCartas());
    }

    private void createTableaux() {
        for (int i = 0; i < 7; i++) {
            TableauDeck t = new TableauDeck();
            t.inicializar(drawPile.getCartas(i + 1));
            tableau.add(t);
        }
    }

    private void createFoundations() {
        for (Palo palo : Palo.values()) {
            foundation.add(new FoundationDeck(palo));
        }
    }

    public ArrayList<TableauDeck> getTableau() { return tableau; }
    public TableauDeck getTableau(int i){
        return tableau.get(i);
    }
    public ArrayList<FoundationDeck> getFoundations() { return foundation; }
    public FoundationDeck getFoundation(int i){
        return foundation.get(i-1);
    }
    public DrawPile getDrawPile() { return drawPile; }
    public WastePile getWastePile() { return wastePile; }
    public FoundationDeck getLastFoundationUpdated() { return lastFoundationUpdated; }


    public void drawCards() {
        ArrayList<CartaInglesa> cards = drawPile.retirarCartas();
        wastePile.addCartas(cards);
    }

    public void cycleWastePile() {
        if (!wastePile.hayCartas()) return; // no hay cartas que ciclar

        List<CartaInglesa> todas = wastePile.getUltimasCartas(wastePile.getSize());
        wastePile.emptyPile();        // vaciar waste
        // invertir orden para volver a colocar en drawPile
        Collections.reverse(todas);
        drawPile.voltear();
        drawPile.recargar((ArrayList<CartaInglesa>) todas);   // volver al drawPile

    }

    public boolean drawOrCycle() {
        if (drawPile.hayCartas()) {
            drawCards();
            return true;
        } else if (wastePile.hayCartas()) {
            cycleWastePile();
            return true;
        }
        return false; // no hay cartas que mover
    }


    public void reloadDrawPile() {
        drawPile.recargar(wastePile.emptyPile());
    }

    public boolean moveWasteToTableau(int tableauDestino) {
        TableauDeck destino = tableau.get(tableauDestino);
        CartaInglesa carta = wastePile.verCarta();
        if (carta != null && destino.sePuedeAgregarCarta(carta)) {
            wastePile.getCarta();
            destino.agregarCarta(carta);
            return true;
        }
        return false;
    }

    public boolean moveTableauToTableau(int tableauFuente, int tableauDestino, CartaInglesa cartaFuente) {
        TableauDeck fuente = tableau.get(tableauFuente);
        TableauDeck destino = tableau.get(tableauDestino);
        int indexCarta= fuente.getCards().indexOf(cartaFuente);
        if (fuente.isEmpty()) return false;
        if (indexCarta < 0 || indexCarta >= fuente.getCards().size()) return false;

        // Obtenemos bloque de cartas desde indexCarta hasta el final
        ArrayList<CartaInglesa> bloque = fuente.removeStartingAt(indexCarta);

        if (bloque.isEmpty()) return false;

        // Verificamos si se puede colocar el bloque en destino
        if (destino.agregarBloqueDeCartas(bloque)) {
            if (!fuente.isEmpty()) {
                fuente.getUltimaCarta().makeFaceUp();
            }
            return true;
        } else {
            // si no se pudo, regresamos el bloque a la fuente
            fuente.getCards().addAll(bloque);
            return false;
        }
    }




    public boolean moveWasteToFoundation(int foundationIdx) {
        if (foundationIdx < 0 || foundationIdx > 3) return false;
        CartaInglesa carta = wastePile.verCarta();
        if (carta == null) return false;

        FoundationDeck destino = foundation.get(foundationIdx);
        if (destino.agregarCarta(carta)) {
            wastePile.getCarta();
            lastFoundationUpdated = destino;
            return true;
        }
        return false;
    }

    public boolean moveTableauToFoundation(int tableauIdx, int foundationIdx) {
        if (tableauIdx < 0 || tableauIdx > 6 || foundationIdx < 0 || foundationIdx > 3) return false;

        TableauDeck fuente = tableau.get(tableauIdx);
        if (fuente.isEmpty()) return false;

        CartaInglesa carta = fuente.removerUltimaCarta();
        FoundationDeck destino = foundation.get(foundationIdx);

        if (destino.agregarCarta(carta)) {
            lastFoundationUpdated = destino;
            if (!fuente.isEmpty()) fuente.verUltimaCarta().makeFaceUp();
            return true;
        } else {
            fuente.agregarCarta(carta);
            return false;
        }
    }

    public boolean isGameOver() {

        for (FoundationDeck f : foundation) {
            if (f.estaVacio() || f.getUltimaCarta().getValor() != 13) return false;
        }

        return true;
    }


}
