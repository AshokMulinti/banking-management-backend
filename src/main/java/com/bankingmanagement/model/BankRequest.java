package com.bankingmanagement.model;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    @Getter
    @Setter
    @ToString
    public class BankRequest {
        private int bankCode;
       // @NotNull
        private String bankName;
      //  @NotNull
        private String bankAddress;
}
