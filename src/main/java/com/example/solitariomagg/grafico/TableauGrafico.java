package com.example.solitariomagg.grafico;

import com.example.solitariomagg.Solitaire.TableauDeck;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.scene.layout.Pane;

public class TableauGrafico {
    private final TableauDeck modelo;
    private final Pane contenedor;

    public TableauGrafico(TableauDeck modelo, Pane contenedor) {
        this.modelo = modelo;
        this.contenedor = contenedor;
    }

    public void actualizar() {
        contenedor.getChildren().clear();
        int y = 0;
        for (CartaInglesa carta : modelo.getCards()) {
            CartaGrafica cg = new CartaGrafica(carta);
            cg.getVista().setLayoutY(y);
            contenedor.getChildren().add(cg.getVista());
            y += 25;
        }
    }
}
