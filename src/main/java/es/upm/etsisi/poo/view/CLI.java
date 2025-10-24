package es.upm.etsisi.poo.view;

import java.util.Scanner;

public class CLI {

    Scanner scanner;

    public CLI() {
        scanner = new Scanner(System.in);
    }

    public String readCommand() {
        System.out.print("Type command: ");
        return scanner.nextLine();
    }

    public String[] split(String text, String regex) {
        return text.split(regex);
    }
}
