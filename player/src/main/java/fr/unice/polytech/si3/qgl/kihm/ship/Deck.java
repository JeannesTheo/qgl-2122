package fr.unice.polytech.si3.qgl.kihm.ship;

public class Deck {
    private int width;
    private int length;

    public Deck() {
    }

    public Deck(int width, int length) {
        this.width = width;
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deck deck = (Deck) o;

        if (width != deck.width) return false;
        return length == deck.length;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + length;
        return result;
    }

    @Override
    public String toString() {
        return "{\"width\": " + this.width + ", \"length\": " + this.length + '}';
    }
}
