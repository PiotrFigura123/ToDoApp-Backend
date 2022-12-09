package com.ToDoApp.ToDoApp.logic;

import com.ToDoApp.ToDoApp.TaskConfigurationProperties;
import com.ToDoApp.ToDoApp.model.*;
import com.ToDoApp.ToDoApp.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configurated to allow just 1 group and other indone group")
    void createGroup_noMultipleGroupsConfig_And_openGroup_throwsIllegalStateException() {
        //given
        TaskGroupRepository mockGroupRepository = groupReposirotyReturning(true);

        TaskConfigurationProperties mockConfig = configurationReturning(false);

        var toTest = new ProjectService(null, mockGroupRepository, mockConfig, null);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        //  then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ndone group");
        /**
         *
         * assertThatExceptionOfType(IllegalStateException.class)
         *                 .isThrownBy(()->toTest.createGroup(LocalDateTime.now(),0));
         *
         *
         *  assertThatThrownBy(()->{
         *             toTest.createGroup(LocalDateTime.now(),0);
         *         }).isInstanceOf(IllegalStateException.class);
         */


    }

    @Test
    @DisplayName("should throw IllegalStateException when configuration ok and no projects for a given id")
    void createGroup_configOK_And_openGroup_throwsIllegalArgumentException() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        TaskConfigurationProperties mockConfig = configurationReturning(true);
        var toTest = new ProjectService(mockRepository, null, mockConfig, null);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        //  then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("with given id not");


    }

    @Test
    @DisplayName("should throw IllegalStateException when configurated to allow just 1 and no groups and no projects")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_noProjects_throwsIllegalArgumentException() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        TaskGroupRepository mockgroupRepository = groupReposirotyReturning(false);

        TaskConfigurationProperties mockConfig = configurationReturning(true);
        var toTest = new ProjectService(mockRepository, mockgroupRepository, mockConfig, null);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        //  then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("with given id not");


    }

    @Test
    @DisplayName("Should create new group from project")
    void createGroup_configurationOk_existProject_saveNewGroup() {
        //given
        var today = LocalDate.now().atStartOfDay();
        //and
        var project = projectWith("bar",Set.of(-1,-2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).
                thenReturn(Optional.of(project));
        //and

        //and
        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        var serviceWithInMemRepo = dummyGroupService(inMemoryGroupRepo);
        int countBeforeCall= inMemoryGroupRepo.count();
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);

        //SUT
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepo, mockConfig, serviceWithInMemRepo);

        //when
        GroupReadModel result = toTest.createGroup(today,1);

        //then
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(countBeforeCall +1).isEqualTo(inMemoryGroupRepo.count());

    }

    private static TaskGroupService dummyGroupService(InMemoryGroupRepository inMemoryGroupRepo) {
        return new TaskGroupService(inMemoryGroupRepo, null);
    }

    private Project projectWith(String projectDescripton, Set<Integer> daysToDeadline) {
        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                }).collect(Collectors.toSet());

        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescripton);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private static TaskGroupRepository groupReposirotyReturning(boolean value) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(value);
        return mockGroupRepository;
    }

    private static TaskConfigurationProperties configurationReturning(boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);

        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }
    private static class InMemoryGroupRepository implements TaskGroupRepository{
        private int index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count() {
            return map.values().size();
        }
        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) {
            if (entity.getId() == 0) {
                try {

                    var field = TaskGroup.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projetId) {
            return map.values()
                    .stream().filter(group->!group.isDone())
                    .anyMatch(group->group.getProject()!=null && group.getProject().getId()==projetId);
        }

        @Override
        public boolean existsByDescription(String description) {
            return map.values().stream()
                    .anyMatch(group -> group.getDescription().equals(description));
        }
    }
    }

