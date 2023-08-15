package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

@Component // to give control to manage this to spring we should declare @Component
public class UserDaoService {

	private static List<User> users = new ArrayList<>();

	private static int userCount = 0;

	// initializing 
	static {
		users.add(new User(++userCount, "Adam", LocalDate.now().minusYears(30)));
		users.add(new User(++userCount, "John", LocalDate.now().minusYears(25)));
		users.add(new User(++userCount, "Jane", LocalDate.now().minusYears(40)));
	}

	public List<User> findAllUsers() {
		return users;
	}

	public User findUserById(int id) {

		Predicate<? super User> predicate = user -> user.getId().equals(id);
		return users.stream().filter(predicate).findFirst().orElse(null);
	}

	public User save(User user) {
		user.setId(++userCount);
		users.add(user);
		return user;
	}

	public void deleteById(int id) {
		Predicate<? super User> predicate = user -> user.getId().equals(id);
		users.removeIf(predicate);
	}

}
