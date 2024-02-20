package com.jsp.todo_app.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsp.todo_app.service.TodoService;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {

//  service-    business logic-  java
//	controller- recive(mapping)- servlet
//	dao-        database logic-  hybernate

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TodoService service = new TodoService();
		service.signup(req, resp);
	}
}
