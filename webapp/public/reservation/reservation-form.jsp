<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentLocale" value="${not empty sessionScope.locale ? sessionScope.locale : 'es'}" />
<fmt:setLocale value="${currentLocale}" />
<fmt:setBundle basename="/WEB-INF/i18n/messages" />
<!DOCTYPE html>
<html lang="${currentLocale}">
<head>
    <meta charset="UTF-8" />
    <title><fmt:message key="reservation.form.title" /></title>
</head>
<body>
    <h1><fmt:message key="reservation.form.heading" /></h1>

    <c:if test="${not empty error}">
        <c:forEach var="entry" items="${error.entrySet()}">
            <div class="error-message"><fmt:message key="${entry.value}" /></div>
        </c:forEach>
    </c:if>

    <c:set var="form" value="${not empty reservationForm ? reservationForm : null}" />

    <c:url var="formAction" value="/public/reservations" />
    <form method="post" action="${formAction}">
        <div>
            <label for="categoryId"><fmt:message key="reservation.form.category" /></label>
            <select id="categoryId" name="categoryId" required>
                <option value=""><fmt:message key="reservation.form.selectOption" /></option>
                <c:forEach var="category" items="${vehicleCategories}">
                    <option value="${category.categoryId}" <c:if test="${form.categoryId == category.categoryId}">selected</c:if>>
                        <c:out value="${category.categoryName}" />
                    </option>
                </c:forEach>
            </select>
        </div>

        <div>
            <label for="pickupHeadquartersId"><fmt:message key="reservation.form.pickupHeadquarters" /></label>
            <select id="pickupHeadquartersId" name="pickupHeadquartersId" required>
                <option value=""><fmt:message key="reservation.form.selectOption" /></option>
                <c:forEach var="hq" items="${headquarters}">
                    <option value="${hq.headquartersId}" <c:if test="${form.pickupHeadquartersId == hq.headquartersId}">selected</c:if>>
                        <c:out value="${hq.name}" />
                    </option>
                </c:forEach>
            </select>
        </div>

        <div>
            <label for="returnHeadquartersId"><fmt:message key="reservation.form.returnHeadquarters" /></label>
            <select id="returnHeadquartersId" name="returnHeadquartersId" required>
                <option value=""><fmt:message key="reservation.form.selectOption" /></option>
                <c:forEach var="hq" items="${headquarters}">
                    <option value="${hq.headquartersId}" <c:if test="${form.returnHeadquartersId == hq.headquartersId}">selected</c:if>>
                        <c:out value="${hq.name}" />
                    </option>
                </c:forEach>
            </select>
        </div>

        <div>
            <label for="startDate"><fmt:message key="reservation.form.startDate" /></label>
            <input id="startDate" type="datetime-local" name="startDate" value="${form.startDate}" required />
        </div>

        <div>
            <label for="endDate"><fmt:message key="reservation.form.endDate" /></label>
            <input id="endDate" type="datetime-local" name="endDate" value="${form.endDate}" required />
        </div>

        <div>
            <button type="submit"><fmt:message key="reservation.form.submit" /></button>
        </div>
    </form>
</body>
</html>
