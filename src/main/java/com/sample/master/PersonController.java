package com.sample.master;

import org.h2.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RestController
public class PersonController {

    @Autowired
    PersonService personService;

    @GetMapping("/persons")
    private List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("/persons/{id}")
    private Person getPerson(@PathVariable("id") int id) {
        return personService.getPersonById(id);
    }

    @DeleteMapping("/persons/{id}")
    private void deletePerson(@PathVariable("id") int id) {
        personService.delete(id);
    }

    @PostMapping("/persons")
    private int savePerson(@RequestBody Person person) {
        personService.saveOrUpdate(person);
        return person.getId();
    }

    @GetMapping("/executesql/")
    public void isEvenOrOdd() throws Exception {
        initializeJiraDataBaseForSla();
    }


    public void initializeJiraDataBaseForSla() throws Exception {
        //final DataSource datasource;
        //datasource = new SimpleDriverDataSource(new JDBCDriver(), "jdbc:h2:mem:testdb", sa, "");

        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass((Class<Driver>)Class.forName("org.h2.Driver"));
        dataSource.setUrl("jdbc:h2:mem:mytestdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        final Connection connection = dataSource.getConnection();
        try {
            ScriptUtils.executeSqlScript(connection, new EncodedResource(new ClassPathResource("/data.sql"), StandardCharsets.UTF_8));
           // ScriptUtils.executeSqlScript(connection, new EncodedResource(new ClassPathResource("sql/sla/jira.sql"), StandardCharsets.UTF_8));
        } finally {
            connection.close();
        }
    }
}