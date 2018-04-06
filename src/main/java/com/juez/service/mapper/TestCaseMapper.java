package com.juez.service.mapper;

import com.juez.domain.*;
import com.juez.service.dto.TestCaseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TestCase and its DTO TestCaseDTO.
 */
@Mapper(componentModel = "spring", uses = {ProblemMapper.class, })
public interface TestCaseMapper extends EntityMapper <TestCaseDTO, TestCase> {

    @Mapping(source = "problem.id", target = "problemId")
    TestCaseDTO toDto(TestCase testCase); 

    @Mapping(source = "problemId", target = "problem")
    TestCase toEntity(TestCaseDTO testCaseDTO); 
    default TestCase fromId(Long id) {
        if (id == null) {
            return null;
        }
        TestCase testCase = new TestCase();
        testCase.setId(id);
        return testCase;
    }
}
