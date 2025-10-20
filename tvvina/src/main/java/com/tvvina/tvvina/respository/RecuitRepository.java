package com.tvvina.tvvina.respository;

import com.tvvina.tvvina.domain.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecuitRepository extends JpaRepository<Recruit,Integer> {
}
