<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" href="./img/Logo.png">

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css"
	integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS"
	crossorigin="anonymous">

<link rel="stylesheet" type="text/css" href="./css/index.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<script type="text/javascript" src="./js/index.js"></script>
</head>

<body>

	<div class="backgroundImage">
		<div class="header">
			<a href="" class="logo"><img src="./img/Logo.png" height="90px"></a>
			<div class="header-right">
				<h1>Om's Development Center</h1>
			</div>
			<div style="align-content:flex-basis; width: 10%; margin-bottom: 0.5%;">
				<button onclick="location.href='index.jsp'" type="button"
					class="btn btn-success btn-block"><i class="fa fa-home" aria-hidden="true"></i> Home</button>
			</div>
		</div>
		<div class="container">
			<div class="container" align="center">
				<div>
					<nav class="navbar navbar-expand-md navbar-dark bg-dark">
						<h3
							style="color: white; font-weight: 900; margin-left: 30%; align-items: center;">
							<b>X-Workz Bulk Mail App</b>
						</h3>
					</nav>
				</div>
				<div class="row"
					 text-align: center; margin-top: 2%">
					<div class="col-md-3"></div>
					<div class="col-md-6">
						<h3 style="color: red;">
							<b>${msg}</b>
						</h3>
					</div>
					<div class="col-md-3"></div>
				</div>
				<div class="row"
					style="color: white; text-align: center; margin-top: 2%">
					<div class="container container_border ">
						<div class="">
							<!-- <h2 align="center"
						style="margin-top: 5%; color: white; padding-top: 3%">Click here Send birthday Mails Now</h2> -->
							<div class="panel panel-default">
								<div class="panel-body" align="center" style="margin-top: 2%">
									<div class="row mt-3 mb-3">
										<div class="col-sm-4"></div>
										<div class="col-sm-4" align="center">
											
											<form action="addSubscriberToDB.do">
												<table class="col-md-6 table table-bordered table-dark"
													border="1" border-color="white" align="center"
													style="color: white">
													<tr>
														<td><label>Full Name</label></td>
														<td><input name="fullName" type="text"></td>
													</tr>
													<tr>
														<td><label>EmailId</label></td>
														<td><input name="emailId" type="text"></td>
													</tr>
													<tr>
														<td><label>Date Of Birth</label></td>
														<td><input name="dob" type="date"></td>
													</tr>
												</table>
												 <input class="btn btn-success btn-block" type="submit" value="Add">
											</form>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
		<div>
			<nav class="navbar navbar-expand-md navbar-dark bg-dark"></nav>
		</div>
	</div>
	<!-- Latest compiled JavaScript -->
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js"
		integrity="sha384-wHAiFfRlMFy6i5SRaxvfOCifBUQy1xHdJ/yoi7FRNXMRBu5WHdZYu1hA6ZOblgut"
		crossorigin="anonymous"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"
		integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k"
		crossorigin="anonymous"></script>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script type="text/javascript"
		src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.0/jquery.validate.min.js"></script>
</body>
</html>