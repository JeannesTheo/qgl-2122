package fr.unice.polytech.si3.qgl.kihm.logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PrinterTest {
    final Level[] levels = {Level.OFF, Level.SEVERE, Level.WARNING, Level.INFO, Level.CONFIG, Level.FINE, Level.FINER, Level.FINEST, Level.ALL};
    Printer printer;
    Level defaultLevel;

    @BeforeEach
    void setUp() {
        this.defaultLevel = Level.INFO;
        this.printer = Printer.get();
        assertNotEquals(null, Printer.get());
    }

    @AfterEach
    void reset() {
        this.defaultLevel = Level.OFF;
        this.printer.setLogLevel(this.defaultLevel);
    }

    String print(Consumer<String> function, String message) {
        this.printer.setLogLevel(Level.ALL);
        String res = "";
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PrintStream capture = new PrintStream(os);
            System.setOut(capture);

            function.accept(message);

            res = os.toString();
            String[] words = res.split(" ");
            res = (words[words.length - 1]).replaceAll("\n\r", "");
            capture.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.setOut(System.out);
        return res;
    }

    @Test
    void changeLogLevel() {
        for (Level lvl : this.levels) {
            this.printer.setLogLevel(lvl);
            assertEquals(lvl, this.printer.getLogLevel());
        }
    }

    /*@Test
    void finest() {
        Consumer<String> func = e -> this.printer.finest(e);
//        assertEquals("finest", this.print(func, "finest"));
        func.accept("finest");
    }

    @Test
    void finer() {
        Consumer<String> func = e -> this.printer.finer(e);
//        assertEquals("finer", this.print(func, "finer"));
        func.accept("finer");
    }

    @Test
    void fine() {
        Consumer<String> func = e -> this.printer.fine(e);
//        assertEquals("fine", this.print(func, "fine"));
        func.accept("fine");
    }

    @Test
    void config() {
        Consumer<String> func = e -> this.printer.config(e);
//        assertEquals("config", this.print(func, "config"));
        func.accept("config");
    }

    @Test
    void info() {
        Consumer<String> func = e -> this.printer.info(e);
//        assertEquals("info", this.print(func, "info"));
        func.accept("info");
    }

    @Test
    void warning() {
        Consumer<String> func = e -> this.printer.warning(e);
//        assertEquals("warning", this.print(func, "warning"));
        func.accept("warning");
    }

    @Test
    void severe() {
        Consumer<String> func = e -> this.printer.severe(e);
//        assertEquals("severe", this.print(func, "severe"));
        func.accept("severe");
    }*/
}