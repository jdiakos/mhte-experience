package com.iknowhow.mhte.projectsexperience.domain.repository;

import com.iknowhow.mhte.projectsexperience.domain.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
