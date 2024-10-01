package com.bankingmanagement.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountUpdateRequest {
    @NotNull
    private String id;
    private int accountNo;
    private String accountType;
    private int accountBalance;
}
