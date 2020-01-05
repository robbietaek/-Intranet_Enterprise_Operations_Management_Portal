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
			<div class="row">
				<div class="col-lg-12">
					<div class="card card-default">
						<div class="card-body">
							<form>
								<div class="form-group">
									<label for="exampleFormControlInput1">제목</label>
									<h3 class="form-control">${b.subject}</h3>
								</div>

								<div class="form-group">
									<label for="exampleFormControlInput1">업무종류</label>
									<h3 class="form-control">${b.subtype}</h3>
								</div>
								
							<div class="form-group" >
									<label for="exampleFormControlTextarea1">내용</label>
									<h3 class="form">${b.content}</h3>
								</div>
								<hr>
								<div class="form-group">
									<label for="exampleFormControlFile1">첨부파일</label>
									<c:if test="${!empty b.file1 }">
										<a href="file/${b.file1}">${b.file1}</a>
									</c:if>
								</div>

							</form>

							<div class="form-footer pt-4 pt-5 mt-4 border-top text-right">
							<c:if test="${sessionScope.login==b.id}">
								<button type="button" class="btn btn-primary btn-default"
									onclick="location.href ='myworkeditForm.do?num=${b.num}&pageNum=${pageNum}&id=${b.id}'">수정하기</button>
								<button type="button" class="btn btn-secondary btn-default"
									onclick="location.href ='myworkdelete.do?num=${b.num}&pageNum=${pageNum}&id=${b.id}'">삭제하기</button>
							</c:if>
							</div>

						</div>

					</div>

				</div>
			</div>
		</div>
	</div>
</body>
</html>
