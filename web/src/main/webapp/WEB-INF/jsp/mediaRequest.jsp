<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<input id="descriptionUrl" type="text" class="innerShadow"></input>
<a href="#"></a>

<div id="descriptionVideoContainer">
    <ul>
        <c:forEach var="i" begin="1" end="1" step="1" varStatus ="status">
<!--            <li><c:out value="${i}" /> </li>-->
        </c:forEach>
    </ul>
    <c:out value="${webPageSummary}" />
</div>