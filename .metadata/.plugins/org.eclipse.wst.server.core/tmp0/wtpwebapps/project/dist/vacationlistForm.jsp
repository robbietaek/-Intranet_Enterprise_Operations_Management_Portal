<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="decorator"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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



<!--
    HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries
  -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
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
					<div class="col-xl-12">
						<div class="card card-default">
							<div class="card-header card-header-border-bottom">
								<h2>승인된 휴가현황</h2>
							</div>
							<div class="card-body">
								<form action="vacationlistForm.do" name="sf" method="post">
									<input type="hidden" name="pageNumV" value="1">

									<div class="input-group">

										<select name="columnV">
											<option value="">선택하세요</option>
											<option value="vacationtype">분류</option>
											<option value="startdate">시작일</option>
											<option value="enddate">종료일</option>
										</select>
										<script>
											document.sf.columnV.value = "${param.columnV}";
										</script>

										<input type="text" name="findV" id="search-input"
											class="form-control" placeholder="검색" autofocus
											autocomplete="off" value="${param.findV}" />
										<button type="submit" name="search" id="search-btn"
											class="btn btn-flat">
											<i class="mdi mdi-magnify"></i>
										</button>
									</div>
								</form>


								<table class="table table-striped">
									<c:if test="${vacationcnt == 0}">
										<tr>
											<td colspan="5">등록된 휴가가 없습니다.</td>
										</tr>
								</table>
								</c:if>
								<c:if test="${vacationcnt > 0}">
									<thead>
										<tr>
											<th scope="col">분류</th>
											<th scope="col">시작일</th>
											<th scope="col">종료일</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="b" items="${vlist}">
											<tr>
												<td>${b.vacationtype}</td>
												<td>${b.startdate}</td>
												<td>${b.enddate}</td>
											</tr>
										</c:forEach>
									</tbody>
									</table>


									<ul class="pagination border-rounded justify-content-center">

										<c:if test="${pageNumV <= 1 }">
											<li class="page-item"><a class="page-link" href="#"
												aria-label="Previous"> <span aria-hidden="true"
													class="mdi mdi-chevron-left"></span> <span class="sr-only">Previous</span>
											</a></li>
										</c:if>

										<c:if test="${pageNumV > 1 }">
											<li class="page-item"><a class="page-link"
												href="grp1Form.do?pageNum=${pageNumV-1}"
												aria-label="Previous"> <span aria-hidden="true"
													class="mdi mdi-chevron-left"></span> <span class="sr-only">Previous</span>
											</a></li>
										</c:if>


										<c:forEach var="a" begin="${startpageV}" end="${endpageV}">
											<c:if test="${a==pageNumV}">
												<li class="page-item"><a class="page-link" href="">${a}</a>
												</li>
											</c:if>

											<c:if test="${a!=pageNumV}">
												<li class="page-item"><a class="page-link"
													href="grp1Form.do?pageNum=${a}">${a}</a></li>
											</c:if>
										</c:forEach>



										<c:if test="${pageNumV >= maxpageV }">
											<li class="page-item"><a class="page-link" href="#"
												aria-label="Next"> <span aria-hidden="true"
													class="mdi mdi-chevron-right"></span> <span class="sr-only">Next</span>
											</a></li>
										</c:if>


										<c:if test="${pageNumV < maxpageV }">
											<li class="page-item"><a class="page-link"
												href="grp1Form.do?pageNum=${pageNumV + 1}" aria-label="Next">
													<span aria-hidden="true" class="mdi mdi-chevron-right"></span>
													<span class="sr-only">Next</span>
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
