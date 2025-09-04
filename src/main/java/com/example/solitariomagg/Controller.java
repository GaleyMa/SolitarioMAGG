package com.example.solitariomagg;

import com.example.solitariomagg.Solitaire.SolitaireGame;
import com.example.solitariomagg.Solitaire.FoundationDeck;
import com.example.solitariomagg.Solitaire.TableauDeck;
import com.example.solitariomagg.Solitaire.WastePile;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.List;

public class Controller {

    @FXML private ImageView drawPile;
    @FXML private Pane wastePane;         // para mostrar 3 cartas del waste
    @FXML private Pane wasteBackPane;     // reverso del waste (clic para ciclar)
    @FXML private HBox tableauContainer;  // contenedor de los 7 tableau
    @FXML private Pane foundation0, foundation1, foundation2, foundation3;
    @FXML private Pane tableau0, tableau1, tableau2, tableau3, tableau4, tableau5, tableau6;

    private ImageView cartaSeleccionada = null;
    private int origenSeleccionado = -1;       // 0 = waste, 1–7 = tableau
    private int indiceOrigenSeleccionado = -1; // índice del tableau o foundation
    private SolitaireGame game;

    @FXML
    public void initialize() {
        game = new SolitaireGame();
        configurarWasteBack();
        actualizarVista();

        // Clic en drawPile: robar cartas al waste
        drawPile.setOnMouseClicked(e -> {
            if (game.getDrawPile().hayCartas()) {
                game.drawCards();
            } else {
                // Si draw está vacío, recargar desde waste
                game.reloadDrawPile();
            }
            actualizarVista();
        });
    }

    /**
     * Configura el reverso del waste (imagen clickeable para ciclar)
     */
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

    /**
     * Configura el clic sobre una carta
     */
    private void configurarClickCarta(ImageView cartaView, int origen, int indice) {
        cartaView.setOnMouseClicked(e -> {
            if (cartaSeleccionada == null) {
                // Primera selección
                cartaSeleccionada = cartaView;
                origenSeleccionado = origen;
                indiceOrigenSeleccionado = indice;
                cartaView.setStyle("-fx-effect: dropshadow(gaussian, yellow, 15, 0.8, 0, 0);");
            } else {
                // Intentar movimiento
                boolean movimiento = false;

                if (origenSeleccionado == 0) { // desde waste
                    if (origen >= 1 && origen <= 7) {
                        movimiento = game.moveWasteToTableau(indice);
                    } else if (origen >= 10 && origen <= 13) {
                        movimiento = game.moveWasteToFoundation(indice - 10);
                    }
                } else if (origenSeleccionado >= 1 && origenSeleccionado <= 7) { // desde tableau
                    int fuente = indiceOrigenSeleccionado;
                    if (origen >= 1 && origen <= 7) {
                        movimiento = game.moveTableauToTableau(fuente, indice);
                    } else if (origen >= 10 && origen <= 13) {
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

    /**
     * Redibuja todo el tablero
     */
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

    /**
     * Muestra hasta 3 cartas del waste
     */
    private void mostrarWaste() {
        WastePile waste = game.getWastePile();
        List<CartaInglesa> visibles = waste.getUltimasCartas(3);

        int x = 0;
        for (CartaInglesa carta : visibles) {
            ImageView iv = crearImagenDeCarta(carta);
            configurarClickCarta(iv, 0, -1); // origen waste
            iv.setLayoutX(x);
            wastePane.getChildren().add(iv);
            x += 25; // separarlas un poco
        }
    }

    /**
     * Muestra la última carta de una foundation
     */
    private void mostrarFundacion(Pane pane, int idx) {
        FoundationDeck foundation = game.getFoundations().get(idx);

        ImageView iv;
        if (foundation.getUltimaCarta() != null) {
            iv = crearImagenDeCarta(foundation.getUltimaCarta());
        } else {
            // Mostrar un "slot vacío" con el reverso
            Image img = new Image(getClass().getResourceAsStream("/cartas/back.png"));
            iv = new ImageView(img);
            iv.setFitWidth(80);
            iv.setFitHeight(120);
        }

        configurarClickCarta(iv, 10 + idx, idx); // 10–13 = foundations
        pane.getChildren().add(iv);
    }

    /**
     * Muestra las cartas de un tableau
     */
    private void mostrarTableau(Pane pane, int idx) {
        List<CartaInglesa> cartas = game.getTableau().get(idx).getCards();
        int y = 0;
        for (CartaInglesa c : cartas) {
            ImageView iv = crearImagenDeCarta(c);
            configurarClickCarta(iv, 1, idx); // origen = tableau
            iv.setLayoutY(y);
            pane.getChildren().add(iv);
            y += 25;
        }
    }

    /**
     * Crea el ImageView de una carta
     */
    private ImageView crearImagenDeCarta(CartaInglesa carta) {
        String nombreImg = carta.isFaceup() ? generarNombreArchivo(carta) + ".png" : "back.png";
        var stream = getClass().getResourceAsStream("/cartas/" + nombreImg);
        if (stream == null) throw new RuntimeException("No se encontró la imagen: " + nombreImg);

        ImageView iv = new ImageView(new Image(stream));
        iv.setFitWidth(80);
        iv.setFitHeight(120);
        return iv;
    }

    /**
     * Genera el nombre del archivo de la carta
     */
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
