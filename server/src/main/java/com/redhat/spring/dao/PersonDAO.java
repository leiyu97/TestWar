package com.redhat.spring.dao;

import com.redhat.spring.model.Person;

import java.util.List;

/**
 * Created by lyu on 28/07/16.
 */
public interface PersonDAO {

    public void save(Person p);

    public List<Person> list();
}
