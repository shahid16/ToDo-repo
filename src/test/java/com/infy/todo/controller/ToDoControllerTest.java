package com.infy.todo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.infy.todo.entity.Priority;
import com.infy.todo.entity.ToDoItem;
import com.infy.todo.service.ToDoService;

@WebMvcTest(ToDoController.class)
class ToDoControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ToDoService toDoService;
	private ToDoItem toDoItem;
	private List<ToDoItem> toDoItemList = new ArrayList<>();
	String ToDoJson = "{\"taskName\":\"Design\",\"expectedDate\":\"08-04-2023\",\"priority\":\"1\"}";

	@BeforeEach
	void setUp() {
		toDoItem = ToDoItem.builder().taskId(1L).taskName("Design").expectedDate(LocalDate.now())
				.priority(Priority.of(1)).build();
	}

	@Test
	public void testSaveToDo() throws Exception {

		Mockito.when(toDoService.saveToDo(Mockito.any(ToDoItem.class))).thenReturn(toDoItem);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/newToDoItem/").accept(MediaType.APPLICATION_JSON)
				.content(ToDoJson).contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder).andExpect(status().isCreated())
				.andExpect(jsonPath("$.taskName").value("Design"));
	}

	@Test
	void testFetchToDoList() throws Exception {

		toDoItemList.add(toDoItem);
		
		Mockito.when(toDoService.fetchToDoList()).thenReturn(toDoItemList);

		mockMvc.perform(get("/allToDo/")).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andExpect(jsonPath("$[0].taskName", Matchers.equalTo("Design")));

	}

	@Test
	void testFetchToDoById() throws Exception {

		Mockito.when(toDoService.fetchToDoById(1L)).thenReturn(toDoItem);

		mockMvc.perform(get("/ToDo/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.taskId").value(toDoItem.getTaskId()))
				.andExpect(jsonPath("$.taskName").value(toDoItem.getTaskName()));
	}

	@Test
	void testSearchToDo() throws Exception {
		String key = "Doc";
		ToDoItem searchtoDoITem = ToDoItem.builder().taskId(1L).taskName("Documentation").expectedDate(LocalDate.now())
				.priority(Priority.of(1)).build();
		toDoItemList.add(searchtoDoITem);
		
		Mockito.when(toDoService.searchToDo(key)).thenReturn(toDoItemList);
		
		mockMvc.perform(get("/search").param("key", key)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].taskName").value("Documentation"));

	}

	@Test
	void testUpdateToDoItem() throws Exception {
		long toDoId = 1L;

		ToDoItem updatedtoDoITem = ToDoItem.builder().taskId(1L).taskName("Development").expectedDate(LocalDate.now())
				.priority(Priority.of(1)).build();

		Mockito.when(toDoService.fetchToDoById(toDoId)).thenReturn(toDoItem);
		Mockito.when(toDoService.updateToDoItem(Mockito.any(ToDoItem.class))).thenReturn(updatedtoDoITem);

		String updatedToDoJson = "{\"taskName\":\"Development\",\"expectedDate\":\"11-04-2023\",\"priority\":\"1\"}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/ToDo/1").accept(MediaType.APPLICATION_JSON)
				.content(updatedToDoJson).contentType(MediaType.APPLICATION_JSON);

		updatedtoDoITem.getExpectedDate();
		toDoItem.getPriority();
		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.taskId").value(toDoItem.getTaskId()))
				.andExpect(jsonPath("$.taskName").value(toDoItem.getTaskName()))
				.andExpect(jsonPath("$.expectedDate").value("11-04-2023"))
				.andExpect(jsonPath("$.priority").value("HIGH"));

	}

	@Test
	void testDeleteToDoById() throws Exception {

		Mockito.when(toDoService.deleteToDoById(1L)).thenReturn("ToDoItem deleted");

		MvcResult requestResult = mockMvc.perform(delete("/ToDo/1")).andExpect(status().isOk())
				.andExpect(status().isOk()).andReturn();

		String result = requestResult.getResponse().getContentAsString();
		assertEquals(result, "ToDoItem deleted");
	}

}
