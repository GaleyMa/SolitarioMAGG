package com.example.solitariomagg.grafico;

import com.example.solitariomagg.Controller;
import com.example.solitariomagg.Solitaire.TableauDeck;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Objects;

public class TableauGrafico extends Pane {
    private final TableauDeck modelo;
    private final Controller controller;

    public TableauGrafico(TableauDeck modelo, Controller controller) {
        this.modelo = modelo;
        this.controller = controller;
        setPrefSize(80, 300); // espacio para la pila larga
    }

    public void actualizar() {
        getChildren().clear();
        List<CartaInglesa> cartas = modelo.getCards();

        if (cartas.isEmpty()) {
            // slot vacío
            Image slotImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cartas/back.png")));
            ImageView slot = new ImageView(slotImg);
            slot.setFitWidth(80);
            slot.setFitHeight(120);
            slot.setOpacity(0.3);
            getChildren().add(slot);
        } else {
            int y = 0;
            for (CartaInglesa carta : cartas) {
                CartaGrafica c = new CartaGrafica(carta, OrigenCarta.TABLEAU, controller);
                c.setLayoutY(y);
                getChildren().add(c);
                y += 25; // offset vertical
            }
        }
    }
}

/*package com.example.solitariomagg.grafico;
import com.example.solitariomagg.Solitaire.*;
import com.example.solitariomagg.cartas.CartaInglesa;
import com.example.solitariomagg.Controller;
import javafx.scene.layout.Pane;

public class TableauGrafico {
    private final TableauDeck modelo;
    private final Pane contenedor;
    private final Controller controller;

    public TableauGrafico(TableauDeck modelo, Pane contenedor, Controller controller) {
        this.modelo = modelo;
        this.contenedor = contenedor;
        this.controller = controller;
    }

    public void actualizar() {
        contenedor.getChildren().clear();
        int y = 0;
        if (modelo.isEmpty()){
            for (CartaInglesa carta : modelo.getCards()) {
                CartaGrafica cg = new CartaGrafica(carta, OrigenCarta.TABLEAU, controller);
                cg.setLayoutY(y);
                contenedor.getChildren().add(cg);
                y += 25; // separación vertical


            }
        }else{
            for (CartaInglesa carta : modelo.getCards()) {
                CartaGrafica cg = new CartaGrafica(carta, OrigenCarta.TABLEAU, controller);
                cg.setLayoutY(y);
                contenedor.getChildren().add(cg);
                y += 25; // separación vertical
            }
        }
    }
    public TableauDeck getModelo() {
        return modelo;
    }

    // En TableauGrafico
    public boolean contieneCarta(CartaGrafica cg) {
        for (CartaInglesa c : modelo.getCards()) {
            if (c == cg.getCarta()) {
                return true;
            }
        }
        return false;
    }

}*/