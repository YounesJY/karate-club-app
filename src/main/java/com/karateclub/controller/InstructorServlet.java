// [file name]: InstructorServlet.java
package com.karateclub.controller;

import com.karateclub.service.InstructorService;
import com.karateclub.service.InstructorServiceImpl;
import com.karateclub.model.Instructor;
import com.karateclub.model.Member;
import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;
import com.karateclub.service.exception.BusinessRuleException;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "InstructorServlet", value = "/instructors")
public class InstructorServlet extends HttpServlet {
    private InstructorService instructorService;

    @Override
    public void init() {
        instructorService = new InstructorServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteInstructor(request, response);
                    break;
                case "viewStudents":
                    viewStudents(request, response);
                    break;
                case "assignStudent":
                    showAssignStudentForm(request, response);
                    break;
                default:
                    listInstructors(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "create":
                    createInstructor(request, response);
                    break;
                case "update":
                    updateInstructor(request, response);
                    break;
                case "assignStudent":
                    assignStudent(request, response);
                    break;
                case "removeStudent":
                    removeStudent(request, response);
                    break;
                default:
                    listInstructors(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listInstructors(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Instructor> instructors = instructorService.getAllInstructors();
        request.setAttribute("instructors", instructors);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/instructors/list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/instructors/form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Instructor instructor = instructorService.getInstructorById(id);
        request.setAttribute("instructor", instructor);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/instructors/form.jsp");
        dispatcher.forward(request, response);
    }

    private void viewStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Instructor instructor = instructorService.getInstructorById(id);
        List<Member> students = instructorService.getMembersByInstructor(id);

        request.setAttribute("instructor", instructor);
        request.setAttribute("students", students);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/instructors/students.jsp");
        dispatcher.forward(request, response);
    }

    private void showAssignStudentForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Instructor instructor = instructorService.getInstructorById(id);

        // Get available members (you would need a method to get members not assigned to this instructor)
        // List<Member> availableMembers = memberService.getMembersNotAssignedToInstructor(id);
        // request.setAttribute("availableMembers", availableMembers);

        request.setAttribute("instructor", instructor);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/instructors/assign-student.jsp");
        dispatcher.forward(request, response);
    }

    private void createInstructor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Instructor instructor = new Instructor();
            setInstructorFromRequest(instructor, request);

            instructorService.createInstructor(instructor);
            request.getSession().setAttribute("successMessage", "Instructor created successfully!");
            response.sendRedirect("instructors");

        } catch (ValidationException | BusinessRuleException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("instructor", new Instructor());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/instructors/form.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void updateInstructor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Instructor instructor = instructorService.getInstructorById(id);
            setInstructorFromRequest(instructor, request);

            instructorService.updateInstructor(instructor);
            request.getSession().setAttribute("successMessage", "Instructor updated successfully!");
            response.sendRedirect("instructors");

        } catch (ValidationException | BusinessRuleException | NotFoundException e) {
            request.setAttribute("errorMessage", e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/instructors/form.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void deleteInstructor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            instructorService.deleteInstructor(id);
            request.getSession().setAttribute("successMessage", "Instructor deleted successfully!");

        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Error deleting instructor: " + e.getMessage());
        }

        response.sendRedirect("instructors");
    }

    private void assignStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int instructorId = Integer.parseInt(request.getParameter("instructorId"));
            int memberId = Integer.parseInt(request.getParameter("memberId"));

            instructorService.assignMemberToInstructor(instructorId, memberId);
            request.getSession().setAttribute("successMessage", "Student assigned successfully!");

        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Error assigning student: " + e.getMessage());
        }

        response.sendRedirect("instructors?action=viewStudents&id=" + request.getParameter("instructorId"));
    }

    private void removeStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int instructorId = Integer.parseInt(request.getParameter("instructorId"));
        try {
            int memberId = Integer.parseInt(request.getParameter("memberId"));

            instructorService.removeMemberFromInstructor(instructorId, memberId);
            request.getSession().setAttribute("successMessage", "Student removed successfully!");

        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Error removing student: " + e.getMessage());
        }

        response.sendRedirect("instructors?action=viewStudents&id=" + instructorId);
    }

    private void setInstructorFromRequest(Instructor instructor, HttpServletRequest request) {
        String qualification = request.getParameter("qualification");
        if (qualification != null && !qualification.trim().isEmpty()) {
            instructor.setQualification(qualification.trim());
        }

        // Set other properties as needed
        // Note: In a real application, you would need to handle the Person association
    }
}