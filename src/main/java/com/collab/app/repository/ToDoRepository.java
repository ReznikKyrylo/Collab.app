package com.collab.app.repository;

import com.collab.app.model.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {

    @Query(value = "select id, title, created_at, owner_id from todos where owner_id = ?1 union " +
            "select id, title, created_at, owner_id from todos inner join todo_collaborator on id = todo_id and " +
            "collaborator_id = ?1", nativeQuery = true)
    List<ToDo> getByUserId(long userId);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM todo_collaborator where collaborator_id=:collaborator_id and todo_id=:todo_id",nativeQuery = true)
    void deleteCollaborator(@Param("collaborator_id") long collaborator_id,@Param("todo_id") long todo_id);
}
