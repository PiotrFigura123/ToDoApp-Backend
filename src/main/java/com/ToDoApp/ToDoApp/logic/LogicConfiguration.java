package com.ToDoApp.ToDoApp.logic;

import com.ToDoApp.ToDoApp.TaskConfigurationProperties;
import com.ToDoApp.ToDoApp.model.ProjectRepository;
import com.ToDoApp.ToDoApp.model.TaskGroupRepository;
import com.ToDoApp.ToDoApp.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogicConfiguration {
    @Bean
    ProjectService projectService(
            final ProjectRepository repository,
            final TaskGroupRepository taskGroupRepository,
            final TaskConfigurationProperties config,
            final TaskGroupService taskGroupService
            ){
        return new ProjectService(repository,taskGroupRepository,config, taskGroupService);
    }
    @Bean
    TaskGroupService taskGroupService(
            final TaskGroupRepository taskGroupRepository,
            final TaskRepository taskRepository
    ){
        return new TaskGroupService(taskGroupRepository,taskRepository);
        }
    }


