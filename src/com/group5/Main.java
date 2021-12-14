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
	    char again = 'Y';
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
                case "A" -> {
                    processes = algorithms.setProcesses();
                    process = new Process[processes]; // create array of objects process based on number of processes
                    algorithms.setProcessATBT(process, processes, choice);
                    algorithms.SRTF(process);


                }
                case "B" -> {
                    processes = algorithms.setProcesses();
                    process = new Process[processes]; // create array of objects process based on number of processes
                    algorithms.setProcessATBT(process, processes, choice);
                    algorithms.RR(process);

                }
                case "C" -> {
                    processes = algorithms.setProcesses();
                    process = new Process[processes]; // create array of objects process based on number of processes
                    algorithms.setProcessATBT(process, processes, choice);
                    algorithms.RRO(process);

                }
                case "D" -> {
                    choice = "PPRIO";
                    processes = algorithms.setProcesses();
                    process = new Process[processes]; // create array of objects process based on number of processes
                    algorithms.setProcessATBT(process, processes, choice);
                    algorithms.PPRIO(process);
                }
                case "E" -> again ='n';
            }
            if(again == 'Y') {
                System.out.print("\nDo you want to try again? [Y/N]: ");
                again = input.next().charAt(0);
                again = Character.toUpperCase(again);
                input.nextLine();
                while(again != 'Y' && again != 'N') {
                    System.out.print("Incorrect input, enter Y/N: ");
                    again = input.next().charAt(0);
                    again = Character.toUpperCase(again);
                }
                System.out.println();
            }
        }
        while(again == 'Y');
    }
}
