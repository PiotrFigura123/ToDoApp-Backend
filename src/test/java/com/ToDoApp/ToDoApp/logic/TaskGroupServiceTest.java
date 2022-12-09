package com.ToDoApp.ToDoApp.logic;

import com.ToDoApp.ToDoApp.model.TaskGroup;
import com.ToDoApp.ToDoApp.model.TaskGroupRepository;
import com.ToDoApp.ToDoApp.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {
    @Test
    @DisplayName("should throw when undone tasks")
    void toggleGroup_undoneTasks_throwIllegalStateException(){
        //given
        TaskRepository mockTaskRepository = taskRepositoryReterning(true);
        //SUT
        var toTest = new TaskGroupService(null,mockTaskRepository);

        //when
        var excepton = catchThrowable(()->toTest.toggleGroup(1));

        //then
        assertThat(excepton)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("has undone tasks");
    }



    @Test
    @DisplayName("should throw when no group")
    void toggleGroup_wrongId_throwsIllegalArgumentException(){
        //given
        TaskRepository mockTaskRepository = taskRepositoryReterning(false);
        //and
        var mockRepository = mock(TaskGroupRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //SUT
        var toTest = new TaskGroupService(mockRepository, mockTaskRepository);
        //when
        var exception = catchThrowable(()->toTest.toggleGroup(1));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Id not found");

    }

    @Test
    @DisplayName("should toggle group")
    void toggleGroup_workAsExpected(){
        //given
        TaskRepository mockTaskRepository = taskRepositoryReterning(false);
        //and
        var group = new TaskGroup();
        var beforeToggle = group.isDone();
        //and
        var mockRepository = mock(TaskGroupRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(group));
        //SUT
        var toTest = new TaskGroupService(mockRepository, mockTaskRepository);
        //when
        toTest.toggleGroup(0);

        //then
        assertThat(group.isDone()).isEqualTo(!beforeToggle);

    }

    private static TaskRepository taskRepositoryReterning(boolean result) {
        TaskRepository mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
        return mockTaskRepository;
    }

}