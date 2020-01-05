<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="decorator"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta name="description"
	content="Sleek Dashboard - Free Bootstrap 4 Admin Dashboard Template and UI Kit. It is very powerful bootstrap admin dashboard, which allows you to build products like admin panels, content management systems and CRMs etc.">


<title></title>

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
function pwsearch() {
	var op = "width=600, height=600, left = 600,top=200";
	open("pwsearchForm.do", "", op);
}
</script>

</head>

</head>
<body class="" id="body">
	<div
		class="container d-flex flex-column justify-content-between vh-100">
		<div class="row justify-content-center mt-5">
			<div class="col-xl-5 col-lg-6 col-md-10">
				<div class="card">
					<div class="card-header bg-primary">
						<div class="app-brand">
							<svg class="brand-icon" xmlns="http://www.w3.org/2000/svg"
								preserveAspectRatio="xMidYMid" width="30" height="33"
								viewBox="0 0 30 33">
                    <g fill="none" fill-rule="evenodd">
                      <path class="logo-fill-blue" fill="#7DBCFF"
									d="M0 4v25l8 4V0zM22 4v25l8 4V0z" />
                      <path class="logo-fill-white" fill="#FFF"
									d="M11 4v25l8 4V0z" />
                    </g>
                  </svg>
							<span class="brand-name">Goodee Academy</span> </a>
						</div>
					</div>
					<div class="card-body p-5">

						<h4 class="text-dark mb-5">로그인</h4>
						<form action="login.do" name="f" method="POST">
							<div class="row">
								<div class="form-group col-md-12 mb-4">
									<input type="text" class="form-control input-lg" name = "id"
										aria-describedby="emailHelp" placeholder="사원번호">
								</div>
								<div class="form-group col-md-12 ">
									<input type="password" class="form-control input-lg"
										name = "pass" placeholder="비밀번호">
								</div>
								<div class="col-md-12">
									<div class="d-flex my-2 justify-content-between">
										<div class="d-inline-block mr-3"></div>

									</div>
									<button type="submit"
										class="btn btn-lg btn-primary btn-block mb-4">Sign In</button>
						</form>
							<p>
								비밀번호를 잊어버리셨나요? <a class="text-blue" href="" onclick = "pwsearch()">비밀번호 찾기</a>

							</p>
						<br>
						<form action="sign-up.do" name="p" method="POST">
							<p>
								아이디가 없으신가요? <a class="text-blue" href="sign-up.do"> 회원가입</a>
							</p>
						</form>
					</div>
				</div>
				</form>
			</div>
		</div>
	</div>
	</div>
	<div class="copyright pl-0">
		<p class="text-center">&copy; 2019 개인프로젝트 김경택</p>
	</div>
	</div>

</body>
</html>