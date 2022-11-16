$(document).ready(function () {
  $.postJSON = function (url, data) {
    return jQuery.ajax({
      type: "POST",
      url: url,
      contentType: "application/json; charset=utf-8",
      data: JSON.stringify(data),
      dataType: "json",
    });
  };

  // Init actions
  var selectArray = $("#users-table select");
  for (var i = 0; i < selectArray.length; i++)
    $(selectArray[i]).val($(selectArray[i]).attr("value"));

  $("#users-table select").on("change", function () {
    var siblings = $(this).parent().siblings();
    $.postJSON(
      "/api/users/modify/" + $(siblings)[$(siblings).length - 1].textContent,
      {
        role: $(this).val(),
      }
    );
  });

  $("#users-table input").on("click", function () {
    if ($(this).val() === "true") $(this).val("false");
    else $(this).val("true");

    var siblings = $(this).parents("td").siblings();
    $.postJSON(
      "/api/users/modify/" + $(siblings)[$(siblings).length - 1].textContent,
      {
        active: $(this).val(),
      }
    );
  });
});
