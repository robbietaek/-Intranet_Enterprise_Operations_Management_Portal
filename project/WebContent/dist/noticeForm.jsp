<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="decorator"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Goodee</title>
</head>
<body>
	<div class="content-wrapper">
		<div class="content">
			<div class="breadcrumb-wrapper">
				<h1>글쓰기</h1>

				<nav aria-label="breadcrumb">
					<ol class="breadcrumb p-0">
						<li class="breadcrumb-item"><a href="index.html"> <span
								class="mdi mdi-home"></span>
						</a>공지사항</li>
					</ol>
				</nav>
			</div>

			<div class="row">
				<div class="col-lg-12">
					<div class="card card-default">
						<div class="card-body">


							<form action="noticeForm.do" name="sf" method="post">
								<input type="hidden" name="pageNum" value="1">

								<div class="input-group">

									<select name="column">
										<option value="">선택하세요</option>
										<option value="subject">제목</option>
										<option value="content">내용</option>
										<option value="subject,content">제목+내용</option>
									</select>
									<script>
										document.sf.column.value = "${param.column}";
									</script>

									<input type="text" name="find" id="search-input"
										class="form-control" placeholder="검색" autofocus
										autocomplete="off" value="${param.find}" />
									<button type="submit" name="search" id="search-btn"
										class="btn btn-flat">
										<i class="mdi mdi-magnify"></i>
									</button>
								</div>
							</form>


								<table class="table table-bordered">
									<c:if test="${boardcnt == 0}">
										<tr>
											<td colspan="5">등록된 게시글이 없습니다.</td>
										</tr>
								</table>
								</c:if>
								<c:if test="${boardcnt > 0}">
									<thead>
										<tr>
											<th scope="col">번호</th>
											<th scope="col">제목</th>
											<th scope="col">첨부파일</th>
											<th scope="col">작성일</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="b" items="${list}">
											<tr>
												<td>${boardnum}</td>
												<c:set var="boardnum" value="${boardnum - 1}" />

												<td><a
													href="noticetextForm.do?num=${b.num}&pageNum=${pageNum}">${b.subject}</a></td>
												<td style="text-align: left"><c:if
														test="${!empty b.file1}">
														<a href="file/${b.file1}" style="text-decoration: none;">O</a>
													</c:if> <c:if test="${empty b.file1}">X</c:if> <c:if
														test="${b.grplevel > 0}">
														<c:forEach begin="1" end="${b.grplevel}">
         								 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							 			</c:forEach>└
  											</c:if></td>
												<td><fmt:formatDate var="rdate" value="${b.regdate}"
														pattern="yyyyMMdd" /> <c:if test="${today == rdate}">
														<fmt:formatDate value="${b.regdate}" pattern="HH:mm:ss" />
													</c:if> <c:if test="${today != rdate}">
														<fmt:formatDate value="${b.regdate}"
															pattern="yy-MM-dd HH:mm" />
													</c:if></td>
											</tr>
										</c:forEach>
									</tbody>
									</table>


									<ul class="pagination border-rounded justify-content-center">

										<c:if test="${pageNum <= 1 }">
											<li class="page-item"><a class="page-link" href="#"
												aria-label="Previous"> <span aria-hidden="true"
													class="mdi mdi-chevron-left"></span> <span class="sr-only">Previous</span>
											</a></li>
										</c:if>

										<c:if test="${pageNum > 1 }">
											<li class="page-item"><a class="page-link"
												href="noticeForm.do?pageNum=${pageNum-1}"
												aria-label="Previous"> <span aria-hidden="true"
													class="mdi mdi-chevron-left"></span> <span class="sr-only">Previous</span>
											</a></li>
										</c:if>


										<c:forEach var="a" begin="${startpage}" end="${endpage}">
											<c:if test="${a==pageNum}">
												<li class="page-item"><a class="page-link" href="">${a}</a>
												</li>
											</c:if>

											<c:if test="${a!=pageNum}">
												<li class="page-item"><a class="page-link"
													href="noticeForm.do?pageNum=${a}">${a}</a></li>
											</c:if>
										</c:forEach>



										<c:if test="${pageNum >= maxpage }">
											<li class="page-item"><a class="page-link" href="#"
												aria-label="Next"> <span aria-hidden="true"
													class="mdi mdi-chevron-right"></span> <span class="sr-only">Next</span>
											</a></li>
										</c:if>


										<c:if test="${pageNum < maxpage }">
											<li class="page-item"><a class="page-link"
												href="noticeForm.do?pageNum=${pageNum + 1}"
												aria-label="Next"> <span aria-hidden="true"
													class="mdi mdi-chevron-right"></span> <span class="sr-only">Next</span>
											</a></li>
										</c:if>

									</ul>


								</c:if>
							<c:if test="${sessionScope.login=='admin'}">
							<div class="text-right mb-3">
								<form action="noticewriteForm.do" name="nt"
									enctype="multipart/form-data" method="post">
									<button type="submit" class="btn btn-primary btn-default">글쓰기</button>
								</form>
							</div>
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
