// [file name]: MemberServlet.java
package com.karateclub.controller;

import com.karateclub.dao.BeltRankDAO;
import com.karateclub.service.MemberService;
import com.karateclub.service.MemberServiceImpl;
import com.karateclub.model.*;
import com.karateclub.service.exception.NotFoundException;
import com.karateclub.service.exception.ValidationException;
import com.karateclub.service.exception.BusinessRuleException;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "MemberServlet", value = "/members")
public class MemberServlet extends HttpServlet {
    private MemberService memberService;

    @Override
    public void init() {
        memberService = new MemberServiceImpl();
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
                case "activate":
                    activateMember(request, response);
                    break;
                case "deactivate":
                    deactivateMember(request, response);
                    break;
                case "promote":
                    showPromoteForm(request, response);
                    break;
                default:
                    listMembers(request, response);
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
                    createMember(request, response);
                    break;
                case "update":
                    updateMember(request, response);
                    break;
                case "delete":
                    deleteMember(request, response);
                    break;
                case "promote":
                    promoteMember(request, response);
                    break;
                default:
                    listMembers(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listMembers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String filter = request.getParameter("filter");
        List<Member> members;

        if ("active".equals(filter)) {
            members = memberService.getActiveMembers();
        } else {
            members = memberService.getAllMembers();
        }

        // Enhanced debug output
        System.out.println("=== DEBUG MemberServlet ===");
        System.out.println("Filter: " + filter);
        System.out.println("Members list: " + members);
        System.out.println("Members size: " + (members != null ? members.size() : "NULL"));
        System.out.println("Members class: " + (members != null ? members.getClass().getName() : "NULL"));

        // Set attribute with different names for testing
        request.setAttribute("members", members);
        request.setAttribute("membersList", members); // Alternative name
        request.setAttribute("testAttribute", "Hello from Servlet");

        System.out.println("Attributes set in request:");
        java.util.Enumeration<String> attrNames = request.getAttributeNames();
        while (attrNames.hasMoreElements()) {
            String name = attrNames.nextElement();
            System.out.println("  " + name + " = " + request.getAttribute(name));
        }
        System.out.println("=== END DEBUG ===");

        request.setAttribute("filter", filter);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/members/list.jsp");
        dispatcher.forward(request, response);
    }
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/members/form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        // This will now use the simplified query
        Member member = memberService.getMemberById(id);
        request.setAttribute("member", member);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/members/form.jsp");
        dispatcher.forward(request, response);
    }

    private void createMember(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Member member = new Member();

            // Initialize the Person object
            member.setPerson(new Person());

            // Set member properties from request parameters
            setMemberFromRequest(member, request);

            memberService.createMember(member);
            request.getSession().setAttribute("successMessage", "Member created successfully!");
            response.sendRedirect("members");

        } catch (ValidationException | BusinessRuleException e) {
            request.setAttribute("errorMessage", e.getMessage());

            // Create a temporary member to preserve form data
            Member tempMember = new Member();
            tempMember.setPerson(new Person());
            setMemberFromRequest(tempMember, request);
            request.setAttribute("member", tempMember);

            // Use forward instead of redirect to preserve the form data and URL context
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/members/form.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void updateMember(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Member member = memberService.getMemberById(id);
            setMemberFromRequest(member, request);

            memberService.updateMember(member);
            request.getSession().setAttribute("successMessage", "Member updated successfully!");
            response.sendRedirect("members");

        } catch (ValidationException | BusinessRuleException | NotFoundException e) {
            request.setAttribute("errorMessage", e.getMessage());

            // Preserve the member data for re-display
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Member member = memberService.getMemberById(id);
                setMemberFromRequest(member, request); // Re-apply the form data
                request.setAttribute("member", member);
            } catch (Exception ex) {
                // If we can't get the member, create a temporary one
                Member tempMember = new Member();
                tempMember.setPerson(new Person());
                setMemberFromRequest(tempMember, request);
                request.setAttribute("member", tempMember);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/members/form.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void deleteMember(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            memberService.deleteMember(id);
            request.getSession().setAttribute("successMessage", "Member deleted successfully!");

        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Error deleting member: " + e.getMessage());
        }

        response.sendRedirect("members");
    }

    private void activateMember(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean activated = memberService.activateMember(id);

            if (activated) {
                request.getSession().setAttribute("successMessage", "Member activated successfully!");
            }

        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Error activating member: " + e.getMessage());
        }

        response.sendRedirect("members");
    }

    private void deactivateMember(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean deactivated = memberService.deactivateMember(id);

            if (deactivated) {
                request.getSession().setAttribute("successMessage", "Member deactivated successfully!");
            }

        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Error deactivating member: " + e.getMessage());
        }

        response.sendRedirect("members");
    }

    private void showPromoteForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Member member = memberService.getMemberById(id);

            // Check if member is active
            if (!member.isActive()) {
                request.getSession().setAttribute("errorMessage", "Cannot promote inactive member: " + member.getPerson().getName());
                response.sendRedirect("members");
                return;
            }

            // Get available belt ranks for promotion (only higher ranks)
            BeltRankDAO beltRankDAO = new BeltRankDAO();
            List<BeltRank> availableRanks = beltRankDAO.getHigherRanks(member.getLastBeltRank().getRankID());

            // Check if there are higher ranks available
            if (availableRanks.isEmpty()) {
                request.getSession().setAttribute("errorMessage",
                        "No higher belt ranks available for " + member.getPerson().getName() +
                                ". Current rank: " + member.getLastBeltRank().getRankName());
                response.sendRedirect("members");
                return;
            }

            request.setAttribute("member", member);
            request.setAttribute("availableRanks", availableRanks);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/members/promote.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Error loading promote form: " + e.getMessage());
            response.sendRedirect("members");
        }
    }

    private void promoteMember(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int memberId = Integer.parseInt(request.getParameter("id"));
            int newRankId = Integer.parseInt(request.getParameter("newRankId"));

            Member member = memberService.promoteMember(memberId, newRankId);
            request.getSession().setAttribute("successMessage",
                    "Member " + member.getPerson().getName() + " promoted successfully to " +
                            member.getLastBeltRank().getRankName() + "!");

        } catch (BusinessRuleException e) {
            request.getSession().setAttribute("errorMessage", "Cannot promote member: " + e.getMessage());
        } catch (NotFoundException e) {
            request.getSession().setAttribute("errorMessage", "Error: " + e.getMessage());
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Error promoting member: " + e.getMessage());
        }

        response.sendRedirect("members");
    }

    private void setMemberFromRequest(Member member, HttpServletRequest request) {
        // Handle Person data - don't create new Person if it already exists
        if (member.getPerson() == null) {
            member.setPerson(new Person());
        }

        // Set Person properties
        String name = request.getParameter("name");
        if (name != null && !name.trim().isEmpty()) {
            member.getPerson().setName(name.trim());
        }

        String contactInfo = request.getParameter("contactInfo");
        if (contactInfo != null && !contactInfo.trim().isEmpty()) {
            member.getPerson().setContactInfo(contactInfo.trim());
        }

        // Set emergency contact
        String emergencyContact = request.getParameter("emergencyContact");
        if (emergencyContact != null && !emergencyContact.trim().isEmpty()) {
            member.setEmergencyContactInfo(emergencyContact.trim());
        }

        // Set belt rank - FIXED: Use the service to get the actual BeltRank object
        String beltRankId = request.getParameter("beltRankId");
        if (beltRankId != null && !beltRankId.trim().isEmpty()) {
            try {
                int rankId = Integer.parseInt(beltRankId);
                // You need to inject BeltRankService or get it from somewhere
                // For now, let's create a temporary solution
                BeltRank beltRank = new BeltRank();
                beltRank.setRankID(rankId);
                // Note: In production, you should fetch the complete BeltRank object
                // BeltRank beltRank = beltRankService.getBeltRankById(rankId);
                member.setLastBeltRank(beltRank);
            } catch (NumberFormatException e) {
                System.out.println("Error parsing belt rank ID: " + e.getMessage());
            }
        }

        // Set active status - FIXED: Checkbox handling
        String active = request.getParameter("active");
        member.setActive("on".equals(active)); // Fixed checkbox value check
    }
}// [file name]: MemberServlet.java
