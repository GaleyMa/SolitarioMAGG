package com.example.solitariomagg.grafico;

import com.example.solitariomagg.Controller;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CartaGrafica extends ImageView {

    private final CartaInglesa carta;
    private final Controller controller;
    private OrigenCarta ubicacion;

    // Referencias opcionales a contenedores
    private TableauGrafico contenedorTableau;
    private FoundationGrafico contenedorFoundation;

    public CartaGrafica(CartaInglesa carta, Controller controller) {
        this.carta = carta;
        this.controller = controller;
        this.ubicacion = OrigenCarta.TABLEAU; // valor por defecto

        // Tamaño estándar
        setFitWidth(90);
        setFitHeight(120);

        // Mostrar la carta
        actualizarImagen();

        // Manejo de clics
        setOnMouseClicked(e -> controller.manejarClickCarta(carta, this));
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
    public void actualizarImagen() {
        String ruta = carta.isFaceup() ? generarNombreArchivo(carta) : "/images/back.png";
        setImage(new Image(getClass().getResourceAsStream(ruta)));
    }

    // 🔹 Getters y setters
    public CartaInglesa getCarta() {
        return carta;
    }

    public OrigenCarta getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(OrigenCarta ubicacion) {
        this.ubicacion = ubicacion;
    }

    public TableauGrafico getContenedorTableau() {
        return contenedorTableau;
    }

    public void setContenedorTableau(TableauGrafico contenedorTableau) {
        this.contenedorTableau = contenedorTableau;
        this.ubicacion = OrigenCarta.TABLEAU;
    }

    public FoundationGrafico getContenedorFoundation() {
        return contenedorFoundation;
    }

    public void setContenedorFoundation(FoundationGrafico contenedorFoundation) {
        this.contenedorFoundation = contenedorFoundation;
        this.ubicacion = OrigenCarta.FOUNDATION;
    }

    public void setUbicacionWaste() {
        this.ubicacion = OrigenCarta.WASTE;
    }
}


/*package com.example.solitariomagg.grafico;

import com.example.solitariomagg.Controller;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CartaGrafica extends ImageView {
    private final CartaInglesa carta;
    private final OrigenCarta ubicacion;

    public CartaGrafica(CartaInglesa carta, OrigenCarta ubicacion, Controller controller) {
        this.carta = carta;
        this.ubicacion = ubicacion;

        String nombreImg = carta.isFaceup()
                ? generarNombreArchivo(carta) + ".png"
                : "back.png";

        var stream = getClass().getResourceAsStream("/cartas/" + nombreImg);
        if (stream == null) {
            stream = getClass().getResourceAsStream("/cartas/back.png");
        }

        Image img = new Image(stream);
        this.setImage(img);
        this.setFitWidth(80);
        this.setFitHeight(120);

        // evento de click
        this.setOnMouseClicked(e -> controller.manejarClickCarta(this));
    }

    public CartaInglesa getCarta() {
        return carta;
    }

    public OrigenCarta getUbicacion() {
        return ubicacion;
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
}

/*package com.example.solitariomagg.grafico;
import com.example.solitariomagg.Controller;
import com.example.solitariomagg.cartas.CartaInglesa;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Objects;

public class CartaGrafica extends ImageView {
    private final CartaInglesa carta;
    private OrigenCarta ubicacion; //aqui indicamos si esta en tableau, waste o foundation

    public CartaGrafica(CartaInglesa carta, OrigenCarta ubicacion, Controller controller) {
        this.carta = carta;
        this.ubicacion = ubicacion;

        String ruta = "/cartas/" + generarNombreArchivo() + ".png";
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(ruta)));
        setImage(img);
        setFitWidth(80);
        setPreserveRatio(true);

        setOnMouseClicked(e -> controller.manejarClickCarta(carta,this));
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
    public CartaInglesa getCarta() {
        return carta;
    }
    public OrigenCarta getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(OrigenCarta ubicacion) {
        this.ubicacion = ubicacion;
    }
}
*/