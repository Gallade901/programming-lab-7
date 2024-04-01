package org.example.server;

import org.example.utility.Console;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


import static org.example.server.utility.CollectionManager.saveCollection;

public class ScannerMonitor {
//    private static final int INTERVAL = 2000; // Интервал проверки в миллисекундах
//    private static final String TARGET_STRING = "save";
//    public static boolean ans = true;
//
//
//    public static boolean check() {
//        Timer timer = new Timer();
//        Scanner scanner = new Scanner(System.in);
//
//
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    String line = scanner.nextLine();
//                    if (line.equals(TARGET_STRING)) {
//                        saveCollection();
//                    } else if (line.equals("exit")) {
//                        timer.cancel();
//                        ans = false;
//                    }
//                } catch (NoSuchElementException e) {
//                    Console.println("Завершение работы сервера");
//                    System.exit(0);
//                }
//            }
//        };
//        timer.scheduleAtFixedRate(task, 10, INTERVAL);
//        return ans;
//    }
    public static boolean check() {
        boolean ans = true;
        Scanner scanner = new Scanner(System.in);
        if (scanner.nextLine().equals("exit")) {
            ans = false;
            System.exit(0);
        }
        return ans;
    }

}