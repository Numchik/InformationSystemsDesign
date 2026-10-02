package org.urfu;

import java.util.Scanner;

public class Task3 {

    /**
     * Коэффициент c(lambda, R) - обратно пропорционален параметрам.
     * variant: 1 -> 1/(λ+R),  2 -> 1/(λ·R),  3 -> 1/λ + 1/R
     */
    static double c(double lambda, double R, int variant) {
        switch (variant) {
            case 1:
                return 1.0 / (lambda + R);
            case 2:
                return 1.0 / (lambda * R);
            default:
                return 1.0 / lambda + 1.0 / R;
        }
    }

    public static void run(Scanner sc) {
        System.out.println("\n--- Задание 3. Рейтинг программиста и ожидаемое число ошибок ---");
        double R0 = Input.readDouble(sc, "Начальный рейтинг R0", 2000);
        double lambda = Input.readDouble(sc, "Уровень языка lambda", 1.6);
        int n = Input.readInt(sc, "Количество написанных программ за период", 3);

        double[] V = new double[n];
        double[] B = new double[n];
        // Значения по умолчанию для варианта 2
        double[] defV = {4, 8, 10};
        double[] defB = {1, 2, 4};
        System.out.println("Введите объем (Кбайт) и число ошибок каждой программы:");
        for (int j = 0; j < n; j++) {
            double dv = j < defV.length ? defV[j] : 1;
            double db = j < defB.length ? defB[j] : 0;
            V[j] = Input.readDouble(sc, "  Программа " + (j + 1) + ": объем V", dv);
            B[j] = Input.readDouble(sc, "  Программа " + (j + 1) + ": ошибки B", db);
        }
        double Vnext = Input.readDouble(sc, "Объем следующей программы (Кбайт)", 14);

        double sumV = 0;
        for (double v : V) sumV += v;

        String[] names = {"c = 1/(λ+R)", "c = 1/(λ·R)", "c = 1/λ + 1/R"};
        System.out.println("\nРезультаты задания 3 (три варианта коэффициента):");
        for (int variant = 1; variant <= 3; variant++) {
            double c0 = c(lambda, R0, variant);

            // Штраф за ошибки: сумма Bk / c(λ, R) по программам, где найдены ошибки.
            // ВНИМАНИЕ: формула приведена в методичке как ДРОБЬ Bk/c.
            // Если преподаватель требует умножение - замените "B[k]/c0" на "B[k]*c0" ниже.
            double penalty = 0;
            for (int k = 0; k < n; k++) {
                if (B[k] > 0) penalty += B[k] / c0;
            }

            // Рейтинг: R1 = R0 * (1 + 10^-3 * (ΣV - Σ Bk/c))
            double R1 = R0 * (1 + 1e-3 * (sumV - penalty));

            // Ожидаемое число ошибок следующей программы: B = c(λ, R1) * V
            double Bnext = c(lambda, R1, variant) * Vnext;

            System.out.printf("%nВариант коэффициента %s:%n", names[variant - 1]);
            System.out.printf("  c(λ, R0) = %.6f, штраф Σ Bk/c = %.4f%n", c0, penalty);
            System.out.printf("  Текущий рейтинг R1 = %.2f%n", R1);
            System.out.printf("  Ожидаемое число ошибок в программе %.0f Кбайт: B = %.5f%n", Vnext, Bnext);
        }
    }
}
