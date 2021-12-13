package com.group5;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Algorithms extends Process {
    Scanner input = new Scanner(System.in);

    ArrayList<Integer> readyQueue = new ArrayList<>();
    int systemTime, idleTime;
    int totalBurst;
    float totalTurnaroundTime, totalWaitingTime;
    float avgTurnaroundTime, avgWaitingTime;
    int completed;
    int previousProcess;

    public Algorithms() {

    }

    public void SRTF(Process[] process) {
        int[] processBurst = new int[process.length];
        systemTime = 0; idleTime = 0;
        totalBurst = 0;
        totalTurnaroundTime = 0; totalWaitingTime = 0;
        avgTurnaroundTime = 0; avgWaitingTime = 0;
        completed = 0;
        previousProcess = 0;

        ArrayList<String> processGanttChart = new ArrayList<>();
        ArrayList<String> burstGanttChart = new ArrayList<>();

        //assign each process burst time to an array
        for(int i = 0; i < process.length; i++) {
            processBurst[i] = process[i].getBurstTime();
            totalBurst += process[i].getBurstTime();
            // burst total ensures that a single burst will never be higher than total
            // to be used in curr_process_burst
        }

        while(completed != process.length) { // run until all processes are complete
            int currentProcess = findSmallestBT(process, processBurst);

            if(systemTime == 0) {
                burstGanttChart.add("0");
            }

            // execute process if process was found
            if(currentProcess != -1) {
                executeProcess(process, processBurst, currentProcess);
                processGanttChart.add(String.valueOf(process[currentProcess].getProcessID()));
                burstGanttChart.add(String.valueOf(systemTime));
                previousProcess = process[currentProcess].getProcessID();
            }
            else {
                systemTime++;
                idleTime++;
                processGanttChart.add("xx");
                burstGanttChart.add(String.valueOf(systemTime));
            }
        }

        displayProcess(process);
        displayGanttChart(processGanttChart, burstGanttChart);

        avgTurnaroundTime = totalTurnaroundTime / process.length;
        avgWaitingTime = totalWaitingTime / process.length;

        displayAvgTATWT(avgTurnaroundTime, avgWaitingTime);
    }

    public void RR(Process[] process) {
        int[] processBurst = new int[process.length];
        systemTime = 0; idleTime = 0;
        totalBurst = 0;
        totalTurnaroundTime = 0; totalWaitingTime = 0;
        avgTurnaroundTime = 0; avgWaitingTime = 0;
        completed = 0;
        previousProcess = -1;
        int timeQuantum;
        int index;
        readyQueue.add(0);

        ArrayList<String> processGanttChart = new ArrayList<>();
        ArrayList<String> burstGanttChart = new ArrayList<>();
        Boolean[] flag = new Boolean[process.length];

        System.out.print("\nEnter time slice: ");
        timeQuantum = isInteger(input);

        //sort process by arrival time in ascending order
        Process temp;
        for(int i = 0; i < process.length; i++) {
            for(int j = 0; j < process.length - i - 1; j++) {
                if(process[j].getArrivalTime() > process[j + 1].getArrivalTime()) {
                    temp = process[j + 1];
                    process[j + 1] = process[j];
                    process[j] = temp;
                }
            }
        }

        for(int i = 0; i < process.length; i++) {
            processBurst[i] = process[i].getBurstTime();
            totalBurst += process[i].getBurstTime();
            flag[i] = false;
            // burst total ensures that a single burst will never be higher than total
            // to be used in curr_process_burst
        }

        while(completed != process.length) {
            //FIFO implementation
            index = readyQueue.get(0); // get the number in front of the array list
            readyQueue.remove(0); // remove the number in front of the array list

            if(systemTime == 0) {
                burstGanttChart.add("0");
            }

            if(processBurst[index] - timeQuantum <= 0) {
                systemTime += processBurst[index];
                processBurst[index] = 0;
                setProperties(process, index);
            }
            else {
                systemTime += timeQuantum;
                processBurst[index] -= timeQuantum;
            }

            processGanttChart.add(String.valueOf(process[index].getProcessID()));
            burstGanttChart.add(String.valueOf(systemTime));

            for(int i = 1; i < process.length; i++) {
                // check if there is a process that has arrived and if execution time is complete
                if(process[i].getArrivalTime() <= systemTime && processBurst[i] > 0 && !flag[i]) {
                    readyQueue.add(i);
                    flag[i] = true;
                }
            }

            if(processBurst[index] > 0) {
                readyQueue.add(index);
            }

            if(readyQueue.isEmpty()) {
                for(int i = 1; i < process.length; i++) {
                    if(processBurst[i] > 0) {
                        readyQueue.add(i);
                        flag[i] = true;
                        break;
                    }
                }
            }
        }

        displayProcess(process);
        displayGanttChart(processGanttChart, burstGanttChart);

        avgTurnaroundTime = totalTurnaroundTime / process.length;
        avgWaitingTime = totalWaitingTime / process.length;

        displayAvgTATWT(avgTurnaroundTime, avgWaitingTime);
    }

    public void RRO(Process[] process) {
        int[] processBurst = new int[process.length];
        systemTime = 0; idleTime = 0;
        totalBurst = 0;
        totalTurnaroundTime = 0; totalWaitingTime = 0;
        avgTurnaroundTime = 0; avgWaitingTime = 0;
        completed = 0;
        previousProcess = -1;
        int timeQuantum;
        int overheadTime;
        int index;
        readyQueue.add(0);

        ArrayList<String> processGanttChart = new ArrayList<>();
        ArrayList<String> burstGanttChart = new ArrayList<>();
        Boolean[] flag = new Boolean[process.length];

        System.out.print("\nEnter time slice: ");
        timeQuantum = isInteger(input);

        System.out.print("Enter overhead time: ");
        overheadTime = isInteger(input);

        //sort process by arrival time in ascending order
        Process temp;
        for(int i = 0; i < process.length; i++) {
            for(int j = 0; j < process.length - i - 1; j++) {
                if(process[j].getArrivalTime() > process[j + 1].getArrivalTime()) {
                    temp = process[j + 1];
                    process[j + 1] = process[j];
                    process[j] = temp;
                }
            }
        }

        for(int i = 0; i < process.length; i++) {
            processBurst[i] = process[i].getBurstTime();
            totalBurst += process[i].getBurstTime();
            flag[i] = false;
            // burst total ensures that a single burst will never be higher than total
            // to be used in curr_process_burst
        }

        while(completed != process.length) {
            //FIFO implementation
            index = readyQueue.get(0); // get the number in front of the array list
            readyQueue.remove(0); // remove the number in front of the array list

            if(systemTime == 0) {
                burstGanttChart.add("0");
            }

            if(processBurst[index] - timeQuantum <= 0) {
                systemTime += processBurst[index];
                processBurst[index] = 0;
                setProperties(process, index);
            }
            else {
                systemTime += timeQuantum;
                processBurst[index] -= timeQuantum;
            }

            processGanttChart.add(String.valueOf(process[index].getProcessID()));
            burstGanttChart.add(String.valueOf(systemTime));

            for(int i = 1; i < process.length; i++) {
                // check if there is a process that has arrived and if execution time is complete
                if(process[i].getArrivalTime() <= systemTime && processBurst[i] > 0 && !flag[i]) {
                    readyQueue.add(i);
                    flag[i] = true;
                }
            }

            if(processBurst[index] > 0) {
                readyQueue.add(index);
            }

            if(readyQueue.isEmpty()) {
                for(int i = 1; i < process.length; i++) {
                    if(processBurst[i] > 0) {
                        readyQueue.add(i);
                        flag[i] = true;
                        break;
                    }
                }
            }

            //add overhead time
            if(completed != process.length) {
                systemTime += overheadTime;
                idleTime += overheadTime;
                processGanttChart.add("xx");
                burstGanttChart.add(String.valueOf(systemTime));
            }
        }

        displayProcess(process);
        displayGanttChart(processGanttChart, burstGanttChart);

        avgTurnaroundTime = totalTurnaroundTime / process.length;
        avgWaitingTime = totalWaitingTime / process.length;

        displayAvgTATWT(avgTurnaroundTime, avgWaitingTime);
    }

    // algorithm for PPRIO
    public void PPRIO(Process[] process) {
        int[] processBurst = new int[process.length];
        systemTime = 0; idleTime = 0;
        totalBurst = 0;
        totalTurnaroundTime = 0; totalWaitingTime = 0;
        avgTurnaroundTime = 0; avgWaitingTime = 0;
        completed = 0; previousProcess = 0;
        int priority_total = 0;

        ArrayList<String> processGanttChart = new ArrayList<>();
        ArrayList<String> burstGanttChart = new ArrayList<>();

        // assign each process burst time to an array
        for(int i = 0; i < process.length; i++) {
            processBurst[i] = process[i].getBurstTime();
            totalBurst += process[i].getBurstTime();
            priority_total += process[i].getPriority();
            // total burst ensures that a single burst will never be higher than total
            // to be used in curr_process_burst
            // same goes for priority total
        }

        while(completed != process.length) { // run until all processes are complete
            int currentProcess = findHighestPriority(process, processBurst, priority_total);

            if(systemTime == 0) {
                burstGanttChart.add("0");
            }

            // execute process if process was found
            if(currentProcess != -1) {
                executeProcess(process, processBurst, currentProcess);
                processGanttChart.add(String.valueOf(process[currentProcess].getProcessID()));
                burstGanttChart.add(String.valueOf(systemTime));
                previousProcess = process[currentProcess].getProcessID();
            }
            else {
                systemTime++;
                idleTime++;
                processGanttChart.add("xx");
                burstGanttChart.add(String.valueOf(systemTime));
            }
        }

        displayProcess(process);
        displayGanttChart(processGanttChart, burstGanttChart);

        avgTurnaroundTime = totalTurnaroundTime / process.length;
        avgWaitingTime = totalWaitingTime / process.length;

        displayAvgTATWT(avgTurnaroundTime, avgWaitingTime);
    }

    // set number of processes
    public int setProcesses() {
        int processes;
        System.out.print("\nEnter number of processes: ");
        processes = isIntegerAndGreaterThanZero(input); // number of processes
        return processes;
    }

    // set arrival time and burst time for processes
    public void setProcessATBT(Process[] process, int processes, String choice) {
        int arrivalTime, burstTime, priority;
        if(choice.equals("PPRIO")) {
            System.out.println("Enter arrival time, burst time, and priority for each process");
            for(int i = 1; i <= processes; i++) {
                System.out.println("\nProcess ID: " + i);
                System.out.print("Arrival Time: ");
                arrivalTime = isInteger(input); // arrival time for each process

                System.out.print("Burst Time: ");
                burstTime = isIntegerAndGreaterThanZero(input); // burst time for each process

                System.out.print("Priority: ");
                priority = isIntegerAndGreaterThanZero(input);
                process[i - 1] = new Process(i, arrivalTime, burstTime, priority); // create processes
            }
        }
        else {
            System.out.println("Enter arrival time and burst time for each process");
            for(int i = 1; i <= processes; i++) {
                System.out.println("\nProcess ID: " + i);
                System.out.print("Arrival time: ");
                arrivalTime = isInteger(input); // arrival time for each process

                System.out.print("Burst time: ");
                burstTime = isIntegerAndGreaterThanZero(input); // burst time for each process

                process[i - 1] = new Process(i, arrivalTime, burstTime); // create processes
            }
        }
    }

    // find smallest burst time among processes
    public int findSmallestBT(Process[] process, int[] processBurst) {
        int index = -1;
        int burst = totalBurst;
        // find the smallest burst time for processes that have arrived
        for(int i = 0; i < process.length; i++) {
            // check if there is a process that has arrived and if execution time is complete
            if(process[i].getArrivalTime() <= systemTime && processBurst[i] != 0) {
                if(processBurst[i] < burst) {
                    index = i;
                    burst = processBurst[i];
                }
                if(processBurst[i] == burst) {
                    // check if there is only 1 process
                    if(process.length != 1) {
                        // check if current iteration arrival time is less than the retrieved index
                        if(process[i].getArrivalTime() < process[index].getArrivalTime()) {
                            index = i;
                            burst = processBurst[i];
                        }
                    }
                    else {
                        index = i;
                        burst = processBurst[i];
                    }
                }
            }
        }
        return index;
    }

    // find process with highest priority
    public int findHighestPriority(Process[] process, int[] processBurst, int priority_total) {
        int index = -1; // holds process with smallest burst time
        int priority = priority_total; // checks if arrived process priority is higher than the previous process

        // find the highest priority with smallest burst time among processes that have arrived
        for(int i = 0; i < process.length; i++) {
            // check if there is a process that has arrived and if execution time is complete
            if(process[i].getArrivalTime() <= systemTime && processBurst[i] != 0) {
                if(process[i].getPriority() < priority) {
                    index = i;
                    priority = process[i].getPriority();
                }
                if(process[i].getPriority() == priority) {
                    if(process.length != 1) { // check if there is only 1 process
                        if(process[i].getArrivalTime() < process[index].getArrivalTime()) {
                            index = i;
                            priority = process[i].getPriority();
                        }
                    }
                    else {
                        index = i;
                        priority = process[i].getPriority();
                    }
                }
            }
        }
        return index;
    }

    // execute process if found
    public void executeProcess(Process[] process, int[] processBurst, int index) {
        // execute process if a process is found
        systemTime++;
        processBurst[index] -= 1;

        if(processBurst[index] == 0) { // set values for process if execution is complete
            setProperties(process, index);
        }
    }

    // set properties if execution is complete
    public void setProperties(Process[] process, int index) {
        process[index].setCompletionTime(systemTime);
        process[index].setTurnaroundTime(process[index].getCompletionTime() - process[index].getArrivalTime());
        process[index].setWaitingTime(process[index].getTurnaroundTime() - process[index].getBurstTime());
        totalTurnaroundTime += process[index].getTurnaroundTime();
        totalWaitingTime += process[index].getWaitingTime();
        completed++;
    }

    // display table
    public void displayProcess(Process[] process) {
        if(process[0].getPriority() == 0) {
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
        }
        else {
            System.out.println("\nProcess\t|\tAT\t|\tBT\t|\tPriority\t|\tCT\t|\tTAT\t|\tWT");
            System.out.println("---------------------------------------------------------------");
            for(Process value: process) {
                System.out.println("P" + value.getProcessID() + "\t\t|\t" +
                        value.getArrivalTime() + "\t|\t" +
                        value.getBurstTime() + "\t|\t\t" +
                        value.getPriority() + "\t\t|\t" +
                        value.getCompletionTime() + "\t|\t" +
                        value.getTurnaroundTime() + "\t|\t" +
                        value.getWaitingTime() + "\t\t");
            }
        }
    }

    //display gantt chart
    public void displayGanttChart(ArrayList<String> processGanttChart, ArrayList<String> burstGanttChart) {
        System.out.println("\nGANTT CHART");
        System.out.print("|");
        for (String value : processGanttChart) {
            String str = "|";
            String strP = " P";
            if (value.equals("xx")) {
                System.out.format("%1s", " ");
            }
            else {
                System.out.format("%1s", strP);
            }
            System.out.format(value + "%2s", str);
        }
        System.out.println();
        for(String value: burstGanttChart) {
            String str = " ";
            if(Integer.parseInt(value) < 10) {
                System.out.format(value + "%4s", str);
            }
            else if(Integer.parseInt(value) >= 10 && Integer.parseInt(value) < 100) {
                System.out.format(value + "%3s", str);
            }
            else {
                System.out.format(value + "%2s", str);
            }
        }
        System.out.println();
    }

    //display avg TAT and avg WT
    public void displayAvgTATWT(float avgTurnaroundTime, float avgWaitingTime) {
        DecimalFormat df = new DecimalFormat("#0.00");
        System.out.println("\nAverage Turnaround Time = " + df.format(avgTurnaroundTime) + "ms");
        System.out.println("Average Waiting Time = " + df.format(avgWaitingTime) + "ms");
    }

    // check if user input is an integer
    public int isInteger(Scanner input) {
        int value;
        if(input.hasNextInt()) {
            value = input.nextInt();
        }
        else {
            input.hasNext();
            value = -1;
        }
        while(value <= -1) {
            if(input.hasNextInt()) {
                value = input.nextInt();
                String strValue = String.valueOf(value);
                for(int i = 0; i < strValue.length(); i++) {
                    if(strValue.indexOf(i) == 0) {
                        new StringBuilder(strValue).deleteCharAt(i).toString();
                    }
                }
                value = Integer.parseInt(strValue);
            }
            else {
                System.out.print("Invalid input, enter a number: ");
                input.next();
                value = -1;
            }
        }
        return value;
    }

    // check if user input is an integer and > 0
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
                String strValue = String.valueOf(value);
                for(int i = 0; i < strValue.length(); i++) {
                    if(strValue.indexOf(i) == 0) {
                        new StringBuilder(strValue).deleteCharAt(i).toString();
                    }
                }
                value = Integer.parseInt(strValue);
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
        return value;
    }
}
