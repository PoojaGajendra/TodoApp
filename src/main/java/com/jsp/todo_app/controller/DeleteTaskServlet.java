package com.jsp.todo_app.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsp.todo_app.service.TodoService;

@WebServlet("/deleteTask")
public class DeleteTaskServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
TodoService service=new TodoService();
service.deleteTask(req,resp);
}

}
