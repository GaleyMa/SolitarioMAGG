package com.example.solitariomagg.Solitaire;

import com.example.solitariomagg.cartas.Palo;
import com.example.solitariomagg.cartas.CartaInglesa;

import java.util.ArrayList;

public class SolitaireGame {
    private ArrayList<TableauDeck> tableau = new ArrayList<>();
    private ArrayList<FoundationDeck> foundation = new ArrayList<>();
    private FoundationDeck lastFoundationUpdated;
    private DrawPile drawPile;
    private WastePile wastePile;

    public SolitaireGame() {
        drawPile = new DrawPile();
        wastePile = new WastePile();
        createTableaux();
        createFoundations();
        wastePile.addCartas(drawPile.retirarCartas());
    }

    public void reloadDrawPile() {
        ArrayList<CartaInglesa> cards = wastePile.emptyPile();
        drawPile.recargar(cards);
    }

    public void drawCards() {
        ArrayList<CartaInglesa> cards = drawPile.retirarCartas();
        wastePile.addCartas(cards);
    }

    // ----------------- MOVIMIENTOS -----------------

    public boolean moveWasteToTableau(int tableauDestino) {
        TableauDeck destino = tableau.get(tableauDestino);
        CartaInglesa carta = wastePile.verCarta();
        if (carta != null && destino.sePuedeAgregarCarta(carta)) {
            wastePile.getCarta(); // eliminar de waste
            destino.agregarCarta(carta);
            return true;
        }
        return false;
    }

    public boolean moveTableauToTableau(int tableauFuente, int tableauDestino) {
        TableauDeck fuente = tableau.get(tableauFuente);
        TableauDeck destino = tableau.get(tableauDestino);

        if (fuente.isEmpty()) return false;

        CartaInglesa cartaInicial = fuente.verUltimaCarta();
        if (cartaInicial != null && destino.sePuedeAgregarCarta(cartaInicial)) {
            ArrayList<CartaInglesa> cartas = fuente.removeStartingAt(cartaInicial.getValor());
            if (destino.agregarBloqueDeCartas(cartas)) {
                if (!fuente.isEmpty()) fuente.verUltimaCarta().makeFaceUp();
                return true;
            }
        }
        return false;
    }

    public boolean moveWasteToFoundation(int foundationIdx) {
        if (foundationIdx < 0 || foundationIdx > 3) return false;
        CartaInglesa carta = wastePile.verCarta();
        if (carta == null) return false;

        FoundationDeck destino = foundation.get(foundationIdx);
        if (destino.agregarCarta(carta)) {
            wastePile.getCarta(); // eliminar de waste
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
            fuente.agregarCarta(carta); // revertir si falla
            return false;
        }
    }

    // ----------------- MÉTODOS AUXILIARES -----------------

    public boolean isGameOver() {
        for (FoundationDeck f : foundation) {
            if (f.estaVacio() || f.getUltimaCarta().getValor() != 13) return false;
        }
        return true;
    }

    private void createFoundations() {
        for (Palo palo : Palo.values()) {
            foundation.add(new FoundationDeck(palo));
        }
    }

    private void createTableaux() {
        for (int i = 0; i < 7; i++) {
            TableauDeck t = new TableauDeck();
            t.inicializar(drawPile.getCartas(i + 1));
            tableau.add(t);
        }
    }
    public ArrayList<FoundationDeck> getFoundations() {
        return foundation;
    }

    public DrawPile getDrawPile() { return drawPile; }
    public WastePile getWastePile() { return wastePile; }
    public ArrayList<TableauDeck> getTableau() { return tableau; }
    public FoundationDeck getLastFoundationUpdated() { return lastFoundationUpdated; }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Foundations:\n");
        for (FoundationDeck f : foundation) str.append(f).append("\n");

        str.append("\nTableaux:\n");
        for (int i = 0; i < tableau.size(); i++) {
            str.append(i).append(" ").append(tableau.get(i)).append("\n");
        }

        str.append("Waste:\n").append(wastePile).append("\n");
        str.append("Draw:\n").append(drawPile).append("\n");
        return str.toString();
    }
}
