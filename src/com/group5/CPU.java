package com.group5;

public class CPU {
    private int processID = 1;
    private int arrivalTime = 0;
    private int burstTime = 0;
    private int quantumTime = 0;
    private int priority = 0;
    private int avgWaitingTime = 0;
    private int avgTurnaroundTime = 0;
    private int[] processes = {};

    public CPU() {

    }

    public CPU(int processID, int arrivalTime, int burstTime) {
        this.processID = processID;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
    }
    public CPU(int processID, int arrivalTime, int burstTime, int quantumTime, int priority, int avgWaitingTime, int avgTurnaroundTime) {
        this.processID = processID;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.quantumTime = quantumTime;
        this.priority = priority;
        this.avgWaitingTime = avgWaitingTime;
        this.avgTurnaroundTime = avgTurnaroundTime;
    }

    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getQuantumTime() {
        return quantumTime;
    }

    public void setQuantumTime(int quantumTime) {
        this.quantumTime = quantumTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getAvgWaitingTime() {
        return avgWaitingTime;
    }

    public void setAvgWaitingTime(int avgWaitingTime) {
        this.avgWaitingTime = avgWaitingTime;
    }

    public int getAvgTurnaroundTime() {
        return avgTurnaroundTime;
    }

    public void setAvgTurnaroundTime(int avgTurnaroundTime) {
        this.avgTurnaroundTime = avgTurnaroundTime;
    }

    public void SRTF(int arrivalTime, int burstTime) {

    }
}
