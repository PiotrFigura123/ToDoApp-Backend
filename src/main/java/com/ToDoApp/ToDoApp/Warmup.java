package com.ToDoApp.ToDoApp;


import com.ToDoApp.ToDoApp.model.Task;
import com.ToDoApp.ToDoApp.model.TaskGroup;
import com.ToDoApp.ToDoApp.model.TaskGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Warmup implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(Warmup.class);

    private final TaskGroupRepository groupRepository;

    public Warmup(TaskGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        logger.info("Application wormup after context refresh");
        final String description = "";
        if (!groupRepository.existsByDescription(description)) {
            var group = new TaskGroup();
            group.setDescription(description);
            group.setTasks(Set.of(
                    new Task("ContextClosedRefresh", null, group),
                    new Task("ContextRefreshedRefresh", null, group),
                    new Task("ContextStoppedRefresh", null, group),
                    new Task("ContextStartedRefresh", null, group)
            ));
            groupRepository.save(group);
        }
    }
}
