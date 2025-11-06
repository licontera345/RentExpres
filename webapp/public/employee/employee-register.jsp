<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentLocale" value="${not empty sessionScope.language ? sessionScope.language : 'es'}" />
<fmt:setLocale value="${currentLocale}" />
<fmt:setBundle basename="/WEB-INF/i18n/messages" />
<!DOCTYPE html>
<html lang="${currentLocale}">
<head>
    <meta charset="UTF-8" />
    <title><fmt:message key="register.employee.title" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/public.css" />
</head>
<body>
    <main class="form-wrapper">
        <header>
            <h1><fmt:message key="register.employee.heading" /></h1>
            <p><fmt:message key="register.employee.subtitle" /></p>
        </header>

        <c:if test="${not empty flashSuccess}">
            <div class="alert alert-success">
                <fmt:message key="${flashSuccess}" />
            </div>
        </c:if>

        <c:if test="${not empty errorGeneral}">
            <div class="alert alert-error">
                <c:out value="${errorGeneral}" />
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/public/employees/register" class="form-grid">
            <section class="form-section">
                <h2><fmt:message key="register.employee.section.identity" /></h2>
                <label>
                    <span><fmt:message key="register.employee.field.employeeName" /></span>
                    <input type="text" name="employeeName" value="${formData.employeeName}" maxlength="255" required />
                    <c:if test="${not empty errors.employeeName}">
                        <small class="field-error"><c:out value="${errors.employeeName}" /></small>
                    </c:if>
                </label>
                <label>
                    <span><fmt:message key="register.employee.field.firstName" /></span>
                    <input type="text" name="firstName" value="${formData.firstName}" maxlength="255" required />
                    <c:if test="${not empty errors.firstName}">
                        <small class="field-error"><c:out value="${errors.firstName}" /></small>
                    </c:if>
                </label>
                <label>
                    <span><fmt:message key="register.employee.field.lastName1" /></span>
                    <input type="text" name="lastName1" value="${formData.lastName1}" maxlength="255" required />
                    <c:if test="${not empty errors.lastName1}">
                        <small class="field-error"><c:out value="${errors.lastName1}" /></small>
                    </c:if>
                </label>
                <label>
                    <span><fmt:message key="register.employee.field.lastName2" /></span>
                    <input type="text" name="lastName2" value="${formData.lastName2}" maxlength="255" />
                </label>
            </section>

            <section class="form-section">
                <h2><fmt:message key="register.employee.section.contact" /></h2>
                <label>
                    <span><fmt:message key="register.employee.field.email" /></span>
                    <input type="email" name="email" value="${formData.email}" maxlength="255" required />
                    <c:if test="${not empty errors.email}">
                        <small class="field-error"><c:out value="${errors.email}" /></small>
                    </c:if>
                </label>
                <label>
                    <span><fmt:message key="register.employee.field.phone" /></span>
                    <input type="text" name="phone" value="${formData.phone}" maxlength="50" />
                </label>
            </section>

            <section class="form-section">
                <h2><fmt:message key="register.employee.section.assignment" /></h2>
                <label>
                    <span><fmt:message key="register.employee.field.headquarters" /></span>
                    <select name="headquartersId" required>
                        <option value=""><fmt:message key="filter.option.select" /></option>
                        <c:forEach var="hq" items="${headquarters}">
                            <option value="${hq.headquartersId}" <c:if test="${formData.headquartersId == hq.headquartersId}">selected</c:if>>
                                <c:out value="${hq.name}" />
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty errors.headquartersId}">
                        <small class="field-error"><c:out value="${errors.headquartersId}" /></small>
                    </c:if>
                </label>
                <label>
                    <span><fmt:message key="register.employee.field.role" /></span>
                    <select name="roleId" required>
                        <option value=""><fmt:message key="filter.option.select" /></option>
                        <c:forEach var="role" items="${roles}">
                            <option value="${role.roleId}" <c:if test="${formData.roleId == role.roleId}">selected</c:if>>
                                <c:out value="${role.roleName}" />
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty errors.roleId}">
                        <small class="field-error"><c:out value="${errors.roleId}" /></small>
                    </c:if>
                </label>
            </section>

            <section class="form-section">
                <h2><fmt:message key="register.employee.section.security" /></h2>
                <label>
                    <span><fmt:message key="register.employee.field.password" /></span>
                    <input type="password" name="password" minlength="6" maxlength="255" required />
                    <c:if test="${not empty errors.password}">
                        <small class="field-error"><c:out value="${errors.password}" /></small>
                    </c:if>
                </label>
            </section>

            <div class="form-actions">
                <button type="submit" class="btn primary"><fmt:message key="actions.save" /></button>
                <a class="btn secondary" href="${pageContext.request.contextPath}/private/home"><fmt:message key="actions.cancel" /></a>
            </div>
        </form>

        <c:if test="${not empty errors && empty errors.generalError}">
            <div class="alert alert-error">
                <ul>
                    <c:forEach var="entry" items="${errors}">
                        <li><c:out value="${entry.value}" /></li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
    </main>
</body>
</html>
