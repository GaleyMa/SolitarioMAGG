package com.example.solitariomagg;

import com.example.solitariomagg.Solitaire.*;
import com.example.solitariomagg.grafico.*;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;

import java.util.ArrayList;

public class Controller {

    private SolitaireGame game;
    private TableroGrafico tablero;
    private CartaGrafica cartaSeleccionada;

    @FXML private Pane wastePane, wasteBackPane;
    @FXML private Pane tableau1, tableau2, tableau3, tableau4, tableau5, tableau6, tableau7;
    @FXML private Pane foundation1, foundation2, foundation3, foundation4;

    private ArrayList<Pane> tableauPanes, foundationPanes;

    @FXML
    public void initialize() {
        tableauPanes = new ArrayList<>() {{
            add(tableau1); add(tableau2); add(tableau3);
            add(tableau4); add(tableau5); add(tableau6); add(tableau7);
        }};
        foundationPanes = new ArrayList<>() {{
            add(foundation1); add(foundation2); add(foundation3); add(foundation4);
        }};
        game = new SolitaireGame();
        tablero = new TableroGrafico(game, tableauPanes, foundationPanes, wastePane, wasteBackPane, this);
        actualizarVista();
    }

    public void actualizarVista() { tablero.actualizar(); }

    public void seleccionarCarta(CartaInglesa carta, ImageView vista) {
        CartaGrafica cg = (CartaGrafica) vista;
        if (!carta.isFaceup()) return;

        if (cartaSeleccionada == null) {
            cartaSeleccionada = cg;
            cg.setStyle("-fx-effect: dropshadow(one-pass-box, grey, 20, 0.5, 0, 0);");
            return;
        }

        boolean movimiento = false;

        if (cartaSeleccionada.getOrigen() == OrigenCarta.WASTE) {
            if (cg.getOrigen() == OrigenCarta.TABLEAU)
                movimiento = game.moveWasteToTableau(cg.getIndiceMazo());
            else if (cg.getOrigen() == OrigenCarta.FOUNDATION)
                movimiento = game.moveWasteToFoundation(cg.getIndiceMazo());

        } else if (cartaSeleccionada.getOrigen() == OrigenCarta.TABLEAU) {
            int fuente = cartaSeleccionada.getIndiceMazo();
            if (cg.getOrigen() == OrigenCarta.TABLEAU)
                movimiento = game.moveTableauToTableau(fuente, cg.getIndiceMazo(), cartaSeleccionada.getCarta());
            else if (cg.getOrigen() == OrigenCarta.FOUNDATION
                    && cartaSeleccionada.getCarta() == game.getTableau().get(fuente).verUltimaCarta())
                movimiento = game.moveTableauToFoundation(fuente, cg.getIndiceMazo());
        }

        cartaSeleccionada.setStyle("");
        cartaSeleccionada = null;

        if (movimiento) {
            actualizarVista();
            if (game.isGameOver()) mostrarGameOver(false);
        }
    }

    public void manejarWaste() {
        boolean accion = game.drawOrCycle(); // este ya decide si roba o recicla
        if (accion) {
            actualizarVista();  // para que se redibuje el waste
        }
    }

    public void intentarMoverACasillaVaciaTableau(int destinoIdx) {
        if (cartaSeleccionada != null) {
            boolean mov = false;
            if (cartaSeleccionada.getOrigen() == OrigenCarta.WASTE) {
                mov = game.moveWasteToTableau(destinoIdx);
            } else if (cartaSeleccionada.getOrigen() == OrigenCarta.TABLEAU) {
                int fuenteIdx = cartaSeleccionada.getIndiceMazo();
                mov = game.moveTableauToTableau(fuenteIdx, destinoIdx, cartaSeleccionada.getCarta());
            }
            cartaSeleccionada.setStyle("");
            cartaSeleccionada = null;
            if (mov) actualizarVista();
        }
    }

    public void intentarMoverACasillaVaciaFoundation(int destinoIdx) {
        if (cartaSeleccionada != null) {
            boolean mov = false;
            if (cartaSeleccionada.getOrigen() == OrigenCarta.WASTE) {
                mov = game.moveWasteToFoundation(destinoIdx);
            } else if (cartaSeleccionada.getOrigen() == OrigenCarta.TABLEAU) {
                int fuenteIdx = cartaSeleccionada.getIndiceMazo();
                if (cartaSeleccionada.getCarta() == game.getTableau().get(fuenteIdx).verUltimaCarta()) {
                    mov = game.moveTableauToFoundation(fuenteIdx, destinoIdx);
                }
            }
            cartaSeleccionada.setStyle("");
            cartaSeleccionada = null;
            if (mov) actualizarVista();
        }
    }

    private void reiniciarJuego() {
        game = new SolitaireGame();
        tablero = new TableroGrafico(game, tableauPanes, foundationPanes, wastePane, wasteBackPane, this);
        cartaSeleccionada = null;
        actualizarVista();
    }

    public void reinicioButtonClicked(ActionEvent e) {
        mostrarGameOver(true);
        reiniciarJuego();
    }

    private void mostrarGameOver(boolean quit) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(quit ? "Perdiste :C" : "Ganaste!");
        alert.showAndWait();
        if (!quit) mostrarCreditos();
    }
    @FXML
    private void creditosButtonClicked(ActionEvent event) {
        mostrarCreditos();
    }

    private void mostrarCreditos() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Créditos");
        alert.setHeaderText("Elaborado por: Mayra García Garrido");
        alert.setContentText("Con base en proyecto 'Solitario' elaborado por Prof. Cecilia Curlango");
        alert.showAndWait();
    }
}