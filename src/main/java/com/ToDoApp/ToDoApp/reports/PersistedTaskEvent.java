package com.ToDoApp.ToDoApp.reports;

import com.ToDoApp.ToDoApp.model.event.TaskEvent;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "task_events")
class PersistedTaskEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int taskId;
    LocalDateTime occurence;
    String name;

    public PersistedTaskEvent() {
    }

    PersistedTaskEvent(TaskEvent soure) {
        taskId =soure.getTaskId();
        name = soure.getClass().getSimpleName();
        occurence = LocalDateTime.ofInstant(soure.getOccurrence(), ZoneId.systemDefault());
    }

}
