package com.example.lab9g4.servlets;

import com.example.lab9g4.beans.Title;
import com.example.lab9g4.daos.TitleDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "TitleServlet", value = "/TitleServlet")
public class TitleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        String offset = request.getParameter("offset") == null ? "0" : request.getParameter("offset");
        String limit = request.getParameter("limit") == null ? "100" : request.getParameter("limit");

        TitleDao titleDao = new TitleDao();

        switch (action) {
            case "lista":
                //saca del modelo
                ArrayList<Title> list = titleDao.list(offset, limit);

                //mandar la lista a la vista -> job/lista.jsp
                request.setAttribute("lista", list);
                //request.setAttribute("offset",offset);
                //request.setAttribute("limit",limit);
                RequestDispatcher rd = request.getRequestDispatcher("title/lista.jsp");
                rd.forward(request, response);
                break;
            case "new":
                request.getRequestDispatcher("title/form_new.jsp").forward(request, response);
                break;
            case "edit":
                String id = request.getParameter("id");
                Title title = titleDao.buscarPorId(id);

                if (title != null) {
                    request.setAttribute("title", title);
                    request.getRequestDispatcher("title/form_edit.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/TitleServlet");
                }
                break;
            case "del":
                String idd = request.getParameter("id");
                Title title1 = titleDao.buscarPorId(idd);

                if (title1 != null) {
                    try {
                        titleDao.borrar(idd);
                    } catch (SQLException e) {
                        System.out.println("Log: excepcion: " + e.getMessage());
                    }
                }
                response.sendRedirect(request.getContextPath() + "/TitleServlet");
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        ArrayList<Title> lista = new ArrayList<>();

        TitleDao employeeDao = new TitleDao();

        String action = request.getParameter("action") == null ? "crear" : request.getParameter("action");

        Title title = new Title();

        if(action.equals("crear") || action.equals("e")){
            title.setTitle(request.getParameter("title"));
            title.setFromDate(request.getParameter("fromDate"));
            title.setToDate(request.getParameter("toDate"));
        }

        switch (action){
            case "crear":
                /*title.setEmpNo(employeeDao.searchLastId()+1);*/
                employeeDao.create(title);
                response.sendRedirect(request.getContextPath()+"/EmployeeServlet");
                break;
            case "e":
                employee.setEmpNo(Integer.parseInt(request.getParameter("empNo")));
                employeeDao.actualizar(employee);
                response.sendRedirect(request.getContextPath()+"/EmployeeServlet");
                break;
            case "s":
                String textBuscar = request.getParameter("textoBuscar");
                lista = employeeDao.searchByName(textBuscar);

                request.setAttribute("lista",lista);
                request.setAttribute("busqueda",textBuscar);
                request.getRequestDispatcher("employee/lista.jsp").forward(request,response);
                break;

            /* Método de paginado por POST
                case "limit":

                String offset = request.getParameter("offset") == null ? "0": request.getParameter("offset");
                String limit = request.getParameter("limit") == null ? "100": request.getParameter("limit");

                lista = employeeDao.list(offset, limit);

                request.setAttribute("lista",lista);
                request.setAttribute("offset",offset);
                request.setAttribute("limit",limit);

                request.getRequestDispatcher("employee/lista.jsp").forward(request,response);
                break;*/
        }
    }
}