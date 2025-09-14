package com.example.solitariomagg.grafico;

import com.example.solitariomagg.Solitaire.SolitaireGame;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class TableroGrafico {

    private  SolitaireGame juego;
    private  ArrayList<TableauGrafico> tableauGraficos = new ArrayList<>();
    private  ArrayList<FoundationGrafico> foundationGraficos = new ArrayList<>();
    private  WasteGrafico wasteGrafico;

    public TableroGrafico(SolitaireGame juego,
                          List<Pane> tableauPanes,
                          List<Pane> foundationPanes,
                          HBox wasteBox) {
        this.juego = juego;

        for (int i = 0; i < 7; i++) {
            tableauGraficos.add(new TableauGrafico(juego.getTableau(i), tableauPanes.get(i)));
        }
        for (int i = 0; i < 4; i++) {
            foundationGraficos.add(new FoundationGrafico(juego.getFoundation(i), foundationPanes.get(i)));
        }
        wasteGrafico = new WasteGrafico(juego.getWastePile(), wasteBox);
    }

    public void actualizar() {
        tableauGraficos.forEach(TableauGrafico::actualizar);
        foundationGraficos.forEach(FoundationGrafico::actualizar);
        wasteGrafico.actualizar();
    }

    public SolitaireGame getJuego() {
        return juego;
    }
}

