/**
 * 
 */
$(document).ready(function() {
	allHide();
	$('.reports').hide();
});

//let getDropdown = () => fetch('https://raw.githubusercontent.com/xworkzodc/newsfeed/master/mailSender.json').then(data => data.json());

/*async function getDropdownDetails(stringValue) {
	var data = await getDropdown();
	if (stringValue == "NewsFeed") {
		$("#imgURL").attr("value", data.News_Feed);
		$("#newsImg").attr("src", data.News_Feed).load(function() {
			this.width;
		});

		$('#imgURL').val(data.News_Feed);
	}
	if (stringValue == "Birthday") {
		$("#imgURL1").attr("value", data.Birthday);
		$("#newsImg1").attr("src", data.Birthday).load(function() {
			this.width;
		});

		$('#imgURL').val(data.Birthday);
	}
}*/

function changeMsgType() {
	$('#msgTypeChng').val($('#mailType').val());
}

function getListDetails() {
	$.ajax({
		type: "get",
		url: "getListDetails.do?msgType=" + $('#mailType').val(),
		dataType: 'json',
		success: function(response) {
			var htmlTable = "";
			for (i = 0; i < response.lists.length; i++) {
				htmlTable = htmlTable + "<tr><td>" + (i + 1) +
					"</td><td>" + response.lists[i].name +
					"</td><td>" +
					response.lists[i].stats.member_count +
					"</td><td>" +
					response.lists[i].permission_reminder +
					"</td></tr>"
			}
			$('.listBody').empty();
			$('.listBody').append(htmlTable);
		}
	});
}

function allHide() {

	$('.birthday').hide();
	$('.reports').hide();
}

$(".birthday").ready(function() {
	$('input[type]').focusout(function(event) {
		$(this).valid();
	});
	$('#bithdayQt').focusout(function(event) {
		$(this).valid();
	});
	$(".birthday").validate({
		rules: {
			subName: {
				required: true,
			},
			listName: {
				required: true
			},
			imageURL: {
				required: true
			},
			birthdayQuotes: {
				required: true,

			}
		},
		messages: {
			subName: "Please Enter Subject Name",
			listName: "Please Enter List Name",
			imageURL: "Please Enter Image URL",
			birthdayQuotes: "Please Enter Birthday Quotes",
		}
	});
});

function clickFunc(className) {
	hideMainTab();
	$('.' + className).show();
}

function handleSelect(page) {
	window.location = page.value + ".jsp";
}

$(document).ready(function() {
	var table = $('#example').DataTable({
		responsive: true,
		scrollY: 500,
		deferRender: true,
		scroller: true
	});

	new $.fn.dataTable.FixedHeader(table);
});

document.querySelector("#today").valueAsDate = new Date();