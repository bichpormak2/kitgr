package com.bichpormak.idz_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class Main {

    public static void main(String[] args) {

        List<Multiply> multiplies = new ArrayList<>(Arrays.asList(new MultiplyStandard(), new MultiplyBoolean(), new MultiplyTropical(Math::min)));

        int[][] firstMatrix = {
                {1, 2, 3},
                {1, 2, 3},
                {1, 2, 3}
        };

        int[][] secondMatrix = {
                {1, 2, 3},
                {1, 2, 3},
                {1, 2, 3}
        };

        for (Multiply multiply : multiplies) {
            multiply.printMatrix(multiply.multiply(firstMatrix, secondMatrix));
        }



    }

}
