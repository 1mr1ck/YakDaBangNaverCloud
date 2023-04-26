package com.jxjtech.yakmanager.service;

import com.jxjtech.yakmanager.dto.ImgUrlDTO;
import com.jxjtech.yakmanager.entity.ImgUrlEntity;
import com.jxjtech.yakmanager.repository.RecentPharmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentPharmService {

    private final RecentPharmRepository recentPharmRepository;


    public List<ImgUrlDTO> imgUrlList() {
        List<ImgUrlEntity> imgUrlEntityList = recentPharmRepository.findAll();
        if (imgUrlEntityList.size() == 0) {
            return null;
        }
        return ImgUrlDTO.of(imgUrlEntityList);
    }

    @Transactional
    public ImgUrlDTO create(ImgUrlDTO dto) {
        ImgUrlEntity result = new ImgUrlEntity(dto);
        recentPharmRepository.save(result);

        List<ImgUrlEntity> imgUrlEntityList = recentPharmRepository.findAll();
        if(imgUrlEntityList.size() > 5) {
            recentPharmRepository.delete(imgUrlEntityList.get(0));
        }

        return dto;
    }

    @Transactional
    public boolean delete(Long img_url_id) {
        if (recentPharmRepository.findById(img_url_id).isEmpty()) {
            return false;
        }
        recentPharmRepository.deleteById(img_url_id);
        return true;
    }
}
