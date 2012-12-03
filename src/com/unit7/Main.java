package com.unit7;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * File: Main
 * User: unit7
 * Date: 02.12.12
 * Time: 18:44
 */
public class Main {

    /**
     * Здесь создается основное окно приложения.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Monte-Carlo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(dimension.width >> 2, dimension.height >> 2, dimension.width >> 1, dimension.height >> 1);

        frame.setLayout(null);

        JLabel label = new JLabel();

        label.setBounds(0, 0, frame.getWidth(), frame.getHeight());

        label.setVerticalAlignment(SwingConstants.TOP);
        label.setFont(Font.getFont(Font.MONOSPACED));

        frame.getContentPane().add(label);

        frame.setResizable(false);
        frame.setVisible(true);

        label.setText(monteCarlo());
    }

    /**
     * Рассчеты методом Монте-Карло
     */
    private static String monteCarlo() {

        // интенсивности подключений
        int[][] requests = {
                { 18, 18, 16, 11, 8, 11, 13, 8, 17, 11, 14, 8, 11, 13 },
                { 12, 15, 17, 20, 11, 12, 15, 11, 12, 19, 17, 17, 11, 15 },
                { 17, 13, 11, 5, 11, 16, 12, 18, 11, 19, 17, 22, 14, 15 },
                { 14, 15, 12, 16, 18, 15, 11, 9, 22, 16, 10, 6, 8, 18 },
                { 19, 20, 16, 20, 11, 17, 19, 13, 23, 10, 11, 22, 17, 20 }
        };

        // времена работы абонентов
        double[][] times = {
                { 0.52, 0.08, 0.07, 0.08, 1.0, 0.13, 0.4, 0.67, 1.02, 0.17 },
                { 0.85, 0.09, 0.41, 0.22, 0.06, 0.05, 0.27, 0.46, 0.06, 1.14 },
                { 1.64, 0.38, 0.27, 0.08, 0.24, 0.63, 0.58, 0.06, 0.02, 0.38 },
                { 0.06, 0.11, 0.27, 0.54, 0.58, 0.37, 0.11, 1.94, 0.42, 0.63 },
                { 0.37, 0.35, 1.3, 0.06, 0.55, 0.62, 0.99, 0.23, 0.06, 1.16 }
        };

        // сами абоненты
        Subscriber[] subscribers = new Subscriber[5];
        for(int i = 0; i < subscribers.length; ++i) {
            subscribers[i] = new Subscriber(requests[i], times[i], i);
            subscribers[i].nextTime(0);
        }

        // счетчик текущего времени
        double currentTime = 0.0;

        // время наблюдения
        double observeTime = 100.0;

        // кол-во запросов
        int request = 0;

        // кол-во отказов
        int fails = 0;

        // терминал с тремя каналами
        Terminal terminal = new Terminal(3);

        // выполняем в течение указанного времени
        while (currentTime < observeTime) {

            // пробуем поставить на обработку абонента
            for(int i = 0; i < subscribers.length; ++i) {

                if(!terminal.isInQueue(subscribers[i]) && subscribers[i].getTimeIn() <= currentTime) {

                    // если терминал занят, увеличиваем счетчик отказов
                    if(!terminal.push(subscribers[i])) {
                        subscribers[i].nextTime(currentTime);
                        fails += 1;
                    }

                    request += 1;
                }
            }

            // отключаем пользователей, закончивших работу и генерируем новое время
            for(int i = 0; i < subscribers.length; ++i) {

                if(subscribers[i].getTimeOut() <= currentTime) {
                    terminal.pop(subscribers[i]);
                    subscribers[i].nextTime(currentTime);
                }
            }

            currentTime += 0.01;
        }

        StringBuilder result = new StringBuilder("<html>Average intensity of requests to the terminal:<br>");

        for(int i = 0; i < subscribers.length; ++i)
            result.append(String.format("%.3f ", subscribers[i].getRequestTime()));

        result.append("<br><br>Average processing time:<br>");
        for(int i = 0; i < subscribers.length; ++i)
            result.append(String.format("%.3f ", subscribers[i].getWorkTime()));

        result.append("<br><br>Total number of requests: " + request);
        result.append("<br>Total number of denials: " + fails);
        result.append("<br>Total number of successful requests processed: " + (request - fails));
        result.append("<br>The probability of denial for three channels: " +
                String.format("%.3f", (double) fails / request));

        double lambda = 0;
        double muy = 0;

        for(int i = 0; i < subscribers.length; ++i) {
            lambda += subscribers[i].getRequestTime();
            muy += subscribers[i].getWorkTime();
        }

        lambda /= subscribers.length;
        muy = subscribers.length / muy;

        result.append("<br><br>Analytical method: " + String.format("%.3f", analytical(lambda, muy, 3)) + "</html>");

        return result.toString();
    }

    /**
     * вычисление аналитическим методом
     */
    private static double analytical(double lambda, double muy, int count) {
        double p = Math.pow(lambda / muy, count) / factorial(count);
        double d = 0;

        for(int i = 0; i <= count; ++i)
            d += Math.pow(lambda / muy, i) / factorial(i);

        return p / d;
    }

    private static long factorial(int k) {
        long ans = 1;
        for(int i = 1; i <= k; ++i)
            ans *= i;

        return ans;
    }
}