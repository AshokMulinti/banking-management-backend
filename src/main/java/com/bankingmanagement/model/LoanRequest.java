package com.bankingmanagement.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoanRequest {
    private int loanId;
    private String loanType;
    private int loanAmount;
    private int branchId;
    private int custId;
}
