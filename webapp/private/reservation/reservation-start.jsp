<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentLocale" value="${not empty sessionScope.locale ? sessionScope.locale : 'es'}" />
<fmt:setLocale value="${currentLocale}" />
<fmt:setBundle basename="/WEB-INF/i18n/messages" />
<!DOCTYPE html>
<html lang="${currentLocale}">
<head>
    <meta charset="UTF-8" />
    <title><fmt:message key="reservation.start.title" /></title>
</head>
<body>
    <h1><fmt:message key="reservation.start.heading" /></h1>

    <c:if test="${not empty error}">
        <c:forEach var="entry" items="${error.entrySet()}">
            <div class="error-message"><fmt:message key="${entry.value}" /></div>
        </c:forEach>
    </c:if>

    <c:if test="${not empty reservation}">
        <section>
            <h2><fmt:message key="reservation.start.vehicleSelected" /></h2>
            <ul>
                <li><strong><fmt:message key="vehicle.detail.brand" />:</strong> <c:out value="${reservation.vehicle.brand}" /></li>
                <li><strong><fmt:message key="vehicle.detail.model" />:</strong> <c:out value="${reservation.vehicle.model}" /></li>
                <li><strong><fmt:message key="vehicle.detail.licensePlate" />:</strong> <c:out value="${reservation.vehicle.licensePlate}" /></li>
            </ul>
        </section>

        <section>
            <h2><fmt:message key="reservation.start.nextSteps" /></h2>
            <p><fmt:message key="reservation.start.instructions" /></p>
            <c:url var="continueUrl" value="/private/reservations/new" />
            <form method="get" action="${continueUrl}">
                <button type="submit"><fmt:message key="reservation.start.continue" /></button>
            </form>
        </section>
    </c:if>
</body>
</html>
