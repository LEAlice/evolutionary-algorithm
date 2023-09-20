package org.example.NQueen;

import lombok.Data;

/**
 * @author lzy
 */
@Data
public class NQueen implements Comparable<NQueen> {

    private int[] gene;

    private String code;

    private int fitness;

    private int id;

    @Override
    public int compareTo(NQueen other) {
        int dif = other.fitness - this.fitness;
        if (dif != 0) {
            return dif;
        }
        return other.id - this.id;
    }
}
