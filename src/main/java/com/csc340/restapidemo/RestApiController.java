package com.csc340.restapidemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class RestApiController {

    /**
     * Hello World API endpoint.
     *
     * @return response string.
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    /**
     * Greeting API endpoint.
     *
     * @param name the request parameter
     * @return the response string.
     */
    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "Dora") String name) {
        return "Hola, soy " + name;
    }


    /**
     * List all students.
     *
     * @return the list of students.
     */
    @GetMapping("students/all")
    public Object getAllStudents() {
        return StudentFile.getStudentDatabase();
    }

    /**
     * Get one student by Id
     *
     * @param id the unique student id.
     * @return the student.
     */
    @GetMapping("students/{id}")
    public Student getStudentById(@PathVariable int id) {
        return StudentFile.getStudentById(id);
    }


    /**
     * Create a new Student entry.
     *
     * @param student the new Student
     * @return the List of Students.
     */
    @PostMapping("students/create")
    public Object createStudent(@RequestBody Student student) {
        List<Student> students = StudentFile.getStudentDatabase();
        students.add(student);
        StudentFile.writeStudent(students);

        StudentFile.writeStudent((List<Student>) student);
        return StudentFile.getStudentDatabase();
    }
    @PutMapping("/students/update/{id}")
    public List<Student> updateStudent(@PathVariable int id, @RequestBody Student student) {
        StudentFile.updateStudent(id, student);
        return StudentFile.getStudentDatabase();
    }

    /**
     * Delete a Student by id
     *
     * @param id the id of student to be deleted.
     * @return the List of Students.
     */
    @DeleteMapping("students/delete/{id}")
    public Object deleteStudent(@PathVariable int id) {
        StudentFile.deleteStudent(id);
        return StudentFile.getStudentDatabase();
    }

    /**
     * Get a quote from quotable and make it available our own API endpoint
     *
     * @return The quote json response
     */
    @GetMapping("/quote")
    public Object getQuote() {
        try {
            String url = "https://api.quotable.io/random";
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            //We are expecting a String object as a response from the above API.
            String jSonQuote = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(jSonQuote);

            //Parse out the most important info from the response and use it for whatever you want. In this case, just print.
            String quoteAuthor = root.get("author").asText();
            String quoteContent = root.get("content").asText();
            System.out.println("Author: " + quoteAuthor);
            System.out.println("Quote: " + quoteContent);

            return root;

        } catch (JsonProcessingException ex) {
            Logger.getLogger(RestApiController.class.getName()).log(Level.SEVERE,
                    null, ex);
            return "error in /quote";
        }
    }

    /**
     * Get a list of universities from hipolabs and make them available at our own API
     * endpoint.
     *
     * @return json array
     */
    @GetMapping("/univ")
    public Object getUniversities() {
        try {
            String url = "http://universities.hipolabs.com/search?name=sports";
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            String jsonListResponse = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(jsonListResponse);

            //The response from the above API is a JSON Array, which we loop through.
            for (JsonNode rt : root) {
                //Extract relevant info from the response and use it for what you want, in this case just print to the console.
                String name = rt.get("name").asText();
                String country = rt.get("country").asText();
                System.out.println(name + ": " + country);
            }

            return root;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RestApiController.class.getName()).log(Level.SEVERE,
                    null, ex);
            return "error in /univ";
        }

    }
        @GetMapping("/zoo")
        public Object zoofact() {
            try {
                String url = "https://api.api-ninjas.com/v1/facts?limit=3";
                RestTemplate restTemplate = new RestTemplate();
                ObjectMapper mapper = new ObjectMapper();

                String jsonListResponse = restTemplate.getForObject(url, String.class);
                JsonNode root = mapper.readTree(jsonListResponse);

                for (JsonNode rt : root) {

                    String fact = rt.get("fact").asText();
                    System.out.println("Fact: " + fact);
                }

                return root;
            } catch (Exception ex) {
                Logger.getLogger(RestApiController.class.getName()).log(Level.SEVERE, null, ex);
                return "error in /zoo";
            }
        }
    }

