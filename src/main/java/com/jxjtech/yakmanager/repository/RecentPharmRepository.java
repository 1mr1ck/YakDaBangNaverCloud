package com.jxjtech.yakmanager.repository;

import com.jxjtech.yakmanager.entity.ImgUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentPharmRepository extends JpaRepository<ImgUrlEntity, Long> {


}
