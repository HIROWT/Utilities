package com.example.demo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Person;

//@Component is the same, but this makes it obvious

@Repository("fakeDao")
public class FakePersonDataAccessService implements PersonDao{
	private static List<Person> DB = new ArrayList<>();
	
	@Override
	public int insertPerson(UUID id, Person person) {
		DB.add(new Person(id, person.getName()));
		return 0;
	}

	@Override
	public List<Person> selectAllPeople() {
		// TODO Auto-generated method stub
		return DB;
	}

	@Override
	public Optional<Person> selectPersonById(UUID id) {
		// TODO Auto-generated method stub
		return DB.stream().filter(person -> person.getID().equals(id)).findFirst();
	}

	@Override
	public int deletePersonById(UUID id) {
		Optional<Person> personMaybe = selectPersonById(id);
		if(personMaybe.isEmpty()) {
			return 0;
		}
		DB.remove(personMaybe.get());
		return 1;
	}

	@Override
	public int updatePersonByID(UUID id, Person person) {
		return selectPersonById(id).map(p -> 
		{int indexOfPersonToDelete = DB.indexOf(person); 
		if(indexOfPersonToDelete >= 0) {
			DB.set(indexOfPersonToDelete, person);
			return 1;
		}
		return 0;
		}).orElse(0);
	}

}
