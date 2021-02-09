package com.company;

abstract class TwoDimensionalShape {
    private Colour firstColour;

    public TwoDimensionalShape() {
        firstColour = Colour.BLUE;
    }

    public TwoDimensionalShape(Colour firstColour) {
        this.firstColour = firstColour;
    }

    Colour getColour() {
        return firstColour;
    }

    void setColour(Colour firstColour) {
        this.firstColour = firstColour;
    }

    abstract double calculateArea();
    abstract double calculatePerimeterLength();

}
