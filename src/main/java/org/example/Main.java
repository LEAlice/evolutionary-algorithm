package org.example;

import org.example.NQueen.NQueen;
import org.example.NQueen.NQueensTool;

public class Main {
    public static void main(String[] args) {

        NQueensTool tool = new NQueensTool();
        NQueen nQueen = tool.evolution(8, 100, 1000, 0.3);

        System.out.println("code = " + nQueen.getCode());
        System.out.println("fitness = " + nQueen.getFitness());

    }

}