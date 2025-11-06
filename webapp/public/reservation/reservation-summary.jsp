<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentLocale" value="${not empty sessionScope.locale ? sessionScope.locale : 'es'}" />
<fmt:setLocale value="${currentLocale}" />
<fmt:setBundle basename="/WEB-INF/i18n/messages" />
<!DOCTYPE html>
<html lang="${currentLocale}">
<head>
    <meta charset="UTF-8" />
    <title><fmt:message key="reservation.summary.title" /></title>
</head>
<body>
    <h1><fmt:message key="reservation.summary.heading" /></h1>

    <c:if test="${not empty error}">
        <c:forEach var="entry" items="${error.entrySet()}">
            <div class="error-message"><fmt:message key="${entry.value}" /></div>
        </c:forEach>
    </c:if>

    <c:if test="${not empty reservationSummary}">
        <section>
            <h2><fmt:message key="reservation.summary.details" /></h2>
            <ul>
                <li><strong><fmt:message key="reservation.summary.category" />:</strong> <c:out value="${reservationSummary.categoryName}" /></li>
                <li><strong><fmt:message key="reservation.summary.pickup" />:</strong> <c:out value="${reservationSummary.pickupHeadquartersName}" /></li>
                <li><strong><fmt:message key="reservation.summary.return" />:</strong> <c:out value="${reservationSummary.returnHeadquartersName}" /></li>
                <li><strong><fmt:message key="reservation.summary.startDate" />:</strong> <c:out value="${reservationSummary.pickupDateFormatted}" /></li>
                <li><strong><fmt:message key="reservation.summary.endDate" />:</strong> <c:out value="${reservationSummary.returnDateFormatted}" /></li>
            </ul>
        </section>

        <section>
            <h2><fmt:message key="reservation.summary.nextSteps" /></h2>
            <p><fmt:message key="reservation.summary.instructions" /></p>
            <div>
                <c:url var="editUrl" value="/public/reservations" />
                <form method="get" action="${editUrl}">
                    <button type="submit"><fmt:message key="reservation.summary.edit" /></button>
                </form>
            </div>
            <div>
                <c:url var="confirmUrl" value="/private/reservations/confirm" />
                <form method="post" action="${confirmUrl}">
                    <button type="submit"><fmt:message key="reservation.summary.confirm" /></button>
                </form>
            </div>
        </section>
    </c:if>
</body>
</html>
