package com.jxjtech.yakmanager.service;

import com.jxjtech.yakmanager.dto.DrugPackageInfoResponseDTO;
import com.jxjtech.yakmanager.dto.DrugPriceDTO;
import com.jxjtech.yakmanager.dto.GetPackageInfoDTO;
import com.jxjtech.yakmanager.dto.NarcoticDrugRecordDTO;
import com.jxjtech.yakmanager.entity.DrugPriceEntity;
import com.jxjtech.yakmanager.entity.NarcoticDrugRecordEntity;
import com.jxjtech.yakmanager.exception.AppException;
import com.jxjtech.yakmanager.exception.ErrorCode;
import com.jxjtech.yakmanager.repository.DrugPriceRepository;
import com.jxjtech.yakmanager.repository.NarcoticDrugRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
@Slf4j
public class FileService {

    @Value("${fileDir}")
    private String fileDir;

    private final JdbcTemplate jdbcTemplate;
    private final NarcoticDrugRecordRepository narcoticDrugRecordRepository;
    private final DrugPriceRepository drugPriceRepository;


    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        String origName = file.getOriginalFilename();

        String savedPath = fileDir + origName;

        file.transferTo(new File(savedPath));

        List<NarcoticDrugRecordDTO> narcoticDrugRecordDTOS = new ArrayList<>();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        try {
            Workbook workbook = null;

            File excel = new File(savedPath);
            FileInputStream excelFile = new FileInputStream(savedPath);
            if (extension.equals("xlsx")) {
                workbook = new XSSFWorkbook(excelFile);
            } else if (extension.equals("xls")) {
                workbook = new HSSFWorkbook(excelFile);
            }
            Sheet worksheet = workbook.getSheetAt(0);

            for (int i = 4; i < worksheet.getLastRowNum() + 1; i++) {
                Row row = worksheet.getRow(i);

                NarcoticDrugRecordDTO dto = new NarcoticDrugRecordDTO();
                dto.setDrugName(row.getCell(0).getStringCellValue());
                log.info("Name : " + dto.getDrugName());
                dto.setDrugQuantity(String.valueOf(row.getCell(5).getNumericCellValue()));
                log.info("Quantity : " + dto.getDrugQuantity());

                DrugPriceDTO drugPriceDTO = packageInfo(dto.getDrugName());
                dto.setDrugCode(drugPriceDTO.getDrugCode());
                dto.setProductCode(drugPriceDTO.getProductCode());
                dto.setCheck(0);

                narcoticDrugRecordDTOS.add(dto);
            }

            log.info(narcoticDrugRecordDTOS.size() + "테스트");
            excelFile.close();
            excel.delete();
        } catch (AppException e) {
            throw new AppException(ErrorCode.NOT_CONTENT);
        }

        List<NarcoticDrugRecordEntity> result = NarcoticDrugRecordEntity.ofList(narcoticDrugRecordDTOS);
        narcoticDrugRecordRepository.saveAll(result);

        return null;
    }


    public DrugPriceDTO packageInfo(String drugName) {

        if(drugName.contains("_")) {
            String[] s = drugName.split("_");
            if(s[0].contains("(")) {
                s[0] = s[0].split("\\(")[0];
            }
            drugName = s[0] + s[1];
        }

        String[] units = {
                "밀리그램", "밀리그람", "mg", "마이크로그램", "마이크로그람", "㎍", "그램", "그람", "g"
        };

        String unit = "";
        for(int i=0; i<units.length; i++) {
            if(drugName.contains("(")) {
                drugName = drugName.split("\\(")[0];
            }
            if(drugName.contains(units[i])) {
                unit = units[i];
                break;
            }
        }

        if(!unit.equals("")) {
            drugName = drugName.split(unit)[0];
        }



        log.info(drugName);
        drugName = "%" + drugName + "%";

        DrugPriceEntity drugPriceEntity = drugPriceRepository.findByLikeDrugName(drugName)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_CONTENT)).get(0);

        DrugPriceDTO result = new DrugPriceDTO(drugPriceEntity);


        log.info(drugPriceEntity.toString());
        return result;
    }
}
