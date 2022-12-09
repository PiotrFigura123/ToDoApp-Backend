package com.ToDoApp.ToDoApp.model.event;

import com.ToDoApp.ToDoApp.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent {
    TaskDone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
