package lab1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static double calcDouble(double a, double b) {
        return a + (a + b - 1) / (a * a + 2) - Math.pow(a * b, 3);
    }

    static double calcIntToDouble(int a, int b) {
        double x = (double) a;
        double y = (double) b;
        return x + (x + y - 1) / (x * x + 2) - Math.pow(x * y, 3);
    }

    static long calcDoubleToLong(double a, double b) {
        return Math.round(calcDouble(a, b));
    }

    static void task1(Scanner in) {
        System.out.println("Варіант 1: дійсні -> дійсний");
        System.out.print("a = ");
        double a1 = in.nextDouble();
        System.out.print("b = ");
        double b1 = in.nextDouble();
        System.out.println("Результат: " + calcDouble(a1, b1));

        System.out.println("Варіант 2: цілі -> дійсний");
        System.out.print("a = ");
        int a2 = in.nextInt();
        System.out.print("b = ");
        int b2 = in.nextInt();
        System.out.println("Результат: " + calcIntToDouble(a2, b2));

        System.out.println("Варіант 3: дійсні -> цілий");
        System.out.print("a = ");
        double a3 = in.nextDouble();
        System.out.print("b = ");
        double b3 = in.nextDouble();
        System.out.println("Результат: " + calcDoubleToLong(a3, b3));
    }


    static double[] inputArray(Scanner in) {
        int n;
        do {
            System.out.print("n (1..200) = ");
            n = in.nextInt();
        } while (n < 1 || n > 200);

        double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            System.out.print("X[" + i + "] = ");
            x[i] = in.nextDouble();
        }
        return x;
    }

    static void processArray(double[] x, double a, double b) {
        double sum = 0;
        double product = 1;
        boolean hasLess = false;
        boolean hasGreater = false;
        boolean hasInside = false;
        double max = 0;
        double min = 0;

        for (double v : x) {
            if (v < a) {
                sum += v;
                hasLess = true;
            } else if (v > b) {
                product *= v;
                hasGreater = true;
            } else {
                if (!hasInside) {
                    max = v;
                    min = v;
                    hasInside = true;
                } else {
                    max = Math.max(max, v);
                    min = Math.min(min, v);
                }
            }
        }

        System.out.println(hasLess ? "Сума X(i) < a: " + sum
                                   : "Елементів, менших за a, немає");
        System.out.println(hasGreater ? "Добуток X(i) > b: " + product
                                      : "Елементів, більших за b, немає");
        if (hasInside) {
            System.out.println("max на [a, b]: " + max);
            System.out.println("min на [a, b]: " + min);
        } else {
            System.out.println("Елементів з відрізка [a, b] немає");
        }
    }

    static void task2(Scanner in) {
        double a;
        double b;
        do {
            System.out.println("Введіть a та b (a < b)");
            System.out.print("a = ");
            a = in.nextDouble();
            System.out.print("b = ");
            b = in.nextDouble();
        } while (a >= b);

        double[] x = inputArray(in);
        System.out.println("Масив: " + Arrays.toString(x));
        processArray(x, a, b);
    }

    static double[][] inputMatrix(Scanner in, String name, int n) {
        System.out.println("Матриця " + name);
        double[][] m = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(name + "[" + i + "][" + j + "] = ");
                m[i][j] = in.nextDouble();
            }
        }
        return m;
    }

    static void printMatrix(String title, double[][] m) {
        System.out.println(title);
        for (double[] row : m) {
            for (double v : row) {
                System.out.print(v + "\t");
            }
            System.out.println();
        }
    }

    static double rowMax(double[] row) {
        double max = row[0];
        for (double v : row) {
            max = Math.max(max, v);
        }
        return max;
    }

    static double[][] build(double[][] a, double[][] b) {
        int n = a.length;
        double[][] x = new double[n][n];
        for (int i = 0; i < n; i++) {
            double max = rowMax(b[i]);
            for (int j = 0; j < n; j++) {
                x[i][j] = a[i][j] * max;
            }
        }
        return x;
    }

    static void task3(Scanner in) {
        int n;
        do {
            System.out.print("n (1..15) = ");
            n = in.nextInt();
        } while (n < 1 || n > 15);

        double[][] a = inputMatrix(in, "A", n);
        double[][] b = inputMatrix(in, "B", n);

        printMatrix("Матриця A:", a);
        printMatrix("Матриця B:", b);
        printMatrix("Матриця X:", build(a, b));
    }

    static final Pattern WORD = Pattern.compile("\\p{L}+(?:['’ʼ-]\\p{L}+)*");

    static boolean hasDoubleLetter(String word) {
        String s = word.toLowerCase();
        for (int i = 1; i < s.length(); i++) {
            if (Character.isLetter(s.charAt(i)) && s.charAt(i) == s.charAt(i - 1)) {
                return true;
            }
        }
        return false;
    }

    static String removeWords(String text, List<String> removed) {
        Matcher m = WORD.matcher(text);
        StringBuilder rest = new StringBuilder();
        while (m.find()) {
            String word = m.group();
            if (hasDoubleLetter(word)) {
                removed.add(word);
                m.appendReplacement(rest, "");
            } else {
                m.appendReplacement(rest, Matcher.quoteReplacement(word));
            }
        }
        m.appendTail(rest);

        return rest.toString()
                .replaceAll("\\s+", " ")
                .replaceAll(" (?=[,.;:!?])", "")
                .trim();
    }

    static void task4(Scanner in) {
        System.out.println("Введіть текст:");
        String text = in.nextLine();

        List<String> removed = new ArrayList<>();
        String rest = removeWords(text, removed);

        System.out.println("Вилучені слова: " + String.join(" ", removed));
        System.out.println("Текст, що залишився: " + rest);
    }



    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        in.useLocale(Locale.US); 

        int choice;
        do {
            System.out.print("\nНомер завдання (1-4), 0 - вихід: ");
            choice = in.nextInt();
            in.nextLine(); 

            switch (choice) {
                case 1 -> task1(in);
                case 2 -> task2(in);
                case 3 -> task3(in);
                case 4 -> task4(in);
                case 0 -> System.out.println("Роботу завершено");
                default -> System.out.println("Немає такого завдання");
            }
        } while (choice != 0);

        in.close();
    }
}
