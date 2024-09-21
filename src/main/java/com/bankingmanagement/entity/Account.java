package com.bankingmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="T_Account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_seq_generator")
    @SequenceGenerator(name = "bank_seq_generator", sequenceName = "account_id_sequence", allocationSize = 1)
    @Column(name="Account_Number")
    private int accountNo;
    @Column(name="Account_Type")
    private String accountType;
    @Column(name="Account_Balance")
    private int accountBalance;
    //@Column(name="Account_BranchID")
    //private int branchId;
    //@Column(name="Account_CustId")
//    private int custId;

    @ManyToOne
    @JoinColumn(name = "Branch_ID")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "Cust_ID")
    private Customer customer;
}
