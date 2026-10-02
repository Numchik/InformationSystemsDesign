package org.urfu;

import java.util.Scanner;

public class Task2 {

    public static void run(Scanner sc) {
        System.out.println("\n--- Задание 2. Структурные параметры, объем, время, надежность ---");
        int targets = Input.readInt(sc, "Число одновременно сопровождаемых целей", 25);
        int measures = Input.readInt(sc, "Количество измерений каждого отслеживаемого параметра", 28);
        int tracked = Input.readInt(sc, "Количество отслеживаемых параметров", 8);
        int calculated = Input.readInt(sc, "Количество рассчитываемых параметров по каждой цели", 3);
        // m и nu задаются самостоятельно (nu - в пределах 10..30)
        int m = Input.readInt(sc, "Количество программистов в бригаде m", 10);
        int nu = Input.readInt(sc, "Производительность nu (отлаженных команд в день, 10..30)", 20);
        int workDayHours = Input.readInt(sc, "Длительность рабочего дня, часов", 8);

        double n2 = targets * tracked * measures + targets * calculated;

        // а) Структурные параметры
        double k = n2 / 8;                       // число модулей
        double K, levels = 1;
        if (k > 8) {                             // многоуровневая (иерархическая) структура
            levels = Task1.log2(n2) / 3 + 1;     // число уровней иерархии
            K = n2 / 8 + n2 / 64;                // число модулей с учетом иерархии
        } else {
            K = k;
        }

        // б) Длина программы
        double N = 220 * K + K * Task1.log2(K);
        // в) Объем программного обеспечения
        double V = K * 220 * Task1.log2(48);
        // г) Количество команд ассемблера (коэффициент пересчета Кнута 3/8)
        double P = 3 * N / 8;
        // д) Календарное время программирования, дней
        double TkDays = 3 * N / (8.0 * m * nu);
        // е) Потенциальное количество ошибок
        double B = V / 3000;
        // ж) Начальная надежность ПО (наработка на отказ), Tk переводим в часы
        double tN = TkDays * workDayHours / (2 * Math.log(B));

        System.out.println("\nРезультаты задания 2:");
        System.out.printf("n2* = %.0f%n", n2);
        System.out.printf("Число модулей k = n2*/8 = %.2f%n", k);
        if (k > 8) {
            System.out.printf("k >> 8, структура иерархическая: уровней i = %.2f (округл. %d)%n", levels, (int) Math.round(levels));
            System.out.printf("Число модулей с учетом иерархии K = %.2f%n", K);
        }
        System.out.printf("Длина программы N = %.2f%n", N);
        System.out.printf("Объем ПО V = %.2f бит%n", V);
        System.out.printf("Команд ассемблера P = %.2f%n", P);
        System.out.printf("Календарное время Tk = %.2f дней (%.2f часов)%n", TkDays, TkDays * workDayHours);
        System.out.printf("Потенциальное число ошибок B = %.2f%n", B);
        System.out.printf("Начальная надежность tн = %.2f часов%n", tN);
    }
}
