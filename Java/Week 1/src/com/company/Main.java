package com.company;

public class Main {

    public static void main(String[] args) {
        Triangle testTriangle = new Triangle(5, 7, 9);
        int longestSide = testTriangle.getLongestSide();
        System.out.println(testTriangle);
        System.out.println("The longest side of the triangle is " + longestSide);

        TwoDimensionalShape shape;
        System.out.println("Possible shapes: ");
        Circle c = new Circle(4);
        shape = c;
        System.out.println(shape);

        Rectangle r = new Rectangle(4, 5);
        shape = r;
        System.out.println(shape);

        Triangle t = new Triangle(4, 5, 6);
        shape = t;
        System.out.println(shape);

        TwoDimensionalShape col = new Circle(10);
        Colour colGet = col.getColour();
        System.out.println("Circle start col = " + colGet);
        col.setColour(Colour.CYAN);
        System.out.println("Circle col now = " + col.getColour());

        TwoDimensionalShape col2 = new Triangle(10, 15, 29);
        colGet = col2.getColour();
        System.out.println("Triangle start col = " + colGet);

    }

}
