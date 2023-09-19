package org.example;

import lombok.Data;

/**
 * @author lzy
 */
@Data
public class NQueen implements Comparable<NQueen> {

    private int[] gene;

    private String code;

    private int fitness;

    @Override
    public int compareTo(NQueen other) {
        int dif = other.fitness - this.fitness;
        if (dif != 0) {
            return dif;
        }
        return other.getCode().compareTo(this.getCode());
    }
}
