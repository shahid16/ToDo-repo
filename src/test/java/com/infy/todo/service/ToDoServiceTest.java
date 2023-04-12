package com.infy.todo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.infy.todo.Exception.ToDoItemNotFoundException;
import com.infy.todo.dao.ToDoRepository;
import com.infy.todo.entity.Priority;
import com.infy.todo.entity.ToDoItem;

@ExtendWith(value = { MockitoExtension.class })
class ToDoServiceTest {

	@Mock
	private ToDoRepository toDoRepository;

	@InjectMocks
	private ToDoServiceImpl toDoService;

	private ToDoItem toDoItem;

	@BeforeEach
	void setUp() throws Exception {

		toDoItem = ToDoItem.builder().taskName("Development").expectedDate(LocalDate.now()).priority(Priority.of(1))
				.build();

	}

	@Test
	public void givenToDoObject_whenSaveToDo_thenReturnToDoObject() {

		given(toDoRepository.save(toDoItem)).willReturn(toDoItem);
		ToDoItem savedToDoItem = toDoService.saveToDo(toDoItem);

		assertThat(savedToDoItem).isNotNull();
	}

	@Test
	public void givenToDoList_whenGetAllToDo_thenReturnToDoList() {

		ToDoItem toDoItem1 = ToDoItem.builder().taskName("Design").expectedDate(LocalDate.now())
				.priority(Priority.of(2)).build();
		given(toDoRepository.findAll()).willReturn(List.of(toDoItem, toDoItem1));
		List<ToDoItem> toDoList = toDoService.fetchToDoList();

		assertThat(toDoList).isNotNull();
		assertThat(toDoList.size()).isEqualTo(2);
	}
	
	@Test
	public void givenSearchKey_whenSearchToDo_thenReturnToDoList() {
		
		String key = "Dev";
		given(toDoRepository.searchToDo(key)).willReturn(List.of(toDoItem));
		List<ToDoItem> toDoList = toDoService.searchToDo(key);
		assertThat(toDoList).isNotNull();
		
	}

	@Test
	public void givenToDoId_whenGetToDoById_thenReturnToDoObject() throws ToDoItemNotFoundException {

		given(toDoRepository.findById(toDoItem.getTaskId())).willReturn(Optional.of(toDoItem));
		Optional<ToDoItem> savedtoDoItem = Optional.ofNullable(toDoService.fetchToDoById(toDoItem.getTaskId()));

		assertThat(savedtoDoItem).isNotNull();

	}

	@Test
	public void givenToDoObject_whenUpdateToDo_thenReturnUpdatedToDo() {

		given(toDoRepository.save(toDoItem)).willReturn(toDoItem);
		toDoItem.setTaskName("Testing");
		ToDoItem updatedtoDoItem = toDoService.updateToDoItem(toDoItem);

		assertThat(updatedtoDoItem.getTaskName()).isEqualTo("Testing");
	}

	@Test
	public void givenToDoId_whenDeleteToDo_thenNothing() {

		long toDoId = 1L;
		willDoNothing().given(toDoRepository).deleteById(toDoId);
		toDoService.deleteToDoById(toDoId);

		verify(toDoRepository, times(1)).deleteById(toDoId);
	}

}
