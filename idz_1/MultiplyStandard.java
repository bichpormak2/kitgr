package com.bichpormak.idz_1;

public class MultiplyStandard implements Multiply {

    @Override
    public int[][] multiply(int[][] firstMatrix, int[][] secondMatrix) {

        final int[][] result = new int[firstMatrix.length][secondMatrix[0].length];

        for (int i = 0; i < firstMatrix.length; i++) {
            for (int j = 0; j < secondMatrix[0].length; j++) {

                result[i][j] = 0;

                for (int k = 0; k < firstMatrix[0].length; k++) {

                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];

                }
            }
        }

        return result;

    }

}