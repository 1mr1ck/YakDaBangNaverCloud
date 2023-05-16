package com.jxjtech.yakmanager.dto;

import com.jxjtech.yakmanager.entity.ImgUrlEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ImgUrlDTO {

    private Long img_url_id;
    private String img_url;
    private String password;

    public static List<ImgUrlDTO> of(List<ImgUrlEntity> imgUrlEntityList) {
        List<ImgUrlDTO> result = new ArrayList<>();

        if (imgUrlEntityList.size() < 6) {
            for (ImgUrlEntity img : imgUrlEntityList) {
                ImgUrlDTO dto = new ImgUrlDTO(img);
                result.add(dto);
            }
        } else {
            for(int i = imgUrlEntityList.size()-1; i > imgUrlEntityList.size()-6; i--) {
                ImgUrlDTO dto = new ImgUrlDTO(imgUrlEntityList.get(i));
                result.add(dto);
            }
            Collections.reverse(result.subList(0, 5));

            for (ImgUrlDTO imgUrlDTO : result) {
                log.info(imgUrlDTO.getImg_url_id().toString());
            }
        }


        return result;
    }

    public ImgUrlDTO(ImgUrlEntity imgUrlEntity) {
        this.img_url_id = imgUrlEntity.getImg_url_id();
        this.img_url = imgUrlEntity.getImg_url();
    }
}
