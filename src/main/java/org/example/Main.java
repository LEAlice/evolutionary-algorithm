package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        // 8皇后问题
        int n = 8;
        // 种群大小
        int size = 10;
        // 迭代次数
        int iterations = 1000;
        // 变异率
        double rate = 0.2;

        NQueensTool tool = new NQueensTool();
        TreeSet<NQueen> population = tool.init(n, size);

        int fitness = 0;
        if (population.first() != null) {
            fitness = population.first().getFitness();
        }
        int maxFitness = tool.maxFitness(n);

        for (int i = 0; i < iterations && fitness < maxFitness; i++) {
            List<NQueen> list = tool.selectParents(population, 5);
            List<NQueen> parents = tool.selectTwoParents(list);
            List<NQueen> child = tool.crossover(parents.get(0), parents.get(1));
            List<NQueen> mutatedChild = new ArrayList<>();
            for (NQueen nQueen : child) {
                mutatedChild.add(tool.mutation(nQueen, rate));
            }
            // 更新种群
            population.add(mutatedChild.get(0));
            population.add(mutatedChild.get(1));
            population.pollLast();
            population.pollLast();
            System.out.println("echo " + i);
        }

        System.out.println("code = " + population.first().getCode());
        System.out.println("fitness = " + population.first().getFitness());
    }

}