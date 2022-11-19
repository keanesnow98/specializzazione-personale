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

  $("#specialties-table tbody")
    .not($(".switch"))
    .on("click", "tr", function (event) {
      if ($(this).hasClass("table-active")) {
        $(this).removeClass("table-active");
        $("#text").val("");
        $("#id").remove();
        $("#add-or-modify").text("Add");
      } else {
        $(".table-active").removeClass("table-active");
        $(this).addClass("table-active");
        var $data = $(this)[0].children;
        $("#text").val($data[0].textContent);
        $("#id").remove();
        $("#text").before(
          `<input type="text" id="id" name="id" hidden value="${$data[2].textContent}" />`
        );
        $("#add-or-modify").text("Modify");
      }
    });

  $("#specialties-table input").on("click", function () {
    if ($(this).val() === "true") $(this).val("false");
    else $(this).val("true");

    var siblings = $(this).parents("td").siblings();
    $.postJSON(
      "/specializzazionevvf/api/specialties/modify/" +
        $(siblings)[$(siblings).length - 1].textContent,
      {
        disabled: $(this).val(),
      }
    );
  });
});
