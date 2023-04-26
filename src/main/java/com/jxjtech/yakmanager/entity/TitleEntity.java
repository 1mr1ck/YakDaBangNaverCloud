package com.jxjtech.yakmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "title")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TitleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "titleId")
    private Long titleId;
    @Column(name = "titleName")
    private String titleName;
    @Column(name = "pharmacyId")
    private Long pharmacyId;
    @Column(name = "drugRecordCnt")
    private int drugRecordCnt;
    @Column(name = "totalPrice")
    private int totalPrice;
    @Column(name = "modDate")
    @UpdateTimestamp
    private Timestamp modDate;

    public TitleEntity(Long pharmacyId, String titleName) {
        this.pharmacyId = pharmacyId;
        this.titleName = titleName;
    }

    public static TitleEntity changeName(TitleEntity titleEntity, String titleName) {
        TitleEntity title = titleEntity;
        title.setTitleName(titleName);

        return title;
    }
}
