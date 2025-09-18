package com.example.solitariomagg.grafico;

import java.util.List;

public class TableroGrafico {
    private final WasteGrafico wasteGrafico;
    private final List<TableauGrafico> tableauGraficos;
    private final List<FoundationGrafico> foundationGraficos;

    public TableroGrafico(WasteGrafico wasteGrafico,
                          List<TableauGrafico> tableauGraficos,
                          List<FoundationGrafico> foundationGraficos) {
        this.wasteGrafico = wasteGrafico;
        this.tableauGraficos = tableauGraficos;
        this.foundationGraficos = foundationGraficos;
    }

    public WasteGrafico getWasteGrafico() {
        return wasteGrafico;
    }

    public List<TableauGrafico> getTableauGraficos() {
        return tableauGraficos;
    }

    public List<FoundationGrafico> getFoundationGraficos() {
        return foundationGraficos;
    }

    public void actualizar() {
        wasteGrafico.actualizar();
        for (FoundationGrafico f : foundationGraficos) f.actualizar();
        for (TableauGrafico t : tableauGraficos) t.actualizar();
    }
}

/*package com.example.solitariomagg.grafico;

import com.example.solitariomagg.Solitaire.SolitaireGame;
import com.example.solitariomagg.cartas.CartaInglesa;
import com.example.solitariomagg.Controller;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class TableroGrafico {

    private final SolitaireGame game;
    private final ArrayList<TableauGrafico> tableaus;
    private final ArrayList<FoundationGrafico> foundations;
    private final WasteGrafico wasteGrafico;

    public TableroGrafico(SolitaireGame game,
                          ArrayList<Pane> tableauPanes,
                          ArrayList<Pane> foundationPanes,
                          Pane wastePane,
                          Pane wasteBackPane,
                          Controller controller) {

        this.game = game;

        // Crear gráficos de tableaus
        tableaus = new ArrayList<>();
        for (int i = 0; i < tableauPanes.size(); i++) {
            Pane pane = tableauPanes.get(i);
            if (pane != null) {
                tableaus.add(new TableauGrafico(game.getTableau().get(i), pane, controller));
            }
        }

        // Crear gráficos de foundations
        foundations = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Pane pane = foundationPanes.get(i);
            if (pane != null) {
                foundations.add(new FoundationGrafico(game.getFoundation(i), pane, controller));
            }
        }

        // Crear gráfico del waste

        wasteGrafico = new WasteGrafico(game.getWastePile(), wastePane,wasteBackPane, controller);

        // Inicializar todas las vistas
        actualizar();
    }

    public void actualizar() {
        for (TableauGrafico t : tableaus) t.actualizar();
        for (FoundationGrafico f : foundations) f.actualizar();
        if (wasteGrafico != null) wasteGrafico.actualizar();
    }

    // Helper para obtener índice desde un TableauGrafico
    public int getIndiceTableau(CartaGrafica cg) {
        for (int i = 0; i < tableaus.size(); i++) {
            if (tableaus.get(i).contieneCarta(cg))return i;
        }
        return -1;
    }

    // Helper para obtener índice desde un FoundationGrafico
    public int getIndiceFoundation(CartaGrafica cg) {
        for (int i = 0; i < foundations.size(); i++) {
            if (foundations.get(i).contieneCarta(cg)) return i;
        }
        return -1; // nunca se debe usar directamente si devuelve -1
    }
}
*/