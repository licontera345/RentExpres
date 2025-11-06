<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentLocale" value="${not empty sessionScope.locale ? sessionScope.locale : 'es'}" />
<fmt:setLocale value="${currentLocale}" />
<fmt:setBundle basename="/WEB-INF/i18n/messages" />
<!DOCTYPE html>
<html lang="${currentLocale}">
<head>
    <meta charset="UTF-8" />
    <title><fmt:message key="register.user.title" /></title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f6fb;
            color: #1f2933;
        }
        .page-wrapper {
            max-width: 960px;
            margin: 0 auto;
            padding: 2rem 1.5rem 3rem;
        }
        h1 {
            margin-bottom: 0.5rem;
            font-size: 2rem;
        }
        p.page-subtitle {
            margin-top: 0;
            margin-bottom: 2rem;
            color: #52606d;
        }
        form {
            background: #ffffff;
            border-radius: 12px;
            padding: 2rem;
            box-shadow: 0 8px 24px rgba(15, 23, 42, 0.08);
        }
        fieldset {
            border: none;
            margin: 0 0 1.5rem;
            padding: 0;
        }
        legend {
            font-weight: 600;
            margin-bottom: 1rem;
            font-size: 1.1rem;
            color: #1f2933;
        }
        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            gap: 1.5rem;
        }
        .form-group {
            display: flex;
            flex-direction: column;
        }
        label {
            font-weight: 600;
            margin-bottom: 0.5rem;
            color: #334155;
        }
        input,
        select {
            border: 1px solid #cbd2d9;
            border-radius: 8px;
            padding: 0.75rem 1rem;
            font-size: 1rem;
            color: #1f2933;
        }
        input:focus,
        select:focus {
            outline: none;
            border-color: #2563eb;
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.15);
        }
        .form-actions {
            text-align: right;
            margin-top: 2rem;
        }
        .btn-primary {
            background: #2563eb;
            color: #ffffff;
            border: none;
            border-radius: 8px;
            padding: 0.85rem 2.5rem;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
        }
        .btn-primary:hover {
            background: #1d4ed8;
        }
        .alert {
            padding: 1rem 1.25rem;
            border-radius: 10px;
            margin-bottom: 1.5rem;
            font-weight: 600;
        }
        .alert-success {
            background: #ecfdf5;
            color: #047857;
            border: 1px solid #34d399;
        }
        .alert-error {
            background: #fef2f2;
            color: #b91c1c;
            border: 1px solid #fca5a5;
        }
        .error-message {
            margin-top: 0.5rem;
            font-size: 0.9rem;
            color: #b91c1c;
        }
    </style>
</head>
<body>
    <div class="page-wrapper">
        <h1><fmt:message key="register.user.heading" /></h1>
        <p class="page-subtitle"><fmt:message key="register.user.subtitle" /></p>

        <c:if test="${not empty requestScope["flashSuccess"]}">
            <div class="alert alert-success">
                <c:out value="${requestScope["flashSuccess"]}" />
            </div>
        </c:if>
        <c:if test="${not empty requestScope["flashError"]}">
            <div class="alert alert-error">
                <c:out value="${requestScope["flashError"]}" />
            </div>
        </c:if>

        <c:set var="formData" value="${requestScope['formData']}" />
        <c:set var="errors" value="${requestScope['errors']}" />
        <c:set var="selectedProvince" value="${formData['provinceId']}" />
        <c:choose>
            <c:when test="${not empty selectedProvince}">
                <fmt:parseNumber value="${selectedProvince}" type="number" var="selectedProvinceId" />
            </c:when>
            <c:otherwise>
                <c:set var="selectedProvinceId" value="${null}" />
            </c:otherwise>
        </c:choose>

        <c:url var="formAction" value="/public/users/register" />
        <form method="post" action="${formAction}">
            <fieldset>
                <legend><fmt:message key="register.user.section.identity" /></legend>
                <div class="form-grid">
                    <div class="form-group">
                        <label for="firstName"><fmt:message key="register.user.field.firstName" /></label>
                        <input id="firstName" name="firstName" type="text" value="${formData['firstName']}" required />
                        <c:if test="${not empty errors['firstName']}">
                            <div class="error-message"><c:out value="${errors['firstName']}" /></div>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label for="lastName1"><fmt:message key="register.user.field.lastName1" /></label>
                        <input id="lastName1" name="lastName1" type="text" value="${formData['lastName1']}" required />
                        <c:if test="${not empty errors['lastName1']}">
                            <div class="error-message"><c:out value="${errors['lastName1']}" /></div>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label for="lastName2"><fmt:message key="register.user.field.lastName2" /></label>
                        <input id="lastName2" name="lastName2" type="text" value="${formData['lastName2']}" />
                    </div>
                </div>
            </fieldset>

            <fieldset>
                <legend><fmt:message key="register.user.section.contact" /></legend>
                <div class="form-grid">
                    <div class="form-group">
                        <label for="email"><fmt:message key="register.user.field.email" /></label>
                        <input id="email" name="email" type="email" value="${formData['email']}" required />
                        <c:if test="${not empty errors['email']}">
                            <div class="error-message"><c:out value="${errors['email']}" /></div>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label for="phone"><fmt:message key="register.user.field.phone" /></label>
                        <input id="phone" name="phone" type="tel" value="${formData['phone']}" />
                    </div>
                </div>
            </fieldset>

            <fieldset>
                <legend><fmt:message key="register.user.section.access" /></legend>
                <div class="form-grid">
                    <div class="form-group">
                        <label for="password"><fmt:message key="register.user.field.password" /></label>
                        <input id="password" name="password" type="password" required />
                        <c:if test="${not empty errors['password']}">
                            <div class="error-message"><c:out value="${errors['password']}" /></div>
                        </c:if>
                    </div>
                </div>
            </fieldset>

            <fieldset>
                <legend><fmt:message key="register.user.section.address" /></legend>
                <div class="form-grid">
                    <div class="form-group">
                        <label for="street"><fmt:message key="register.user.field.street" /></label>
                        <input id="street" name="street" type="text" value="${formData['street']}" required />
                        <c:if test="${not empty errors['street']}">
                            <div class="error-message"><c:out value="${errors['street']}" /></div>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label for="addressNumber"><fmt:message key="register.user.field.addressNumber" /></label>
                        <input id="addressNumber" name="addressNumber" type="text" value="${formData['addressNumber']}" required />
                        <c:if test="${not empty errors['addressNumber']}">
                            <div class="error-message"><c:out value="${errors['addressNumber']}" /></div>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label for="provinceId"><fmt:message key="register.user.field.province" /></label>
                        <select id="provinceId" name="provinceId" required>
                            <option value=""><fmt:message key="register.user.field.selectOption" /></option>
                            <c:forEach var="province" items="${provinces}">
                                <option value="${province.provinceId}" <c:if test="${selectedProvinceId == province.provinceId}">selected</c:if>>
                                    <c:out value="${province.provinceName}" />
                                </option>
                            </c:forEach>
                        </select>
                        <c:if test="${not empty errors['provinceId']}">
                            <div class="error-message"><c:out value="${errors['provinceId']}" /></div>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label for="cityId"><fmt:message key="register.user.field.city" /></label>
                        <select id="cityId" name="cityId" required>
                            <option value=""><fmt:message key="register.user.field.selectOption" /></option>
                            <c:forEach var="city" items="${cities}">
                                <c:if test="${empty selectedProvinceId || city.provinceId == selectedProvinceId}">
                                    <option value="${city.cityId}" <c:if test="${formData['cityId'] == city.cityId}">selected</c:if>>
                                        <c:out value="${city.cityName}" />
                                    </option>
                                </c:if>
                            </c:forEach>
                        </select>
                        <c:if test="${not empty errors['cityId']}">
                            <div class="error-message"><c:out value="${errors['cityId']}" /></div>
                        </c:if>
                    </div>
                </div>
            </fieldset>

            <c:if test="${not empty errors['generalError']}">
                <div class="alert alert-error">
                    <c:out value="${errors['generalError']}" />
                </div>
            </c:if>

            <div class="form-actions">
                <button type="submit" class="btn-primary">
                    <fmt:message key="register.user.submit" />
                </button>
            </div>
        </form>
    </div>
</body>
</html>
