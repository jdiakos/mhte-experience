package com.iknowhow.mhte.projectsexperience.domain.repository;

import com.iknowhow.mhte.projectsexperience.domain.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByProjectId(Long projectId);
}
