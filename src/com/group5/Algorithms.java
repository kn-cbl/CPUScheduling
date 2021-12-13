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


    public void RR() {
        int value,tq;
        int timer = 0, max = 0;
        float aveWT = 0, aveTAT = 0;

        System.out.print("\nEnter the number of processes: ");

        //Check whether the input is an integer
        value = isIntegerAndGreaterThanZero(input);

        int p[]=new int[value];
        int arrival[] = new int[value];
        int burst[] = new int[value];
        int comp[] = new int [value];
        int wait[] = new int[value];
        int turn[] = new int[value];
        int queue[] = new int[value];
        int temp_burst[] = new int[value];
        int fbt[]=new int[value];
        boolean complete[] = new boolean[value];
        int gc=0,j,tbt=0;

        //Enter Arrival and Burst time for each process
        System.out.print("\nEnter the arrival time and burst time for each Process\n");
        for(int i = 0; i < value; i++){
            System.out.println("\nProcess ID: "+(i+1));
            p[i]=i+1;
            System.out.print("Arrival Time: ");
            arrival[i] = isInteger(input);

            System.out.print("Burst Time: ");
            burst[i] = isIntegerAndGreaterThanZero(input);

            temp_burst[i] = burst[i];
            fbt[i]=burst[i];
            tbt+=burst[i];
        }

        //Enter Time Quantum
        System.out.print("\nEnter Time Quantum: ");
        tq = isIntegerAndGreaterThanZero(input);

        //Initializing the queue and complete array
        for(int i = 0; i < value; i++){
            complete[i] = false;
            queue[i] = 0;
        }
        //Incrementing Timer until the first process arrives
        while(timer < arrival[0]){
            timer++;
        }
        queue[0] = 1;

        while(true){
            boolean flag = true;
            for(int i = 0; i < value; i++){
                if(temp_burst[i] != 0){
                    flag = false;
                    break;
                }
            }
            if(flag)
                break;

            for(int i = 0; (i < value) && (queue[i] != 0); i++){
                int ctr = 0;
                while((ctr < tq) && (temp_burst[queue[0]-1] > 0)){
                    temp_burst[queue[0]-1] -= 1;
                    timer += 1;
                    ctr++;

                    //Updating the ready queue until all the processes arrive
                    processArrival(timer, arrival, value, max, queue);
                }
                if((temp_burst[queue[0]-1] == 0) && (!complete[queue[0] - 1])){
                    //turn currently stores exit times
                    turn[queue[0]-1] = timer;
                    complete[queue[0]-1] = true;
                }

                //checks whether or not CPU is idle
                boolean idle = true;
                if(queue[value-1] == 0){
                    for(int k = 0; k < value && queue[k] != 0; k++){
                        if(!complete[queue[k] - 1]){
                            idle = false;
                        }
                    }
                }
                else
                    idle = false;

                if(idle){
                    timer++;
                    processArrival(timer, arrival, value, max, queue);
                }

                //Maintaining the entries of processes after each preemption in the ready Queue
                sortArray(queue,value);
            }
        }

        //Checking Completion Time
        for(int  i = 0 ; i < value; i++) {
            if( i == 0)
            {
                comp[i] = arrival[i] + burst[i];
            }
            else
            {
                if( arrival[i] > comp[i-1])
                {
                    comp[i] = arrival[i] + burst[i];
                }
                else
                    comp[i] = comp[i-1] + burst[i];
            }
        }

        System.out.println("\n________________________________________________________________________________________________");
        System.out.print("JOB\t\tARRIVAL TIME\tBURST TIME\t\tCOMPLETION TIME\t\tTURNAROUND TIME\t\tWAITING TIME");
        System.out.println("\n¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");

        for(int i = 0; i < value; i++){
            System.out.print("P"+(i+1)+"\t\t"+
                    arrival[i]+"\t\t\t\t"+
                    burst[i]+"\t\t\t\t"+
                    turn[i]+"\t\t\t\t\t"+
                    (turn[i]-arrival[i])+ "\t\t\t\t"+
                    ((turn[i]-arrival[i])-burst[i])+"\n");
        }

        System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
        for(int i =0; i< value; i++){
            aveTAT += (turn[i]-arrival[i]);
            aveWT += ((turn[i]-arrival[i])-burst[i]);
        }

        System.out.println("\n===========================================GANTT CHART===========================================");
        int a;
        while(tbt>0) {
            for (a = 0; a < value; a++) {
                if (burst[a] > tq) {
                    burst[a] -= tq;tbt-=tq;
                    System.out.print(" p"+p[a]+": "+gc+"-"+(gc+tq)+" | ");gc+=tq;
                    for (j = 0; j < value; j++) {
                        if ((j != a) && (burst[j] != 0)) wait[j] += tq;
                    }
                }
                else {
                    if(burst[a]>0){
                        System.out.print(" p"+p[a]+": "+gc+"-"+(gc+burst[a])+" | ");gc+=burst[a];
                    }
                    for (j = 0; j < value; j++) {
                        if ((j != a) && (burst[j] != 0)) wait[j] += burst[a];
                    }
                    tbt-=burst[a];
                    burst[a] = 0;
                }
            }
        }
        System.out.println("\n=================================================================================================");

        displayAvgTATWT(aveTAT/value, aveWT/value);
    }

    public void RRO(Process[] process) {
        int[] processBurst = new int[process.length];

        systemTime = 0; idleTime = 0;
        totalBurst = 0;
        totalTurnaroundTime = 0; totalWaitingTime = 0;
        avgTurnaroundTime = 0; avgWaitingTime = 0;
        completed = 0;
        previousProcess = -1;
        int timeQuantum = 0;
        int overheadTime = 0;
        int index = 0;
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
            index = readyQueue.get(0);
            readyQueue.remove(0);

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

            for(int i = 0; i < overheadTime; i++) {
                systemTime += overheadTime;
                idleTime += overheadTime;
                processGanttChart.add("xx");
                burstGanttChart.add(String.valueOf(systemTime));
            }

            for(int i = 1; i < process.length; i++) {
                // check if there is a process that has arrived and if execution time is complete
                if(process[i].getArrivalTime() <= systemTime && processBurst[i] > 0 && flag[i] == false) {
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
        if(choice == "PPRIO") {
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
            } else {
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
            else {
                System.out.format(value + "%3s", str);
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

    public void PPRIO2() {
        int n,prcs[],at[],prio[],bt[],btstore[],ct[],wt[],tat[];

        int count=0,total_Burst_Time =0, totalwaitingTime = 0, totalturnaroundtime=0;

        System.out.print("\nEnter the number of processes: ");
        n = isIntegerAndGreaterThanZero(input);

        float ave = n, awt,atat;
        int ii,iii,iiii,aa,b,c,x; //counters
        prcs = new int[n];
        at = new int [n];
        bt = new int[n];
        prio = new int[n];
        btstore = new int[n];
        ct = new int[n];
        wt = new int[n];
        tat = new int[n];

        ArrayList<String> processGanttChart = new ArrayList<>();
        ArrayList<String> burstGanttChart = new ArrayList<>();

        // this for loop asks for the user input for the arrival time, burst time and priority
        for(x=0;x<n;x++) {
            System.out.println("\nProcess ID: "+(x+1));
            System.out.print("Arrival Time: ");
            at[x] = isInteger(input);
            System.out.print("Burst Time: ");
            bt[x] = isIntegerAndGreaterThanZero(input);
            System.out.print("Priority: ");
            prio[x] = isIntegerAndGreaterThanZero(input);
            prcs[x]=x+1;
        }

        System.out.println("\n\nGANTT CHART");
        System.out.println("TIME\t\tPROCESS NO.");

        //this for loop computes for the total amount of burst time and stores the burst time in a different storage, it also sets the completion time to 0
        for (ii = 0; ii < n; ii++) {
            total_Burst_Time += bt[ii];
            btstore[ii] = bt[ii];
            ct[ii] = 0;
        }

        //this while loop computes for the completion time, as such it is based on the total amount of burst time as this determines the total amount of time taken to finish the process
        while (count <= total_Burst_Time) {
            b = 0;
            int large = 1000;
            //checks at which position is the count is or if the current burst time is already exhausted
            for (aa = 0; aa < n; aa++) {
                if (at[aa] > count||bt[aa]<=0) {
                    b++;
                }
            }
            //restarts the loop
            if (b == n) {
                count++;
                continue;
            }

            //this for loop stores the highest priority in large
            for (iii = 0; iii < n; iii++) {
                if (at[iii] > count || bt[iii] <= 0) {
                    continue;
                }
                else {
                    if (prio[iii]< large) {
                        large = prio[iii];
                    }
                }
            }

            //computes the completion time and displays each time a process executes a burst
            for (int iiiii = 0; iiiii < n; iiiii++) {
                if (large == prio[iiiii]) {
                    bt[iiiii] = bt[iiiii] - 1;
                    count++;

                    if (bt[iiiii] <= 0) {
                        ct[iiiii] = count;
                    }
                    else {
                        processGanttChart.add(String.valueOf(prcs[iiiii]));
                        burstGanttChart.add(String.valueOf(count));
                        //System.out.print(prcs[iiiii] + " ");
                    }
                }
            }
        }
        //computes the wait time and turnaround time of each process
        for (int tt = 0; tt < n; tt++) {
            wt[tt] = ct[tt] - btstore[tt] - at[tt];
            tat[tt] = ct[tt] - at[tt];
        }
        //computes for the total amount of waiting time and turnaround time
        for (int ttt = 0; ttt < n; ttt++) {
            totalwaitingTime = +totalwaitingTime + wt[ttt];
            totalturnaroundtime = totalturnaroundtime + tat[ttt];
        }
        //computes the averages
        awt = totalwaitingTime /ave;
        atat= totalturnaroundtime / ave;

        //displays the results
        System.out.println("Process\t|\tAT\t|\tBT\t|\tPriority\t|\tCT\t|\tTAT\t|\tWT");
        System.out.println("----------------------------------------------------------------");
        for(int i=0;i<n;i++) {
            System.out.println("P" + prcs[i]+ "\t\t|\t"+
                    at[i]+ "\t|\t"+
                    btstore[i]+ "\t|\t\t"+
                    prio[i]+ "\t\t|\t"+
                    ct[i]+ "\t|\t"+
                    tat[i]+ "\t|\t"+
                    wt[i]);
        }
        displayGanttChart(processGanttChart, burstGanttChart);
        displayAvgTATWT(atat, awt);
    }

    public void updateQueue(int queue[], int timer, int arrival[], int n, int maxProccessIndex){
        int zeroIndex = -1;
        for(int i = 0; i < n; i++){
            if(queue[i] == 0){
                zeroIndex = i;
                break;
            }
        }
        if(zeroIndex == -1)
            return;
        queue[zeroIndex] = maxProccessIndex + 1;
    }

    public void processArrival(int timer, int arrival[], int n, int maxProccessIndex, int queue[]){
        if(timer <= arrival[n-1]){
            boolean newArrival = false;
            for(int j = (maxProccessIndex+1); j < n; j++){
                if(arrival[j] <= timer){
                    if(maxProccessIndex < j){
                        maxProccessIndex = j;
                        newArrival = true;
                    }
                }
            }
            if(newArrival) //adds the index of the arriving process(if any)
                updateQueue(queue,timer,arrival,n, maxProccessIndex);
        }
    }

    public void sortArray(int queue[], int n){
        for(int i = 0; (i < n-1) && (queue[i+1] != 0) ; i++){
            int temp = queue[i];
            queue[i] = queue[i+1];
            queue[i+1] = temp;
        }
    }
}
