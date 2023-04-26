package com.jxjtech.yakmanager.entity;

import com.jxjtech.yakmanager.dto.ImgUrlDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "img_url")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImgUrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_url_id")
    private Long img_url_id;
    @Column(name = "img_url")
    private String img_url;

    public ImgUrlEntity(ImgUrlDTO dto) {
        this.img_url = dto.getImg_url();
    }
}
