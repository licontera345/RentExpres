<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentLocale" value="${not empty sessionScope.locale ? sessionScope.locale : 'es'}" />
<fmt:setLocale value="${currentLocale}" />
<fmt:setBundle basename="/WEB-INF/i18n/messages" />
<!DOCTYPE html>
<html lang="${currentLocale}">
<head>
    <meta charset="UTF-8" />
    <title><fmt:message key="vehicle.management.title" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/private.css" />
</head>
<body>
    <header>
        <h1><fmt:message key="vehicle.management.heading" /></h1>
        <nav>
            <a href="${pageContext.request.contextPath}/private/home"><fmt:message key="navigation.backToDashboard" /></a>
        </nav>
    </header>

    <section class="filters">
        <h2><fmt:message key="vehicle.management.filters.title" /></h2>
        <form method="get" action="${pageContext.request.contextPath}/private/vehicles">
            <div class="form-grid">
                <label>
                    <span><fmt:message key="vehicle.field.brand" /></span>
                    <input type="text" name="brand" value="${criteria.brand}" />
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.model" /></span>
                    <input type="text" name="model" value="${criteria.model}" />
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.category" /></span>
                    <select name="categoryId">
                        <option value=""><fmt:message key="filter.option.all" /></option>
                        <c:forEach var="category" items="${vehicleCategories}">
                            <option value="${category.categoryId}" <c:if test="${criteria.categoryId == category.categoryId}">selected</c:if>>
                                <c:out value="${category.name}" />
                            </option>
                        </c:forEach>
                    </select>
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.status" /></span>
                    <select name="vehicleStatusId">
                        <option value=""><fmt:message key="filter.option.all" /></option>
                        <c:forEach var="status" items="${vehicleStatuses}">
                            <option value="${status.vehicleStatusId}" <c:if test="${criteria.vehicleStatusId == status.vehicleStatusId}">selected</c:if>>
                                <c:out value="${status.name}" />
                            </option>
                        </c:forEach>
                    </select>
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.headquarters" /></span>
                    <select name="headquartersId">
                        <option value=""><fmt:message key="filter.option.all" /></option>
                        <c:forEach var="hq" items="${headquarters}">
                            <option value="${hq.headquartersId}" <c:if test="${criteria.currentHeadquartersId == hq.headquartersId}">selected</c:if>>
                                <c:out value="${hq.name}" />
                            </option>
                        </c:forEach>
                    </select>
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.year.from" /></span>
                    <input type="number" name="manufactureYearFrom" value="${criteria.manufactureYearFrom}" min="1950" max="2100" />
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.year.to" /></span>
                    <input type="number" name="manufactureYearTo" value="${criteria.manufactureYearTo}" min="1950" max="2100" />
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.dailyPrice.min" /></span>
                    <input type="number" step="0.01" name="dailyPriceMin" value="${criteria.dailyPriceMin}" min="0" />
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.dailyPrice.max" /></span>
                    <input type="number" step="0.01" name="dailyPriceMax" value="${criteria.dailyPriceMax}" min="0" />
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.pageSize" /></span>
                    <select name="pageSize">
                        <c:forEach var="size" items="${pageSizes}">
                            <option value="${size}" <c:if test="${criteria.pageSize == size}">selected</c:if>>${size}</option>
                        </c:forEach>
                    </select>
                </label>
            </div>
            <input type="hidden" name="orderBy" value="${criteria.orderBy}" />
            <input type="hidden" name="orderDir" value="${criteria.orderDir}" />
            <div class="actions">
                <button type="submit" class="btn primary"><fmt:message key="actions.search" /></button>
                <a class="btn secondary" href="${pageContext.request.contextPath}/private/vehicles"><fmt:message key="actions.reset" /></a>
                <a class="btn success" href="${pageContext.request.contextPath}/private/vehicles?action=create"><fmt:message key="vehicle.management.new" /></a>
            </div>
        </form>
    </section>

    <section class="results">
        <h2><fmt:message key="vehicle.management.results.title" /></h2>
        <c:if test="${total == 0}">
            <p><fmt:message key="vehicle.management.results.empty" /></p>
        </c:if>
        <c:if test="${total > 0}">
            <table class="data-table">
                <thead>
                    <tr>
                        <th><fmt:message key="vehicle.field.id" /></th>
                        <th><fmt:message key="vehicle.field.brand" /></th>
                        <th><fmt:message key="vehicle.field.model" /></th>
                        <th><fmt:message key="vehicle.field.category" /></th>
                        <th><fmt:message key="vehicle.field.year" /></th>
                        <th><fmt:message key="vehicle.field.dailyPrice" /></th>
                        <th><fmt:message key="vehicle.field.mileage" /></th>
                        <th><fmt:message key="vehicle.field.status" /></th>
                        <th><fmt:message key="table.column.actions" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="vehicle" items="${vehicles}">
                        <tr>
                            <td><c:out value="${vehicle.vehicleId}" /></td>
                            <td><c:out value="${vehicle.brand}" /></td>
                            <td><c:out value="${vehicle.model}" /></td>
                            <td><c:out value="${vehicle.vehicleCategory.name}" /></td>
                            <td><c:out value="${vehicle.manufactureYear}" /></td>
                            <td><c:out value="${vehicle.dailyPrice}" /></td>
                            <td><c:out value="${vehicle.currentMileage}" /></td>
                            <td><c:out value="${vehicle.vehicleStatus.name}" /></td>
                            <td class="table-actions">
                                <a class="btn small" href="${pageContext.request.contextPath}/private/vehicles?action=edit&amp;vehicleId=${vehicle.vehicleId}"><fmt:message key="actions.edit" /></a>
                                <form method="post" action="${pageContext.request.contextPath}/private/vehicles/delete" class="inline">
                                    <input type="hidden" name="vehicleId" value="${vehicle.vehicleId}" />
                                    <button type="submit" class="btn small danger" onclick="return confirm('<fmt:message key="vehicle.management.delete.confirm" />');"><fmt:message key="actions.delete" /></button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div class="pagination">
                <span><fmt:message key="pagination.summary">
                    <fmt:param value="${fromRow}" />
                    <fmt:param value="${toRow}" />
                    <fmt:param value="${total}" />
                </fmt:message></span>
                <div class="pagination-controls">
                    <c:if test="${hasPrev}">
                        <c:url var="prevUrl" value="/private/vehicles">
                            <c:param name="page" value="${currentPage - 1}" />
                            <c:param name="pageSize" value="${criteria.pageSize}" />
                            <c:param name="brand" value="${criteria.brand}" />
                            <c:param name="model" value="${criteria.model}" />
                            <c:param name="categoryId" value="${criteria.categoryId}" />
                            <c:param name="vehicleStatusId" value="${criteria.vehicleStatusId}" />
                            <c:param name="headquartersId" value="${criteria.currentHeadquartersId}" />
                            <c:param name="manufactureYearFrom" value="${criteria.manufactureYearFrom}" />
                            <c:param name="manufactureYearTo" value="${criteria.manufactureYearTo}" />
                            <c:param name="dailyPriceMin" value="${criteria.dailyPriceMin}" />
                            <c:param name="dailyPriceMax" value="${criteria.dailyPriceMax}" />
                            <c:param name="orderBy" value="${criteria.orderBy}" />
                            <c:param name="orderDir" value="${criteria.orderDir}" />
                        </c:url>
                        <a class="btn small" href="${pageContext.request.contextPath}${prevUrl}"><fmt:message key="pagination.previous" /></a>
                    </c:if>
                    <span><fmt:message key="pagination.pageOf">
                        <fmt:param value="${currentPage}" />
                        <fmt:param value="${totalPages}" />
                    </fmt:message></span>
                    <c:if test="${hasNext}">
                        <c:url var="nextUrl" value="/private/vehicles">
                            <c:param name="page" value="${currentPage + 1}" />
                            <c:param name="pageSize" value="${criteria.pageSize}" />
                            <c:param name="brand" value="${criteria.brand}" />
                            <c:param name="model" value="${criteria.model}" />
                            <c:param name="categoryId" value="${criteria.categoryId}" />
                            <c:param name="vehicleStatusId" value="${criteria.vehicleStatusId}" />
                            <c:param name="headquartersId" value="${criteria.currentHeadquartersId}" />
                            <c:param name="manufactureYearFrom" value="${criteria.manufactureYearFrom}" />
                            <c:param name="manufactureYearTo" value="${criteria.manufactureYearTo}" />
                            <c:param name="dailyPriceMin" value="${criteria.dailyPriceMin}" />
                            <c:param name="dailyPriceMax" value="${criteria.dailyPriceMax}" />
                            <c:param name="orderBy" value="${criteria.orderBy}" />
                            <c:param name="orderDir" value="${criteria.orderDir}" />
                        </c:url>
                        <a class="btn small" href="${pageContext.request.contextPath}${nextUrl}"><fmt:message key="pagination.next" /></a>
                    </c:if>
                </div>
            </div>
        </c:if>
    </section>

    <section class="form-section">
        <c:choose>
            <c:when test="${not empty vehicleForm and empty vehicleForm.vehicleId}">
                <h2><fmt:message key="vehicle.management.form.create" /></h2>
            </c:when>
            <c:when test="${not empty vehicleForm and not empty vehicleForm.vehicleId}">
                <h2><fmt:message key="vehicle.management.form.edit">
                    <fmt:param value="${vehicleForm.vehicleId}" />
                </fmt:message></h2>
            </c:when>
            <c:otherwise>
                <h2><fmt:message key="vehicle.management.form.title" /></h2>
            </c:otherwise>
        </c:choose>
        <c:if test="${not empty vehicleForm}">
            <form method="post" action="${pageContext.request.contextPath}/private/vehicles/save" enctype="multipart/form-data" class="form-grid">
                <input type="hidden" name="vehicleId" value="${vehicleForm.vehicleId}" />
                <label>
                    <span><fmt:message key="vehicle.field.brand" /></span>
                    <input type="text" name="brand" value="${vehicleForm.brand}" required />
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.model" /></span>
                    <input type="text" name="model" value="${vehicleForm.model}" required />
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.year" /></span>
                    <input type="number" name="manufactureYear" value="${vehicleForm.manufactureYear}" min="1950" max="2100" required />
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.dailyPrice" /></span>
                    <input type="number" step="0.01" name="dailyPrice" value="${vehicleForm.dailyPrice}" min="0" required />
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.licensePlate" /></span>
                    <input type="text" name="licensePlate" value="${vehicleForm.licensePlate}" required />
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.vin" /></span>
                    <input type="text" name="vinNumber" value="${vehicleForm.vinNumber}" required />
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.mileage" /></span>
                    <input type="number" name="currentMileage" value="${vehicleForm.currentMileage}" min="0" required />
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.status" /></span>
                    <select name="vehicleStatusId" required>
                        <option value=""><fmt:message key="filter.option.select" /></option>
                        <c:forEach var="status" items="${vehicleStatuses}">
                            <option value="${status.vehicleStatusId}" <c:if test="${vehicleForm.vehicleStatusId == status.vehicleStatusId}">selected</c:if>>
                                <c:out value="${status.name}" />
                            </option>
                        </c:forEach>
                    </select>
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.category" /></span>
                    <select name="categoryId" required>
                        <option value=""><fmt:message key="filter.option.select" /></option>
                        <c:forEach var="category" items="${vehicleCategories}">
                            <option value="${category.categoryId}" <c:if test="${vehicleForm.categoryId == category.categoryId}">selected</c:if>>
                                <c:out value="${category.name}" />
                            </option>
                        </c:forEach>
                    </select>
                </label>
                <label>
                    <span><fmt:message key="vehicle.field.headquarters" /></span>
                    <select name="headquartersId" required>
                        <option value=""><fmt:message key="filter.option.select" /></option>
                        <c:forEach var="hq" items="${headquarters}">
                            <option value="${hq.headquartersId}" <c:if test="${vehicleForm.currentHeadquartersId == hq.headquartersId}">selected</c:if>>
                                <c:out value="${hq.name}" />
                            </option>
                        </c:forEach>
                    </select>
                </label>
                <fieldset class="images-fieldset">
                    <legend><fmt:message key="vehicle.field.images" /></legend>
                    <c:choose>
                        <c:when test="${not empty vehicleFormImages}">
                            <p><fmt:message key="vehicle.field.images.keep" /></p>
                            <ul class="image-list">
                                <c:forEach var="imageName" items="${vehicleFormImages}">
                                    <li>
                                        <label>
                                            <input type="checkbox" name="existingImage" value="${imageName}" checked />
                                            <c:out value="${imageName}" />
                                        </label>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:when>
                        <c:otherwise>
                            <p><fmt:message key="vehicle.field.images.empty" /></p>
                        </c:otherwise>
                    </c:choose>
                    <div class="upload">
                        <label class="file-input">
                            <span><fmt:message key="vehicle.field.images.upload" /></span>
                            <input type="file" name="images" accept="image/*" multiple />
                        </label>
                    </div>
                </fieldset>
                <div class="form-actions">
                    <button type="submit" class="btn primary"><fmt:message key="actions.save" /></button>
                    <a class="btn secondary" href="${pageContext.request.contextPath}/private/vehicles"><fmt:message key="actions.cancel" /></a>
                </div>
            </form>
        </c:if>
        <c:if test="${empty vehicleForm}">
            <p><fmt:message key="vehicle.management.form.helper" /></p>
        </c:if>
    </section>
</body>
</html>
