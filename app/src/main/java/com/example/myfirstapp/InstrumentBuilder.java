package com.example.myfirstapp;

public class InstrumentBuilder {
    private String symbol;
    private String name;
    private String category;
    private double lastPrice;
    private double lastChange;
    private double lastChangePercent;

    public InstrumentBuilder setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public InstrumentBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public InstrumentBuilder setCategory(String category) {
        this.category = category;
        return this;
    }

    public InstrumentBuilder setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
        return this;
    }

    public InstrumentBuilder setLastChange(double lastChange) {
        this.lastChange = lastChange;
        return this;
    }

    public InstrumentBuilder setLastChangePercent(double lastChangePercent) {
        this.lastChangePercent = lastChangePercent;
        return this;
    }

    public Instrument createInstrument() {
        return new Instrument(symbol, name, category, lastPrice, lastChange, lastChangePercent);
    }
}