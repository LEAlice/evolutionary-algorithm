package org.example.NQueen;

import java.util.*;

/**
 * @author lzy
 */
public class NQueensTool {

    private int count;

    /**
     *
     * @param n n皇后问题
     * @param size 种群大小
     * @param iterations 迭代次数
     * @param rate 变异率
     * @return 经过进化后得到的最佳个体
     */
    public NQueen evolution(int n, int size, int iterations, double rate) {
        TreeSet<NQueen> population = this.init(n, size);
        int bestFitness = 0;
        if (population.first() != null) {
            bestFitness = population.first().getFitness();
        }
        int maxFitness = this.maxFitness(n);

        for (int i = 0; !(i >= iterations || bestFitness >= maxFitness); i++) {
            List<NQueen> list = this.selectParents(population, 5);
            List<NQueen> parents = this.selectTwoParents(list);
            List<NQueen> child = this.crossover(parents.get(0), parents.get(1));
            List<NQueen> mutatedChild = new ArrayList<>();
            for (NQueen nQueen : child) {
                mutatedChild.add(this.mutation(nQueen, rate));
            }
            // 更新种群
            population.addAll(mutatedChild);
            // 淘汰适应度最低的两个个体
            population.pollLast();
            population.pollLast();
            bestFitness = population.first().getFitness();
            System.out.println("echo " + (i + 1) + ", bestFitness = " + bestFitness);
        }
        return population.first();
    }

    public TreeSet<NQueen> init(int n, int size) {
        count = 0;
        List<Integer> source = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            source.add(i);
        }
        TreeSet<NQueen> population = new TreeSet<>();
        for (int i = 0; i < size; i++) {
            NQueen nQueen = new NQueen();
            int[] gene = randomGene(source);
            nQueen.setGene(gene);
            nQueen.setCode(toString(gene));
            nQueen.setFitness(calculateFitness(gene));
            nQueen.setId(count++);
            population.add(nQueen);
        }
        return population;
    }

    private int[] randomGene(List<Integer> source) {
        Collections.shuffle(source);
        return source.stream().mapToInt(i -> i).toArray();
    }

    public int maxFitness(int n) {
        int maxFitness = 0;
        for (int i = 0; i < n; i++) {
            maxFitness += i;
        }
        return maxFitness;
    }

    public int calculateFitness(int[] gene) {
        int maxFitness = maxFitness(gene.length);
        int conflictNum = calculateConflict(gene);
        return maxFitness - conflictNum;
    }

    public String toString(int[] gene) {
        StringBuilder sb = new StringBuilder();
        for (int i : gene) {
            sb.append(i);
        }
        return sb.toString();
    }

    public int calculateConflict(int[] gene){
        int n = gene.length;
        int conflictNum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(gene[i] - gene[j]) == Math.abs(i - j)) {
                    conflictNum++;
                }
            }
        }
        return conflictNum;
    }

    public List<NQueen> selectTwoParents(List<NQueen> list) {
        Queue<NQueen> queue = new PriorityQueue<>(list.size());
        for (NQueen nQueen : list) {
            queue.offer(nQueen);
        }
        List<NQueen> parents = new ArrayList<>();
        parents.add(queue.poll());
        parents.add(queue.poll());
        return parents;
    }

    /**
     * 从种群中随机选择指定数量的个体
     *
     * @param population 种群
     * @param num 选择的个体数
     * @return 被选择的个体数
     */
    public List<NQueen> selectParents(TreeSet<NQueen> population, int num) {
        List<NQueen> list = new ArrayList<>();
        while (list.size() < num) {
            for (NQueen nqueen: population) {
                if (Math.random() < 0.1) {
                    list.add(nqueen);
                    if (list.size() == num) {
                        break;
                    }
                }
            }
        }
        return list;
    }

    /**
     * 变异操作，具体实现为生成一个随机数，若在变异几率内，则随机交换基因上两个位置的值
     *
     * @param nQueen 个体
     * @param rate 变异几率
     * @return 经过一定几率变异后的基因
     */
    public NQueen mutation(NQueen nQueen, double rate) {
        int[] gene = nQueen.getGene();
        int length = gene.length;
        double r = Math.random();
        if (r <= rate) {
            int mutationPos = randomPos(length);
            int temp = gene[mutationPos];
            gene[mutationPos] = gene[length - mutationPos - 1];
            gene[length - mutationPos - 1] = temp;
        }
        return geneToNQueen(gene);
    }

    /**
     * 基因交叉操作，产生两个子代
     *
     * @param parent1 父代1
     * @param parent2 父代2
     * @return 两个子代
     */
    public List<NQueen> crossover(NQueen parent1, NQueen parent2) {
        int[] gene1 = parent1.getGene();
        int[] gene2 = parent2.getGene();
        int length = gene1.length;
        int[][] child = new int[2][length];
        List<Integer> child1 = new ArrayList<>();
        List<Integer> child2 = new ArrayList<>();
        int pos = randomPos(length) - 1;
        if (pos < 1) {
            pos = 1;
        }
//        System.out.println("pos = " + pos);
        for (int i = 0; i < pos; i++) {
            child1.add(gene2[i]);
            child2.add(gene1[i]);
        }
        for (int i = 0; i < length; i++) {
            if (!child1.contains(gene1[i])) {
                child1.add(gene1[i]);
            }
            if (!child2.contains(gene2[i])) {
                child2.add(gene2[i]);
            }
        }
        child[0] = child1.stream().mapToInt(i -> i).toArray();
        child[1] = child2.stream().mapToInt(i -> i).toArray();
        List<NQueen> res = new ArrayList<>();
        res.add(geneToNQueen(child[0]));
        res.add(geneToNQueen(child[1]));
        return res;
    }

    /**
     * 获取基因随机位置
     *
     * @param length 基因长度
     * @return 基因的一个随机位置
     */
    private int randomPos(int length) {
        return (int) (Math.random() * length);
    }

    private NQueen geneToNQueen(int[] gene) {
        NQueen nQueen = new NQueen();
        nQueen.setGene(gene);
        nQueen.setCode(toString(gene));
        nQueen.setFitness(calculateFitness(gene));
        nQueen.setId(count++);
        return nQueen;
    }
}
