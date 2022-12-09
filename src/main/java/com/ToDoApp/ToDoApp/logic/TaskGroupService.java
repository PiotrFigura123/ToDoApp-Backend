package com.ToDoApp.ToDoApp.logic;

import com.ToDoApp.ToDoApp.model.Project;
import com.ToDoApp.ToDoApp.model.TaskGroup;
import com.ToDoApp.ToDoApp.model.TaskGroupRepository;
import com.ToDoApp.ToDoApp.model.TaskRepository;
import com.ToDoApp.ToDoApp.model.projection.GroupReadModel;
import com.ToDoApp.ToDoApp.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;


public class TaskGroupService {
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    public TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        return createGroup(source, null);
    }

    GroupReadModel createGroup(GroupWriteModel source, Project project) {
        TaskGroup result = repository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll()
                .stream().map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(Integer groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("group has undone tasks");
        }
        TaskGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given Id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }


}

