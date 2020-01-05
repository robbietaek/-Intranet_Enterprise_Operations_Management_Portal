<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="decorator"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta name="description"
	content="Sleek Dashboard - Free Bootstrap 4 Admin Dashboard Template and UI Kit. It is very powerful bootstrap admin dashboard, which allows you to build products like admin panels, content management systems and CRMs etc.">


<title>쪽지함</title>

<!-- GOOGLE FONTS -->
<link
	href="https://fonts.googleapis.com/css?family=Montserrat:400,500|Poppins:400,500,600,700|Roboto:400,500"
	rel="stylesheet" />
<link
	href="https://cdn.materialdesignicons.com/4.4.95/css/materialdesignicons.min.css"
	rel="stylesheet" />


<!-- PLUGINS CSS STYLE -->
<link href="assets/plugins/nprogress/nprogress.css" rel="stylesheet" />



<!-- SLEEK CSS -->
<link id="sleek-css" rel="stylesheet" href="assets/css/sleek.css" />

<!-- FAVICON -->
<link href="assets/img/favicon.png" rel="shortcut icon" />




<script src="assets/plugins/nprogress/nprogress.js"></script>
<script type="text/javascript">
	function opener_close() {
		self.close();
	}
</script>
</head>


<body class="header-fixed sidebar-fixed sidebar-dark header-light"
	id="body">

	<script>
		NProgress.configure({
			showSpinner : false
		});
		NProgress.start();
	</script>



	<div class="wrapper">
		<div class="content-wrapper">
			<div class="content">


				<div class="row">
					<div class="col-lg-12">
						<div class="card card-default">
							<div class="card-header card-header-border-bottom">
								<h2>보낸 쪽지함</h2>
							</div>
							<div class="card-body">



								<form action="sentmessageboxForm.do" name="sf" method="post">
									<input type="hidden" name="pageNum" value="1">

									<div class="input-group">

										<select name="column">
											<option value="">선택하세요</option>
											<option value="subject">제목</option>
											<option value="content">내용</option>
											<option value="recvid">아이디</option>
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

											<th scope="col">제목</th>
											<th scope="col">받은사람</th>
											<th scope="col">작성일</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="b" items="${list}">
											<tr>
												<td><a
													href="messagetextForm.do?num=${b.num}&pageNum=${pageNum}">${b.subject}</a></td>
												<td>${b.recvid}</td>
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
												href="sentmessageboxForm.do?pageNum=${pageNum-1}"
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
													href="sentmessageboxForm.do?pageNum=${a}">${a}</a></li>
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
												href="sentmessageboxForm.do?pageNum=${pageNum + 1}"
												aria-label="Next"> <span aria-hidden="true"
													class="mdi mdi-chevron-right"></span> <span class="sr-only">Next</span>
											</a></li>
										</c:if>

									</ul>
								</c:if>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script src="assets/plugins/jquery/jquery.min.js"></script>
	<script src="assets/plugins/slimscrollbar/jquery.slimscroll.min.js"></script>
	<script src="assets/plugins/jekyll-search.min.js"></script>
	<script src="assets/js/sleek.bundle.js"></script>

</body>
</html>
