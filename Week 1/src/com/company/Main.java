package com.company;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Triangle testTriangle = new Triangle(10, 5, 5);
        Triangle testTriangle2 = new Triangle(3, 5, 7);
        Triangle testTriangle3 = new Triangle(6, 5, 5);
        double longestSide = testTriangle.getLongestSide();
        System.out.println(testTriangle);
        System.out.println("The longest side of the triangle is " + longestSide);

        TwoDimensionalShape shape;
        System.out.println("\nPossible shapes: ");
        Triangle t = new Triangle(4, 5, 6);
        shape = t;
        System.out.println(shape);
        if (shape instanceof MultiVariantShape) {
            System.out.println("This shape has multiple variants");
        }
        else {
            System.out.println("This shape has only one variant");
        }

        shape = new Circle(4);
        System.out.println("\n" + shape);
        if (shape instanceof MultiVariantShape) {
            System.out.println("This shape has multiple variants");
        }
        else {
            System.out.println("This shape has only one variant");
        }

        shape = new Rectangle(4, 5);
        System.out.println("\n" + shape);
        if (shape instanceof MultiVariantShape) {
            System.out.println("This shape has multiple variants");
        }
        else {
            System.out.println("This shape has only one variant");
        }

        TwoDimensionalShape col = new Circle(10);
        Colour colGet = col.getColour();
        System.out.println("\nCircle start col = " + colGet);
        col.setColour(Colour.CYAN);
        System.out.println("Circle col now = " + col.getColour());

        TwoDimensionalShape col2 = new Triangle(10, 15, 29);
        colGet = col2.getColour();
        System.out.println("Triangle start col = " + colGet);

        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("\nTriangle perimeter length: " + testTriangle.calculatePerimeterLength());
        System.out.println("Triangle area: " + df.format(testTriangle.calculateArea()));

        System.out.println("\nTriangle 2 perimeter length: " + testTriangle2.calculatePerimeterLength());
        System.out.println("Triangle 2 area: " + df.format(testTriangle2.calculateArea()));

        System.out.println("\nTriangle 3 perimeter length: " + testTriangle3.calculatePerimeterLength());
        System.out.format("Triangle 3 area: %.2f\n\n", testTriangle3.calculateArea());

        System.out.println("Triangle 1 is " + testTriangle.getVariant());
        System.out.println("Triangle 2 is " + testTriangle2.getVariant());
        System.out.println("Triangle 3 is " + testTriangle3.getVariant());
        Triangle testTriangle4 = new Triangle(10, 15, 29);
        System.out.println("Triangle 4 is " + testTriangle4.getVariant());
        Triangle testTriangle5 = new Triangle(5, 12, 13);
        System.out.println("Triangle 5 is " + testTriangle5.getVariant());
        Triangle testTriangle6 = new Triangle(5, 5, 5);
        System.out.println("Triangle 6 is " + testTriangle6.getVariant());
        Triangle testTriangle7 = new Triangle(0, -1, 8);
        System.out.println("Triangle 7 is " + testTriangle7.getVariant());

        TwoDimensionalShape[] twoDimensionalShapes = new TwoDimensionalShape[100];
        int k = 1;
        twoDimensionalShapes[0] = new Triangle((int) (Math.random()*10),(int)  (Math.random()*10),(int)  (Math.random()*10));
        for (int i = 1; i < 100; i++) {
            double j = Math.random();
            if (j >= 0 && j < 0.33) {
                twoDimensionalShapes[i] = new Triangle((int) (Math.random()*10),(int)  (Math.random()*10),(int)  (Math.random()*10));
                k++;
            }
            else if (j >= 0.33 && j < 0.66) {
                twoDimensionalShapes[i] = new Circle((int) (Math.random()*10));
            }
            else {
                twoDimensionalShapes[i] = new Rectangle((int) (Math.random()*10), (int) (Math.random()*10));
            }
        }
        System.out.println("\nLength: " + Array.getLength(twoDimensionalShapes));
        System.out.println(Arrays.toString(twoDimensionalShapes));

        System.out.println("\nThere are " + k + " triangles in this array");
        System.out.println("There are " + testTriangle.getPopulation() + " triangles overall in this programme");

        TwoDimensionalShape firstShape = twoDimensionalShapes[0];
        Triangle firstTriangle = (Triangle) firstShape;
        TriangleVariant variant = firstTriangle.getVariant();
        int population = firstTriangle.getPopulation();
        System.out.println("The first shape of twoDimensionalShapes array is a " + variant + " triangle. Total triangles = " + population);

        String test1 = "HeLLo3";
        System.out.println(test1.toLowerCase());
    }

}
