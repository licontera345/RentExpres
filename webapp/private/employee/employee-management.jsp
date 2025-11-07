<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentLocale" value="${not empty sessionScope.locale ? sessionScope.locale : 'es'}" />
<fmt:setLocale value="${currentLocale}" />
<fmt:setBundle basename="/WEB-INF/i18n/messages" />
<!DOCTYPE html>
<html lang="${currentLocale}">
<head>
    <meta charset="UTF-8" />
    <title><fmt:message key="employee.management.title" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/private.css" />
</head>
<body>
    <header>
        <h1><fmt:message key="employee.management.heading" /></h1>
        <nav>
            <a href="${pageContext.request.contextPath}/private/home"><fmt:message key="navigation.backToDashboard" /></a>
        </nav>
    </header>

    <section class="filters">
        <h2><fmt:message key="employee.management.filters.title" /></h2>
        <form method="get" action="${pageContext.request.contextPath}/private/employees" class="form-grid">
            <label>
                <span><fmt:message key="register.employee.field.employeeName" /></span>
                <input type="text" name="employeeName" value="${employeeCriteria.employeeName}" />
            </label>
            <label>
                <span><fmt:message key="register.employee.field.firstName" /></span>
                <input type="text" name="firstName" value="${employeeCriteria.firstName}" />
            </label>
            <label>
                <span><fmt:message key="register.employee.field.lastName1" /></span>
                <input type="text" name="lastName1" value="${employeeCriteria.lastName1}" />
            </label>
            <label>
                <span><fmt:message key="register.employee.field.lastName2" /></span>
                <input type="text" name="lastName2" value="${employeeCriteria.lastName2}" />
            </label>
            <label>
                <span><fmt:message key="register.employee.field.email" /></span>
                <input type="text" name="email" value="${employeeCriteria.email}" />
            </label>
            <label>
                <span><fmt:message key="register.employee.field.phone" /></span>
                <input type="text" name="phone" value="${employeeCriteria.phone}" />
            </label>
            <label>
                <span><fmt:message key="register.employee.field.headquarters" /></span>
                <select name="headquartersId">
                    <option value=""><fmt:message key="filter.option.all" /></option>
                    <c:forEach var="hq" items="${headquarters}">
                        <option value="${hq.headquartersId}" <c:if test="${employeeCriteria.headquartersId == hq.headquartersId}">selected</c:if>>
                            <c:out value="${hq.name}" />
                        </option>
                    </c:forEach>
                </select>
            </label>
            <label>
                <span><fmt:message key="register.employee.field.role" /></span>
                <select name="roleId">
                    <option value=""><fmt:message key="filter.option.all" /></option>
                    <c:forEach var="role" items="${roles}">
                        <option value="${role.roleId}" <c:if test="${employeeCriteria.roleId == role.roleId}">selected</c:if>>
                            <c:out value="${role.name}" />
                        </option>
                    </c:forEach>
                </select>
            </label>
            <label>
                <span><fmt:message key="employee.management.filters.status" /></span>
                <select name="activeStatus">
                    <option value=""><fmt:message key="employee.management.filters.active.all" /></option>
                    <option value="active" <c:if test="${activeFilter == 'active'}">selected</c:if>>
                        <fmt:message key="employee.status.active" />
                    </option>
                    <option value="inactive" <c:if test="${activeFilter == 'inactive'}">selected</c:if>>
                        <fmt:message key="employee.status.inactive" />
                    </option>
                </select>
            </label>
            <label>
                <span><fmt:message key="vehicle.field.pageSize" /></span>
                <select name="pageSize">
                    <c:forEach var="size" items="${pageSizes}">
                        <option value="${size}" <c:if test="${employeeCriteria.pageSize == size}">selected</c:if>>${size}</option>
                    </c:forEach>
                </select>
            </label>
            <input type="hidden" name="orderBy" value="${employeeCriteria.orderBy}" />
            <input type="hidden" name="orderDir" value="${employeeCriteria.orderDir}" />
            <div class="actions">
                <button type="submit" class="btn primary"><fmt:message key="actions.search" /></button>
                <a class="btn secondary" href="${pageContext.request.contextPath}/private/employees"><fmt:message key="actions.reset" /></a>
            </div>
        </form>
    </section>

    <section class="results">
        <h2><fmt:message key="employee.management.results.title" /></h2>
        <c:if test="${total == 0}">
            <p><fmt:message key="employee.management.results.empty" /></p>
        </c:if>
        <c:if test="${total > 0}">
            <p class="results-summary">
                <fmt:message key="employee.management.results.summary">
                    <fmt:param value="${fromRow}" />
                    <fmt:param value="${toRow}" />
                    <fmt:param value="${total}" />
                </fmt:message>
            </p>
            <table class="data-table">
                <thead>
                    <tr>
                        <th><fmt:message key="employee.table.column.id" /></th>
                        <th><fmt:message key="register.employee.field.employeeName" /></th>
                        <th><fmt:message key="register.employee.field.firstName" /></th>
                        <th><fmt:message key="register.employee.field.lastName1" /></th>
                        <th><fmt:message key="register.employee.field.email" /></th>
                        <th><fmt:message key="register.employee.field.phone" /></th>
                        <th><fmt:message key="register.employee.field.headquarters" /></th>
                        <th><fmt:message key="register.employee.field.role" /></th>
                        <th><fmt:message key="employee.table.column.status" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="employee" items="${employees}">
                        <tr>
                            <td><c:out value="${employee.employeeId}" /></td>
                            <td><c:out value="${employee.employeeName}" /></td>
                            <td><c:out value="${employee.firstName}" /></td>
                            <td><c:out value="${employee.lastName1}" /></td>
                            <td><c:out value="${employee.email}" /></td>
                            <td><c:out value="${employee.phone}" /></td>
                            <td><c:out value="${employee.headquarters != null ? employee.headquarters.name : ''}" /></td>
                            <td><c:out value="${employee.role != null ? employee.role.name : ''}" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${employee.activeStatus}">
                                        <span class="badge success"><fmt:message key="employee.status.active" /></span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge danger"><fmt:message key="employee.status.inactive" /></span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <div class="pagination">
                <c:if test="${hasPrev}">
                    <c:url var="prevUrl" value="/private/employees">
                        <c:param name="page" value="${currentPage - 1}" />
                        <c:param name="pageSize" value="${employeeCriteria.pageSize}" />
                        <c:if test="${not empty employeeCriteria.employeeName}"><c:param name="employeeName" value="${employeeCriteria.employeeName}" /></c:if>
                        <c:if test="${not empty employeeCriteria.firstName}"><c:param name="firstName" value="${employeeCriteria.firstName}" /></c:if>
                        <c:if test="${not empty employeeCriteria.lastName1}"><c:param name="lastName1" value="${employeeCriteria.lastName1}" /></c:if>
                        <c:if test="${not empty employeeCriteria.lastName2}"><c:param name="lastName2" value="${employeeCriteria.lastName2}" /></c:if>
                        <c:if test="${not empty employeeCriteria.email}"><c:param name="email" value="${employeeCriteria.email}" /></c:if>
                        <c:if test="${not empty employeeCriteria.phone}"><c:param name="phone" value="${employeeCriteria.phone}" /></c:if>
                        <c:if test="${not empty employeeCriteria.headquartersId}"><c:param name="headquartersId" value="${employeeCriteria.headquartersId}" /></c:if>
                        <c:if test="${not empty employeeCriteria.roleId}"><c:param name="roleId" value="${employeeCriteria.roleId}" /></c:if>
                        <c:if test="${not empty activeFilter}"><c:param name="activeStatus" value="${activeFilter}" /></c:if>
                        <c:if test="${not empty employeeCriteria.orderBy}"><c:param name="orderBy" value="${employeeCriteria.orderBy}" /></c:if>
                        <c:if test="${not empty employeeCriteria.orderDir}"><c:param name="orderDir" value="${employeeCriteria.orderDir}" /></c:if>
                    </c:url>
                    <a class="btn secondary" href="${pageContext.request.contextPath}${prevUrl}">
                        <fmt:message key="employee.management.pagination.previous" />
                    </a>
                </c:if>
                <span class="page-indicator">
                    <fmt:message key="employee.management.pagination.page">
                        <fmt:param value="${currentPage}" />
                        <fmt:param value="${totalPages}" />
                    </fmt:message>
                </span>
                <c:if test="${hasNext}">
                    <c:url var="nextUrl" value="/private/employees">
                        <c:param name="page" value="${currentPage + 1}" />
                        <c:param name="pageSize" value="${employeeCriteria.pageSize}" />
                        <c:if test="${not empty employeeCriteria.employeeName}"><c:param name="employeeName" value="${employeeCriteria.employeeName}" /></c:if>
                        <c:if test="${not empty employeeCriteria.firstName}"><c:param name="firstName" value="${employeeCriteria.firstName}" /></c:if>
                        <c:if test="${not empty employeeCriteria.lastName1}"><c:param name="lastName1" value="${employeeCriteria.lastName1}" /></c:if>
                        <c:if test="${not empty employeeCriteria.lastName2}"><c:param name="lastName2" value="${employeeCriteria.lastName2}" /></c:if>
                        <c:if test="${not empty employeeCriteria.email}"><c:param name="email" value="${employeeCriteria.email}" /></c:if>
                        <c:if test="${not empty employeeCriteria.phone}"><c:param name="phone" value="${employeeCriteria.phone}" /></c:if>
                        <c:if test="${not empty employeeCriteria.headquartersId}"><c:param name="headquartersId" value="${employeeCriteria.headquartersId}" /></c:if>
                        <c:if test="${not empty employeeCriteria.roleId}"><c:param name="roleId" value="${employeeCriteria.roleId}" /></c:if>
                        <c:if test="${not empty activeFilter}"><c:param name="activeStatus" value="${activeFilter}" /></c:if>
                        <c:if test="${not empty employeeCriteria.orderBy}"><c:param name="orderBy" value="${employeeCriteria.orderBy}" /></c:if>
                        <c:if test="${not empty employeeCriteria.orderDir}"><c:param name="orderDir" value="${employeeCriteria.orderDir}" /></c:if>
                    </c:url>
                    <a class="btn secondary" href="${pageContext.request.contextPath}${nextUrl}">
                        <fmt:message key="employee.management.pagination.next" />
                    </a>
                </c:if>
            </div>
        </c:if>
    </section>
</body>
</html>
