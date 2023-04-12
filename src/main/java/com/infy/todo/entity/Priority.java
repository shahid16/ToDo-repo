package com.infy.todo.entity;

import java.util.stream.Stream;

public enum Priority {

	HIGH(1), MEDIUM(2), LOW(3);

	private int value;

	private Priority(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	
	  public static Priority of(int priority) {
	  
	  return Stream.of(Priority.values()) .filter(p -> p.getValue() == priority)
	  .findFirst() .orElseThrow(IllegalArgumentException::new);
	  
	  }
	 

}
