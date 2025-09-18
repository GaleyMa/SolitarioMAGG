package com.example.solitariomagg;

import com.example.solitariomagg.Solitaire.SolitaireGame;
import com.example.solitariomagg.grafico.*;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;

import java.util.ArrayList;

public class Controller {

    private SolitaireGame game;
    private TableroGrafico tablero;

    private CartaGrafica cartaSeleccionada;

    // UI bindings
    @FXML private Pane wastePane;
    @FXML private Pane foundation1;
    @FXML private Pane foundation2;
    @FXML private Pane foundation3;
    @FXML private Pane foundation4;

    @FXML private Pane tableau1;
    @FXML private Pane tableau2;
    @FXML private Pane tableau3;
    @FXML private Pane tableau4;
    @FXML private Pane tableau5;
    @FXML private Pane tableau6;
    @FXML private Pane tableau7;

    @FXML
    public void initialize() {
        // Crear modelo
        game = new SolitaireGame();

        // Crear gráficos
        WasteGrafico wasteGrafico = new WasteGrafico(game.getWastePile(), this);
        wastePane.getChildren().add(wasteGrafico);

        ArrayList<FoundationGrafico> foundationGraficos = new ArrayList<>();
        foundationGraficos.add(new FoundationGrafico(game.getFoundations().get(0), this));
        foundationGraficos.add(new FoundationGrafico(game.getFoundations().get(1), this));
        foundationGraficos.add(new FoundationGrafico(game.getFoundations().get(2), this));
        foundationGraficos.add(new FoundationGrafico(game.getFoundations().get(3), this));

        foundation1.getChildren().add(foundationGraficos.get(0));
        foundation2.getChildren().add(foundationGraficos.get(1));
        foundation3.getChildren().add(foundationGraficos.get(2));
        foundation4.getChildren().add(foundationGraficos.get(3));

        ArrayList<TableauGrafico> tableauGraficos = new ArrayList<>();
        tableauGraficos.add(new TableauGrafico(game.getTableau().get(0), this));
        tableauGraficos.add(new TableauGrafico(game.getTableau().get(1), this));
        tableauGraficos.add(new TableauGrafico(game.getTableau().get(2), this));
        tableauGraficos.add(new TableauGrafico(game.getTableau().get(3), this));
        tableauGraficos.add(new TableauGrafico(game.getTableau().get(4), this));
        tableauGraficos.add(new TableauGrafico(game.getTableau().get(5), this));
        tableauGraficos.add(new TableauGrafico(game.getTableau().get(6), this));

        tableau1.getChildren().add(tableauGraficos.get(0));
        tableau2.getChildren().add(tableauGraficos.get(1));
        tableau3.getChildren().add(tableauGraficos.get(2));
        tableau4.getChildren().add(tableauGraficos.get(3));
        tableau5.getChildren().add(tableauGraficos.get(4));
        tableau6.getChildren().add(tableauGraficos.get(5));
        tableau7.getChildren().add(tableauGraficos.get(6));

        // Crear tablero gráfico con todo
        tablero = new TableroGrafico(wasteGrafico, tableauGraficos, foundationGraficos);

        // Refrescar vista inicial
        tablero.actualizar();
    }

    public void actualizarVista() {
        tablero.actualizar();
    }

    public void manejarClickCarta(CartaInglesa carta, CartaGrafica vista) {
        if (!carta.isFaceup()) return;

        if (cartaSeleccionada == null) {
            cartaSeleccionada = vista;
            cartaSeleccionada.setStyle("-fx-effect: dropshadow(one-pass-box, grey, 20, 0.5, 0, 0);");
        } else {
            boolean movimiento = false;

            OrigenCarta origen = cartaSeleccionada.getUbicacion();
            OrigenCarta destino = vista.getUbicacion();

            if (origen == OrigenCarta.WASTE) {
                if (destino == OrigenCarta.TABLEAU) {
                    int idx = tablero.getTableauGraficos().indexOf(vista.getContenedorTableau());
                    movimiento = game.moveWasteToTableau(idx);
                } else if (destino == OrigenCarta.FOUNDATION) {
                    int idx = tablero.getFoundationGraficos().indexOf(vista.getContenedorFoundation());
                    movimiento = game.moveWasteToFoundation(idx);
                }
            } else if (origen == OrigenCarta.TABLEAU) {
                int fuenteIdx = tablero.getTableauGraficos().indexOf(cartaSeleccionada.getContenedorTableau());
                if (destino == OrigenCarta.TABLEAU) {
                    int destinoIdx = tablero.getTableauGraficos().indexOf(vista.getContenedorTableau());
                    movimiento = game.moveTableauToTableau(fuenteIdx, destinoIdx, cartaSeleccionada.getCarta());
                } else if (destino == OrigenCarta.FOUNDATION) {
                    int destinoIdx = tablero.getFoundationGraficos().indexOf(vista.getContenedorFoundation());
                    movimiento = game.moveTableauToFoundation(fuenteIdx, destinoIdx);
                }
            }

            cartaSeleccionada.setStyle("");
            cartaSeleccionada = null;

            if (movimiento) {
                actualizarVista();
                if (game.isGameOver()) mostrarGameOver(false);
            }
        }
    }

    public void reinicioButtonClicked(ActionEvent event) {
        mostrarGameOver(true);
        reiniciarJuego();
    }

    private void reiniciarJuego() {
        game = new SolitaireGame();
        initialize();
    }

    private void mostrarGameOver(boolean quit) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(quit ? "Perdiste :C" : "¡Ganaste!");
        alert.showAndWait();
    }
}

/*package com.example.solitariomagg;

import com.example.solitariomagg.Solitaire.SolitaireGame;
import com.example.solitariomagg.Solitaire.FoundationDeck;
import com.example.solitariomagg.Solitaire.WastePile;
import com.example.solitariomagg.cartas.CartaInglesa;
import com.example.solitariomagg.grafico.CartaGrafica;
import com.example.solitariomagg.grafico.OrigenCarta;
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
    @FXML private Pane wastePane;
    @FXML private Pane wasteBackPane;
    @FXML private Pane foundation0, foundation1, foundation2, foundation3;
    @FXML private Pane tableau0, tableau1, tableau2, tableau3, tableau4, tableau5, tableau6;

    private CartaGrafica cartaSeleccionada = null;

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

    public void manejarClickCarta(CartaGrafica cg) {
        if (cg.getCarta() != null && !cg.getCarta().isFaceup()) {
            return; // ignorar carta boca abajo
        }

        if (cartaSeleccionada == null) {
            cartaSeleccionada = cg;
            cg.setStyle("-fx-effect: dropshadow(one-pass-box, grey, 20, 0.5, 0, 0);");
            return;
        }

        boolean movimiento = false;
        OrigenCarta origen = cartaSeleccionada.getUbicacion();
        OrigenCarta destino = cg.getUbicacion();

        // --- De Waste ---
        if (origen == OrigenCarta.WASTE) {
            if (destino == OrigenCarta.TABLEAU) {
                int idxDestino = getIndiceTableau(cg);
                movimiento = game.moveWasteToTableau(idxDestino);
            } else if (destino == OrigenCarta.FOUNDATION) {
                int idxFoundation = getIndiceFoundation(cg);
                movimiento = game.moveWasteToFoundation(idxFoundation);
            }

            // --- De Tableau ---
        } else if (origen == OrigenCarta.TABLEAU) {
            int idxOrigen = getIndiceTableau(cartaSeleccionada);
            CartaInglesa cartaFuente = cartaSeleccionada.getCarta();

            if (destino == OrigenCarta.TABLEAU) {
                int idxDestino = getIndiceTableau(cg);
                movimiento = game.moveTableauToTableau(idxOrigen, idxDestino, cartaFuente);
            } else if (destino == OrigenCarta.FOUNDATION) {
                int idxFoundation = getIndiceFoundation(cg);
                // solo la última carta de la pila puede ir a foundation
                List<CartaInglesa> cartasOrigen = game.getTableau().get(idxOrigen).getCards();
                if (!cartasOrigen.isEmpty() &&
                        cartasOrigen.get(cartasOrigen.size() - 1) == cartaFuente) {
                    movimiento = game.moveTableauToFoundation(idxOrigen, idxFoundation);
                }
            }
        }

        // limpiar selección
        cartaSeleccionada.setStyle("");
        cartaSeleccionada = null;

        if (movimiento) {
            actualizarVista();
            if (game.isGameOver()) {
                mostrarGameOver(false);
            }
        }
    }

    private void mostrarGameOver(boolean quit) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(quit ? "Perdiste :C" : "¡Ganaste!");
        alert.showAndWait();
    }

    private void actualizarVista() {
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

        mostrarWaste();
        mostrarFundacion(foundation0, 0);
        mostrarFundacion(foundation1, 1);
        mostrarFundacion(foundation2, 2);
        mostrarFundacion(foundation3, 3);
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
            CartaGrafica cg = new CartaGrafica(carta, OrigenCarta.WASTE, this);
            cg.setLayoutX(x);
            wastePane.getChildren().add(cg);
            x += 25;
        }
    }

    private void mostrarFundacion(Pane pane, int idx) {
        FoundationDeck foundation = game.getFoundations().get(idx);

        CartaInglesa carta = foundation.getUltimaCarta();
        ImageView iv;
        if (carta != null) {
            iv = crearImagenDeCarta(carta);
            CartaGrafica cg = new CartaGrafica(carta, OrigenCarta.FOUNDATION, this);
            pane.getChildren().add(cg);
        } else {
            Image img = new Image(getClass().getResourceAsStream("/cartas/back.png"));
            iv = new ImageView(img);
            iv.setFitWidth(80);
            iv.setFitHeight(120);
            pane.getChildren().add(iv);
        }
    }

    private void mostrarTableau(Pane pane, int idx) {
        List<CartaInglesa> cartas = game.getTableau().get(idx).getCards();
        if (cartas.isEmpty()) {
            Image img = new Image(getClass().getResourceAsStream("/cartas/back.png"));
            ImageView slotVacio = new ImageView(img);
            slotVacio.setFitWidth(80);
            slotVacio.setFitHeight(120);
            slotVacio.setOpacity(0.3);
            pane.getChildren().add(slotVacio);
            return;
        }

        int y = 0;
        for (CartaInglesa c : cartas) {
            CartaGrafica cg = new CartaGrafica(c, OrigenCarta.TABLEAU, this);
            cg.setLayoutY(y);
            pane.getChildren().add(cg);
            y += 25;
        }
    }

    private ImageView crearImagenDeCarta(CartaInglesa carta) {
        String nombreImg = carta.isFaceup()
                ? generarNombreArchivo(carta) + ".png"
                : "back.png";

        var stream = getClass().getResourceAsStream("/cartas/" + nombreImg);
        if (stream == null) {
            stream = getClass().getResourceAsStream("/cartas/back.png");
        }

        ImageView iv = new ImageView(new Image(stream));
        iv.setFitWidth(80);
        iv.setFitHeight(120);
        return iv;
    }

    private String generarNombreArchivo(CartaInglesa carta) {
        int valor = carta.getValor();
        String palo;
        switch (carta.getPalo()) {
            case CORAZON: palo = "c"; break;
            case PICA: palo = "p"; break;
            case DIAMANTE: palo = "d"; break;
            case TREBOL: palo = "t"; break;
            default: palo = "x"; break;
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

    // 🔹 Helpers
    private int getIndiceTableau(CartaGrafica cg) {
        for (int i = 0; i < game.getTableau().size(); i++) {
            if (game.getTableau().get(i).getCards().contains(cg.getCarta())) {
                return i;
            }
        }
        return -1;
    }

    private int getIndiceFoundation(CartaGrafica cg) {
        for (int i = 0; i < game.getFoundations().size(); i++) {
            if (game.getFoundations().get(i).getCartas().contains(cg.getCarta())) {
                return i;
            }
        }
        return -1;
    }
}

*/
/*package com.example.solitariomagg;
import com.example.solitariomagg.Solitaire.*;
import com.example.solitariomagg.grafico.*;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
import java.util.ArrayList;

public class Controller {

    private SolitaireGame game;
    private TableroGrafico tablero;

    // Carta seleccionada en el primer click
    private CartaGrafica cartaSeleccionada;
    private ImageView vistaSeleccionada;

    private ImageView cartaSeleccionadaVista;
    private OrigenCarta origenSeleccionado;
    private int indiceOrigenSeleccionado;   // índice del mazo (ej. tableau 3, foundation 2)
    private int indiceCartaSeleccionada;    // índice de la carta dentro del mazo
    //private CartaInglesa cartaSeleccionada;


    // UI bindings individuales
    @FXML private Pane wastePane;         // para mostrar 3 cartas del waste
    @FXML private Pane wasteBackPane;     // reverso del waste (clic para ciclar)


    @FXML
    private Pane tableau1;
    @FXML
    private Pane tableau2;
    @FXML
    private Pane tableau3;
    @FXML
    private Pane tableau4;
    @FXML
    private Pane tableau5;
    @FXML
    private Pane tableau6;
    @FXML
    private Pane tableau7;

    @FXML
    private Pane foundation1;
    @FXML
    private Pane foundation2;
    @FXML
    private Pane foundation3;
    @FXML
    private Pane foundation4;

    // Listas internas para manejo más cómodo
    private ArrayList<Pane> tableauPanes;
    private ArrayList<Pane> foundationPanes;

    @FXML
    public void initialize() {
        // Inicializar las listas de panes
        tableauPanes = new ArrayList<>();
        tableauPanes.add(tableau1);
        tableauPanes.add(tableau2);
        tableauPanes.add(tableau3);
        tableauPanes.add(tableau4);
        tableauPanes.add(tableau5);
        tableauPanes.add(tableau6);
        tableauPanes.add(tableau7);

        foundationPanes = new ArrayList<>();
        foundationPanes.add(foundation1);
        foundationPanes.add(foundation2);
        foundationPanes.add(foundation3);
        foundationPanes.add(foundation4);

        // Inicializar juego y tablero gráfico
        game = new SolitaireGame();
        tablero = new TableroGrafico(game, tableauPanes, foundationPanes, wastePane,wasteBackPane, this);
        actualizarVista();
    }

    public void actualizarVista() {
        tablero.actualizar();
    }
    public void manejarClickCarta(CartaInglesa carta, ImageView vista) {
        // Convertimos vista a CartaGrafica para acceder a la ubicación
        CartaGrafica cg = (CartaGrafica) vista;

        // Ignorar cartas boca abajo
        if (!carta.isFaceup()) {
            System.out.println("Ignorado: clic en carta boca abajo -> " + carta);
            return;
        }

        if (cartaSeleccionada == null) {
            // Primer click: seleccionar carta
            cartaSeleccionada = cg;
            cartaSeleccionada.setStyle("-fx-effect: dropshadow(one-pass-box, grey, 20, 0.5, 0, 0);");
        } else {
            // Segundo click: intentar movimiento
            boolean movimiento = false;
            OrigenCarta origen = cartaSeleccionada.getUbicacion();
            OrigenCarta destino = cg.getUbicacion();

            if (origen == OrigenCarta.WASTE) {
                if (destino == OrigenCarta.TABLEAU) {
                    int idx = tablero.getIndiceTableau(cg); // método helper para obtener índice del tableau destino
                    movimiento = game.moveWasteToTableau(idx);
                } else if (destino == OrigenCarta.FOUNDATION) {
                    int idx = tablero.getIndiceFoundation(cg);
                    movimiento = game.moveWasteToFoundation(idx);
                }
            } else if (origen == OrigenCarta.TABLEAU) {
                int fuenteIdx = tablero.getIndiceTableau(cartaSeleccionada);
                if (destino == OrigenCarta.TABLEAU) {
                    int destinoIdx = tablero.getIndiceTableau(cg);
                    movimiento = game.moveTableauToTableau(fuenteIdx, destinoIdx, cartaSeleccionada.getCarta());
                } else if (destino == OrigenCarta.FOUNDATION) {
                    int destinoIdx = tablero.getIndiceFoundation(cg);
                    // solo mover si la carta seleccionada es la última del tableau
                    if (cartaSeleccionada.getCarta() == game.getTableau().get(fuenteIdx).getUltimaCarta()) {
                        movimiento = game.moveTableauToFoundation(fuenteIdx, destinoIdx);
                    } else {
                        movimiento = false;
                    }
                }
            }

            // Limpiar selección visual
            cartaSeleccionada.setStyle("");
            cartaSeleccionada = null;

            // Actualizar la vista si hubo movimiento
            if (movimiento) {
                actualizarVista();
                if (game.isGameOver()) {
                    mostrarGameOver(false);
                }
            }
        }
    }

    public void manejarWaste(boolean vacio) {

        if (vacio) {
            game.reloadDrawPile();
        } else {
            game.drawOrCycle();// cicla las cartas
            tablero.actualizar();   // actualiza WasteGrafico y demás
        }
    }
    private void reiniciarJuego() {
        game = new SolitaireGame();
        tablero = new TableroGrafico(game, tableauPanes, foundationPanes, wastePane,wasteBackPane, this);
        cartaSeleccionada = null;
        vistaSeleccionada = null;
        actualizarVista();
    }


    @FXML
    private void manejarClickWaste() {
        CartaInglesa carta = game.getWastePile().verCarta();
        if (carta != null) {
            manejarClickCarta(carta, null); // No hay ImageView asociado aquí
        }
    }


    public void creditosButtonClicked(ActionEvent event) {
        mostrarCreditos();
    }

    public void mostrarCreditos() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Creditos");
        alert.setHeaderText("Elaborado por: Mayra García Garrido");
        alert.setContentText("Con base en proyecto 'Solitario' elaborado por Prof. Cecilia Curlango\n Repositorio: https://github.com/GaleyMa/SolitarioMAGG#");
        alert.show();
    }

    public void reinicioButtonClicked(ActionEvent event) {
        mostrarGameOver(true);
        reiniciarJuego();
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
}
*/