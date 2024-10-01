package com.bankingmanagement.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BranchUpdateRequest {
    @NotNull
    private String id;
    private int branchId;
    private String branchAddress;
    private String branchName;
    private int branchCode;
}
