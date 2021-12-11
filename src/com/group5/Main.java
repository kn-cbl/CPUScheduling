package com.group5;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Algorithms algorithms = new Algorithms();
	    String choice;
	    char again;
        int processNumber;
        int arrivalTime; // arrival time for process
        int burstTime; // burst time for process

        do {
            System.out.println("CPU Scheduling Algorithm" +
                    "\n[A] Shortest Remaining Time First (SRTF)" +
                    "\n[B] Round Robin (RR)" +
                    "\n[C] Round Robin with Overhead (RRO)" +
                    "\n[D] Preemptive Priority (P-PRIO)" +
                    "\n[E] Multi-level Feedback Queue (MLFQ)" +
                    "\n[F] Exit");

            System.out.print("\nChoose an algorithm: ");
            choice = input.nextLine().toUpperCase();
            while (!Pattern.matches("[ABCDEF]", choice)) { // input error checking
                System.out.print("Incorrect input, please try again: ");
                choice = input.nextLine().toUpperCase();
            }

            System.out.print("\nEnter number of processes: ");
            processNumber = algorithms.isIntegerAndGreaterThanZero(input); // number of processes

            Process[] process = new Process[processNumber]; // create array of objects process based on number of processes

            System.out.println("Enter arrival time and burst time for each process\n");
            for(int i = 1; i <= processNumber; i++) {
                System.out.println("Process ID: " + i);
                System.out.print("Arrival time: ");
                arrivalTime = algorithms.isInteger(input); // arrival time for each process

                System.out.print("Burst time: ");
                burstTime = algorithms.isIntegerAndGreaterThanZero(input); // burst time for each process

                process[i - 1] = new Process(i, arrivalTime, burstTime); // create processes
            }

            switch(choice) {
                case "A":
                    System.out.println();
                    algorithms.SRTF(process);
                    break;
                case "B":
                    break;
                case "C":
                    break;
                case "D":
                    algorithms.PPRIO(process);
                    break;
                case "E":
                    break;
                case "F":
                    break;
                default:
                    break;
            }
            System.out.print("\nDo you want to try again? Enter Y/y to try again: ");
            again = input.next().charAt(0);
            System.out.println();
        }
        while(again == 'Y' || again == 'y');
    }
}
