<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
					style="color: white; text-align: center; margin-top: 2%">
					<div class="col-md-3"></div>
					<div class="col-md-6">
						<h3 style="color: red;">
							<b>${Successmsg}${Failmsg}${loginfaildbypasswod}${mailReport}</b>
						</h3>
					</div>
					<div class="col-md-3"></div>
				</div>
			</div>
			<div class="container container_border ">
				<div class="">
					<!-- <h2 align="center"
						style="margin-top: 5%; color: white; padding-top: 3%">Click here Send birthday Mails Now</h2> -->
					<div class="panel panel-default">
						<div class="panel-body" align="center" style="margin-top: 2%">
							<div class="row mt-4  table table-borderless ">
								<div class="col-sm-auto" align="center">
									<table class="col-sm-auto table-sm " align="center">
										<tr>
											<form class="mailSchedule" action="birthdayMailSchedule.do"
												method="get">
												<td colspan="2"><input
													class="btn btn-success btn-block" type="submit"
													value="Send BirthdayMails" title="Send Birthday mail for today"></td>
											</form>

											<form action="todayBirthdaysFromDb.do" method="get">
												<td colspan="2" align="center"><input
													class="btn btn-success btn-block" type="submit"
													value="Today's Birthday" title="Get all todays birthday details"></td>
											</form>

											<form action="weekBirthdaysFromDb.do" method="get">
												<td colspan="2" align="center"><input
													class="btn btn-success btn-block" type="submit"
													value="Week Birthdays" title="Get all current Week birthday details"></td>
											</form>

											<form action="monthBirthdaysFromDb.do" method="get">
												<td colspan="2" align="center"><input
													class="btn btn-success btn-block" type="submit"
													value="Month Birthdays" title="Get all this month birthday details"></td>
											</form>

											<form action="excelToDB.do" method="get">
												<td colspan="2" align="center"><input
													class="btn btn-success btn-block" type="submit"
													value="Excel to DB" title="Read data from Excel and write into database"></td>
											</form>

											<form action="addNewSubscriber.do" method="get">
											<td colspan="2" align="center"><button
													class="btn btn-success btn-block" type="submit"
													formaction="addSubscriber.jsp" title="Add new subscriber">Add Subscriber </button></td>
											</form>
											
											<td colspan="2" align="center"><button
													class="btn btn-success btn-block" target="_blank" type="submit" onclick=" window.open('https://docs.google.com/spreadsheets/d/1zIJN49Jlh2QKUIbC5JkFlquQUoydYctQ/', '_blank');"
													title="Excel file Link"> Excel Link </button></td>
										</tr>
									</table>
								</div>
							</div>
							<div class="row align-items-center  ">
								<table class="col-sm-auto " border-color="white" align="center" style="color: white">
									<c:forEach items="${birthdayList}" var="item">
										<tr align="center">
											<td class="col-md-auto" align="center"><c:out
													value="${item.getFullName()}" /></td>
											<td class="col-md-auto" align="center"><c:out
													value="${item.getEmail()}" /></td>
											<td class="col-md-auto" align="center"><c:out
													value="${item.getDob()}" /></td>
											<td class="col-md-auto" align="center"><c:out
													value="${item.getStatus()}" /></td>
										</tr>
									</c:forEach>
								</table>
							</div>			
						</div>
					</div>
				</div>
			</div>
		</div>
		<div>
			<nav class="navbar navbar-expand-md navbar-dark bg-dark"> </nav>
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