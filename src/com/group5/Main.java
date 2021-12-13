package com.group5;

//created by
//Cabel, Kean
//De Leon, Elmer Kyle
//Yu, Shaina

import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Algorithms algorithms = new Algorithms();
	    String choice;
	    char again = ' ';
	    Process[] process;
        int processes;

        do {
            System.out.println("CPU Scheduling Algorithm" +
                    "\n[A] Shortest Remaining Time First (SRTF)" +
                    "\n[B] Round Robin (RR)" +
                    "\n[C] Round Robin with Overhead (RRO)" +
                    "\n[D] Preemptive Priority (P-PRIO)" +
                    "\n[E] Exit");

            System.out.print("\nChoose an algorithm: ");
            choice = input.nextLine().toUpperCase();
            while (!Pattern.matches("[ABCDEF]", choice)) { // input error checking
                System.out.print("Incorrect input, please try again: ");
                choice = input.nextLine().toUpperCase();
            }

            switch(choice) {
                case "A":
                    processes = algorithms.setProcesses();
                    process = new Process[processes]; // create array of objects process based on number of processes
                    algorithms.setProcessATBT(process, processes, choice);
                    algorithms.SRTF(process);

                    System.out.print("\nDo you want to try again?\nEnter Y/y to try again: ");
                    again = input.next().charAt(0);
                    input.nextLine();
                    System.out.println();
                    break;
                case "B":
                    algorithms.RR();
                    System.out.print("\nDo you want to try again?\nEnter Y/y to try again: ");
                    again = input.next().charAt(0);
                    input.nextLine();
                    System.out.println();
                    break;
                case "C":
                    processes = algorithms.setProcesses();
                    process = new Process[processes]; // create array of objects process based on number of processes
                    algorithms.setProcessATBT(process, processes, choice);
                    algorithms.RRO(process);

                    System.out.print("\nDo you want to try again?\nEnter Y/y to try again: ");
                    again = input.next().charAt(0);
                    input.nextLine();
                    System.out.println();
                    break;
                case "D":
                    choice = "PPRIO";
                    processes = algorithms.setProcesses();
                    process = new Process[processes]; // create array of objects process based on number of processes
                    algorithms.setProcessATBT(process, processes, choice);
                    algorithms.PPRIO(process);

                    System.out.print("\nDo you want to try again?\nEnter Y/y to try again: ");
                    again = input.next().charAt(0);
                    input.nextLine();
                    System.out.println();
                    break;

                    /*algorithms.PPRIO();
                    System.out.print("\nDo you want to try again?\nEnter Y/y to try again: ");
                    again = input.next().charAt(0);
                    input.nextLine();
                    System.out.println();
                    break;*/
                case "E":
                    again = 'n';
                    break;
                default:
                    break;
            }
        }
        while(again == 'Y' || again == 'y');
    }
}
