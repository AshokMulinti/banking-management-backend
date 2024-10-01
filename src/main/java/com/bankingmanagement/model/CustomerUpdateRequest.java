package com.bankingmanagement.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerUpdateRequest {
    @NotNull
    private String id;

    private int custId;
    private String custName;
    private int custPhone;
    private String custAddress;
}
