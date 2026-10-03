import java.util.Arrays;

/**
 * Реализация модели Джелинского-Моранды для оценки надёжности ПО.
 */
public class JelinskiMorandaModel {

    private final double[] x;      // интервалы между ошибками
    private final int n;           // число обнаруженных ошибок

    private double B;              // оценка общего числа ошибок
    private double K;              // коэффициент пропорциональности
    private double xNext;          // среднее время до (n+1)-й ошибки
    private double tk;             // время до окончания тестирования
    private boolean valid = true;

    public JelinskiMorandaModel(double[] x) {
        if (x == null || x.length == 0) {
            throw new IllegalArgumentException("Массив интервалов не может быть пустым");
        }
        this.x = Arrays.copyOf(x, x.length);
        this.n = x.length;
    }
    /** Сумма всех X_i */
    private double sumX() {
        double s = 0;
        for (double v : x) s += v;
        return s;
    }
    /** Сумма i * X_i (i = 1..n) */
    private double sumIX() {
        double s = 0;
        for (int i = 0; i < n; i++) {
            s += (i + 1) * x[i];
        }
        return s;
    }
    /** Левая часть уравнения (3): сумма 1/(B - i + 1) */
    private double leftSide(double B) {
        double s = 0;
        for (int i = 1; i <= n; i++) {
            s += 1.0 / (B - i + 1);
        }
        return s;
    }
    /** Правая часть уравнения (3) */
    private double rightSide(double B) {
        double S1 = sumX();
        double S2 = sumIX();
        return (n * S1) / ((B + 1) * S1 - S2);
    }
    /** f(B) = left - right; ищем корень */
    private double f(double B) {
        return leftSide(B) - rightSide(B);
    }
    /**
     * Численное решение уравнения для B методом бисекции.
     * B должно быть > n (иначе знаменатели обращаются в 0).
     */
    public double solveB() {
        double lo = n + 1e-9;
        double hi = n + 1.0;
        double maxB = n + 1000.0;
        int iterations = 0;
        while (f(hi) > 0 && hi < maxB && iterations < 1000) {
            hi = Math.min(hi * 2.0, maxB);
            iterations++;
        }
        // Проверка: корень существует?
        if (f(hi) > 0) {
            throw new ArithmeticException(
                    "Корень B не найден: данные не удовлетворяют модели");
        }
        if (f(lo) < 0 && f(hi) < 0) {
            throw new ArithmeticException(
                    "f(B) не меняет знак: корень отсутствует, модель неприме-нима");
        }
        for (int i = 0; i < 500; i++) {
            double mid = 0.5 * (lo + hi);
            if (f(mid) > 0) lo = mid;
            else hi = mid;
            if (Math.abs(hi - lo) < 1e-12) break;
        }
        return 0.5 * (lo + hi);
    }
    /** Полный расчёт всех параметров */
    public void compute() {
        try {
            B = solveB();
        } catch (ArithmeticException e) {
            System.out.println("!!! " + e.getMessage());
            // помечаем результат как невалидный и выходим
            valid = false;
            B = Double.NaN;
            K = Double.NaN;
            xNext = Double.NaN;
            tk = Double.NaN;
            return;
        }
        double S1 = sumX();
        double S2 = sumIX();

        K = n / ((B + 1) * S1 - S2);

        if (B - n <= 0) {
            xNext = Double.POSITIVE_INFINITY;
            tk = Double.POSITIVE_INFINITY;
        } else {
            xNext = 1.0 / (K * (B - n));
            tk = (B - n) * (B - n + 1) / (2.0 * K);
        }
    }
    // ---- геттеры ----
    public double getB()     { return B; }
    public double getK()     { return K; }
    public double getXNext() { return xNext; }
    public double getTk()    { return tk; }
    public int getN()        { return n; }
    public boolean isValid() { return valid; }
    /** Красивый вывод результатов */
    public void printReport(String variantName) {
        System.out.println("=================================================");
        System.out.println("Вариант: " + variantName);
        System.out.println("Число обнаруженных ошибок n = " + n);
        if (!valid) {
            System.out.println("!!! Модель неприменима к данным.");
            System.out.println("    Корень B не найден: f(B) не меняет знак.");
            System.out.println("    Причина: интервалы X_i не убывают,");
            System.out.println("    что противоречит допущениям модели.");
            System.out.println("=================================================");
            return;
        }
        System.out.printf("Оценка общего числа ошибок B  = %.6f%n", B);
        System.out.printf("Коэффициент пропорциональности K = %.8f%n", K);
        System.out.printf("Среднее время до (n+1)-й ошибки X_(n+1) = %.2f ч%n", xNext);
        System.out.printf("Время до окончания тестирования t_k = %.2f ч%n", tk);
        System.out.println("=================================================");
    }
}
