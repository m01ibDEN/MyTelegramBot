package org.example;

import java.util.HashSet;
import java.util.Random;

public class Game {
    private static String x;

    private static String o;

    private static String grid;

    private static HashSet<Integer> freeSqrs = new HashSet<>();

    public static String getX() {
        return x;
    }

    public static String getO() {
        return o;
    }

    public static String getGrid() {
        return grid;
    }

    public static HashSet<Integer> getFreeSqrs() {
        return freeSqrs;
    }

    public static void setSquare(int number, String symbol) {
        freeSqrs.remove(number);
        switch (number) {
            case 1:
                grid = grid.substring(0, 2) + symbol + grid.substring(3);
                break;
            case 2:
                grid = grid.substring(0, 6) + symbol + grid.substring(7);
                break;
            case 3:
                grid = grid.substring(0, 10) + symbol + grid.substring(11);
                break;

            case 4:
                grid = grid.substring(0, 28) + symbol + grid.substring(29);
                break;
            case 5:
                grid = grid.substring(0, 32) + symbol + grid.substring(33);
                break;
            case 6:
                grid = grid.substring(0, 36) + symbol + grid.substring(37);
                break;

            case 7:
                grid = grid.substring(0, 54) + symbol + grid.substring(55);
                break;
            case 8:
                grid = grid.substring(0, 58) + symbol + grid.substring(59);
                break;
            case 9:
                grid = grid.substring(0, 62) + symbol + grid.substring(63);
                break;
        }
    }

    public static void setup() {
        for(int i = 1; i < 10; i++) freeSqrs.add(i);
        x = "X";
        o = "O";
        grid = "  1 | 2 | 3 \n" +
                "----+---+---\n" +
                "  4 | 5 | 6 \n" +
                "----+---+---\n" +
                "  7 | 8 | 9 \n";

    }

    public static void botAction() {
        Random random = new Random();
        int number = random.nextInt(freeSqrs.size());
        number = Integer.parseInt(freeSqrs.toArray()[number].toString());
        setSquare(number, getO());
    }

    public static String end() {
        char pos1 = getGrid().toCharArray()[2];
        char pos2 = getGrid().toCharArray()[6];
        char pos3 = getGrid().toCharArray()[10];
        char pos4 = getGrid().toCharArray()[28];
        char pos5 = getGrid().toCharArray()[32];
        char pos6 = getGrid().toCharArray()[36];
        char pos7 = getGrid().toCharArray()[54];
        char pos8 = getGrid().toCharArray()[58];
        char pos9 = getGrid().toCharArray()[62];

        boolean uflag1 = (pos1 == pos2 && pos2 == pos3 && pos3 == getX().toCharArray()[0]);
        boolean uflag2 = (pos4 == pos5 && pos5== pos6 && pos6 == getX().toCharArray()[0]);
        boolean uflag3 = (pos7 == pos8 && pos8 == pos9 && pos9 == getX().toCharArray()[0]);

        boolean uflag4 = (pos1 == pos4 && pos4 == pos7 && pos7 == getX().toCharArray()[0]);
        boolean uflag5 = (pos2 == pos5 && pos5== pos8 && pos8 == getX().toCharArray()[0]);
        boolean uflag6 = (pos3 == pos6 && pos6 == pos9 && pos9 == getX().toCharArray()[0]);

        boolean uflag7 = (pos1 == pos5 && pos5 == pos9 && pos9 == getX().toCharArray()[0]);
        boolean uflag8 = (pos3 == pos5 && pos5== pos7 && pos7 == getX().toCharArray()[0]);

        boolean eflag1 = (pos1 == pos2 && pos2 == pos3 && pos3 == getO().toCharArray()[0]);
        boolean eflag2 = (pos4 == pos5 && pos5== pos6 && pos6 == getO().toCharArray()[0]);
        boolean eflag3 = (pos7 == pos8 && pos8 == pos9 && pos9 == getO().toCharArray()[0]);

        boolean eflag4 = (pos1 == pos4 && pos4 == pos7 && pos7 == getO().toCharArray()[0]);
        boolean eflag5 = (pos2 == pos5 && pos5== pos8 && pos8 == getO().toCharArray()[0]);
        boolean eflag6 = (pos3 == pos6 && pos6 == pos9 && pos9 == getO().toCharArray()[0]);

        boolean eflag7 = (pos1 == pos5 && pos5 == pos9 && pos9 == getO().toCharArray()[0]);
        boolean eflag8 = (pos3 == pos5 && pos5== pos7 && pos7 == getO().toCharArray()[0]);

        if (uflag1 || uflag2 || uflag3 || uflag4 || uflag5 || uflag6 || uflag7 || uflag8) {
            return "user";
        }

        else if (eflag1 || eflag2 || eflag3 || eflag4 || eflag5 || eflag6 || eflag7 || eflag8) {
            return "bot";
        }

        else if (freeSqrs.isEmpty()) {
            return "none";
        }

        return null;
    }

    public static void main(String[] args) {
        setup();
        for (int i = 0; i < getGrid().length(); i++) {
            if (Character.isDigit(getGrid().toCharArray()[i])) {
                System.out.print(getGrid().toCharArray()[i] + " ");
            }
        }
        System.out.println("\n");
        for (int i = 0; i < getGrid().length(); i++) {
            if (Character.isDigit(getGrid().toCharArray()[i])) {
                System.out.print(i + " ");
            }
        }
    }
}
