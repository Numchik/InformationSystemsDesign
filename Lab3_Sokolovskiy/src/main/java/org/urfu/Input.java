package org.urfu;

import java.util.Scanner;

/**
 * Вспомогательный класс: чтение чисел из консоли со значением по умолчанию.
 * Если пользователь просто нажал Enter — берётся значение варианта 2.
 */
public class Input {

    public static int readInt(Scanner sc, String prompt, int def) {
        System.out.printf("%s [%d]: ", prompt, def);
        String s = sc.nextLine().trim();
        return s.isEmpty() ? def : Integer.parseInt(s.replace(',', '.'));
    }

    public static double readDouble(Scanner sc, String prompt, double def) {
        System.out.printf("%s [%.2f]: ", prompt, def);
        String s = sc.nextLine().trim();
        return s.isEmpty() ? def : Double.parseDouble(s.replace(',', '.'));
    }
}
