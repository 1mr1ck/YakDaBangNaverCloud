package com.jxjtech.yakmanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jxjtech.yakmanager.dto.*;
import com.jxjtech.yakmanager.entity.*;
import com.jxjtech.yakmanager.exception.AppException;
import com.jxjtech.yakmanager.exception.ErrorCode;
import com.jxjtech.yakmanager.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryService {
    private final NarcoticDrugRecordRepository narcoticDrugRecordRepository;
    private final MemberRepository memberRepository;

    private final PharmacyRepository pharmacyRepository;
    private final PharmacyMemberRepository pharmacyMemberRepository;
    private final TitleRepository titleRepository;
    private final InvitationRepository invitationRepository;
    private final DrugRecordRepository drugRecordRepository;
    private final DrugPriceRepository drugPriceRepository;
    private final DrugPackageRepository drugPackageRepository;
    private static final long INVITATION_CODE_EXPIRE_TIME = 1000 * 60L;


    private void sheetSet(Workbook workbook, Long titleId) {
        if (titleRepository.findById(titleId).isEmpty()) {
            return;
        }

        Sheet sheet = workbook.createSheet(titleRepository.findById(titleId).get().getTitleName());
        sheet.setColumnWidth(1, 10000);

        // 헤더 셋팅 ( 컬럼명 )
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("등록일자");
        headerRow.createCell(1).setCellValue("제품명");
        headerRow.createCell(2).setCellValue("수량");
        headerRow.createCell(3).setCellValue("단가");
        headerRow.createCell(4).setCellValue("합계금액");

        // 데이터 셋팅
        int rowNum = 1;
        List<DrugRecordEntity> dataList = drugRecordRepository.findAllByTitleId(titleId);
        for (DrugRecordEntity data : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getRegDate().toString());
            row.createCell(1).setCellValue(data.getDrugName());
            row.createCell(2).setCellValue(data.getDrugTotalQuantity());
            row.createCell(3).setCellValue(data.getDrugPrice());
            row.createCell(4).setCellValue(data.getDrugTotalPrice());
        }

    }

    private void mailSend(String pharmacyName) throws MessagingException, UnsupportedEncodingException {
        MemberEntity memberEntity = memberRepository.findById(getMemberId()).orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
        String memberEmail = memberEntity.getMemberEmail();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("jnjtech@jxjtech.co.kr", "eobnjxnmvccstwpc");
            }
        });
        // Create a new message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("jnjtech@jxjtech.co.kr", "약매니저"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(memberEmail));
        message.setSubject("<약매니저> 재고관리 엑셀 출력본입니다.");

        // Create a Multipart object to contain the message body and the attachment
        Multipart multipart = new MimeMultipart();

        // Create a BodyPart object to contain the message body
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setText("약매니저를 사용해 주셔서 감사합니다.");

        // Add the message body to the Multipart object
        multipart.addBodyPart(bodyPart);

        // Create a BodyPart object to contain the attachment
        DataSource dataSource = new FileDataSource(pharmacyName + ".xlsx");
        String fileName = pharmacyName + ".xlsx";
        String encodedFileName = MimeUtility.encodeWord(fileName, "UTF-8", "B");
        BodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.setDataHandler(new DataHandler(dataSource));
        attachmentPart.setFileName(encodedFileName);

        // Add the attachment to the Multipart object
        multipart.addBodyPart(attachmentPart);

        // Add the Multipart object to the message
        message.setContent(multipart);

        // Send the message
        Transport.send(message);
    }

    private Long getMemberId() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    /**
     * 데이터 수정
     */
    @Transactional
    public boolean DBUpdate() throws JsonProcessingException {
        DrugPackageInDrugRecordDTO dto = new DrugPackageInDrugRecordDTO();

        List<DrugRecordEntity> drugRecordEntities = drugRecordRepository.findAll();
        for (DrugRecordEntity entity : drugRecordEntities) {
            dto = dto.modifyPackage(entity);
            String savePackageInfo = DrugPackageInDrugRecordDTO.saveDrugPackage(dto);
            entity.setDrugPackage(savePackageInfo);
            drugRecordRepository.save(entity);
        }
        return true;
    }

    /**
     * 약기록상세정보
     */
    public GetDrugRecordDTO getDrugRecord(Long drugId) throws JsonProcessingException {
        DrugRecordEntity entity = drugRecordRepository.findById(drugId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA));

        Long titleId = entity.getTitleId();
        Long pharmacyId = titleRepository.findById(titleId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA)).getPharmacyId();
        Long memberId = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA)).getMemberId();
        if (!memberId.equals(getMemberId())) {
            throw new AppException(ErrorCode.INVALID_JWT_TOKEN);
        }

        return new GetDrugRecordDTO(entity);
    }

    /**
     * 약기록 수정
     */
    @Transactional
    public boolean modifyDrugRecord(Long drugId, DrugRecordRequestDTO drugRecordRequestDTO) throws JsonProcessingException {
        DrugRecordEntity entity = drugRecordRepository.findById(drugId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA));

        Long titleId = entity.getTitleId();
        Long pharmacyId = titleRepository.findById(titleId).orElseThrow(() -> new RuntimeException("error")).getPharmacyId();
        pharmacyMemberRepository.findByPharmacyId(pharmacyId, getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_BELONG_TO_PHARMACY));

        entity = DrugRecordEntity.modify(entity, drugRecordRequestDTO);
        drugRecordRepository.save(entity);

        return true;
    }

    /**
     * 약기록 삭제
     */
    @Transactional
    public boolean deleteDrugRecord(Long drugId) {
        if (drugRecordRepository.findById(drugId).isEmpty()) {
            return false;
        }

        DrugRecordEntity entity = drugRecordRepository.findById(drugId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA));

        Long titleId = entity.getTitleId();
        Long pharmacyId = titleRepository.findById(titleId).orElseThrow(() -> new RuntimeException("error")).getPharmacyId();
        pharmacyMemberRepository.findByPharmacyId(pharmacyId, getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_BELONG_TO_PHARMACY));

        drugRecordRepository.deleteById(drugId);
        return true;
    }


    /**
     * 의약품 포장정보
     */
    public DrugPackageInfoResponseDTO getPackageInfo(DrugPackageInfoRequestDTO drugPackageInfoRequestDTO) {
        DrugPackageInfoResponseDTO result = new DrugPackageInfoResponseDTO();
        Integer drugCode = drugPackageInfoRequestDTO.getDrugCode();
        Integer productCode = drugPackageInfoRequestDTO.getProductCode();

        DrugPriceEntity drugPriceEntity = new DrugPriceEntity();
        if(productCode != null) {
            drugPriceEntity = drugPriceRepository.findByProductCode(productCode)
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA));
        } else {
            drugPriceEntity = drugPriceRepository.findByDrugCode(drugCode)
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA));
        }

        String drugUnit = drugPriceEntity.getDrugUnit();
        // 1. drugCode is not null, productCode is not null
        if(drugCode != null && productCode != null) {
            List<DrugPackageEntity> drugPackageEntities = drugPackageRepository.findAllByDrugCode(drugCode);
            // 1-1. drugUnit equals 캡슐 || 정
            if(drugUnit.equals("캡슐") || drugUnit.equals("정")) {
                log.info("둘다 not null이고 캡슐 or 정");
                result = DrugPackageInfoResponseDTO.ofDrugCodeAndProductCodeNotNull(drugPackageEntities, drugPriceEntity);
            } else {
                // 1-2. 캡슐이나 정이 아닐경우
                result = DrugPackageInfoResponseDTO.ofNotCapsuleAndNotPill(drugPriceEntity);
            }
        } else if(productCode != null) {
            // 2. drugCode is null, productCode is not null
            result = DrugPackageInfoResponseDTO.ofOnlyProductCode(drugPriceEntity);
        } else if(drugCode != null) {
            // 3. drugCode is not null, productCode is null
            List<DrugPackageEntity> drugPackageEntities = drugPackageRepository.findAllByDrugCode(drugCode);
            result = DrugPackageInfoResponseDTO.ofOnlyDrugCode(drugPackageEntities, drugPriceEntity);
        }


        return result;
    }

    /**
     * 재고관리 약검색
     */
    public List<DrugSearchDTO> drugSearCh(String drugName) {
        if (drugName.length() < 2) {
            return null;
        }

        drugName += "%";
        List<DrugPriceEntity> DrugList = drugPriceRepository.findAllByDrugName(drugName);

        return DrugSearchDTO.getList(DrugList);
    }

    /**
     * 약국리스트
     */
    public List<PharmacyTitleDTO> pharmacyList() {
        Long memberId = getMemberId();

        List<PharmacyMemberEntity> pharmacyMemberEntityList = pharmacyMemberRepository.findByMemberId(memberId);
        List<PharmacyTitleDTO> resList = new ArrayList<>();
        for (PharmacyMemberEntity pm : pharmacyMemberEntityList) {
            Long pharmacyId = pm.getPharmacyId();
            Authority role = pm.getPharmacyMemberRole(); // pharmacyMemberRole
            if (pharmacyRepository.findById(pharmacyId).isEmpty()) {
                throw new AppException(ErrorCode.NOT_EXIST_DATA);
            }

            PharmacyEntity pharmacyEntity = pharmacyRepository.findById(pharmacyId).get();
            List<TitleEntity> titleEntityList = titleRepository.findAllByPharmacyId(pharmacyId);

            PharmacyTitleDTO result = new PharmacyTitleDTO(pharmacyEntity, role, titleEntityList);
            resList.add(result);
        }

        return resList;
    }

    /**
     * 약국생성
     */
    public PharmacyDTO createPharmacy(PharmacyRequestDTO pharmacyRequestDTO) {
        if (pharmacyRequestDTO.getPharmacyName().length() < 1) {
            throw new AppException(ErrorCode.NOT_CONTENT);
        }

        Long memberId = getMemberId();

        PharmacyEntity result = new PharmacyEntity(memberId, pharmacyRequestDTO.getPharmacyName());
        pharmacyRepository.save(result);

        result = pharmacyRepository.findByMemberIdMax(memberId);

        PharmacyMemberEntity pharmacyMemberEntity = new PharmacyMemberEntity(result);
        pharmacyMemberRepository.save(pharmacyMemberEntity);

        return new PharmacyDTO(result);
    }

    /**
     * 약국 수정
     */
    @Transactional
    public PharmacyDTO updatePharmacy(PharmacyRequestDTO pharmacyRequestDTO, Long pharmacyId) throws Exception {
        if (pharmacyRequestDTO.getPharmacyName().length() < 1) {
            throw new AppException(ErrorCode.NOT_CONTENT);
        }

        PharmacyEntity pharmacyEntity = pharmacyRepository.findById(pharmacyId).orElseThrow(() -> new Exception("존재하지 않는 약국입니다."));

        if (!getMemberId().equals(pharmacyEntity.getMemberId())) {
            throw new AppException(ErrorCode.NOT_ADMIN);
        }
        pharmacyEntity.setPharmacyName(pharmacyRequestDTO.getPharmacyName());

        return new PharmacyDTO(pharmacyEntity);
    }

    /**
     * 약국 삭제
     */
    @Transactional
    public boolean deletePharmacy(Long pharmacyId) throws Exception {
        PharmacyEntity pharmacyEntity = pharmacyRepository.findById(pharmacyId).orElseThrow(() -> new Exception("약국이 존재하지 않습니다."));

        if (!getMemberId().equals(pharmacyEntity.getMemberId())) {
            throw new AppException(ErrorCode.NOT_BELONG_TO_PHARMACY);
        }

        pharmacyRepository.deleteById(pharmacyId);
        return true;
    }

    /**
     * 약국엑셀추출
     */
    public boolean exportPharmacy(Long pharmacyId) throws MessagingException, UnsupportedEncodingException {
        if (!pharmacyRepository.existsById(pharmacyId)) {
            return false;
        }

        pharmacyMemberRepository.findByPharmacyId(pharmacyId, getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_BELONG_TO_PHARMACY));

        String pharmacyName = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA)).getPharmacyName();

        List<Long> titleIdList = titleRepository.findAllTitleIdByPharmacyId(pharmacyId);

        if (titleIdList.size() > 0) {
            Workbook workbook = new XSSFWorkbook();
            for (Long titleId : titleIdList) {
                if (titleRepository.findById(titleId).isEmpty()) {
                    return false;
                }
                sheetSet(workbook, titleId);
            }

            try {
                FileOutputStream outputStream = new FileOutputStream(pharmacyName + ".xlsx");
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mailSend(pharmacyName);

            return true;
        }
        return false;
    }

    /**
     * 초대코드 생성
     */
    @Transactional
    public InvitationDTO createInvitationCode(Long pharmacyId) {
        pharmacyMemberRepository.findByPharmacyId(pharmacyId, getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_BELONG_TO_PHARMACY));

        Random rNum = new Random();
        StringBuilder invitationCode = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            String rStr = Integer.toString(rNum.nextInt(10));
            invitationCode.append(rStr);
        }

        Long regDate = new Date().getTime();
        Long invitationCodeTime = new Date().getTime() + INVITATION_CODE_EXPIRE_TIME;

        InvitationDTO result = new InvitationDTO(pharmacyId, invitationCode.toString(), regDate, invitationCodeTime);
        Optional<InvitationEntity> optInvitationEntity = invitationRepository.findByPharmacyId(pharmacyId);

        optInvitationEntity.ifPresent(invitationRepository::delete);
        InvitationEntity invitationEntity = new InvitationEntity(result);
        invitationRepository.save(invitationEntity);

        // 시간은 60초
        return result;
    }

    /**
     * 약국멤버 조회
     */
    public List<PharmacyMemberDTO> pharmacyMemberList(Long pharmacyId) {
        pharmacyMemberRepository.findByPharmacyId(pharmacyId, getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_BELONG_TO_PHARMACY));

        List<PharmacyMemberEntity> pharmacyMemberEntityList = pharmacyMemberRepository.findAllByPharmacyId(pharmacyId);

        if (pharmacyMemberEntityList.size() > 0) {
            List<PharmacyMemberDTO> result = new ArrayList<>();

            for (PharmacyMemberEntity pm : pharmacyMemberEntityList) {
                PharmacyMemberDTO dto = new PharmacyMemberDTO(pm);
                result.add(dto);
            }

            return result;
        }

        return null;
    }

    /**
     * 약국멤버탈퇴
     */
    @Transactional
    public boolean deletePharmacyMember(Long pharmacyId) {
        PharmacyMemberEntity pharmacyMemberEntity = pharmacyMemberRepository.findByPharmacyId(pharmacyId, getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA));

        pharmacyMemberRepository.deleteById(pharmacyMemberEntity.getPharmacyMemberId());

        return true;
    }

    /**
     * 타이틀리스트
     */
    public List<TitleDTO> titleList(Long pharmacyId) {
        pharmacyMemberRepository.findByPharmacyId(pharmacyId, getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_BELONG_TO_PHARMACY));

        List<TitleEntity> titleEntityList = titleRepository.findAllByPharmacyId(pharmacyId);

        return TitleDTO.getDTOList(titleEntityList);
    }

    /**
     * 타이틀 생성
     */
    public TitleDTO createTitle(Long pharmacyId, TitleRequestDTO titleRequestDTO) {
        Long memberId = getMemberId();

        // 약국이 존재하는지 체크
        if (pharmacyRepository.findById(pharmacyId).isEmpty()) {
            throw new AppException(ErrorCode.NOT_EXIST_DATA);
        } else if (pharmacyMemberRepository.findByPharmacyId(pharmacyId, memberId).isEmpty()) {
            throw new AppException(ErrorCode.NOT_BELONG_TO_PHARMACY);
        }

        TitleEntity title = new TitleEntity(pharmacyId, titleRequestDTO.getTitleName());
        titleRepository.save(title);

        return new TitleDTO(title);
    }

    /**
     * 타이틀 전체삭제
     */
    @Transactional
    public boolean deleteTitleAll(Long pharmacyId) {
        pharmacyMemberRepository.findByPharmacyId(pharmacyId, getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_BELONG_TO_PHARMACY));

        Authority pharmacyMemberRole = pharmacyMemberRepository.findByPharmacyId(pharmacyId, getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA)).getPharmacyMemberRole();

        if (!pharmacyMemberRole.toString().equals("admin")) {
            throw new AppException(ErrorCode.NOT_ADMIN);
        }

        titleRepository.deleteAllByPharmacyId(pharmacyId);
        return true;
    }

    /**
     * 약국 가입
     */
    public String joinPharmacy(String invitationCode) {
        Long memberId = getMemberId();

        InvitationEntity invitationEntity = invitationRepository.findByInvitationCode(invitationCode)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA));

        if (!invitationCode.equals(invitationEntity.getInvitationCode())) {
            return "초대코드가 일치하지 않습니다.";
        } else if (new Date().getTime() > invitationEntity.getRegDate() + 1000 * 60) {
            return "초대코드가 만료되었습니다.";
        } else if (pharmacyMemberRepository.findByPharmacyId(
                invitationEntity.getPharmacyId(), memberId).isPresent()) {
            return "이미 가입된 멤버입니다.";
        }

        PharmacyMemberEntity pharmacyMemberEntity = new PharmacyMemberEntity(memberId, invitationEntity.getPharmacyId());
        pharmacyMemberRepository.save(pharmacyMemberEntity);

        return "약국 가입완료";
    }

    /**
     * 약국 멤버추방
     */
    // 약국 멤버 추방
    @Transactional
    public boolean expel(Long pharmacyMemberId) {
        PharmacyMemberEntity pharmacyMember = pharmacyMemberRepository.findById(pharmacyMemberId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_BELONG_TO_PHARMACY));

        pharmacyMemberRepository.delete(pharmacyMember);

        return true;
    }

    /**
     * 타이틀 수정
     */
    @Transactional
    public TitleDTO updateTitle(Long titleId, TitleRequestDTO dto) {
        TitleEntity titleEntity = titleRepository.findById(titleId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA));

        pharmacyMemberRepository.findByPharmacyId(titleEntity.getPharmacyId(), getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_BELONG_TO_PHARMACY));

        titleEntity = TitleEntity.changeName(titleEntity, dto.getTitleName());

        return new TitleDTO(titleEntity, dto.getTitleName());
    }

    /**
     * 타이틀 삭제
     */
    @Transactional
    public boolean deleteTitle(Long titleId) {
        TitleEntity titleEntity = titleRepository.findById(titleId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA));

        pharmacyMemberRepository.findByPharmacyId(titleEntity.getPharmacyId(), getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_BELONG_TO_PHARMACY));

        titleRepository.delete(titleEntity);
        return true;
    }

    /**
     * 타이틀 약기록 리스트
     */
    public List<GetDrugRecordDTO> getDrugList(Long titleId) throws JsonProcessingException {
        Long pharmacyId = titleRepository.findById(titleId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA)).getPharmacyId();

        pharmacyMemberRepository.findByPharmacyId(pharmacyId, getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_BELONG_TO_PHARMACY));

        List<DrugRecordEntity> drugRecordEntities = drugRecordRepository.findAllByTitleId(titleId);
        if (drugRecordEntities.size() == 0) {
            return null;
        }

        List<GetDrugRecordDTO> result = GetDrugRecordDTO.toDTOList(drugRecordEntities);
        for (GetDrugRecordDTO dto : result) {
            List<DrugPriceEntity> priceEntities = drugPriceRepository.findAllByDrugCode(dto.getDrugCode());
            DrugPriceEntity priceEntity = priceEntities.get(0);
//            log.info(priceEntity.getDrugUnit() + " " + dto.getDrugPrice() + " " + priceEntity.getDrugPrice());

            if(priceEntity == null || priceEntity.getDrugPrice() == null) {
                continue;
            }
            int beforePrice = dto.getDrugPrice();
            int nowPrice = Integer.parseInt(priceEntity.getDrugPrice());
            if (beforePrice != nowPrice && (priceEntity.getDrugUnit().equals("정") || priceEntity.getDrugUnit().equals("캡슐"))) {
                log.info("가격변동 발생");
                Integer priceProfit = Integer.parseInt(priceEntity.getDrugPrice()) * dto.getDrugTotalQuantity() - dto.getDrugTotalPrice();
                dto.setNowDrugPrice(Integer.parseInt(priceEntity.getDrugPrice()));
                dto.setPriceProfit(priceProfit);
            }
        }

        return result;
    }

    /**
     * 재고관리 약기록 생성
     */
    public boolean createDrugRecord(Long titleId, DrugRecordRequestDTO drugRecordRequestDTO) throws JsonProcessingException {
        Long pharmacyId = titleRepository.findById(titleId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA)).getPharmacyId();

        pharmacyMemberRepository.findByPharmacyId(pharmacyId, getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_BELONG_TO_PHARMACY));

        DrugRecordDTO dto = new DrugRecordDTO(titleId, drugRecordRequestDTO);
        DrugRecordEntity result = new DrugRecordEntity(dto);
        drugRecordRepository.save(result);
        return true;
    }

    /**
     * 중복 의약품 체크
     */
    public boolean duplDrugRecord(Long titleId, int drugCode) {
        return drugRecordRepository.findByTitleIdAndDrugCode(titleId, drugCode).isEmpty();
    }

    /**
     * 타이틀 엑셀 추출
     */
    public boolean exportTitle(Long titleId) throws MessagingException, UnsupportedEncodingException {
        if (!titleRepository.existsById(titleId)) {
            return false;
        }

        Long pharmacyId = titleRepository.findById(titleId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA)).getPharmacyId();
        pharmacyMemberRepository.findByPharmacyId(pharmacyId, getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_BELONG_TO_PHARMACY));

        String pharmacyName = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_DATA)).getPharmacyName();

        List<DrugRecordEntity> dataList = drugRecordRepository.findAllByTitleId(titleId);

        if (dataList.size() > 0) {
            Workbook workbook = new XSSFWorkbook();
            sheetSet(workbook, titleId);

            try {
                FileOutputStream outputStream = new FileOutputStream(pharmacyName + ".xlsx");
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mailSend(pharmacyName);

            return true;
        }
        return false;
    }

    public List<NarcoticDrugRecordDTO> getNarcoticDrugRecord() {
        List<NarcoticDrugRecordDTO> result = new ArrayList<>();

        List<NarcoticDrugRecordEntity> drugRecordEntities = narcoticDrugRecordRepository.findAll();

        result = drugRecordEntities.stream().map(NarcoticDrugRecordEntity::of).collect(Collectors.toList());


        return result;
    }
}
