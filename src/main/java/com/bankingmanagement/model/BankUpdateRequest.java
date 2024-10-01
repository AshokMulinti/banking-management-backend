package com.bankingmanagement.model;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class BankUpdateRequest {
    @NotNull
    private String id;
    private int bankCode;
    private String bankName;
    private String bankAddress;

}
