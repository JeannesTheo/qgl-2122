package fr.unice.polytech.si3.qgl.kihm.ship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DeckTest {
    Deck deck;
    int width = 4;
    int length = 56;

    @BeforeEach
    void setUp() {
        this.deck = new Deck(this.width, this.length);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(this.width, this.deck.getWidth());
        assertEquals(this.length, this.deck.getLength());
        assertNotEquals(this.width + 0.5, this.deck.getWidth());
        assertNotEquals(this.width + 0.5, this.deck.getLength());
        this.deck.setWidth(78);
        this.deck.setLength(781);
        assertNotEquals(this.width, this.deck.getWidth());
        assertNotEquals(this.length, this.deck.getLength());
    }

    @Test
    void testEquals() {
        assertEquals(this.deck, this.deck);
        assertEquals(new Deck(this.deck.getWidth(), this.deck.getLength()), this.deck);
        assertNotEquals(null, this.deck);
        assertNotEquals(null, new Deck());
        assertNotEquals(new Deck(78, 781), this.deck);
    }

    @Test
    void testHashCode() {
        assertEquals(this.deck.hashCode(), this.deck.hashCode());
        assertEquals(new Deck(this.deck.getWidth(), this.deck.getLength()).hashCode(), this.deck.hashCode());
        assertNotEquals(0, this.deck.hashCode());
        assertEquals(0, new Deck().hashCode());
        assertNotEquals(new Deck(78, 781).hashCode(), this.deck.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("{\"width\": " + this.width + ", \"length\": " + this.length + '}', this.deck.toString());
    }
}