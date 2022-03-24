package fr.unice.polytech.si3.qgl.kihm.logger;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 * La classe permettant d'avoir notre affichage.
 */
public class Printer {

    private static Printer instance;
    private final Logger log;
    private Level logLevel = Level.INFO;

    private Printer() {
        this.log = Logger.getLogger(Printer.class.getName());
        Handler customHandler = new StreamHandler(System.out, new CustomFormatter());
        customHandler.setLevel(Level.ALL);
        this.log.addHandler(customHandler);
        this.log.setLevel(this.logLevel);
        this.log.setUseParentHandlers(false);
    }

    public static Printer get() {
        if (instance == null) {
            instance = new Printer();
        }
        return instance;
    }

    public Level getLogLevel() {
        return this.logLevel;
    }

    public void setLogLevel(Level level) {
        this.logLevel = level;
        this.levelUpdate();
    }

    private void levelUpdate() {
        this.log.setLevel(this.logLevel);
    }

    public void finest(Object message) {
        this.log.finest(message.toString());
    }

    public void finer(Object message) {
        this.log.finer(message.toString());
    }

    public void fine(Object message) {
        this.log.fine(message.toString());
    }

    public void config(Object message) {
        this.log.config(message.toString());
    }

    public void info(Object message) {
        this.log.info(message.toString());
    }

    public void warning(Object message) {
        this.log.warning(message.toString());
    }

    public void severe(Object message) {
        this.log.severe(message.toString());
    }

    public void flush() {
        this.log.getHandlers()[0].flush();
    }
}
