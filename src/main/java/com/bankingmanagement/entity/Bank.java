package com.bankingmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "T_Bank")
public class Bank {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_seq_generator")
    @SequenceGenerator(name = "bank_seq_generator", sequenceName = "bank_code_sequence", allocationSize = 1)
    @Id
    @Column(name="Bank_Code")
    private int bankCode;
    @Column(name="Bank_Name")
    private String bankName;
    @Column(name = "Bank_Address")
    private String bankAddress;

    @OneToMany(mappedBy = "bank")
    private Set<Branch> branchSet;
}
