package com.jxjtech.yakmanager.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity(name = "serverstatus")
@Table
public class ServerStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "serverStatusId")
    private Long serverStatusId;
    private int status;
    private String message;
}
