package com.bankingmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="T_Customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_seq_generator")
    @SequenceGenerator(name = "bank_seq_generator", sequenceName = "customer_id_sequence", allocationSize = 1)
    @Column(name="Cust_ID")
    private int custId;
    @Column(name="Cust_Name")
    private String custName;
    @Column(name="Cust_Phone")
    private int custPhone;
    @Column(name="Cust_Address")
    private String custAddress;

    @OneToMany(mappedBy = "customer")
    private Set<Account> accountSet;
}
