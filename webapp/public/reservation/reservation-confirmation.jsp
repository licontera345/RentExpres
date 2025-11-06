<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentLocale" value="${not empty sessionScope.locale ? sessionScope.locale : 'es'}" />
<fmt:setLocale value="${currentLocale}" />
<fmt:setBundle basename="/WEB-INF/i18n/messages" />
<!DOCTYPE html>
<html lang="${currentLocale}">
<head>
    <meta charset="UTF-8" />
    <title><fmt:message key="reservation.confirmation.title" /></title>
</head>
<body>
    <h1><fmt:message key="reservation.confirmation.heading" /></h1>
    <p><fmt:message key="reservation.confirmation.message" /></p>

    <c:if test="${not empty reservationSummary}">
        <section>
            <h2><fmt:message key="reservation.confirmation.details" /></h2>
            <ul>
                <li><strong><fmt:message key="reservation.summary.category" />:</strong> <c:out value="${reservationSummary.categoryName}" /></li>
                <li><strong><fmt:message key="reservation.summary.pickup" />:</strong> <c:out value="${reservationSummary.pickupHeadquartersName}" /></li>
                <li><strong><fmt:message key="reservation.summary.return" />:</strong> <c:out value="${reservationSummary.returnHeadquartersName}" /></li>
                <li><strong><fmt:message key="reservation.summary.startDate" />:</strong> <c:out value="${reservationSummary.pickupDateFormatted}" /></li>
                <li><strong><fmt:message key="reservation.summary.endDate" />:</strong> <c:out value="${reservationSummary.returnDateFormatted}" /></li>
            </ul>
        </section>
    </c:if>

    <c:url var="homeUrl" value="/" />
    <a href="${homeUrl}"><fmt:message key="reservation.confirmation.backHome" /></a>
</body>
</html>
