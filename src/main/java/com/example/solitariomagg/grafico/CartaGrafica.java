package com.example.solitariomagg.grafico;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CartaGrafica {
    private final CartaInglesa carta;
    private final ImageView vista;

    public CartaGrafica(CartaInglesa carta) {
        this.carta = carta;
        this.vista = crearVista();
    }

    private ImageView crearVista() {
        String nombreImg = "back.png";
        if (carta != null && carta.isFaceup()) {
            nombreImg = generarNombreArchivo() + ".png";
        }
        var stream = getClass().getResourceAsStream("/cartas/" + nombreImg);
        ImageView iv = new ImageView(new Image(stream));
        iv.setFitWidth(80);
        iv.setFitHeight(120);
        return iv;
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

    public ImageView getVista() {
        return vista;
    }

    public CartaInglesa getCarta() {
        return carta;
    }
}
