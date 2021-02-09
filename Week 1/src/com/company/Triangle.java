package com.company;

import java.math.BigDecimal;

import static java.lang.Math.*;

public class Triangle extends TwoDimensionalShape implements MultiVariantShape
{
    private final double len1, len2, len3;
    private TriangleVariant variant;
    static int population = 0;

    public Triangle(double length1, double length2, double length3) {
        super(Colour.GREEN);
        len1 = length1;
        len2 = length2;
        len3 = length3;
        variantCalc();
        Triangle.population++;
    }

    public int getPopulation() {
        return Triangle.population;
    }

    public TriangleVariant getVariant() {
        return variant;
    }

    public double getLongestSide() {
        double longest;
        longest = len1;
        if (len2 > longest) {
            longest = len2;
        }
        if (len3 > longest) {
            longest = len3;
        }
        return longest;
    }

    public void variantCalc() {
        if (illegalTriangle()) {
            variant = TriangleVariant.ILLEGAL;
        }
        else if (impossibleTriangle()) {
            variant = TriangleVariant.IMPOSSIBLE;
        }
        else if (flatTriangle()) {
            variant = TriangleVariant.FLAT;
        }
        else if (equilateralTriangle()) {
            variant = TriangleVariant.EQUILATERAL;
        }
        else if (rightTriangle()) {
            variant = TriangleVariant.RIGHT;
        }
        else if (scaleneTriangle()) {
            variant = TriangleVariant.SCALENE;
        }
        else if (isoscelesTriangle()) {
            variant = TriangleVariant.ISOSCELES;
        }
    }

    public boolean illegalTriangle() {
        return len1 <= 0 || len2 <= 0 || len3 <= 0;
    }

    public boolean impossibleTriangle() {
        if ((len1 + len2 < len3) || (len1 + len3 < len2) || (len2 + len3 < len1)) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean flatTriangle() {
        if (getLongestSide() == len1) {
            if (len1 == len2 + len3) {
                return true;
            }
        }
        else if (getLongestSide() == len2) {
            if (len2 == len1 + len3) {
                return true;
            }
        }
        else {
            if (len3 == len1 + len2) {
                return true;
            }
        }
        return false;
    }

    public boolean equilateralTriangle() {
        if (len1 == len2 && len2 == len3) {
            return true;
        }
        else {
            return false;
        }
    }

 /*   public boolean rightTriangle() {
        double longestSide = getLongestSide();
        double side1, side2;
        longestSide = pow(longestSide, 2);
        if (getLongestSide() == len1) {
            side1 = pow(len2, 2);
            side2 = pow(len3, 2);
        }
        else if (getLongestSide() == len2) {
            side1 = pow(len1, 2);
            side2 = pow(len3, 2);
        }
        else {
            side1 = pow(len1, 2);
            side2 = pow(len2, 2);
        }
        if (longestSide == side1 + side2) {
            return true;
        }
        else {
            return false;
        }
    }*/

    public boolean rightTriangle() {
        BigDecimal longestSide = new BigDecimal(getLongestSide());
        BigDecimal side1 = new BigDecimal(len1);
        BigDecimal side2 = new BigDecimal(len2);
        BigDecimal side3 = new BigDecimal(len3);
        longestSide = longestSide.pow(2);
        if (getLongestSide() == len1) {
            side2 = side2.pow(2);
            side3 = side3.pow(2);
            if (longestSide.equals(side2.add(side3))) {
                return true;
            }
            else {
                return false;
            }
        }
        else if (getLongestSide() == len2) {
            side1 = side1.pow(2);
            side3 = side3.pow(2);
            if (longestSide.equals(side1.add(side3))) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            side1 = side1.pow(2);
            side2 = side2.pow(2);
            if (longestSide.equals(side1.add(side2))) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public boolean scaleneTriangle() {
        if (len1 != len2 && len1 != len3 && len2 != len3) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isoscelesTriangle() {
        if (len1 == len2 || len1 == len3 || len2 == len3) {
            return true;
        }
        else {
            return  false;
        }
    }

    public double calculateArea() {
        double s = (double) calculatePerimeterLength()/2;
        double area = sqrt(s*(s-len1)*(s-len2)*(s-len3));
        return area;
    }

    public double calculatePerimeterLength() {
        double perimeter = len1 + len2 + len3;
        return perimeter;
    }

    public String toString() {
        return "This is a Triangle with sides of length: " + len1 + ", " + len2 + ", "  + len3;
    }
}
