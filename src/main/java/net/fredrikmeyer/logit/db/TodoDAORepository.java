package net.fredrikmeyer.logit.db;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoDAORepository extends JpaRepository<TodoDAO, Long> {

    @Modifying
    @Transactional
    @Query("update TodoDAO t set t.done = :newValue where t.id = :id")
    void setDone(@Param("id") Long id, @Param("newValue") boolean newValue);

    long countByDoneIsTrue();
}
