package com.csc340.restapidemo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class StudentFile {
    private static final String FILE_PATH = "students.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Student> getStudentDatabase() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                return mapper.readValue(file, new TypeReference<List<Student>>() {});
            } else {
                return List.of();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public static void writeStudent(List<Student> studentDatabase) {
        try {
            mapper.writeValue(new File(FILE_PATH), studentDatabase);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateStudent(int id, Student updateStudent) {
        List<Student> students = getStudentDatabase()
                .stream()
                .map(student -> student.getId() == id ? updateStudent : student)
                .collect(Collectors.toList());
        try {
            mapper.writeValue(new File(FILE_PATH), students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Student getStudentById(int id) {
        return getStudentDatabase()
                .stream()
                .filter(student -> student.getId() == id)
                .findFirst()
                .orElse(null);
    }


    public static void deleteStudent(int id) {
        List<Student> students = getStudentDatabase()
                .stream()
                .filter(student -> student.getId() != id)
                .collect(Collectors.toList());
        try {
            mapper.writeValue(new File(FILE_PATH), students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
