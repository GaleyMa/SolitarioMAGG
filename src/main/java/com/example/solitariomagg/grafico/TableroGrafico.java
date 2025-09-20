package com.example.solitariomagg.grafico;

import com.example.solitariomagg.Controller;
import com.example.solitariomagg.Solitaire.SolitaireGame;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class TableroGrafico {
    private final ArrayList<TableauGrafico> tableaus;
    private final ArrayList<FoundationGrafico> foundations;
    private final WasteGrafico wasteGrafico;

    public TableroGrafico(SolitaireGame game,
                          ArrayList<Pane> tableauPanes,
                          ArrayList<Pane> foundationPanes,
                          Pane wastePane,
                          Pane wasteBackPane,
                          Controller controller) {

        tableaus = new ArrayList<>();
        for (int i = 0; i < tableauPanes.size(); i++)
            tableaus.add(new TableauGrafico(game.getTableau().get(i), tableauPanes.get(i), controller, i));

        foundations = new ArrayList<>();
        for (int i = 0; i < foundationPanes.size(); i++)
            foundations.add(new FoundationGrafico(game.getFoundation(i), foundationPanes.get(i), controller, i));

        wasteGrafico = new WasteGrafico(game.getWastePile(), wastePane, wasteBackPane, controller);
    }

    public void actualizar() {
        tableaus.forEach(TableauGrafico::actualizar);
        foundations.forEach(FoundationGrafico::actualizar);
        wasteGrafico.actualizar();
    }

    public int getIndiceTableau(CartaGrafica cg) {
        for (int i = 0; i < tableaus.size(); i++)
            if (tableaus.get(i).contieneCarta(cg)) return i;
        return -1;
    }

    public int getIndiceFoundation(CartaGrafica cg) {
        for (int i = 0; i < foundations.size(); i++)
            if (foundations.get(i).contieneCarta(cg)) return i;
        return -1;
    }
}
