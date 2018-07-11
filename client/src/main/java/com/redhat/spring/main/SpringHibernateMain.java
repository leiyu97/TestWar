package com.redhat.spring.main;

import com.redhat.spring.dao.*;
import com.redhat.spring.model.Person;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class SpringHibernateMain {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");

        PersonDAO personDAO = context.getBean(PersonDAO.class);

        Person person = new Person();
        person.setName("lei");
        person.setCountry("GB");

        personDAO.save(person);

        System.out.println("Person::" + person);

        List<Person> list = personDAO.list();

        for (Person p : list) {
            System.out.println("Person List::" + p);
        }

        context.close();

    }

}
