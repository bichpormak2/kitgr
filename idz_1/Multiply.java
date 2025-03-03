package com.bichpormak.idz_1;

public interface Multiply {

    int[][] multiply(int[][] firstMatrix, int[][] secondMatrix);

    default void printMatrix(int[][] matrix) {

        System.out.println("=====================================");

        if (this instanceof MultiplyStandard) {
            System.out.println("\tСтандартное умножение");
        } else if (this instanceof MultiplyBoolean) {
            System.out.println("\tЛогическое умножение");
        } else {
            System.out.println("\tТропическое умножение");
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {

                System.out.print(matrix[i][j] + " ");

            }

            System.out.println();

        }

    }

}
