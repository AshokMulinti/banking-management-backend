package com.bankingmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "T_Branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_seq_generator")
    @SequenceGenerator(name = "bank_seq_generator", sequenceName = "branch_id_sequence", allocationSize = 1)
    @Column(name="Branch_ID")
    private int branchId;
    @Column(name="Branch_Address")
    private String branchAddress;
    @Column(name="Branch_Name")
    private String branchName;
    @Column(name="Branch_Code")
    private int branchCode;

    @ManyToOne
    @JoinColumn(name="Bank_Code")
    private Bank bank;

}
