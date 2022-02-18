package pl.coderslab;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;


public class TaskManager {
    public static void main(String[] args) {
        String[][] tasks = getDataFromFile();

        Scanner scan = new Scanner(System.in);
        boolean isWorking = true;
        while(isWorking) {
            printMenu();
            String input = scan.nextLine();
            switch (input) {
                case "add":
                    tasks = addTask(tasks);
                    System.out.println(tasks[3][1]);
                    break;

                case "list":
                    list(tasks);
                    break;
                case "remove":
                    tasks = removeTask(tasks);
                    break;
                case "exit":
                    safeToFile(tasks);
                    isWorking = false;
                    System.out.print(ConsoleColors.RED + "Bye, bye.");
                    break;
                default:
                    System.out.println("Wrong input!");
            }
        }

    }

    public static void printMenu() {
        System.out.println(ConsoleColors.BLUE + "Please select an option:");
        System.out.println(ConsoleColors.RESET + "add\nremove\nlist\nexit");
    }

    public static boolean isValidParametr(String parametr, int tabLength){
        if(StringUtils.isNumeric(parametr) &&  Integer.parseInt(parametr) < tabLength && Integer.parseInt(parametr) >= 0) return true;
        else return false;
    }

    public static String[][] getDataFromFile() {
        Path path = Paths.get("tasks.csv");
        File file = new File("tasks.csv");

        long linesInFileCounter = 0;
        try {
            linesInFileCounter = Files.lines(path).count();    // zlicza ilosc lini w pliku
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] linesInFile = new String[(int)linesInFileCounter];
        try {
            Scanner scan = new Scanner(file);
            for (int i = 0; i < linesInFile.length; i++) {       // zapisuje linie do jednowymiarowej tablicy Stringów
                linesInFile[i] = scan.nextLine();
                StringUtils.deleteWhitespace(linesInFile[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String[][] tasks = new String[(int) linesInFileCounter][3];
        for (int i = 0; i < linesInFile.length; i++) {                  // rozdziela linie na dwuwymiarową tablice z wartościami
            tasks[i] = linesInFile[i].split(",");
        }

        return tasks;

    }

    public static String[][] addTask(String[][] tasks) {
        Scanner scan = new Scanner(System.in);
        String [][] copy = new String[tasks.length+ 1][3];
        for (int i = 0; i < tasks.length; i++) {
            for (int j = 0; j < 3; j++) {
                copy[i][j] = tasks[i][j];
            }
        }
            System.out.println("Please add task description:");
            copy[copy.length -1][0] = scan.nextLine();
            System.out.println("Please add task due date:");
            copy[copy.length -1][1] = scan.nextLine();
            System.out.println("Is your task is important: true/false");
            copy[copy.length -1][2] = scan.nextLine();

        return copy;
    }


    public static void list(String[][] tasks) {
        int lp = 0;
        for (String[] task : tasks) {
            System.out.print(lp++ + " ");
            for (String s : task) {
                System.out.print(s + " ");
            }
            System.out.println();
        }
    }

    public static String[][] removeTask(String[][] tasks) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please select number to remove:");
        String toRemoveStr = scan.nextLine();
        while(!isValidParametr(toRemoveStr, tasks.length)){
            System.out.println("Incorrect argument passed. Please give number greater or equal 0");
            toRemoveStr = scan.nextLine();
        }
        int toRemove = Integer.parseInt(toRemoveStr);
        String[][] copy = Arrays.copyOf(tasks, tasks.length - 1);

        for (int i = 0; i < copy.length; i++) {
                for (int j = 0; j < 3; j++) {
                    if (i < toRemove) {
                        copy[i][j] = tasks[i][j];
                    } else {
                        copy[i][j] = tasks[i + 1][j];
                    }
                }
        }
        return copy;
    }

    public static void safeToFile(String[][] tasks){
        try (FileWriter fileWriter = new FileWriter("tasks.csv", false)){
            for (int i = 0; i < tasks.length; i++) {
                for (int j = 0; j < tasks[i].length; j++) {
                    fileWriter.append(tasks[i][j] + ", ");
                }
                fileWriter.append("\n");
            }
        } catch (IOException ex) {
            System.out.println("Błąd zapisu do pliku.");
        }

    }



}
