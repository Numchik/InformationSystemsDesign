package org.urfu;

import java.util.Scanner;

public class Task1 {

    public static void run(Scanner sc) {
        System.out.println("\n--- Задание 1. Потенциальное число ошибок ПО БКС ---");
        // Параметры ТЗ (по умолчанию - вариант 2)
        int targets = Input.readInt(sc, "Число одновременно сопровождаемых целей", 25);
        int measures = Input.readInt(sc, "Количество измерений каждого отслеживаемого параметра", 28);
        int tracked = Input.readInt(sc, "Количество отслеживаемых параметров", 8);
        int calculated = Input.readInt(sc, "Количество рассчитываемых параметров по каждой цели", 3);
        double lambda = Input.readDouble(sc, "Уровень языка программирования lambda", 1.6);

        // n2* = число независимых входных и выходных параметров
        int inputParams = targets * tracked * measures;  // входные операнды
        int outputParams = targets * calculated;          // выходные операнды
        int n2 = inputParams + outputParams;

        // Потенциальный объем программы: V* = (n2* + 2) * log2(n2* + 2)
        double vStar = (n2 + 2) * log2(n2 + 2);

        // Потенциальное число ошибок: B = (V*)^2 / (3000 * lambda)
        double B = vStar * vStar / (3000 * lambda);

        System.out.println("\nРезультаты задания 1:");
        System.out.printf("Входные параметры:  %d x %d x %d = %d%n", targets, tracked, measures, inputParams);
        System.out.printf("Выходные параметры: %d x %d = %d%n", targets, calculated, outputParams);
        System.out.printf("n2* = %d%n", n2);
        System.out.printf("Потенциальный объем V* = %.2f бит%n", vStar);
        System.out.printf("Потенциальное число ошибок B = %.4f%n", B);
    }

    static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }
}
