package com.jsp.todo_app.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.jsp.todo_app.dto.Task;
import com.jsp.todo_app.dto.User;

public class Dao_todo {
	EntityManagerFactory factory = Persistence.createEntityManagerFactory("poi");
	EntityManager manager = factory.createEntityManager();
	EntityTransaction transaction = manager.getTransaction();

	public void save_user(User user) {
		transaction.begin();
		manager.persist(user);
		transaction.commit();
	}

	public void update_task(Task task) {
		transaction.begin();
		manager.merge(task);
		transaction.commit();
	}

	public void save_task(Task task) {
		transaction.begin();
		manager.persist(task);
		transaction.commit();
	}

	public void delete_task(Task task) {
		transaction.begin();
		manager.remove(task);
		transaction.commit();
	}

	public List<User> findUserByEmail(String email) {
		return manager.createQuery("select x from User x where email=?1").setParameter(1, email).getResultList();
	}

	public List<User> findUserByMobile(long mobileNo) {
		return manager.createQuery("select x from User x where mobile=?1").setParameter(1, mobileNo).getResultList();
	}

	public List<Task> findTaskByUserId(int userId) {
		return manager.createQuery("select x from Task x where user_id=?1").setParameter(1, userId).getResultList();
	}

	public Task findTaskById(int Id) {
		return manager.find(Task.class, Id);
	}

}
