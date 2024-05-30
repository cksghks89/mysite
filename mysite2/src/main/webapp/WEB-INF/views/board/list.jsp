<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath }/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"/>
		<div id="content">
			<div id="board">
				<form id="search_form" action="${pageContext.servletContext.contextPath }/board?a=search" method="post">
					<input type="text" id="kwd" name="kwd" value="">
					<input type="submit" value="찾기">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>
				
				<c:choose>
					<c:when test='${empty list || fn:length(list) == 0 }'>
						<tr>
							<td colspan="5">조회된 게시글이 없습니다.</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:set var="countNo" value="${pageResult.startNo }" />
						<c:forEach items="${list}" var="vo" varStatus="status">
							<tr>
								<td>${countNo - status.index }</td>
								<td style="text-align:left; padding-left:${20 * vo.depth}px">
									<c:if test='${vo.depth > 0 }'>
										<img src='${pageContext.request.contextPath }/assets/images/reply.png' />	
									</c:if>
									<a href="${pageContext.request.contextPath }/board?a=view&no=${vo.no}&p=${pageResult.pageNo}&kwd=${pageResult.query}">${vo.title }</a>
								</td>
								<td>${vo.userName }</td>
								<td>${vo.hit }</td>
								<td>${vo.regDate }</td>
								
								<c:if test='${vo.userNo == authUser.no }'>
									<td><a href="${pageContext.servletContext.contextPath }/board?a=delete&no=${vo.no}&p=${pageResult.pageNo}&kwd=${pageResult.query}" class="del">삭제</a></td>
								</c:if>
							</tr>						
						</c:forEach>
					</c:otherwise>
				</c:choose>	
				</table>
				
				<!-- pager 추가 -->
				<div class="pager">
					<ul>
						<li>
							<c:choose>
								<c:when test="${pageResult.prev }">
									<a href="${pageContext.servletContext.contextPath }/board?a=list&p=${pageResult.beginPage - 1}&kwd=${pageResult.query}"><<</a>
								</c:when>
								<c:otherwise>
									<<
								</c:otherwise>
							</c:choose>
						</li>
						<li>
							<c:choose>
								<c:when test="${pageResult.prevTab }">
									<a href="${pageContext.servletContext.contextPath }/board?a=list&p=${pageResult.pageNo - 1}&kwd=${pageResult.query}">◀</a>
								</c:when>
								<c:otherwise>
									◀
								</c:otherwise>
							</c:choose>
						</li>
						
						<c:forEach begin='${pageResult.beginPage}' end="${pageResult.beginPage + pageResult.tabSize - 1}" var="no">
							<c:choose>
								<c:when test='${no == pageResult.pageNo }'>
									<li class="selected">
								</c:when>
								<c:otherwise>
									<li>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${no <= pageResult.endPage }">
									<a href="${pageContext.servletContext.contextPath }/board?a=list&p=${no}&kwd=${pageResult.query}">${no}</a>
								</c:when>
								<c:otherwise>
									${no }
								</c:otherwise>
							</c:choose>
							</li>
						</c:forEach>
						
						<li>
							<c:choose>
								<c:when test="${pageResult.nextTab }">
									<a href="${pageContext.servletContext.contextPath }/board?a=list&p=${pageResult.pageNo + 1}&kwd=${pageResult.query}">▶</a>
								</c:when>
								<c:otherwise>
									▶
								</c:otherwise>
							</c:choose>
						</li>
						<li>
							<c:choose>
								<c:when test="${pageResult.next }">
									<a href="${pageContext.servletContext.contextPath }/board?a=list&p=${pageResult.beginPage + pageResult.tabSize}&kwd=${pageResult.query}">>></a>
								</c:when>
								<c:otherwise>
									>>
								</c:otherwise>
							</c:choose>
						</li>
					</ul>
				</div>
				<!-- pager 추가 -->
				
				<c:if test='${not empty authUser }'>
					<div class="bottom">
						<a href="${pageContext.request.contextPath }/board?a=writeform&p=${pageResult.pageNo}&kwd=${pageResult.query}" id="new-book">글쓰기</a>
					</div>				
				</c:if>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="board"/>
		</c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp"/>
	</div>
</body>
</html>