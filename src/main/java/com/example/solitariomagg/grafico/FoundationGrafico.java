package com.example.solitariomagg.grafico;

import com.example.solitariomagg.Solitaire.FoundationDeck;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.scene.layout.Pane;

public class FoundationGrafico {
    private final FoundationDeck modelo;
    private final Pane contenedor;

    public FoundationGrafico(FoundationDeck modelo, Pane contenedor) {
        this.modelo = modelo;
        this.contenedor = contenedor;
    }

    public void actualizar() {
        contenedor.getChildren().clear();
        if (!modelo.estaVacio()) {
            CartaInglesa carta = modelo.getUltimaCarta();
            CartaGrafica cg = new CartaGrafica(carta);
            contenedor.getChildren().add(cg.getVista());
        }
    }
}

