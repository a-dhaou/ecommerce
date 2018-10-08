<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.ecommerce.general.path.ViewPath" %>

<c:import url='<%=ViewPath.header_admin %>' />
<c:import url='<%=ViewPath.navebar_admin %>' />

<h1 class="text-center">Manage Categories</h1>

<c:choose>
    <c:when test="${requestScope['supCategories'].size() gt 0}">

        <div class="container categories">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="fa fa-edit"></i> Manage Categories
                    <div class="option pull-right">
                        <i class="fa fa-sort"></i> Ordering: [
                        <a class="${(sort == "ASC") ? "active" : "" }" href="?sort=ASC">ASC</a> | 
                        <a class="${(sort == "DESC") ? "active" : "" }" href="?sort=DESC">DESC</a> ] 
                        <i class="fa fa-eye"></i> View: [
                        <span class="active" data-view="full">Full</span> |
                        <span data-view="classic">Classic</span> ]
                    </div>
                </div>
                <div class="panel-body">

                    <c:forEach items="${requestScope['supCategories']}" var="supCategory">
                        <div class='cat'>
                            <div class='hidden-buttons'>
                                <a href='${initParam['adminPath']}edit-category?categoryid=${supCategory['id']}' class='btn btn-xs btn-primary'><i class='fa fa-edit'></i> Edit</a>
                                <a href='${initParam['adminPath']}delete-category?categoryid=${supCategory['id']}' class='confirm btn btn-xs btn-danger'><i class='fa fa-close'></i> Delete</a>
                            </div>
                            <h3>${supCategory['name']}</h3>
                            <div class='full-view'>
                                <p>${(supCategory['description'].equals("")) ? "This category has no description" : supCategory['description']}</p>

                                <c:if test="${supCategory['visibility'] eq 1}">
                                    <span class="visibility cat-span"><i class="fa fa-eye"></i> Hidden</span> 
                                </c:if>

                                <c:if test="${supCategory['allowComments'] eq 1}">
                                    <span class="commenting cat-span"><i class="fa fa-close"></i> Comment Disabled</span> 
                                </c:if>

                                <c:if test="${supCategory['allowAds'] eq 1}">     
                                    <span class="advertises cat-span"><i class="fa fa-close"></i> Ads Disabled</span> 
                                </c:if>

                            </div>
                            <!--start add sub Category -->
                            <c:if test="${requestScope['subCategories'].size() gt 0}">
                                <c:forEach items="${requestScope['subCategories']}" var="subCategory">
                                    <c:if test="${supCategory.id eq subCategory.parent }">

                                        <h4 class='child-head'>Child Categories</h4>
                                        <ul class='list-unstyled child-cats'>
                                            <li class='child-link'>
                                                <a href='${initParam['adminPath']}edit-category?categoryid=${subCategory['id']}'>${subCategory.name}</a>
                                                <a href='${initParam['adminPath']}edit-category?categoryid=${subCategory['id']}' class='btn btn-xs btn-primary'><i class='fa fa-edit'></i> Edit</a>
                                                <a href='${initParam['adminPath']}delete-category?categoryid=${subCategory['id']}' class='confirm btn btn-xs btn-danger'><i class='fa fa-close'></i> Delete</a>
                                            </li><br>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </c:if>
                            <!-- end add sub Category -->
                        </div>
                        <hr
                    </div>
                </c:forEach>
            </div>
        </div>

        <a class="add-category btn btn-primary" href="${initParam['adminPath']}add-category"><i class="fa fa-plus"></i> Add New Category</a>
    </div>
</c:when>
<c:otherwise>

    <div class="container">
        <div class="nice-message">There's No Categories To Show</div>
        <a href="${initParam['adminPath']}categories?action=Add" class="btn btn-primary">
            <i class="fa fa-plus"></i> New Category
        </a>
    </div>
</c:otherwise>
</c:choose>

<c:import url='<%=ViewPath.footer_admin %>' />
