package com.group5;

import java.text.DecimalFormat;
import java.util.Scanner;

public class Algorithms extends Process {
    Scanner input = new Scanner(System.in);

    int systemTime = 0;
    int idleTime = 0;
    float totalTurnaroundTime = 0;
    float totalWaitingTime = 0;
    float avgTurnaroundTime = 0;
    float avgWaitingTime = 0;
    int completed = 0;
    int previousProcess = 0;
    int[] processGanttChart;
    int[] burstGanttChart;

    public Algorithms() {

    }

    public void SRTF(Process[] process) {
        int burst_total = 0;
        int[] processBurst = new int[process.length];

        //assign each process burst time to an array
        for(int i = 0; i < process.length; i++) {
            processBurst[i] = process[i].getBurstTime();
            burst_total += process[i].getBurstTime();
            // burst total ensures that a single burst will never be higher than total
            // to be used in curr_process_burst
        }

        processGanttChart = new int[burst_total + 1];
        burstGanttChart = new int[burst_total + 1];

        while(completed != process.length) { // run until all processes are complete
            int curr_process_index = -1; // holds process with smallest burst time
            int curr_process_burst = burst_total; // checks if retrieved burst time is smaller than current burst

            // find the smallest burst time for processes that have arrived
            for(int j = 0; j < process.length; j++) {
                // check if there is a process that has arrived and if execution time is complete
                if(process[j].getArrivalTime() <= systemTime && processBurst[j] != 0) {
                    if(processBurst[j] < curr_process_burst) {
                        curr_process_index = j;
                        curr_process_burst = processBurst[j];
                    }
                    if(processBurst[j] == curr_process_burst) {
                        if(process.length != 1) { // check if there is only 1 process
                            if(process[j].getArrivalTime() < process[curr_process_index].getArrivalTime()) {
                                curr_process_index = j;
                                curr_process_burst = processBurst[j];
                            }
                        }
                        else {
                            curr_process_index = j;
                            curr_process_burst = processBurst[j];
                        }
                    }
                }
            }

            // execute process if a process is found
            if(curr_process_index != -1) {
                if(systemTime == 0) {
                    //System.out.print("|P" + process[curr_process_index].getProcessID() + ", " + systemTime);
                    //processGanttChart[systemTime] = process[curr_process_index].getProcessID();
                    //burstGanttChart[systemTime] = systemTime;
                }
                systemTime++;
                processBurst[curr_process_index] -= 1;

                if(process[curr_process_index].getProcessID() != previousProcess) {
                    if(systemTime - 1 == 0) {
                        //System.out.print("-" + systemTime);
                        //processGanttChart[systemTime] = process[curr_process_index].getProcessID();
                        //burstGanttChart[systemTime] = systemTime;
                    }
                    else {
                        //System.out.print("|P" + process[curr_process_index].getProcessID() + ", " + systemTime);
                        //processGanttChart[systemTime] = process[curr_process_index].getProcessID();
                        //burstGanttChart[systemTime] = systemTime;
                    }
                }

                if(processBurst[curr_process_index] == 0) { // set values for process if execution is complete
                    process[curr_process_index].setCompletionTime(systemTime);
                    process[curr_process_index].setTurnaroundTime(process[curr_process_index].getCompletionTime() - process[curr_process_index].getArrivalTime());
                    process[curr_process_index].setWaitingTime(process[curr_process_index].getTurnaroundTime() - process[curr_process_index].getBurstTime());
                    totalTurnaroundTime += process[curr_process_index].getTurnaroundTime();
                    totalWaitingTime += process[curr_process_index].getWaitingTime();
                    completed++;
                    if(process.length != 1) {
                        //System.out.print("-" + systemTime);
                        //processGanttChart[systemTime] = process[curr_process_index].getProcessID();
                        //burstGanttChart[systemTime] = systemTime;
                    }
                }
                processGanttChart[systemTime - 1] = process[curr_process_index].getProcessID();
                burstGanttChart[systemTime] = systemTime;
                previousProcess = process[curr_process_index].getProcessID();
            }
            else {
                System.out.print("|x");
                systemTime++;
                idleTime++;
            }
        }

        System.out.println("|");

        avgTurnaroundTime = totalTurnaroundTime / process.length;
        avgWaitingTime = totalWaitingTime / process.length;

        System.out.println("\nProcess\t|\tAT\t|\tBT\t|\tCT\t|\tTAT\t|\tWT");
        System.out.println("-----------------------------------------------");

        for(Process value: process) {
            System.out.println("P" + value.getProcessID() + "\t\t|\t" +
                    value.getArrivalTime() + "\t|\t" +
                    value.getBurstTime() + "\t|\t" +
                    value.getCompletionTime() + "\t|\t" +
                    value.getTurnaroundTime() + "\t|\t" +
                    value.getWaitingTime() + "\t\t");
        }

        System.out.println("\nGANTT CHART");
        System.out.print("|");
        for(int i = 0; i < processGanttChart.length - 1; i++) {
            String str = "|";
            String strP = " P";
            System.out.format("%1s", strP);
            System.out.format(processGanttChart[i] + "%2s", str);
            //System.out.print("P" + processGanttChart[i] + "\t|");
        }
        System.out.println();
        //System.out.print("|");
        for(int value: burstGanttChart) {
            String str = " ";
            if(value < 10) {
                System.out.format(value + "%4s", str);
            }
            else {
                System.out.format(value + "%3s", str);
            }
            //System.out.print(value + "\t");
        }
        System.out.println();

        DecimalFormat df = new DecimalFormat("#0.00");
        System.out.println("\nAverage Turnaround Time = " + df.format(avgTurnaroundTime) + "ms");
        System.out.println("Average Waiting Time = " + df.format(avgWaitingTime) + "ms");
    }

    public void PPRIO(Process[] process) {
        int priority;
        System.out.println("\nEnter priority number for each process");
        for(int i = 1; i <= process.length; i++) {
            System.out.println("\nProcess ID: " + i);
            System.out.print("Priority number: ");
            priority = isInteger(input); // priority for each process

            process[i - 1].setPriority(priority);
        }
        System.out.println();

        int priority_total = 0;
        int[] processBurst = new int[process.length];

        //assign each process burst time to an array
        for(int i = 0; i < process.length; i++) {
            processBurst[i] = process[i].getBurstTime();
            priority_total += process[i].getPriority();
            // burst total ensures that a single burst will never be higher than total
            // to be used in curr_process_burst
            // same goes for priority total
        }

        while(completed != process.length) { // run until all processes are complete
            int curr_process_index = -1; // holds process with smallest burst time
            int curr_process_priority = priority_total; // checks if arrived process priority is higher than the previous process

            // find the highest priority with smallest burst time among processes that have arrived
            for(int j = 0; j < process.length; j++) {
                // check if there is a process that has arrived and if execution time is complete
                if(process[j].getArrivalTime() <= systemTime && processBurst[j] != 0) {
                    if(process[j].getPriority() < curr_process_priority) {
                        curr_process_index = j;
                        curr_process_priority = process[j].getPriority();
                    }
                    if(process[j].getPriority() == curr_process_priority) {
                        if(process.length != 1) { // check if there is only 1 process
                            if(process[j].getArrivalTime() < process[curr_process_index].getArrivalTime()) {
                                curr_process_index = j;
                                curr_process_priority = process[j].getPriority();
                            }
                        }
                        else {
                            curr_process_index = j;
                            curr_process_priority = process[j].getPriority();
                        }
                    }
                }
            }

            // execute process if process was found
            if(curr_process_index != -1) {
                if(systemTime == 0) {
                    System.out.print("|P" + process[curr_process_index].getProcessID() + ", " + systemTime);
                }
                systemTime++;
                processBurst[curr_process_index] -= 1;

                if(process[curr_process_index].getProcessID() != previousProcess) {
                    System.out.print("|P" + process[curr_process_index].getProcessID() + ", " + systemTime);
                }

                if(processBurst[curr_process_index] == 0) { // set values for process if execution is complete
                    process[curr_process_index].setCompletionTime(systemTime);
                    process[curr_process_index].setTurnaroundTime(process[curr_process_index].getCompletionTime() - process[curr_process_index].getArrivalTime());
                    process[curr_process_index].setWaitingTime(process[curr_process_index].getTurnaroundTime() - process[curr_process_index].getBurstTime());
                    totalTurnaroundTime += process[curr_process_index].getTurnaroundTime();
                    totalWaitingTime += process[curr_process_index].getWaitingTime();
                    completed++;
                    System.out.print("-" + systemTime);
                }
                previousProcess = process[curr_process_index].getProcessID();
            }
            else {
                System.out.print("|x");
                systemTime++;
                idleTime++;
            }
        }

        System.out.println("|");

        avgTurnaroundTime = totalTurnaroundTime / process.length;
        avgWaitingTime = totalWaitingTime / process.length;

        System.out.println("\nProcess\t|\tAT\t|\tBT\t|\tPriority\t|\tCT\t|\tTAT\t|\tWT");
        System.out.println("------------------------------------------------------");

        for(Process value: process) {
            System.out.println("P" + value.getProcessID() + "\t\t|\t" +
                    value.getArrivalTime() + "\t|\t" +
                    value.getBurstTime() + "\t|\t\t" +
                    value.getPriority() + "\t\t|\t" +
                    value.getCompletionTime() + "\t|\t" +
                    value.getTurnaroundTime() + "\t|\t" +
                    value.getWaitingTime() + "\t\t");
        }

        DecimalFormat df = new DecimalFormat("##.00");
        System.out.println("\nAverage Turnaround Time = " + df.format(avgTurnaroundTime) + "ms");
        System.out.println("Average Waiting Time = " + df.format(avgWaitingTime) + "ms");
    }

    public int isInteger(Scanner input) {
        int value;
        if(input.hasNextInt()) {
            value = input.nextInt();
        }
        else {
            input.hasNext();
            value = 0;
        }
        while(value < 0) {
            if(input.hasNextInt()) {
                value = input.nextInt();
                System.out.println();
            }
            else {
                System.out.print("Invalid input, enter a number: ");
                input.next();
                value = -1;
            }
        }
        return value;
    }

    public int isIntegerAndGreaterThanZero(Scanner input) {
        int value;
        if(input.hasNextInt()) {
            value = input.nextInt();
            if(value == 0) {
                System.out.print("Number must be greater than 0, please try again: ");
            }
        }
        else {
            input.hasNext();
            value = -1;
        }
        while(value <= 0) {
            if(input.hasNextInt()) {
                value = input.nextInt();
            }
            else {
                System.out.print("Invalid input, enter a number: ");
                input.next();
                value = -1;
            }
            if(value == 0) {
                System.out.print("Number must be greater than 0, please try again: ");
            }
        }
        System.out.println();
        return value;
    }
}
