<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<table class="table table-bordered  table-hover boardlist">
	<thead>
		<tr class="success">
			<th>번호</th>
			<th class="title">제목</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>조회</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="pvo" items="${requestScope.lvo.list}">
			<tr>
				<td>${pvo.no }</td>
				<td><c:choose>
						<c:when test="${sessionScope.mvo!=null}">
							<a
								href="${pageContext.request.contextPath}/front?command=PostDetail&no=${pvo.no }">
								${pvo.title }</a>
						</c:when>
						<c:otherwise>
				${pvo.title }
				</c:otherwise>
					</c:choose></td>
				<td>${pvo.memberVO.name }</td>
				<td>${pvo.timePosted }</td>
				<td>${pvo.hits }</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<%-- paging 처리  --%>
<c:set var="pb" value="${requestScope.lvo.pagingBean}" />
<div class="pagingArea">
<ul class="pagination">
<c:if test="${pb.previousPageGroup}">
	<li>
		<a href="front?command=List&pageNo=${pb.startPageOfPageGroup-1}">&laquo;</a>
	</li>
</c:if>
<c:forEach var="i" begin="${pb.startPageOfPageGroup}" 
end="${pb.endPageOfPageGroup}">
<c:choose>
	<c:when test="${pb.nowPage!=i}">
		<li><a href="front?command=List&pageNo=${i}">${i}</a></li>
	</c:when>
	<c:otherwise>
		<li class="active"><a href="#">${i}</a></li>
	</c:otherwise>
</c:choose>
</c:forEach>
<c:if test="${pb.nextPageGroup}">
	<li>
		<a href="front?command=List&pageNo=${pb.endPageOfPageGroup+1}">&raquo;</a>
	</li>
</c:if>
</ul>
</div>






























