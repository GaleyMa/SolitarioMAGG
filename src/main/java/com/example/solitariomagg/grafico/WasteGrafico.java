package com.example.solitariomagg.grafico;

import com.example.solitariomagg.Controller;
import com.example.solitariomagg.Solitaire.WastePile;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Objects;

public class WasteGrafico {
    private final WastePile modelo;
    private final Pane wastePane;
    private final Pane wasteBackPane;
    private final Controller controller;

    public WasteGrafico(WastePile modelo, Pane wastePane, Pane wasteBackPane, Controller controller) {
        this.modelo = modelo;
        this.wastePane = wastePane;
        this.wasteBackPane = wasteBackPane;
        this.controller = controller;
    }

    public void actualizar() {
        configurarWasteBack();
        mostrarWaste();
    }

    private void mostrarWaste() {
        wastePane.getChildren().clear();
        List<CartaInglesa> visibles = modelo.getUltimasCartas(3);
        int x = 0;
        for (CartaInglesa carta : visibles) {
            CartaGrafica cg = new CartaGrafica(carta, OrigenCarta.WASTE, 0, controller);
            cg.setLayoutX(x);
            if (carta != visibles.get(visibles.size() - 1)) {
                cg.setDisable(true);
            }
            wastePane.getChildren().add(cg);
            x += 30;
        }
    }

    private void configurarWasteBack() {
        Image reverso = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cartas/back.png")));
        ImageView img = new ImageView(reverso);
        img.setFitWidth(80);
        img.setFitHeight(120);
        wasteBackPane.getChildren().clear();
        wasteBackPane.getChildren().add(img);        wasteBackPane.setOnMouseClicked(e -> { controller.manejarWaste();});
    }
}