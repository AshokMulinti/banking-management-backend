package com.bankingmanagement.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountRequest {
    private int accountNo;
    @NotNull
    private String accountType;
    private int accountBalance;
}
