package com.ToDoApp.ToDoApp.reports;

import com.ToDoApp.ToDoApp.model.event.TaskDone;
import com.ToDoApp.ToDoApp.model.event.TaskUndone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
class ChangedTaskEventListener {
    private static final Logger logger = LoggerFactory.getLogger(com.ToDoApp.ToDoApp.reports.ChangedTaskEventListener.class);

    private final PersistedTaskEventRepository repository;

    ChangedTaskEventListener(final PersistedTaskEventRepository repository) {
        this.repository = repository;
    }

    @Async
    @EventListener
    public void on(TaskDone event) {
        logger.info("Got " + event);
        repository.save(new PersistedTaskEvent(event));
    }

    @Async
    @EventListener
    public void on(TaskUndone event) {
        logger.info("Got " + event);
        repository.save(new PersistedTaskEvent(event));
    }
}



