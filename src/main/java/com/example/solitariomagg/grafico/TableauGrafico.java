package com.example.solitariomagg.grafico;

import com.example.solitariomagg.Controller;
import com.example.solitariomagg.Solitaire.TableauDeck;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class TableauGrafico {
    private final TableauDeck modelo;
    private final Pane contenedor;
    private final Controller controller;
    private final int indice;

    public TableauGrafico(TableauDeck modelo, Pane contenedor, Controller controller, int indice) {
        this.modelo = modelo;
        this.contenedor = contenedor;
        this.controller = controller;
        this.indice = indice;
    }

    public void actualizar() {
        contenedor.getChildren().clear();

        if (modelo.isEmpty()) {
            // --- Slot vacío clickeable ---
            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cartas/back.png")));
            ImageView slot = new ImageView(img);
            slot.setFitWidth(80);
            slot.setFitHeight(120);
            slot.setOpacity(0.3);

            slot.setOnMouseClicked(e -> controller.intentarMoverACasillaVaciaTableau(indice));
            contenedor.getChildren().add(slot);

        } else {
            int y = 0;
            for (CartaInglesa carta : modelo.getCards()) {
                CartaGrafica cg = new CartaGrafica(carta, OrigenCarta.TABLEAU, indice, controller);
                cg.setLayoutY(y);
                contenedor.getChildren().add(cg);
                y += 25;
            }
        }
    }

    public boolean contieneCarta(CartaGrafica cg) {
        return modelo.getCards().contains(cg.getCarta());
    }
}