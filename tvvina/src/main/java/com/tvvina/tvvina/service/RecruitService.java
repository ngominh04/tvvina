package com.tvvina.tvvina.service;


import com.tvvina.tvvina.domain.Recruit;
import com.tvvina.tvvina.respository.RecuitRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecruitService {

    private final RecuitRepository recruitmentRepository;

    public RecruitService(RecuitRepository recruitmentRepository) {
        this.recruitmentRepository = recruitmentRepository;
    }

    public List<Recruit> getAll() {
        return recruitmentRepository.findAll();
    }

    public Recruit getById(Integer id) {
        return recruitmentRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài tuyển dụng với id " + id));
    }

    public Recruit save(Recruit recruitment) {
        return recruitmentRepository.save(recruitment);
    }

    public void delete(Integer id) {
        recruitmentRepository.deleteById(id);
    }
}