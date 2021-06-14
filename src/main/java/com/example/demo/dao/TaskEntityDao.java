package com.example.demo.dao;

import com.example.demo.entity.TaskEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskEntityDao extends JpaRepository<TaskEntity, Integer>{
    
    List<TaskEntity> findByOrderByPriorityAsc();
    
    List<TaskEntity> findByOrderByPriorityDesc();
}
