
package com.example.solitariomagg.grafico;

import com.example.solitariomagg.Controller;
import com.example.solitariomagg.Solitaire.FoundationDeck;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class FoundationGrafico extends Pane {
    private final FoundationDeck modelo;
    private final Controller controller;

    public FoundationGrafico(FoundationDeck modelo, Controller controller) {
        this.modelo = modelo;
        this.controller = controller;
        setPrefSize(80, 120);
    }

    public void actualizar() {
        getChildren().clear();

        CartaInglesa carta = modelo.getUltimaCarta();
        if (carta != null) {
            CartaGrafica c = new CartaGrafica(carta, controller);
            getChildren().add(c);
        } else {
            // fundación vacía → mostrar un "slot"
            Image slotImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cartas/back.png")));
            ImageView slot = new ImageView(slotImg);
            slot.setFitWidth(80);
            slot.setFitHeight(120);
            slot.setOpacity(0.3);
            getChildren().add(slot);
        }
    }
}

/*package com.example.solitariomagg.grafico;
import com.example.solitariomagg.Solitaire.*;
import com.example.solitariomagg.cartas.*;
import com.example.solitariomagg.Controller;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import javafx.scene.layout.Pane;

import java.util.Objects;

public class FoundationGrafico {
    private final FoundationDeck modelo;
    private final Pane contenedor;
    private final Controller controller;

    public FoundationGrafico(FoundationDeck modelo, Pane contenedor, Controller controller) {
        this.modelo = modelo;
        this.contenedor = contenedor;
        this.controller = controller;
    }

    public void actualizar() {
        contenedor.getChildren().clear();
        if (!modelo.estaVacio()) {
            CartaInglesa carta = modelo.getUltimaCarta();
            CartaGrafica cg = new CartaGrafica(carta,OrigenCarta.FOUNDATION, controller);
            contenedor.getChildren().add(cg);
        }else {
            // Si no hay cartas, mostrar un "reverso transparente"
            ImageView reverso = new ImageView(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cartas/back.png")))
            );
            reverso.setFitWidth(80);
            reverso.setPreserveRatio(true);
            contenedor.getChildren().add(reverso);
        }
    }


    public FoundationDeck getModelo() {
        return modelo;
    }

    // En FoundationGrafico
    public boolean contieneCarta(CartaGrafica cg) {
        for (CartaInglesa c : modelo.getCartas()) {
            if (c == cg.getCarta()) {
                return true;
            }
        }
        return false;
    }

}*/

