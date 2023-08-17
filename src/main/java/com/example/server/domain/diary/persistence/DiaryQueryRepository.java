package com.example.server.domain.diary.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.diary.model.Diary;

@Repository
@Transactional(readOnly = true)
public interface DiaryQueryRepository extends JpaRepository<Diary, Long> {
}
