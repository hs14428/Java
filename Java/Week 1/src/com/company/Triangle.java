package com.company;

public class Triangle extends TwoDimensionalShape
{
    int len1, len2, len3;

    public Triangle(int length1, int length2, int length3) {
        super(Colour.GREEN);
        len1 = length1;
        len2 = length2;
        len3 = length3;
    }

    public int getLongestSide() {
        int longest;
        longest = len1;
        if (len2 > longest) {
            longest = len2;
        }
        if (len3 > longest) {
            longest = len3;
        }
        return longest;
    }

    public double calculateArea() {
        return -1;
    }

    public int calculatePerimeterLength() {
        return -1;
    }

    public String toString() {
        return "This is a Triangle with sides of length: " + len1 + " " + len2 + " "  + len3;
    }
}
