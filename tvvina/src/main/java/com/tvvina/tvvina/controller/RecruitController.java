package com.tvvina.tvvina.controller;

import com.tvvina.tvvina.domain.Recruit;
import com.tvvina.tvvina.service.RecruitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/recruit")
public class RecruitController {

    private final RecruitService recruitService;

    public RecruitController(RecruitService recruitService) {
        this.recruitService = recruitService;
    }

    // 📄 Lấy tất cả bài tuyển dụng client
    @GetMapping
    public List<Recruit> getAllRecruitments() {
        List<Recruit> recruits =recruitService.getAll();
        List<Recruit> newRecruits = new ArrayList<Recruit>();
        for (Recruit recruit: recruits) {
            if (recruit.getIsDelete()==1){
                newRecruits.add(recruit);
            }
        }
        return newRecruits;
    }
    // 📄 Lấy tất cả bài tuyển dụng admin
    @GetMapping("/admin")
    public List<Recruit> getAllRecruitmentsAd() {
        return recruitService.getAll();
    }

    // 📄 Lấy 1 bài theo id
    @GetMapping("/{id}")
    public Recruit getRecruitmentById(@PathVariable Integer id) {
        return recruitService.getById(id);
    }

    // 🆕 Thêm bài tuyển dụng mới (có ảnh)
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createRecruitment(
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            @RequestParam("basicSalary") String basicSalary,
            @RequestParam("allowance") String allowance,
            @RequestParam("note") String note,
            @RequestParam("quantityTaken") Integer quantityTaken,
            @RequestParam("isDelete") Integer isDelete,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        try {
            Recruit recruit = new Recruit();
            recruit.setName(name);
            recruit.setAddress(address);
            recruit.setBasicSalary(basicSalary);
            recruit.setAllowance(allowance);
            recruit.setNote(note);
            recruit.setQuantityTaken(quantityTaken);
            recruit.setIsDelete(isDelete);
            // ✅ Nếu có file ảnh thì lưu
            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadDir = "uploads/imageFiles/";
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                recruit.setImage(fileName); // chỉ lưu tên file vào DB
            }

            return ResponseEntity.ok(recruitService.save(recruit));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi upload ảnh!");
        }
    }

    // ✏️ Cập nhật bài (cho phép đổi ảnh)
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateRecruitment(
            @PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            @RequestParam("basicSalary") String basicSalary,
            @RequestParam("allowance") String allowance,
            @RequestParam("note") String note,
            @RequestParam("quantityTaken") Integer quantityTaken,
            @RequestParam("isDelete") Integer isDelete,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        try {
            Recruit existing = recruitService.getById(id);
            existing.setName(name);
            existing.setAddress(address);
            existing.setBasicSalary(basicSalary);
            existing.setAllowance(allowance);
            existing.setNote(note);
            existing.setQuantityTaken(quantityTaken);
            existing.setIsDelete(isDelete);

            // Nếu có file ảnh mới → ghi đè
            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadDir = "uploads/imageFiles/";
                String fileName = imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                assert fileName != null;
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                existing.setImage(fileName);
            }

            return ResponseEntity.ok(recruitService.save(existing));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật bài tuyển dụng!");
        }
    }

    // ❌ Xóa bài
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecruitment(@PathVariable Integer id) {
        recruitService.delete(id);
        return ResponseEntity.ok("Đã xóa bài tuyển dụng id = " + id);
    }
}
