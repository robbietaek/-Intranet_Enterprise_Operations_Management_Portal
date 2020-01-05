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
	
	function sendto4check() {
		if (st4.subject.value == "") {
			alert("제목을 입력하세요");
			st4.subject.focus();
			return false;
		}else if (st4.content.value == "") {
			alert("내용을 입력하세요");
			st4.content.focus();
			return false;
		}else{
			st4.submit();
		}
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
					<div class="col-lg-6">
						<div class="card card-default">
							<div class="card-header card-header-border-bottom">
								<h2>부서원 전체에게 보내기</h2>
							</div>
							<div class="card-body">
								<form action = "sendto4.do" name = "st4" method = "POST">
									<div class="form-group">
										<label for="exampleFormControlTextarea1">제목을 입력하세요</label>
										<textarea class="form-control"
											name = "subject" rows="1"></textarea>
									</div>
									<div class="form-group">
										<label for="exampleFormControlTextarea1">내용을 입력하세요.
											(최대300글자)</label>
										<textarea class="form-control"
											name = "content" rows="7"></textarea>
									</div>
									<div class="form-footer pt-4 pt-5 mt-4 border-top">
										<button type="button" class="btn btn-primary btn-default" onclick="sendto4check()">보내기</button>
										<button type="button" class="btn btn-secondary btn-default"
											onclick="opener_close()">취소하기</button>
									</div>
								</form>
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
