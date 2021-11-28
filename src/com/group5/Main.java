package com.group5;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
	    CPU algorithms = new CPU();

	    String choice = "";
        int processes = 0;

        System.out.println("CPU Scheduling Algorithm" +
                "\n[A] Shortest Remaining Time First (SRTF)" +
                "\n[B] Round Robin (RR)" +
                "\n[C] Round Robin with Overhead (RRO)" +
                "\n[D] Preemptive Priority (P-PRIO)" +
                "\n[E] Multi-level Feedback Queue (MLFQ)" +
                "\n[F] Exit");

        System.out.print("\nChoose an algorithm: ");
        choice = input.nextLine().toUpperCase();
        switch(choice) {
            case "A":
                System.out.print("Enter number of processes: ");
                processes = input.nextInt(); // number of processes

                CPU[] cpu = new CPU[processes]; // create array of objects CPU based on number of processes
                int systemTime = 0; // time of CPU runtime
                int idleTime = 0; // time of CPU idle time
                int arrivalTime = 0; // arrival time for process
                int burstTime = 0; // burst time for process
                int turnaroundTime = 0; // turnaround time for process
                int waitingTime = 0; // waiting time for process

                System.out.println("Enter arrival time and burst time for each process");
                for(int i = 0; i < processes; i++) {
                    System.out.println("\nProcess ID: " + i);
                    System.out.print("Arrival time: ");
                    arrivalTime = input.nextInt(); // input arrival time for each process
                    System.out.print("Burst time: ");
                    burstTime = input.nextInt(); // input burst time for each process
                    cpu[i] = new CPU(i, arrivalTime, burstTime); // create processes
                }

                for(int i = 0; i < processes; i++) {
                    for(int j = 0; j < cpu[cpu.length - 1].getArrivalTime(); j++) { // run CPU until last arrival time
                        systemTime += 1;
                        if(systemTime < cpu[j].getArrivalTime()) { // if there is no process yet, increase idle time
                            idleTime += 1;
                        }
                        else {

                        }
                    }
                }

                /*System.out.print("Enter number of processes: ");
                processNumber = input.nextInt();
                int systemTime = 0;
                int idleTime = 0;
                int temp = 0;
                int burst = 0;
                int completion = 0;
                int smallestBT = 0;
                int[] readyQueue = new int[processNumber]; //RQ
                int[] waitingQueue = new int[processNumber]; //WQ
                int[] arrivalTime = new int[processNumber]; //AT
                int[] burstTime = new int[processNumber]; //BT
                int[] completionTime = new int[processNumber]; //CT
                int[] turnaroundTime = new int[processNumber]; //TAT
                int[] waitingTime = new int[processNumber]; //WT

                System.out.println("Enter arrival time for each process");
                for(int i = 0; i < processNumber; i++) {
                    System.out.print("Process ID " + i + ": ");
                    arrivalTime[i] = input.nextInt();
                    cpu[i] = new PreemptiveAlgorithms(i, i, i, i, i, i, i);
                }

                System.out.println("\nEnter burst time for each process");
                for(int i = 0; i < processNumber; i++) {
                    System.out.print("Process ID " + i + ": ");
                    burstTime[i] = input.nextInt();
                }

                for(int i = 0; i < processNumber; i++) { //run until last process

                    burst = burstTime[i];
                    readyQueue[i] = burstTime[i];
                    if(i != processNumber) { //check if its the last process
                        for(int j = 0; j < arrivalTime[i + 1]; j++) { //run until next arrival time
                            systemTime += 1;
                        //for(int j = 0; j <= burst; j++) {
                            if(readyQueue[i] == 0) { //if BT is 0, assign CT
                                completionTime[i] = systemTime;

                            }
                            else {
                                readyQueue[i] -= 1;
                            }

                            if(readyQueue[i + 1] < burstTime[i]) { //if next process BT is less than current process BT
                                waitingQueue[i] = readyQueue[i];
                                smallestBT = readyQueue[i];
                                break;
                            }
                        }
                        System.out.println("\nProcess ID: " + i);
                        System.out.println("Arrival time: " + arrivalTime[i]);
                        System.out.println("Burst time: " + burstTime[i]);
                        System.out.println("Completion time: " + completionTime[i]);
                    }
                    else {
                        if(readyQueue[i] < smallestBT) {
                            for(int k = 0; k <= burst; k++) {
                                if (burstTime[i] == 0) { //if BT is 0, assign CT
                                    completionTime[i] = completion;
                                    break;
                                }
                                else {
                                    burstTime[i] -= 1;
                                    completion += 1;
                                }
                            }
                        }
                        System.out.println("\nProcess ID: " + i);
                        System.out.println("Arrival time: " + arrivalTime[i]);
                        System.out.println("Burst time: " + burstTime[i]);
                        System.out.println("Completion time: " + completionTime[i]);
                    }

                }

                //sort the queue in ascending order
                for(int i = 0; i < waitingQueue.length; i++) {
                    for(int j = 0; j < waitingQueue.length - i - 1; j++) {
                        if(waitingQueue[j] > waitingQueue[j + 1]) {
                            temp = waitingQueue[j + 1];
                            waitingQueue[j + 1] = waitingQueue[j];
                            waitingQueue[j] = temp;
                        }
                    }
                }

                for(int i = 0; i < waitingQueue.length; i++) {
                    burst = waitingQueue[i];
                    for(int j = 0; j < burst; j++) {
                        if (burstTime[i] == 0) { //if BT is 0, assign CT
                            completionTime[i] = completion;
                            break;
                        }
                        else {
                            burstTime[i] -= 1;
                            completion += 1;
                        }
                    }
                }

                for(int i = 0; i < processNumber; i++) {
                    System.out.println("\nProcess ID: " + i);
                    System.out.println("Arrival time: " + arrivalTime[i]);
                    System.out.println("Burst time: " + burstTime[i]);
                    System.out.println("Completion time: " + completionTime[i]);
                }

                /*for(int j = 0; j <= waitingQueue.length - 1; j++) {
                    int burst = burstTime[j];
                    if(waitingQueue[j] != waitingQueue[processNumber - 1]) { //check if its the last queue
                        if(waitingQueue[j] > waitingQueue[j + 1]) { //if current BT in queue is greater than next BT in queue
                            temp = smallestBT;
                            smallestBT = waitingQueue[j + 1]; //next BT in queue becomes smallest BT
                        }
                    }
                    else {
                        for(int k = 0; k <= burst; k++) {
                            if (burstTime[j] == 0) { //if BT is 0, assign CT
                                completionTime[j] = completion;
                                break;
                            }
                            else {
                                burstTime[j] -= 1;
                                completion += 1;
                            }
                        }
                    }

                }*/
                break;
            case "B":
                break;
            case "C":
                break;
            case "D":
                break;
            case "E":
                break;
            case "F":
                break;
            default:
                break;
        }

    }
}
