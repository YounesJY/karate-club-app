// [file name]: BeltTestServlet.java
package com.karateclub.controller;

import com.karateclub.service.BeltTestService;
import com.karateclub.service.BeltTestServiceImpl;
import com.karateclub.model.BeltTest;
import com.karateclub.service.exception.BusinessRuleException;
import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "BeltTestServlet", value = "/tests")
public class BeltTestServlet extends HttpServlet {
    private BeltTestService testService;

    @Override
    public void init() {
        testService = new BeltTestServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";
        try {
            switch (action) {
                case "new":
                    showScheduleForm(request, response);
                    break;
                case "view":
                    viewTest(request, response);
                    break;
                case "byMember":
                    listByMember(request, response);
                    break;
                case "byInstructor":
                    listByInstructor(request, response);
                    break;
                case "report":
                    showReport(request, response);
                    break;
                case "delete":
                    deleteTest(request, response);
                    break;
                default:
                    listTests(request, response);
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
                    createTest(request, response);
                    break;
                case "recordResult":
                    recordResult(request, response);
                    break;
                case "delete":
                    deleteTest(request, response);
                    break;
                default:
                    listTests(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listTests(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<BeltTest> tests = testService.getAllTests();
        request.setAttribute("tests", tests);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/Tests/list.jsp");
        rd.forward(request, response);
    }

    private void showScheduleForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/Tests/schedule.jsp");
        rd.forward(request, response);
    }

    private void viewTest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        BeltTest test = testService.getTestById(id);
        request.setAttribute("test", test);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/Tests/view.jsp");
        rd.forward(request, response);
    }

    private void listByMember(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int memberId = Integer.parseInt(request.getParameter("memberId"));
        List<BeltTest> tests = testService.getTestsByMember(memberId);
        request.setAttribute("tests", tests);
        request.setAttribute("memberId", memberId);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/Tests/by-member.jsp");
        rd.forward(request, response);
    }

    private void listByInstructor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int instructorId = Integer.parseInt(request.getParameter("instructorId"));
        List<BeltTest> tests = testService.getTestsByInstructor(instructorId);
        request.setAttribute("tests", tests);
        request.setAttribute("instructorId", instructorId);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/Tests/by-instructor.jsp");
        rd.forward(request, response);
    }

    private void showReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        if (startDateStr != null && endDateStr != null) {
            LocalDate start = LocalDate.parse(startDateStr);
            LocalDate end = LocalDate.parse(endDateStr);
            List<BeltTest> tests = testService.getTestsByDateRange(start, end);
            request.setAttribute("tests", tests);
            request.setAttribute("startDate", start);
            request.setAttribute("endDate", end);
        }
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/Tests/report.jsp");
        rd.forward(request, response);
    }

    private void createTest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int memberId = Integer.parseInt(request.getParameter("memberId"));
            int rankId = Integer.parseInt(request.getParameter("rankId"));
            int instructorId = Integer.parseInt(request.getParameter("instructorId"));
            LocalDate date = LocalDate.parse(request.getParameter("date"));

            testService.scheduleTest(memberId, rankId, instructorId, date);
            request.getSession().setAttribute("successMessage", "Test scheduled successfully!");
            response.sendRedirect("tests");
        } catch (BusinessRuleException | ValidationException | NotFoundException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
            response.sendRedirect("tests?action=new");
        }
    }

    private void recordResult(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int testId = Integer.parseInt(request.getParameter("testId"));
            boolean result = Boolean.parseBoolean(request.getParameter("result"));
            String paymentIdStr = request.getParameter("paymentId");
            int paymentId = (paymentIdStr != null && !paymentIdStr.trim().isEmpty()) ? Integer.parseInt(paymentIdStr) : 0;

            if (paymentId <= 0) {
                request.getSession().setAttribute("errorMessage", "Payment ID is required to record the result.");
                response.sendRedirect("tests?action=view&id=" + testId);
                return;
            }

            testService.recordTestResult(testId, result, paymentId);
            request.getSession().setAttribute("successMessage", "Result recorded successfully!");
            response.sendRedirect("tests?action=view&id=" + testId);
        } catch (BusinessRuleException | ValidationException | NotFoundException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
            response.sendRedirect("tests");
        }
    }

    private void deleteTest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            testService.deleteTest(id);
            request.getSession().setAttribute("successMessage", "Test deleted successfully!");
        } catch (BusinessRuleException | ValidationException | NotFoundException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
        }
        response.sendRedirect("tests");
    }
}
