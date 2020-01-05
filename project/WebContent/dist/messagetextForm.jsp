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
							<div class="card-body">
								<form>
									<div class="form-group">
										<label for="exampleFormControlInput1">보낸사람</label>
										<h3 class="form-control" id="exampleFormControlInput1">${b.id}</h3>
									</div>
									<div class="form-group">
										<label for="exampleFormControlInput1">제목</label>
										<h3 class="form-control" id="exampleFormControlInput1">${b.subject}</h3>
									</div>
									<div class="form-group">
										<label for="exampleFormControlTextarea1">내용</label>
										<textarea disabled class="form-control" rows="7">${b.content}</textarea>
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
