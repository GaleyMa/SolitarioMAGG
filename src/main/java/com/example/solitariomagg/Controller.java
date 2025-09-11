package com.example.solitariomagg;

import com.example.solitariomagg.Solitaire.SolitaireGame;
import com.example.solitariomagg.Solitaire.FoundationDeck;
import com.example.solitariomagg.Solitaire.WastePile;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;


import java.util.List;

public class Controller {
    @FXML private Button botonCreditos;
    @FXML private Button botonReiniciar;
    @FXML private ImageView drawPile;
    @FXML private Pane wastePane;         // para mostrar 3 cartas del waste
    @FXML private Pane wasteBackPane;     // reverso del waste (clic para ciclar)
    @FXML private Pane foundation0, foundation1, foundation2, foundation3;
    @FXML private Pane tableau0, tableau1, tableau2, tableau3, tableau4, tableau5, tableau6;

    private ImageView cartaSeleccionada = null;
    private int origenSeleccionado = -1;       // 0 = waste, 1–7 = tableau
    private int indiceOrigenSeleccionado = -1;// índice del tableau o foundation

    private SolitaireGame game;

    @FXML
    public void initialize() {
        game = new SolitaireGame();
        configurarWasteBack();
        actualizarVista();

        drawPile.setOnMouseClicked(e -> {
            if (game.drawOrCycle()) {
                actualizarVista();
            }
        });



        wasteBackPane.setOnMouseClicked(e -> {
            if (game.drawOrCycle()) {
                actualizarVista();
            }
        });

    }
    public void reinicioButtonClicked(ActionEvent event) {
        mostrarGameOver(true);
        initialize();
    }

    private void configurarWasteBack() {
        Image reverso = new Image(getClass().getResourceAsStream("/cartas/back.png"));
        ImageView img = new ImageView(reverso);
        img.setFitWidth(80);
        img.setFitHeight(120);
        wasteBackPane.getChildren().add(img);

        wasteBackPane.setOnMouseClicked(e -> {
            if (game.getWastePile().hayCartas()) {
                game.drawCards();
                actualizarVista();
            }
        });
    }

private void selectCarta(ImageView cartaView, int origen, int indice, CartaInglesa carta) {
    cartaView.setOnMouseClicked(e -> {

        if (carta != null && !carta.isFaceup()) {
            System.out.println("Ignorado: clic en carta boca abajo -> " + carta);
            return;
        }

        if (cartaSeleccionada == null) {

            if (carta == null) {
                return;
            }

            cartaSeleccionada = cartaView;
            origenSeleccionado = origen;
            indiceOrigenSeleccionado = indice;
            cartaSeleccionada.setUserData(carta);
            cartaView.setStyle("-fx-effect: dropshadow(one-pass-box, grey, 15, 0.8, 0, 0);");

            System.out.println(String.format("Seleccionada: origen=%d idx=%d carta=%s", origen, indice, carta));
            return;
        }

        boolean movimiento = false;

        //de waste
        if (origenSeleccionado == 0) {
            if (origen >= 1 && origen <= 7) {
                System.out.println("Intentando moveWasteToTableau(dest=" + indice + ")");
                movimiento = game.moveWasteToTableau(indice);
                System.out.println("Resultado moveWasteToTableau: " + movimiento);
            } else if (origen >= 10 && origen <= 13) {
                System.out.println("Intentando moveWasteToFoundation(dest=" + (indice - 10) + ")");
                movimiento = game.moveWasteToFoundation(indice - 10);
                System.out.println("Resultado moveWasteToFoundation: " + movimiento);
            }

            // de tableu
        } else if (origenSeleccionado >= 1 && origenSeleccionado <= 7) {
            int fuenteIdx = indiceOrigenSeleccionado;
            CartaInglesa cartaFuente = (CartaInglesa) cartaSeleccionada.getUserData();
            System.out.println("Carta fuente seleccionada: " + cartaFuente + " (tabla " + fuenteIdx + ")");

            if (origen >= 1 && origen <= 7) {
                System.out.println("Intentando moveTableauToTableau(fuente=" + fuenteIdx + ", destino=" + indice + ", carta=" + cartaFuente + ")");
                movimiento = game.moveTableauToTableau(fuenteIdx, indice, cartaFuente);
                System.out.println("Resultado moveTableauToTableau: " + movimiento);

            } else if (origen >= 10 && origen <= 13) {

                var srcDeck = game.getTableau().get(fuenteIdx);
                CartaInglesa ultima = srcDeck.getUltimaCarta();
                if (ultima == cartaFuente) {
                    movimiento = game.moveTableauToFoundation(fuenteIdx, indice - 10);
                } else {
                    movimiento = false;
                }
            }
        }

        cartaSeleccionada.setStyle("");
        cartaSeleccionada = null;
        origenSeleccionado = -1;
        indiceOrigenSeleccionado = -1;

        if (movimiento) {
            actualizarVista();
            if (game.isGameOver()) {
                mostrarGameOver(false);
            }
        }
    });
}
    private void mostrarGameOver(boolean quit) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        if (quit) {
            alert.setContentText("Perdiste :C");
        }else {
            alert.setContentText("Ganaste!");
        }
        alert.showAndWait();
    }

    private void actualizarVista() {
        // limpiar contenedores
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

        // mostrar waste (hasta 3 cartas)
        mostrarWaste();

        // mostrar foundations
        mostrarFundacion(foundation0, 0);
        mostrarFundacion(foundation1, 1);
        mostrarFundacion(foundation2, 2);
        mostrarFundacion(foundation3, 3);

        // mostrar tableaux
        mostrarTableau(tableau0, 0);
        mostrarTableau(tableau1, 1);
        mostrarTableau(tableau2, 2);
        mostrarTableau(tableau3, 3);
        mostrarTableau(tableau4, 4);
        mostrarTableau(tableau5, 5);
        mostrarTableau(tableau6, 6);
    }

    private void mostrarWaste() {
        WastePile waste = game.getWastePile();
        List<CartaInglesa> visibles = waste.getUltimasCartas(3);

        int x = 0;
        for (CartaInglesa carta : visibles) {
            ImageView iv = crearImagenDeCarta(carta);
            selectCarta(iv, 0, -1, carta);

            iv.setLayoutX(x);
            wastePane.getChildren().add(iv);
            x += 25;
        }
    }

    private void mostrarFundacion(Pane pane, int idx) {
        FoundationDeck foundation = game.getFoundations().get(idx);

        ImageView iv;
        CartaInglesa carta = foundation.getUltimaCarta();
        if (carta != null) {
            iv = crearImagenDeCarta(carta);
        } else {
            Image img = new Image(getClass().getResourceAsStream("/cartas/back.png"));
            iv = new ImageView(img);
            iv.setFitWidth(80);
            iv.setFitHeight(120);
        }

        selectCarta(iv, 10 + idx, 10 + idx, foundation.getUltimaCarta());


        pane.getChildren().add(iv);
    }


    private void mostrarTableau(Pane pane, int idx) {
        List<CartaInglesa> cartas = game.getTableau().get(idx).getCards();
        pane.getChildren().clear(); // limpiar antes de pintar

        if (cartas.isEmpty()) {
            // --- Mostrar slot vacío ---
            Image img = new Image(getClass().getResourceAsStream("/cartas/back.png"));
            ImageView slotVacio = new ImageView(img);
            slotVacio.setFitWidth(80);
            slotVacio.setFitHeight(120);
            slotVacio.setOpacity(0.3);

            slotVacio.setOnMouseClicked(e -> {
                if (cartaSeleccionada != null) {
                    CartaInglesa carta = (CartaInglesa) cartaSeleccionada.getUserData();
                    int fuente = indiceOrigenSeleccionado;

                    boolean movimiento = game.moveWasteToTableau(idx)
                            || game.moveTableauToTableau(fuente, idx, carta);

                    // limpiar selección
                    cartaSeleccionada.setStyle("");
                    cartaSeleccionada = null;
                    origenSeleccionado = -1;
                    indiceOrigenSeleccionado = -1;

                    if (movimiento) actualizarVista();
                }
            });

            pane.getChildren().add(slotVacio);

        } else {
            int y = 0;
            for (CartaInglesa c : cartas) {
                ImageView iv = crearImagenDeCarta(c);
                selectCarta(iv, 1, idx, c); // misma lógica de clicks

                iv.setLayoutY(y);
                pane.getChildren().add(iv);
                y += 25;
            }
        }
    }




    private ImageView crearImagenDeCarta(CartaInglesa carta) {
        String nombreImg = "back.png";

        try {
            if (carta.isFaceup()) {
                nombreImg = generarNombreArchivo(carta) + ".png";
            }

            var stream = getClass().getResourceAsStream("/cartas/" + nombreImg);
            if (stream == null) {
                stream = getClass().getResourceAsStream("/cartas/back.png");
            }

            ImageView iv = new ImageView(new Image(stream));
            iv.setFitWidth(80);
            iv.setFitHeight(120);
            return iv;

        } catch (Exception e) {
           var fallbackStream = getClass().getResourceAsStream("/cartas/back.png");
            ImageView iv = new ImageView(new Image(fallbackStream));
            iv.setFitWidth(80);
            iv.setFitHeight(120);
            return iv;
        }
    }

    private String generarNombreArchivo(CartaInglesa carta) {
        int valor = carta.getValor();

        if (valor < 1 || valor > 13) {
            System.out.println("Valor de carta inválido: " + valor + " (" + carta.getPalo() + ")");
            valor = 1;
        }

        String palo;
        switch (carta.getPalo()) {
            case CORAZON: palo = "c"; break;
            case PICA: palo = "p"; break;
            case DIAMANTE: palo = "d"; break;
            case TREBOL: palo = "t"; break;
            default: palo = "x"; break; // palo desconocido
        }

        String valorStr;
        switch (valor) {
            case 1: valorStr = "a"; break;
            case 11: valorStr = "j"; break;
            case 12: valorStr = "q"; break;
            case 13: valorStr = "k"; break;
            default: valorStr = String.valueOf(valor); break;
        }

        return palo + valorStr;
    }
}
