package com.infy.todo.service;

import java.util.List;

import com.infy.todo.Exception.ToDoItemNotFoundException;
import com.infy.todo.entity.ToDoItem;

public interface ToDoService {

	ToDoItem saveToDo(ToDoItem todoItem);

	List<ToDoItem> fetchToDoList();

	ToDoItem fetchToDoById(Long toDoId) throws ToDoItemNotFoundException;

	String deleteToDoById(Long toDoId);

	ToDoItem updateToDoItem(ToDoItem toDoItem);

	List<ToDoItem> searchToDo(String key);

}
