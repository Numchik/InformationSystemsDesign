package org.urfu;

import java.util.Scanner;

public class Main {
    static void main() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("=== Лабораторная работа №3. Метрики Холстеда (вариант 2) ===");
            System.out.println("1 - Задание 1: потенциальное число ошибок ПО БКС");
            System.out.println("2 - Задание 2: структура, объем, время, надежность ПО");
            System.out.println("3 - Задание 3: рейтинг программиста");
            System.out.println("0 - Выход");
            System.out.print("Выбор: ");
            switch (scanner.nextLine().trim()) {
                case "1":
                    Task1.run(scanner);
                    break;
                case "2":
                    Task2.run(scanner);
                    break;
                case "3":
                    Task3.run(scanner);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Нет такого пункта меню.");
            }
        }
    }
}
