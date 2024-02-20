package com.jsp.todo_app.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.jsp.todo_app.dao.Dao_todo;
import com.jsp.todo_app.dto.Task;
import com.jsp.todo_app.dto.User;

public class TodoService extends HttpServlet {
	Dao_todo dao = new Dao_todo();

	public void signup(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		User user = new User();
		user.setName(req.getParameter("username"));
		user.setEmail(req.getParameter("email"));
		user.setPassword(req.getParameter("password"));
		user.setMobile(Long.parseLong(req.getParameter("mobileno")));
		user.setDob(LocalDate.parse(req.getParameter("dob")));
		user.setGender(req.getParameter("Gender"));

		// email and mobile number validation
		List<User> email = dao.findUserByEmail(user.getEmail());
		List<User> mobileno = dao.findUserByMobile(user.getMobile());

		if (email.isEmpty() && mobileno.isEmpty()) {
			// to save in database
			dao.save_user(user);
			resp.getWriter().print("<h1> Account Created</h1>");
			req.getRequestDispatcher("login.html").include(req, resp);
		} else {
			if (email.isEmpty()) {
				resp.getWriter().print("<h1>  This Mobile Number is used</h1>");
			} else if (mobileno.isEmpty()) {
				resp.getWriter().print("<h1> This Email is used</h1>");
			} else {
				resp.getWriter().print("<h1>  Email and Mobile Number is used</h1>");
			}
			req.getRequestDispatcher("signUp.html").include(req, resp);
		}
	}

	public void login(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		// login validation
		String emph = req.getParameter("emph");
		String password = req.getParameter("password");
		List<User> list = null;
		try {
			long mobile = Long.parseLong(emph);
			// getting object from DB
			list = dao.findUserByMobile(mobile);
			if (list.isEmpty()) {
				resp.getWriter().print("<h1 align='center' style='color:red'>Mobile Number is not registered!</h1>");
			}
		} catch (Exception e) {
			String email = emph;
			list = dao.findUserByEmail(email);
			if (list.isEmpty()) {
				resp.getWriter().print("<h1 align='center' style='color:red'>Email address is not registered!</h1>");
			}
		}
		if (!list.isEmpty()) {
			User user = list.get(0);
			if (user.getPassword().equals(password)) {
				req.getSession().setAttribute("user", user);
				resp.getWriter().print("<h1 align='center' style='color:green'>Login Success</h1>");

				List<Task> tasks = dao.findTaskByUserId(user.getId());
				req.setAttribute("tasks", tasks);

				req.getRequestDispatcher("home.jsp").include(req, resp);
			} else {
				resp.getWriter().print("<h1 align='center' style='color:red'>Incorrect password!</h1>");
				req.getRequestDispatcher("login.html").include(req, resp);
			}
		} else {
			req.getRequestDispatcher("login.html").include(req, resp);
		}
	}

	public void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		req.getSession().removeAttribute("user");
		req.getRequestDispatcher("login.html").include(req, resp);

	}

	public void addTask(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String name = req.getParameter("name");
		String description = req.getParameter("description");
		Part image = req.getPart("image");
		Task task = new Task();
		task.setName(name);
		task.setDescription(description);
		task.setStatus(false);
		task.setAddedTime(LocalDateTime.now());
		// to convert part to byte[] array
		byte[] pic = new byte[image.getInputStream().available()];
		image.getInputStream().read(pic);

		task.setImage(pic);

		User user = (User) req.getSession().getAttribute("user");
		task.setUser(user);

		// to save these details in task table
		dao.save_task(task);
		resp.getWriter().print("<h1 align='center' style='color:green'>Task added successfully</h1>");

		List<Task> tasks = dao.findTaskByUserId(user.getId());
		req.setAttribute("tasks", tasks);

		req.getRequestDispatcher("home.jsp").include(req, resp);
	}

	public void completeTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		Task task = dao.findTaskById(id);
		task.setStatus(true);

		// to update the task status
		dao.update_task(task);

		resp.getWriter().print("<h1 align='center' style='color:green'>Task completed successfully</h1>");
		// whenever u go to home page
		User user = (User) req.getSession().getAttribute("user");
		List<Task> tasks = dao.findTaskByUserId(user.getId());
		req.setAttribute("tasks", tasks);
		req.getRequestDispatcher("home.jsp").include(req, resp);
	}

	public void deleteTask(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		int id = Integer.parseInt(req.getParameter("id"));
		Task task = dao.findTaskById(id);
		// to delete task
		dao.delete_task(task);
		resp.getWriter().print("<h1 align='center' style='color:green'>Task Deleted successfully</h1>");

		// whenever u go to home page
		User user = (User) req.getSession().getAttribute("user");
		List<Task> tasks = dao.findTaskByUserId(user.getId());
		req.setAttribute("tasks", tasks);
		req.getRequestDispatcher("home.jsp").include(req, resp);

	}

	public void updateTask(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String name = req.getParameter("name");
		String description = req.getParameter("description");
		Part image = req.getPart("image");
		Task task = new Task();
		int id = Integer.parseInt(req.getParameter("id"));
		task.setId(id);
		task.setName(name);
		task.setDescription(description);
		task.setStatus(false);
		task.setAddedTime(LocalDateTime.now());

		// to convert part to byte[] array
		byte[] pic = new byte[image.getInputStream().available()];
		image.getInputStream().read(pic);

		if (pic.length == 0)
			task.setImage(dao.findTaskById(id).getImage());
		else
			task.setImage(pic);

		User user = (User) req.getSession().getAttribute("user");
		task.setUser(user);

		// to save these details in task table
		dao.update_task(task);
		resp.getWriter().print("<h1 align='center' style='color:green'>Task updated successfully</h1>");

		List<Task> tasks = dao.findTaskByUserId(user.getId());
		req.setAttribute("tasks", tasks);

		req.getRequestDispatcher("home.jsp").include(req, resp);
	}
}
