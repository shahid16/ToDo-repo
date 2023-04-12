package com.infy.todo.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.infy.todo.entity.Priority;
import com.infy.todo.entity.ToDoItem;

@DataJpaTest
class ToDoDaoTest {

	@Autowired
	private ToDoRepository toDoRepository;

	@Autowired
	private TestEntityManager entityManager;
	private ToDoItem inputToDoItem;

	@BeforeEach
	void setUp() {
		inputToDoItem = ToDoItem.builder().taskName("Development").expectedDate(LocalDate.now())
				.priority(Priority.of(1)).build();

		entityManager.persist(inputToDoItem);
	}

	@Test
	public void whenFindById_thenReturnToDoItem() {
		ToDoItem toDoItem = toDoRepository.findById(inputToDoItem.getTaskId()).get();
		assertEquals(toDoItem.getTaskName(), "Development");
	}

	public void saveToDoTest() {

		toDoRepository.save(inputToDoItem);

		Assertions.assertThat(inputToDoItem.getTaskId()).isGreaterThan(0);
	}
	
	public void searchToDoTest() {
        
		String key = "Dev";
		List<ToDoItem> toDoItemList = toDoRepository.searchToDo(key);

		Assertions.assertThat(toDoItemList.get(0).getTaskName().equals("Development"));
	}

	@Test
	public void getListOfToDoTest() {

		List<ToDoItem> toDoItem = toDoRepository.findAll();

		Assertions.assertThat(toDoItem.size()).isGreaterThan(0);

	}

	@Test

	public void updateToDoTest() {

		ToDoItem toDoItem = toDoRepository.findById(inputToDoItem.getTaskId()).get();

		toDoItem.setTaskName("Development");

		ToDoItem ToDoItemUpdated = toDoRepository.save(toDoItem);

		Assertions.assertThat(ToDoItemUpdated.getTaskName()).isEqualTo("Development");

	}

	@Test

	public void deleteToDoTest() {

		ToDoItem ToDoItem = toDoRepository.findById(inputToDoItem.getTaskId()).get();

		toDoRepository.delete(ToDoItem);

		ToDoItem ToDo = null;
		Optional<ToDoItem> optionalToDo = toDoRepository.findById(inputToDoItem.getTaskId());

		if (optionalToDo.isPresent()) {
			ToDo = optionalToDo.get();
		}

		Assertions.assertThat(ToDo).isNull();
	}

}
