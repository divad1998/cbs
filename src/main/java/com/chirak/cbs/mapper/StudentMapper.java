package com.chirak.cbs.mapper;

import com.chirak.cbs.dto.StudentDto;
import com.chirak.cbs.dto.UpdatedStudentDto;
import com.chirak.cbs.entity.Student;
import jakarta.persistence.MapsId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentMapper {

    StudentMapper instance = Mappers.getMapper(StudentMapper.class);

    Student toStudent(StudentDto studentDto);

    StudentDto toDto(Student student);

    Student toStudent(UpdatedStudentDto dto);

    UpdatedStudentDto toUpdatedStudentDto(Student student);
}