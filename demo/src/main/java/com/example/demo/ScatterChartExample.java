package com.example.demo;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ScatterChartExample extends Application {
    public static int gcd(int a, int b) {
        if (b==0) return a;
        return gcd(b,a%b);
    }

    private static int[] linearCongr(int x0, int a, int c, int m, int n) throws Exception {
        if(n <= 0)
            throw new Exception("Can't have negative amount of values!!");
        int[] res = new int[n];
        res[0] = (a * x0 + c) % m;
        for(int i = 1; i < n; i++) {
            res[i] = (res[i - 1] * a + c) % m;
        }
        return res;
    }

    public static int findA(int m) {
        int a = 2; // начальное значение a

        ArrayList<Integer> primeFactors = findPrimeFactors(m);

        // Увеличиваем значение a до тех пор, пока a-1 не будет делиться на все простые множители из m
        while (!checkDivisibility(a - 1, primeFactors)) {
            a++;
        }

        return a;
    }

    public static int findC(int m, int a) {
        int c = 1; // начальное значение c

        // Если m делится на 4, то увеличиваем значение c до тех пор, пока a-1 не будет делиться на 4
        if (m % 4 == 0) {
            while ((a - 1) % 4 != 0 && gcd(m, c) != 1) {
                c++;
            }
        }
        else {
            while(gcd(m, c) != 1)
                c++;
        }

        return c;
    }

    public static ArrayList<Integer> findPrimeFactors(int number) {
        ArrayList<Integer> primeFactors = new ArrayList<Integer>();

        for (int i = 2; i <= number; i++) {
            while (number % i == 0) {
                primeFactors.add(i);
                number /= i;
            }
        }

        return primeFactors;
    }

    public static boolean checkDivisibility(int number, ArrayList<Integer> factors) {
        for (int factor : factors) {
            if (number % factor != 0) {
                return false;
            }
        }

        return true;
    }

    private static int[] buildLinCongrParams(int m) {
        int a = 0, c = 0, x0 = 0;
        a = findA(m);
        c = findC(m, a);
        x0 = Math.abs(new Random().nextInt()) % m;
        return new int[] {a, c, x0};
    }

    private static BigInteger[] buildLinCongrParams(BigInteger m) {
        BigInteger a, c, x0;
        a = findA(m);
        c = findC(m, a);
        x0 = BigInteger.probablePrime(10, new Random());
        return new BigInteger[] {a, c, x0};
    }

    public static BigInteger findA(BigInteger m) {
        BigInteger a = BigInteger.TWO; // начальное значение a

        ArrayList<BigInteger> primeFactors = findPrimeFactors(m);

        // Увеличиваем значение a до тех пор, пока a-1 не будет делиться на все простые множители из m
        while (!checkDivisibility(a.subtract(BigInteger.ONE), primeFactors)) {
            a = a.add(BigInteger.ONE);
        }

        return a;
    }

    public static ArrayList<BigInteger> findPrimeFactors(BigInteger number) {
        ArrayList<BigInteger> primeFactors = new ArrayList<BigInteger>();

        for (BigInteger i = BigInteger.TWO; i.subtract(number).signum() > -1; i.add(BigInteger.ONE)) {
            while (!number.mod(i).equals(BigInteger.ZERO)) {
                primeFactors.add(i);
                number = number.divide(i);
            }
        }

        return primeFactors;
    }

    public static boolean checkDivisibility(BigInteger number, ArrayList<BigInteger> factors) {
        for (BigInteger factor : factors) {
            if (!number.mod(factor).equals(BigInteger.ZERO)) {
                return false;
            }
        }

        return true;
    }

    public static BigInteger findC(BigInteger m, BigInteger a) {
        BigInteger c = BigInteger.ONE; // начальное значение c

        // Если m делится на 4, то увеличиваем значение c до тех пор, пока a-1 не будет делиться на 4
        if (!m.mod(BigInteger.TWO.pow(2)).equals(BigInteger.ZERO)) {
            while (!(a.subtract(BigInteger.ONE)).mod(BigInteger.TWO.pow(2)).equals(BigInteger.ZERO) && !m.gcd(c).equals(BigInteger.ONE)) {
                c = c.add(BigInteger.ONE);
            }
        }

        return c;
    }




    @Override
    public void start(Stage stage) throws Exception {
        int x0 = 2;
        int a = 2;
        int c = 2;
        int m = 167;
        System.out.println("m = " + m + "\n a = " + a + "\n c = " + c +
                "\n x0 = " +x0);
        int n = 100;
        System.out.println("amount of elements to be generated:" + n);
        int[] res = linearCongr(x0, a, c, m, n);
        for(int i = 0; i < n; i++) {
            System.out.println(res[i]);
        }

        // 2

        m = (int) (Math.pow(2, 10) + 5);
        int[] params = buildLinCongrParams(m);
        a = params[0];
        c = params[1];
        x0 = params[2];

        System.out.println("m = " + m + "\n a = " + a + "\n c = " + c +
                "\n x0 = " +x0);

        res = linearCongr(x0, a, c, m, n);
        for(int i = 0; i < n; i++) {
            System.out.println(res[i]);
        }




        //Defining the axes
        NumberAxis xAxis = new NumberAxis(0, m, 4);
        xAxis.setLabel("Area");

        NumberAxis yAxis = new NumberAxis(0, m, 4);
        yAxis.setLabel("Weight");

        //Creating the Scatter chart
        ScatterChart<String, Number> scatterChart =
                new ScatterChart(xAxis, yAxis);

        //Prepare XYChart.Series objects by setting data
        XYChart.Series series = new XYChart.Series();
        for(int i = 1; i < n; i++) {
            series.getData().add(new XYChart.Data(res[i-1], res[i]));
        }

        //Setting the data to scatter chart
        scatterChart.getData().addAll(series);

        //Creating a Group object
        Group root = new Group(scatterChart);

        //Creating a scene object
        Scene scene = new Scene(root, 600, 400);

        //Setting title to the Stage
        stage.setTitle("Scatter Chart");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }
    public static void main(String args[]) throws Exception {

        launch(args);
    }
}