package com.bankingmanagement.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoanUpdateRequest {
    @NotNull
    private String id;
    private int loanId;
    private String loanType;
    private int loanAmount;
    private int branchId;
    private int custId;
}
