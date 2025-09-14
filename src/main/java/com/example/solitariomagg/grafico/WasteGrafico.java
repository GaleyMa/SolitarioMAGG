package com.example.solitariomagg.grafico;

import com.example.solitariomagg.Solitaire.WastePile;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.scene.layout.HBox;

import java.util.List;

public class WasteGrafico {
    private final WastePile modelo;
    private final HBox contenedor;

    public WasteGrafico(WastePile modelo, HBox contenedor) {
        this.modelo = modelo;
        this.contenedor = contenedor;
    }

    public void actualizar() {
        contenedor.getChildren().clear();
        List<CartaInglesa> visibles = modelo.getUltimasCartas(3);
        for (CartaInglesa carta : visibles) {
            CartaGrafica cg = new CartaGrafica(carta);
            contenedor.getChildren().add(cg.getVista());
        }
    }
}
