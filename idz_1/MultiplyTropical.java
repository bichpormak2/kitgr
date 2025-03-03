package com.bichpormak.idz_1;

import java.util.function.BiFunction;

public class MultiplyTropical implements Multiply {
    private final BiFunction<Integer, Integer, Integer> func;
    private final int initialValue;

    public MultiplyTropical(BiFunction<Integer, Integer, Integer> func) {
        this.func = func;
        this.initialValue = (func.apply(0, 1) == 1) ?
                Integer.MIN_VALUE : Integer.MAX_VALUE;
    }

    @Override
    public int[][] multiply(int[][] firstMatrix, int[][] secondMatrix) {
        int[][] result = new int[firstMatrix.length][secondMatrix[0].length];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = initialValue;

                for (int k = 0; k < firstMatrix[0].length; k++) {
                    int sum = firstMatrix[i][k] + secondMatrix[k][j];
                    result[i][j] = func.apply(result[i][j], sum);
                }
            }
        }
        return result;
    }
}
