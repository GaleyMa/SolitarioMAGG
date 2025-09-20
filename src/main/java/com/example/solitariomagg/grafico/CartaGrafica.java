package com.example.solitariomagg.grafico;

import com.example.solitariomagg.Controller;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Objects;

public class CartaGrafica extends ImageView {
    private final CartaInglesa carta;
    private OrigenCarta origen;
    private int indiceMazo; // índice del tableau o foundation

    public CartaGrafica(CartaInglesa carta, OrigenCarta origen, int indiceMazo, Controller controller) {
        this.carta = carta;
        this.origen = origen;
        this.indiceMazo = indiceMazo;

        String nombreImg = carta.isFaceup()
                ? generarNombreArchivo() + ".png"
                : "back.png";

        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cartas/" + nombreImg)));
        setImage(img);
        setFitWidth(80);
        setPreserveRatio(true);

        setOnMouseClicked(e -> controller.seleccionarCarta(carta, this));
    }

    private String generarNombreArchivo() {
        int valor = carta.getValor();
        String palo;
        switch (carta.getPalo()) {
            case CORAZON -> palo = "c";
            case PICA -> palo = "p";
            case DIAMANTE -> palo = "d";
            case TREBOL -> palo = "t";
            default -> palo = "x";
        }
        String valorStr = switch (valor) {
            case 1 -> "a";
            case 11 -> "j";
            case 12 -> "q";
            case 13 -> "k";
            default -> String.valueOf(valor);
        };
        return palo + valorStr;
    }
    public CartaInglesa getCarta() { return carta; }
    public OrigenCarta getOrigen() { return origen; }
    public int getIndiceMazo() { return indiceMazo; }
}
