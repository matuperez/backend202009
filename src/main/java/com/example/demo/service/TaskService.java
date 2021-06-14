package com.example.demo.service;

import com.example.demo.dao.TaskEntityDao;
import com.example.demo.entity.TaskEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    TaskEntityDao taskEntityDao;

    public List<TaskEntity> getAllTasks() {
        return this.taskEntityDao.findAll();
    }

    public TaskEntity addTask(TaskEntity user) {
        return this.taskEntityDao.save(user);
    }

    public List<TaskEntity> sortByPriority(int priority) {
        return this.taskEntityDao.findByOrderByPriorityAsc();
    }
}
