package com.example.solitariomagg;

import com.example.solitariomagg.Solitaire.SolitaireGame;
import com.example.solitariomagg.Solitaire.FoundationDeck;
import com.example.solitariomagg.Solitaire.TableauDeck;
import com.example.solitariomagg.Solitaire.WastePile;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

public class Controller {

    @FXML private ImageView drawPile;
    @FXML private Pane wastePane;
    @FXML private Pane foundation0;
    @FXML private Pane foundation1;
    @FXML private Pane foundation2;
    @FXML private Pane foundation3;
    @FXML private Pane tableau0;
    @FXML private Pane tableau1;
    @FXML private Pane tableau2;
    @FXML private Pane tableau3;
    @FXML private Pane tableau4;
    @FXML private Pane tableau5;
    @FXML private Pane tableau6;

    private ImageView cartaSeleccionada = null;
    private int origenTablaSeleccionado = -1;   // 0=waste, 1-7=tableaus
    private int cartaIndexSeleccionada = -1;    // índice dentro del tableau

    private SolitaireGame game;

    @FXML
    public void initialize() {
        game = new SolitaireGame();
        actualizarVista();

        drawPile.setOnMouseClicked(e -> {
            game.drawCards();
            actualizarVista();
        });
    }

    /** Configura clic en cada carta */
    private void configurarClickCarta(ImageView cartaView, int origenTabla, int indiceCarta) {
        cartaView.setOnMouseClicked(e -> {
            if (cartaSeleccionada == null) {
                // Primera selección
                cartaSeleccionada = cartaView;
                origenTablaSeleccionado = origenTabla;
                cartaIndexSeleccionada = indiceCarta;
                cartaView.setStyle("-fx-effect: dropshadow(gaussian, yellow, 15, 0.8, 0, 0);");
            } else {
                // Deselección si clickea la misma carta
                if (cartaView == cartaSeleccionada) {
                    cartaSeleccionada.setStyle("");
                    cartaSeleccionada = null;
                    origenTablaSeleccionado = -1;
                    cartaIndexSeleccionada = -1;
                    return;
                }

                // Determinar movimiento válido según origen y destino
                if (origenTablaSeleccionado == 0 && origenTabla >= 1 && origenTabla <= 7) {
                    // Waste -> Tableau
                    game.moveWasteToTableau(origenTabla - 1);
                } else if (origenTablaSeleccionado >= 1 && origenTablaSeleccionado <= 7 &&
                        origenTabla >= 1 && origenTabla <= 7) {
                    // Tableau -> Tableau
                    game.moveTableauToTableau(origenTablaSeleccionado - 1, origenTabla - 1);
                } else if (origenTablaSeleccionado >= 1 && origenTablaSeleccionado <= 7 &&
                        origenTabla >= 0 && origenTabla <= 3) {
                    // Tableau -> Foundation
                    game.moveTableauToFoundation(origenTablaSeleccionado - 1, origenTabla);
                } else if (origenTablaSeleccionado == 0 && origenTabla >= 0 && origenTabla <= 3) {
                    // Waste -> Foundation
                    game.moveWasteToFoundation(origenTabla);
                }
                // Si no es movimiento válido, simplemente deseleccionar

                // Limpiar selección
                cartaSeleccionada.setStyle("");
                cartaSeleccionada = null;
                origenTablaSeleccionado = -1;
                cartaIndexSeleccionada = -1;

                actualizarVista();
            }
        });
    }

    /** Actualiza toda la vista */
    private void actualizarVista() {
        // Limpiar contenedores
        wastePane.getChildren().clear();
        foundation0.getChildren().clear();
        foundation1.getChildren().clear();
        foundation2.getChildren().clear();
        foundation3.getChildren().clear();
        tableau0.getChildren().clear();
        tableau1.getChildren().clear();
        tableau2.getChildren().clear();
        tableau3.getChildren().clear();
        tableau4.getChildren().clear();
        tableau5.getChildren().clear();
        tableau6.getChildren().clear();

        // Mostrar waste
        WastePile waste = game.getWastePile();
        CartaInglesa topWaste = waste.verCarta();
        if (topWaste != null) {
            ImageView iv = crearImagenDeCarta(topWaste);
            configurarClickCarta(iv, 0, 0);
            wastePane.getChildren().add(iv);
        }

        // Mostrar foundations
        mostrarFundacion(foundation0, game.getFoundation(0), 0);
        mostrarFundacion(foundation1, game.getFoundation(1), 1);
        mostrarFundacion(foundation2, game.getFoundation(2), 2);
        mostrarFundacion(foundation3, game.getFoundation(3), 3);

        // Mostrar tableaus
        mostrarTableau(tableau0, game.getTableau().get(0).getCards(), 0);
        mostrarTableau(tableau1, game.getTableau().get(1).getCards(), 1);
        mostrarTableau(tableau2, game.getTableau().get(2).getCards(), 2);
        mostrarTableau(tableau3, game.getTableau().get(3).getCards(), 3);
        mostrarTableau(tableau4, game.getTableau().get(4).getCards(), 4);
        mostrarTableau(tableau5, game.getTableau().get(5).getCards(), 5);
        mostrarTableau(tableau6, game.getTableau().get(6).getCards(), 6);
    }

    /** Muestra la última carta de foundation si existe */
    private void mostrarFundacion(Pane pane, FoundationDeck foundation, int idx) {
        if (foundation != null && foundation.getUltimaCarta() != null) {
            ImageView iv = crearImagenDeCarta(foundation.getUltimaCarta());
            pane.getChildren().add(iv);
        }
    }

    /** Muestra las cartas del tableau y agrega clics */
    private void mostrarTableau(Pane pane, List<CartaInglesa> cartas, int idxTableau) {
        int y = 0;
        for (int i = 0; i < cartas.size(); i++) {
            CartaInglesa c = cartas.get(i);
            ImageView iv = crearImagenDeCarta(c);
            iv.setLayoutY(y);
            pane.getChildren().add(iv);

            configurarClickCarta(iv, idxTableau + 1, i);

            y += 25;
        }
    }

    /** Crea la vista de una carta */
    private ImageView crearImagenDeCarta(CartaInglesa carta) {
        String nombreImg = carta.isFaceup() ? generarNombreArchivo(carta) + ".png" : "back.png";

        var stream = getClass().getResourceAsStream("/cartas/" + nombreImg);
        if (stream == null) throw new RuntimeException("No se encontró la imagen: " + nombreImg);

        Image img = new Image(stream);
        ImageView iv = new ImageView(img);
        iv.setFitWidth(80);
        iv.setFitHeight(120);
        return iv;
    }

    /** Genera nombre de archivo de la carta */
    private String generarNombreArchivo(CartaInglesa carta) {
        String palo;
        switch (carta.getPalo()) {
            case CORAZON: palo = "c"; break;
            case PICA: palo = "p"; break;
            case DIAMANTE: palo = "d"; break;
            case TREBOL: palo = "t"; break;
            default: palo = ""; break;
        }

        String valor;
        switch (carta.getValor()) {
            case 1: valor = "a"; break;
            case 11: valor = "j"; break;
            case 12: valor = "q"; break;
            case 13: valor = "k"; break;
            default: valor = String.valueOf(carta.getValor()); break;
        }

        return palo + valor;
    }
}
