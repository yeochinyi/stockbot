package com.example.myfirstapp;

/**
 * Created by Chris on 1/15/2017.
 */

public class Instrument {

    private String symbol;
    private String name;
    private String category;
    private double lastPrice;
    private double lastChange;
    private double lastChangePercent;


    public Instrument(String symbol, String name, String category, double lastPrice, double lastChange, double lastChangePercent) {
        this.symbol = symbol;
        this.name = name;
        this.category = category;
        this.lastPrice = lastPrice;
        this.lastChange = lastChange;
        this.lastChangePercent = lastChangePercent;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public double getLastChange() {
        return lastChange;
    }

    public double getLastChangePercent() {
        return lastChangePercent;
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Instrument that = (Instrument) o;

        return symbol != null ? symbol.equals(that.symbol) : that.symbol == null;

    }

    @Override
    public int hashCode() {
        return symbol != null ? symbol.hashCode() : 0;
    }
}
