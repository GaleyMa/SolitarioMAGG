package com.example.solitariomagg.grafico;

import com.example.solitariomagg.Controller;
import com.example.solitariomagg.Solitaire.FoundationDeck;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class FoundationGrafico {
    private final FoundationDeck modelo;
    private final Pane contenedor;
    private final Controller controller;
    private final int indice;

    public FoundationGrafico(FoundationDeck modelo, Pane contenedor, Controller controller, int indice) {
        this.modelo = modelo;
        this.contenedor = contenedor;
        this.controller = controller;
        this.indice = indice;
    }

    public void actualizar() {
        contenedor.getChildren().clear();

        CartaInglesa carta = modelo.getUltimaCarta();
        if (carta == null) {
            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cartas/back.png")));
            ImageView slot = new ImageView(img);
            slot.setFitWidth(100);
            slot.setPreserveRatio(true);
            slot.setOpacity(0.3);

            slot.setOnMouseClicked(e -> controller.intentarMoverACasillaVaciaFoundation(indice));
            contenedor.getChildren().add(slot);
        } else {
            CartaGrafica cg = new CartaGrafica(carta, OrigenCarta.FOUNDATION, indice, controller);
            contenedor.getChildren().add(cg);
        }
    }

    public boolean contieneCarta(CartaGrafica cg) {
        return modelo.getUltimaCarta() == cg.getCarta();
    }
}