package com.example.demo.model;

public class DashboardSummary {
    private String financialYear;
    private String month;
    private double totalDonation;
    private double totalExpense;
    private double balance;

    public double getTotalDonation() {
        return totalDonation;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public double getBalance() {
        return balance;
    }
}