package com.infy.todo.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.infy.todo.Exception.ToDoItemNotFoundException;
import com.infy.todo.entity.Priority;
import com.infy.todo.entity.ToDoItem;
import com.infy.todo.service.ToDoService;

@RestController
public class ToDoController {

	@Autowired
	ToDoService todoService;
	private static ObjectMapper mapper = new ObjectMapper();

	private final Logger logger = LoggerFactory.getLogger(ToDoController.class);

	// add todo
	@PostMapping("/newToDoItem/")
	public ResponseEntity<ToDoItem> saveToDo(@RequestBody ToDoItem todoItem) {

		logger.info("Inside saveToDo of EmployeeController");
		ToDoItem toDoItem = todoService.saveToDo(todoItem);
		return new ResponseEntity<ToDoItem>(toDoItem, HttpStatus.CREATED);
	}

	// get all todo's
	@GetMapping("/allToDo/")
	public List<ToDoItem> fetchToDoList() {
        
		
		return todoService.fetchToDoList();

	}

	// get todo item by id
	@GetMapping("/ToDo/{id}")
	public ResponseEntity<ToDoItem> fetchToDoById(@PathVariable("id") final Long toDoId) throws ToDoItemNotFoundException, JsonProcessingException {
		
		 LocalDate date = LocalDate.now();
		  ToDoItem toDoItemnew = new ToDoItem();
		  toDoItemnew.setTaskName("Development");
		  toDoItemnew.setPriority(1);
			toDoItemnew.setExpectedDate(date);
		 
		System.out.println(date);
		
		mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
		String json = mapper.writeValueAsString(toDoItemnew);System.out.println(json);
		ToDoItem updatedtoDoITem = ToDoItem.builder().taskId(1L).taskName("Development").expectedDate(date)
				.priority(Priority.of(1)).build();
		System.out.println(updatedtoDoITem.getTaskName());
		ToDoItem toDoItem = todoService.fetchToDoById(toDoId);
		  return new ResponseEntity<ToDoItem>(toDoItem, HttpStatus.OK);

	}

	@GetMapping("/search")
	public ResponseEntity<List<ToDoItem>> searchToDo(@RequestParam("key") String key) {
		 
		List<ToDoItem> toDoList = todoService.searchToDo(key);
		return new ResponseEntity<List<ToDoItem>>(toDoList,HttpStatus.OK);
	}

	@PutMapping("/ToDo/{toDoId}")
	public ResponseEntity<ToDoItem> updateToDoItem(@PathVariable("toDoId") Long toDoId, @RequestBody ToDoItem toDoItem) throws ToDoItemNotFoundException {
		
		ToDoItem savedtoDoItem = todoService.fetchToDoById(toDoId);
		
		savedtoDoItem.setTaskName(toDoItem.getTaskName());
		savedtoDoItem.setExpectedDate(toDoItem.getExpectedDate());
		savedtoDoItem.setPriority(toDoItem.getPriority().getValue());
		
		ToDoItem updatedtoDoItem = todoService.updateToDoItem(savedtoDoItem);
		
		return new ResponseEntity<ToDoItem>(updatedtoDoItem, HttpStatus.OK);
	}
	
	

	@DeleteMapping("/ToDo/{id}")
	public String deleteToDoById(@PathVariable("id") Long toDoId) {

		
		return todoService.deleteToDoById(toDoId);
	}

}
