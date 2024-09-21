package com.bankingmanagement.model;

public record LoanTO(String id, int loanId, String loanType, int loanAmount, int branchId, int custId) {
}
