package com.bankingmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="T_Loan")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_seq_generator")
    @SequenceGenerator(name = "bank_seq_generator", sequenceName = "loan_id_sequence", allocationSize = 1)
    @Column(name="Loan_ID")
    private int loanId;
    @Column(name="Loan_Type")
    private String loanType;
    @Column(name="Loan_Amount")
    private int loanAmount;
    @Column(name="LoanID")
    private int branchId;
    @Column(name="CustID")
    private int custId;


    @ManyToOne
    @JoinColumn(name="Branch_ID")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "Cust_ID")
    private Customer customer;
}
