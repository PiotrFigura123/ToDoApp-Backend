package com.ToDoApp.ToDoApp.model.event;

import com.ToDoApp.ToDoApp.model.Task;

import java.time.Clock;

public class TaskUndone extends TaskEvent {
    TaskUndone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
