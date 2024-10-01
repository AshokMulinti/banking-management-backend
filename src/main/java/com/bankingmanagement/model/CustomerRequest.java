package com.bankingmanagement.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerRequest {
    private int custId;
    private String custName;
    private int custPhone;
    private String custAddress;
}
