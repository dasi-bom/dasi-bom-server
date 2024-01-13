package com.example.server.domain.folder.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.domain.folder.model.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
}
