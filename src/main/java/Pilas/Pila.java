package Pilas;

public class Pila<T> {
    private T[] pila;
    private int tope;
    public Pila(){
        this.tope=-1;
        this.pila=(T[]) new Object[10];
    }
    public Pila(int max){
        this.tope=-1;
        this.pila=(T[]) new Object[max];
    }
    public void push(T dato){
        if(pila_llena()) System.out.println("\nDesbordamiento\n");
        else {
            tope++;
            pila[tope]=dato;
        }
    }
    public T pop(){
        T dato=null;
        if(pila_vacia()) System.out.println("\nSubesbordamiento\n");
        else {
            dato=pila[tope];
            tope--;
        }
        return dato;
    }
    public boolean pila_llena(){
        if(tope==pila.length-1) return true;
        else return false;
    }
    public boolean pila_vacia(){
        if(tope==-1) return true;
        else return false;
    }

}

