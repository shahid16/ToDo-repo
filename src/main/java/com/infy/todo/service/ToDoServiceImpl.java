package com.infy.todo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.todo.Exception.ToDoItemNotFoundException;
import com.infy.todo.dao.ToDoRepository;
import com.infy.todo.entity.ToDoItem;

@Service
public class ToDoServiceImpl implements ToDoService {

	@Autowired
	private ToDoRepository toDoRepository;

	@Override
	public ToDoItem saveToDo(ToDoItem todoItem) {
	
		return toDoRepository.save(todoItem);
	}

	@Override
	public List<ToDoItem> fetchToDoList() {
		
		return toDoRepository.findAll();
	}

	@Override
	public ToDoItem fetchToDoById(Long toDoId) throws ToDoItemNotFoundException {
		
		Optional<ToDoItem> toDoItem = Optional.ofNullable(
				toDoRepository.findById(toDoId).orElseThrow(() -> new ToDoItemNotFoundException("ToDoItem Not Found")));

		return toDoItem.get();
	}

	@Override
	public String deleteToDoById(Long toDoId) {
		
		toDoRepository.deleteById(toDoId);
		
		return "ToDoItem deleted";

	}


	@Override
	public ToDoItem updateToDoItem(ToDoItem toDoItem) {
		
		return toDoRepository.save(toDoItem);
	}

	@Override
	public List<ToDoItem> searchToDo(String key) {
	
		List<ToDoItem> todo = toDoRepository.searchToDo(key);
        
		return todo;
		
	}

}
