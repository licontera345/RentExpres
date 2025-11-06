<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentLocale" value="${not empty sessionScope.locale ? sessionScope.locale : 'es'}" />
<fmt:setLocale value="${currentLocale}" />
<fmt:setBundle basename="/WEB-INF/i18n/messages" />
<!DOCTYPE html>
<html lang="${currentLocale}">
<head>
    <meta charset="UTF-8" />
    <title><fmt:message key="vehicle.detail.title" /></title>
</head>
<body>
    <h1><fmt:message key="vehicle.detail.heading" /></h1>

    <c:if test="${not empty error}">
        <c:forEach var="entry" items="${error.entrySet()}">
            <div class="error-message"><fmt:message key="${entry.value}" /></div>
        </c:forEach>
    </c:if>

    <c:choose>
        <c:when test="${not empty vehicle}">
            <section>
                <h2><fmt:message key="vehicle.detail.summary" /></h2>
                <ul>
                    <li><strong><fmt:message key="vehicle.detail.brand" />:</strong> <c:out value="${vehicle.brand}" /></li>
                    <li><strong><fmt:message key="vehicle.detail.model" />:</strong> <c:out value="${vehicle.model}" /></li>
                    <li><strong><fmt:message key="vehicle.detail.manufactureYear" />:</strong> <c:out value="${vehicle.manufactureYear}" /></li>
                    <li><strong><fmt:message key="vehicle.detail.dailyPrice" />:</strong> <c:out value="${vehicle.dailyPrice}" /></li>
                    <li><strong><fmt:message key="vehicle.detail.licensePlate" />:</strong> <c:out value="${vehicle.licensePlate}" /></li>
                    <li><strong><fmt:message key="vehicle.detail.currentMileage" />:</strong> <c:out value="${vehicle.currentMileage}" /></li>
                </ul>
            </section>

            <section>
                <h2><fmt:message key="vehicle.detail.images.heading" /></h2>
                <c:choose>
                    <c:when test="${not empty vehicleImages}">
                        <ul>
                            <c:forEach var="image" items="${vehicleImages}">
                                <li><c:out value="${image}" /></li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <p><fmt:message key="vehicle.detail.images.empty" /></p>
                    </c:otherwise>
                </c:choose>
            </section>

            <section>
                <h2><fmt:message key="vehicle.detail.actions" /></h2>
                <c:url var="startReservationUrl" value="/private/reservations/start" />
                <form method="post" action="${startReservationUrl}">
                    <input type="hidden" name="vehicleId" value="${vehicle.vehicleId}" />
                    <button type="submit"><fmt:message key="vehicle.detail.reserveButton" /></button>
                </form>
            </section>
        </c:when>
        <c:otherwise>
            <p><fmt:message key="vehicle.detail.notAvailable" /></p>
        </c:otherwise>
    </c:choose>
</body>
</html>
