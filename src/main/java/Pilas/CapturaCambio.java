package Pilas;

import com.example.solitariomagg.cartas.CartaInglesa;
import com.example.solitariomagg.grafico.OrigenCarta;
import java.util.ArrayList;


public class CapturaCambio {
    OrigenCarta ubicacion;
    int indice;
    ArrayList<CartaInglesa> bloque;

    public CapturaCambio(OrigenCarta ubicacion, CartaInglesa carta) {
        this.ubicacion = ubicacion;
        this.indice = -1;
        this.bloque = new ArrayList<>();
        bloque.add(carta);
    }

    public CapturaCambio(OrigenCarta ubicacion, int indice, ArrayList<CartaInglesa> cartas) {
        this.ubicacion = ubicacion;
        this.indice = indice;
        this.bloque = new ArrayList<>(cartas);
    }

    public OrigenCarta getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(OrigenCarta ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public ArrayList<CartaInglesa> getBloque() {
        return bloque;
    }

    public void setBloque(ArrayList<CartaInglesa> bloque) {
        this.bloque = bloque;
    }
}
