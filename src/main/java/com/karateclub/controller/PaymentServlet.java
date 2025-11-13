// [file name]: PaymentServlet.java
package com.karateclub.controller;

import com.karateclub.service.PaymentService;
import com.karateclub.service.PaymentServiceImpl;
import com.karateclub.model.Payment;
import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;
import com.karateclub.service.exception.BusinessRuleException;
import com.karateclub.dao.MemberDAO;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet(name = "PaymentServlet", value = "/payments")
public class PaymentServlet extends HttpServlet {
    private PaymentService paymentService;

    @Override
    public void init() {
        paymentService = new PaymentServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "view":
                    viewPayment(request, response);
                    break;
                case "byMember":
                    showPaymentsByMember(request, response);
                    break;
                case "report":
                    showReport(request, response);
                    break;
                case "delete":
                    deletePayment(request, response);
                    break;
                default:
                    listPayments(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "create":
                    createPayment(request, response);
                    break;
                case "process":
                    processPayment(request, response);
                    break;
                default:
                    listPayments(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listPayments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Payment> payments = paymentService.getAllPayments();
        request.setAttribute("payments", payments);

        // Dashboard metrics
        double totalRevenue = payments.stream().mapToDouble(Payment::getAmount).sum();
        double avgPayment = payments.isEmpty() ? 0.0 : totalRevenue / payments.size();

        YearMonth ym = YearMonth.now();
        LocalDate monthStart = ym.atDay(1);
        LocalDate monthEnd = ym.atEndOfMonth();
        double monthRevenue = paymentService.calculateTotalRevenue(monthStart, monthEnd);

        MemberDAO memberDAO = new MemberDAO();
        int unpaidCount = memberDAO.findMembersWithUnpaidSubscriptions().size();

        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("monthRevenue", monthRevenue);
        request.setAttribute("unpaidCount", unpaidCount);
        request.setAttribute("avgPayment", avgPayment);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/payments/list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/payments/form.jsp");
        dispatcher.forward(request, response);
    }

    private void viewPayment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Payment payment = paymentService.getPaymentById(id);
        request.setAttribute("payment", payment);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/payments/view.jsp");
        dispatcher.forward(request, response);
    }

    private void showPaymentsByMember(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int memberId = Integer.parseInt(request.getParameter("memberId"));
        List<Payment> payments = paymentService.getPaymentsByMember(memberId);

        request.setAttribute("payments", payments);
        request.setAttribute("memberId", memberId);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/payments/by-member.jsp");
        dispatcher.forward(request, response);
    }

    private void showReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        if (startDateStr != null && endDateStr != null) {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            double totalRevenue = paymentService.calculateTotalRevenue(startDate, endDate);
            List<Payment> payments = paymentService.getPaymentsByDateRange(startDate, endDate);

            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("payments", payments);
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            request.setAttribute("startDateFormatted", startDate.format(fmt));
            request.setAttribute("endDateFormatted", endDate.format(fmt));
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/payments/report.jsp");
        dispatcher.forward(request, response);
    }

    private void createPayment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Payment payment = new Payment();
            setPaymentFromRequest(payment, request);

            paymentService.createPayment(payment);
            request.getSession().setAttribute("successMessage", "Payment created successfully!");
            response.sendRedirect("payments");

        } catch (ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("payment", new Payment());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/payments/form.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void processPayment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            int memberId = Integer.parseInt(request.getParameter("memberId"));
            double amount = Double.parseDouble(request.getParameter("amount"));

            paymentService.processPayment(memberId, amount, LocalDate.now());
            request.getSession().setAttribute("successMessage", "Payment processed successfully!");
            response.sendRedirect("payments?action=byMember&memberId=" + memberId);

        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Error processing payment: " + e.getMessage());
            response.sendRedirect("payments");
        }
    }

    private void deletePayment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            paymentService.deletePayment(id);
            request.getSession().setAttribute("successMessage", "Payment deleted successfully!");
        } catch (BusinessRuleException | NotFoundException | ValidationException e) {
            request.getSession().setAttribute("errorMessage", "Error deleting payment: " + e.getMessage());
        }
        response.sendRedirect("payments");
    }

    private void setPaymentFromRequest(Payment payment, HttpServletRequest request) {
        // Set payment properties from request parameters
        // This is simplified - you would need proper parsing and validation
        String amountStr = request.getParameter("amount");
        if (amountStr != null && !amountStr.trim().isEmpty()) {
            payment.setAmount(Double.parseDouble(amountStr));
        }

        String dateStr = request.getParameter("date");
        if (dateStr != null && !dateStr.trim().isEmpty()) {
            payment.setDate(LocalDate.parse(dateStr));
        }

        // Note: Member association intentionally left to service layer in processPayment
    }
}