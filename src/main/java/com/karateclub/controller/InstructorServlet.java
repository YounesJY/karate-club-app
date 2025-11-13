// [file name]: InstructorServlet.java
package com.karateclub.controller;

import com.karateclub.service.InstructorService;
import com.karateclub.service.InstructorServiceImpl;
import com.karateclub.model.Instructor;
import com.karateclub.model.Member;
import com.karateclub.model.Person; // Added import
import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;
import com.karateclub.service.exception.BusinessRuleException;

import com.karateclub.util.SecurityUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "InstructorServlet", value = "/instructors")
public class InstructorServlet extends HttpServlet {
    private InstructorService instructorService;

    @Override
    public void init() {
        instructorService = new InstructorServiceImpl();
    }

    // In InstructorServlet - doGet method
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        // Check authorization for modification actions
        if (action.equals("new") || action.equals("edit") || action.equals("delete") ||
                action.equals("assignStudent")) {

            if (SecurityUtil.redirectIfNotAdmin(request, response)) {
                return;
            }
        }

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
                    viewStudents(request, response); // All roles can view students
                    break;
                case "assignStudent":
                    showAssignStudentForm(request, response);
                    break;
                default:
                    listInstructors(request, response); // All roles can view list
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // In InstructorServlet - doPost method
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // All POST operations require ADMIN role
        if (SecurityUtil.redirectIfNotAdmin(request, response)) {
            return;
        }

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

        // Build a map of instructorId -> studentCount to avoid lazy loading in JSP
        Map<Integer, Integer> studentCounts = new HashMap<>();
        for (Instructor ins : instructors) {
            try {
                studentCounts.put(ins.getInstructorID(), instructorService.getInstructorStudentCount(ins.getInstructorID()));
            } catch (Exception e) {
                studentCounts.put(ins.getInstructorID(), 0);
            }
        }
        request.setAttribute("studentCounts", studentCounts);

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
        request.setAttribute("memberAssignments", instructor.getMemberInstructors()); // provide assignments with dates

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
        // Ensure a Person object exists
        if (instructor.getPerson() == null) {
            instructor.setPerson(new Person());
        }
        // If editing, we receive a hidden personId
        String personIdParam = request.getParameter("personId");
        if (personIdParam != null && !personIdParam.trim().isEmpty()) {
            try {
                int personId = Integer.parseInt(personIdParam.trim());
                if (personId > 0) {
                    instructor.getPerson().setPersonID(personId);
                }
            } catch (NumberFormatException ignored) { }
        }
        // Set person fields from form
        String name = request.getParameter("name");
        if (name != null && !name.trim().isEmpty()) {
            instructor.getPerson().setName(name.trim());
        }
        String address = request.getParameter("address");
        if (address != null) {
            instructor.getPerson().setAddress(address.trim());
        }
        String contactInfo = request.getParameter("contactInfo");
        if (contactInfo != null && !contactInfo.trim().isEmpty()) {
            instructor.getPerson().setContactInfo(contactInfo.trim());
        }
    }
}