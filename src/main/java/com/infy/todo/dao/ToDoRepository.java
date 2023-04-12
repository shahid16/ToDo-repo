package com.infy.todo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.infy.todo.entity.ToDoItem;

@Repository
public interface ToDoRepository extends JpaRepository<ToDoItem, Long> {
	
	
	  @Query( value = "SELECT * FROM TO_DO_ITEM t WHERE " +
	  "t.TASK_NAME LIKE CONCAT('%',:key,'%')" +
	  "Or t.EXPECTED_DATE LIKE CONCAT('%',:key,'%')" +
	  "Or t.PRIORITY LIKE CONCAT('%',:key,'%')", nativeQuery = true) 
	  List<ToDoItem> searchToDo(@Param("key") String key);
	 

}
