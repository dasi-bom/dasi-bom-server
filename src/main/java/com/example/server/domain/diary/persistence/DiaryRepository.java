package com.example.server.domain.diary.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.diary.model.Diary;
import com.example.server.domain.member.model.Member;

@Repository
@Transactional
public interface DiaryRepository extends JpaRepository<Diary, Long>, CustomDiaryRepository {
	Optional<Diary> findByIdAndAuthor(Long id, Member author);

	@Query("SELECT MAX(d.id) FROM Diary d")
	Optional<Long> getLastId();
}
