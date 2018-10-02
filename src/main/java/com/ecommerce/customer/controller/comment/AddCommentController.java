package com.ecommerce.customer.controller.comment;

import com.ecommerce.bean.Comment;
import com.ecommerce.bean.Item;
import com.ecommerce.bean.User;
import com.ecommerce.dao.CommentDaoImpl;
import com.ecommerce.dao.ItemDaoImpl;
import com.ecommerce.helper.CookieHelper;
import com.ecommerce.helper.Helper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AddCommentController", urlPatterns = {"/add-comment"})
public class AddCommentController extends HttpServlet {

    String customerJspPath = null;
    ServletContext servletContext = null;

    @Override
    public void init() throws ServletException {
        servletContext = getServletContext();
        customerJspPath = servletContext.getInitParameter("customerJspPath");
    }

    /**
     * doPost method created to receive Comment for item :
     * <1> receive id and comment of this item
     * <2> get item from DB
     * <3> set item in request to show it again
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // set page title
        Helper.setTitle(request, "Show Item");
        // make new list of errors
        List<String> formErrors = new ArrayList();
        // set message errors to the request
        request.setAttribute("formErrors", formErrors);

        // get comment param from FORM 
        String comment = request.getParameter("comment");
        // get itemId param from request
        String itemId = request.getParameter("itemid");

        // return the itemId if number or return 0
        long id = itemId != null && Helper.isNumber(itemId) ? Long.parseLong(itemId) : 0;

        // get approve item
        Item item = new ItemDaoImpl(servletContext).getApprovedItemById(id);

        // set item to request
        request.setAttribute("item", item);

        // get all comments with descinding order links with item id
        List<Comment> itemComments = new CommentDaoImpl(servletContext).getItemComments(id, "DESC");

        // set comments to request
        request.setAttribute("commentsOfItem", itemComments);

        // check if there is no comment
        if (comment == null || comment.isEmpty()) {
            // add error to form errors
            formErrors.add("You Must Add Comment");

            // forward to show items page
            Helper.forwardRequest(request, response, customerJspPath + "item_views/show_item.jsp");

        } else {// if no errors in textfield Comment
            //get user id
            Long userId = getCurrentUserId(request, response);

            // make new user and set info to it
            User user = User.builder()
                    .id(userId)
                    .build();

            // make new comment and set info to it
            Comment com = Comment.builder()
                    .comment(comment)
                    .item(item)
                    .user(user)
                    .build();

            // add comment to DB 
            boolean commentAdded = new CommentDaoImpl(servletContext).addComment(com);

            //if comment added
            if (commentAdded) {
                // set success message to request
                request.setAttribute("success", "comment added");

            } else {//if comment does not added
                String error = "error in add";
                // set error message to request 
                request.setAttribute("error", error);
            }

            // forword request to manage page
            Helper.forwardRequest(request, response, customerJspPath + "item_views/show_item.jsp");
        }
    }

    public long getCurrentUserId(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long userId;
        if (CookieHelper.isCookie("userId", request, response)) {
            // get user id from cookie
            userId = Long.parseLong(CookieHelper.getCookie("userId", request, response));
        } else {
            // get userId from session
            userId = (Long) request.getSession().getAttribute("userId");
        }
        return userId;
    }

}
