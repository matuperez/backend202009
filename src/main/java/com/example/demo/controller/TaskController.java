package com.example.demo.controller;

import com.example.demo.exceptions.TaskNotFoundException;
import com.example.demo.dao.TaskEntityDao;
import com.example.demo.entity.TaskEntity;
import com.example.demo.service.TaskService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    private final TaskEntityDao repository;

    TaskController(TaskEntityDao repository) {
        this.repository = repository;
    }

    @Autowired
    TaskService taskService;

    @RequestMapping("/status")
    public String index() {
        return "Service up!!!";
    }

    @RequestMapping(value = "/addtask", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody()
    public TaskEntity addNewTask(@RequestBody TaskEntity task) {
        return this.taskService.addTask(task);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<TaskEntity>> getAllUsers() {

        List<TaskEntity> tasks = taskService.getAllTasks();
        if (tasks.isEmpty()) {
            return new ResponseEntity<List<TaskEntity>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<TaskEntity>>(tasks, HttpStatus.OK);

    }

    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
    TaskEntity one(@PathVariable int id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> saveResource(@RequestBody TaskEntity task,
            @PathVariable("id") int id) {

        if(id!=task.getId())
        {
            System.out.println("Wrong ID.");
            return new ResponseEntity<TaskEntity>(HttpStatus.BAD_REQUEST);
        }
        Optional<TaskEntity> currentTask = repository.findById(id);

        if (currentTask.isEmpty()) {
            System.out.println("Task with id " + id + " not found");
            return new ResponseEntity<TaskEntity>(HttpStatus.NOT_FOUND);
        }

        repository.save(task);
        return ResponseEntity.ok("Task was updated: " + task.toString());
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int id) {
        repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        repository.deleteById(id);
        return ResponseEntity.ok("resource deleted");
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Exception> handleAllExceptions(RuntimeException ex) {
        return new ResponseEntity<Exception>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/pagedtasks")
    public Page<TaskEntity> findAllTasks(@RequestParam("page") int page,
            @RequestParam("size") int size, Pageable pageable) {
        return repository.findAll(pageable);
    }

    @GetMapping("/sortedtasks")
    public Page<TaskEntity> findAllTasksSortedById(@RequestParam("sort") String sort, Pageable pageable) {
        return repository.findAll(pageable);
    }
}
