package com.example.solitariomagg;

import com.example.solitariomagg.Solitaire.*;
import com.example.solitariomagg.grafico.*;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;
import Pilas.Pila;
import Pilas.CapturaCambio;

import java.util.ArrayList;

public class Controller {

    private SolitaireGame game;
    private TableroGrafico tablero;
    private CartaGrafica cartaSeleccionada;
    private Pila<CapturaCambio> cambios;

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
        cambios= new Pila<CapturaCambio>(6);
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
        CapturaCambio origen = null;
        CapturaCambio destino = null;

        if (cartaSeleccionada.getOrigen() == OrigenCarta.WASTE) {
            origen = new CapturaCambio(cartaSeleccionada.getOrigen(), game.getWastePile().getUltimasCartas(game.getWastePile().getSize()));
            if (cg.getOrigen() == OrigenCarta.TABLEAU) {
                destino = new CapturaCambio(cg.getOrigen(), cg.getIndiceMazo(), game.getTableau(cg.getIndiceMazo()).getCards());
                movimiento = game.moveWasteToTableau(cg.getIndiceMazo());
            }else if (cg.getOrigen() == OrigenCarta.FOUNDATION) {
                destino= new CapturaCambio(cg.getOrigen(),cg.getIndiceMazo(), game.getTableau(cg.getIndiceMazo()).getCards());
                movimiento = game.moveWasteToFoundation(cg.getIndiceMazo());
            }
        } else if (cartaSeleccionada.getOrigen() == OrigenCarta.TABLEAU) {
            int fuente = cartaSeleccionada.getIndiceMazo();
            origen=new CapturaCambio(OrigenCarta.TABLEAU,fuente,game.getTableau(fuente).getCards());
            if (cg.getOrigen() == OrigenCarta.TABLEAU) {
                destino= new CapturaCambio(OrigenCarta.TABLEAU,cg.getIndiceMazo(),game.getTableau(cg.getIndiceMazo()).getCards());
                movimiento = game.moveTableauToTableau(fuente, cg.getIndiceMazo(), cartaSeleccionada.getCarta());
            }else if (cg.getOrigen() == OrigenCarta.FOUNDATION && cartaSeleccionada.getCarta() == game.getTableau().get(fuente).verUltimaCarta()){
                destino= new CapturaCambio(OrigenCarta.FOUNDATION,game.getFoundation(cg.getIndiceMazo()).getCartas());
                movimiento = game.moveTableauToFoundation(fuente, cg.getIndiceMazo());
            }
        }

        cartaSeleccionada.setStyle("");
        cartaSeleccionada = null;

        if (movimiento) {
            actualizarVista();
            if (game.isGameOver()) mostrarGameOver(false);

            if(cambios.pila_llena()){
                Pila<CapturaCambio> aux= new Pila<>(6);
                Pila<CapturaCambio> basura= new Pila<>(6);

                for(int i=0;i<4;i++){
                 aux.push(cambios.pop());
                }
                basura.push(cambios.pop());
                basura.push(cambios.pop());

                for(int i=0;i<4;i++){
                    cambios.push(aux.pop());
                }

            }
            cambios.push(origen);
            cambios.push(destino);
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

    private void revertirMovimiento(){
        CapturaCambio destino = cambios.pop();
        CapturaCambio origen = cambios.pop();
        int idx= destino.getIndice();
        switch (destino.getUbicacion()) {
            case TABLEAU:
                game.setTableau(idx,destino.getBloque());
                break;
            case FOUNDATION:
                game.setFoundation(idx,destino.getBloque());
                break;
        }

        switch (origen.getUbicacion()) {
            case TABLEAU:
                game.setTableau(origen.getIndice(),origen.getBloque());
                break;
            case WASTE:
                game.setWastePile(origen.getBloque());
                break;
        }

        actualizarVista();
    }

    private void undo(){
        if(cambios.pila_vacia()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Limite de 'Undo' excedido");
            alert.setContentText("Solo se permiten deshacer los últimos 3 movimientos seguidos");
        }else{
            revertirMovimiento();
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