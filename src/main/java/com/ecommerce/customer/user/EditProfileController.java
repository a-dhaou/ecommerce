// <editor-fold >
package com.ecommerce.customer.user;

import com.ecommerce.general.helper.CookieHelper;
import com.ecommerce.general.helper.HashHelper;
import com.ecommerce.general.helper.Helper;
import com.ecommerce.general.path.ViewPath;
import com.ecommerce.general.user.User;
import com.ecommerce.general.user.UserDaoImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "EditProfileController", urlPatterns = {"/edit-profile"})
public class EditProfileController extends HttpServlet {

    ServletContext servletContext = null;

    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        servletContext = getServletContext();
    }// </editor-fold >
    // <editor-fold >

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long userId = null;

        if (CookieHelper.isCookie("userId", request, response)) {
            userId = Long.parseLong(CookieHelper.getCookie("userId", request, response));
        } else {
            // get userId from session
            userId = (Long) request.getSession().getAttribute("userId");
        }

        // get user with id 
        User user = new UserDaoImpl(servletContext).getUserById(userId);

        if (user != null) {
            // set user to request
            request.setAttribute("user", user);

            // forward to edit profile page
            Helper.forwardRequest(request, response, ViewPath.edit_profile);
        } else {
            Helper.redriectToPrevPage(request, response, "This user is not found", true);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //get FORM Patameter
        String name = request.getParameter("name");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");

        List<String> formErrors = vildateFormParams(name, email, fullName);
        
        
        String password = (newPassword == null || newPassword.isEmpty()) ? oldPassword : HashHelper.stringHash(newPassword);

        // set errors to the request
        request.setAttribute("errors", formErrors);

        if (formErrors.size() > 0) {// if there is errors

            // forword to login page
            Helper.setTitle(request, "Edit Profile");
            Helper.forwardRequest(request, response, ViewPath.edit_profile);

        } else {//if there is no errors

            //get id of current user 
            Long userId = getCurrentUserId(request, response);

            //create new user
            User user = User.builder()
                    .id(userId)
                    .name(name)
                    .password(password)
                    .email(email)
                    .fullName(fullName)
                    .build();

            //update user data 
            boolean userUpdated = new UserDaoImpl(servletContext).updateUser(user);

            //set message success or error
            if (!userUpdated) {
                // add new error to errors if User not added
                //<improve>formErrors.add("can not update this User");             request.setAttribute("success", "User Has Been Updated");
                request.setAttribute("error", "can not update this User");

            } else {
                // set success message if User added
                request.setAttribute("success", "User Has Been Updated");
            }

            // set User to request
            request.setAttribute("user", user);

            // forword to add page
            Helper.forwardRequest(request, response, ViewPath.edit_profile);

        }
    }// </editor-fold>

    public List<String> vildateFormParams(String name, String email,
            String fullName) {
        List<String> formErrors = new ArrayList();

        if (name == null || name.length() < 4) {
            formErrors.add("name Must Be At Least 4 Characters or more ..");
        }
        if (email == null || email.length() < 4) {
            formErrors.add("Email Cant Be <strong>Empty</strong> and Must Be At Least 4 Characters ");
        }
        if (fullName == null || fullName.length() < 4) {
            formErrors.add("fullName Cant Be <strong>Empty</strong> and Must Be At Least 4 Characters ");
        }

        return formErrors;
    }

    public long getCurrentUserId(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long userId;

        if (CookieHelper.isCookie("userId", request, response)) {
            userId = Long.parseLong(CookieHelper.getCookie("userId", request, response));
        } else {
            // get userId from session
            userId = (Long) request.getSession().getAttribute("userId");
        }
        return userId;
    }

}
