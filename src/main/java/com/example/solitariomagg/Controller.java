package com.example.solitariomagg;

import com.example.solitariomagg.Solitaire.SolitaireGame;
import com.example.solitariomagg.Solitaire.FoundationDeck;
import com.example.solitariomagg.Solitaire.WastePile;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.fxml.FXML;
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
        initialize();
    }

    private void configurarWasteBack() {
        Image reverso = new Image(getClass().getResourceAsStream("/cartas/back.png"));
        ImageView img = new ImageView(reverso);
        img.setFitWidth(80);
        img.setFitHeight(120);
        wasteBackPane.getChildren().add(img);

        // Clic → ciclar 3 cartas visibles (si quieres avanzar en bloques de 3)
        wasteBackPane.setOnMouseClicked(e -> {
            if (game.getWastePile().hayCartas()) {
                game.drawCards();
                actualizarVista();
            }
        });
    }
/*
    private void configurarClickCarta(ImageView cartaView, int origen, int indice, CartaInglesa carta) {
        cartaView.setOnMouseClicked(e -> {
            if (cartaSeleccionada == null) {
                // Primera selección
                cartaSeleccionada = cartaView;
                origenSeleccionado = origen;
                indiceOrigenSeleccionado = indice;
                cartaSeleccionada.setUserData(carta); // guardo la carta asociada
                cartaView.setStyle("-fx-effect: dropshadow(gaussian, yellow, 15, 0.8, 0, 0);");
            } else {
                // Intentar movimiento
                boolean movimiento = false;

                if (origenSeleccionado == 0) { // desde waste
                    if (origen >= 1 && origen <= 7) { // hacia tableau
                        movimiento = game.moveWasteToTableau(indice);
                    } else if (origen >= 10 && origen <= 13) { // hacia foundation
                        movimiento = game.moveWasteToFoundation(indice - 10);
                    }
                } else if (origenSeleccionado >= 1 && origenSeleccionado <= 7) { // desde tableau
                    int fuente = indiceOrigenSeleccionado;
                    CartaInglesa cartaFuente = (CartaInglesa) cartaSeleccionada.getUserData();
                    if (origen >= 1 && origen <= 7) { // hacia tableau
                        movimiento = game.moveTableauToTableau(fuente, indice, cartaFuente);
                    } else if (origen >= 10 && origen <= 13) { // hacia foundation
                        movimiento = game.moveTableauToFoundation(fuente, indice - 10);
                    }
                }


                // limpiar selección
                cartaSeleccionada.setStyle("");
                cartaSeleccionada = null;
                origenSeleccionado = -1;
                indiceOrigenSeleccionado = -1;

                if (movimiento) actualizarVista();
            }
        });
    }
*/
    private void configurarClickCarta(ImageView cartaView, int origen, int indice, CartaInglesa carta) {
        cartaView.setOnMouseClicked(e -> {
            if (cartaSeleccionada == null) {
                // Primera selección
                cartaSeleccionada = cartaView;
                origenSeleccionado = origen;
                indiceOrigenSeleccionado = indice;
                cartaSeleccionada.setUserData(carta);
                cartaView.setStyle("-fx-effect: dropshadow(gaussian, yellow, 15, 0.8, 0, 0);");
            } else {
                boolean movimiento = false;

                if (origenSeleccionado >= 1 && origenSeleccionado <= 7) { // tableau a otro
                    int fuente = indiceOrigenSeleccionado;
                    CartaInglesa cartaFuente = (CartaInglesa) cartaSeleccionada.getUserData();
                    if (origen >= 1 && origen <= 7) {
                        movimiento = game.moveTableauToTableau(fuente, indice, cartaFuente);
                    } else if (origen >= 10 && origen <= 13) {
                        movimiento = game.moveTableauToFoundation(fuente, indice - 10);
                    }
                } else if (origenSeleccionado == 0) { // waste a tableau
                    if (origen >= 1 && origen <= 7) {
                        CartaInglesa cartaFuente = (CartaInglesa) cartaSeleccionada.getUserData();
                        movimiento = game.moveWasteToTableau(indice);
                    }
                }

                // limpiar selección
                cartaSeleccionada.setStyle("");
                cartaSeleccionada = null;
                origenSeleccionado = -1;
                indiceOrigenSeleccionado = -1;

                if (movimiento) actualizarVista();
            }
        });
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
            configurarClickCarta(iv, 0, -1, carta);

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

        configurarClickCarta(iv, 10 + idx, idx, foundation.getUltimaCarta());

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
            // --- Mostrar las cartas ---
            int y = 0;
            for (CartaInglesa c : cartas) {
                ImageView iv = crearImagenDeCarta(c);
                configurarClickCarta(iv, 1, idx, c); // misma lógica de clicks

                iv.setLayoutY(y);
                pane.getChildren().add(iv);
                y += 25;
            }
        }
    }




    private ImageView crearImagenDeCarta(CartaInglesa carta) {
        String nombreImg = "back.png"; // fallback por defecto

        try {
            if (carta.isFaceup()) {
                nombreImg = generarNombreArchivo(carta) + ".png";
            }

            var stream = getClass().getResourceAsStream("/cartas/" + nombreImg);
            if (stream == null) {
                System.out.println("No se encontró la imagen: " + nombreImg + ", usando back.png");
                stream = getClass().getResourceAsStream("/cartas/back.png");
            }

            ImageView iv = new ImageView(new Image(stream));
            iv.setFitWidth(80);
            iv.setFitHeight(120);
            return iv;

        } catch (Exception e) {
            System.out.println("⚠ Error al crear ImageView de la carta: " + carta);
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
            System.out.println("⚠ Valor de carta inválido: " + valor + " (" + carta.getPalo() + ")");
            valor = 1; // fallback
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
