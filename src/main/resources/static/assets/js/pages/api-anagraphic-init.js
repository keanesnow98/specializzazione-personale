var idOfSel = "";

$(document).ready(function () {
  "use strict";

  $("#anagraphic-table").DataTable({
    select: { style: "single" },
    language: {
      paginate: {
        previous: "<i class='mdi mdi-chevron-left'>",
        next: "<i class='mdi mdi-chevron-right'>",
      },
    },
    drawCallback: function () {
      $(".dataTables_paginate > .pagination").addClass("pagination-rounded");
    },
  });

  function appendSpecialty() {
    var newSpecialty = $("#specialty-table tbody tr:first")
      .clone()
      .appendTo($("#specialty-table tbody"));
    newSpecialty.removeAttr("hidden");

    $(newSpecialty.get("input")).attr("required", "required");
  }

  $("#add-specialty").on("click", function () {
    appendSpecialty();
  });

  $("#specialty-table").on("click", ".action-icon", function () {
    $(this).parents("tr").remove();
  });

  $("#anagraphic-table tbody").on("click", "tr", function (event) {
    // console.log($('#anagraphic-table').DataTable().row(event.currentTarget).data())
    if ($(this).hasClass("selected")) {
      reset();
    } else {
      var $data = $(this)[0].children;
      $("#firstName").val($data[0].getAttribute("data-first-name"));
      $("#lastName").val($data[0].getAttribute("data-last-name"));
      $("#qualification").val($data[1].textContent);
      $("#turno").val($data[2].textContent);
      $("#fiscalCode").val($data[3].textContent);
      $("#fiscalCode").change();
      $("#phoneNumber").val($data[4].textContent);
      $("#contactEmail").val($data[5].textContent);
      $("#contactEmail").change();
      idOfSel = $($data[6]).attr("data-id");

      $("#specialty-table tbody tr:not(:first)").remove();
      // Organizing specialty table
      var dataArray = $data[6].children;
      var dataCount = dataArray.length / 3;
      for (var i = 0; i < dataCount; i++) {
        appendSpecialty();
        $("#specialty-table tbody tr:last select").val(
          dataArray[i * 3].textContent
        );
        $("#specialty-table tbody tr:last input:first").val(
          dataArray[i * 3 + 1].textContent
        );
        $("#specialty-table tbody tr:last input:last").val(
          dataArray[i * 3 + 2].textContent
        );
      }

      $("#id").remove();
      $("#firstName").before(
        `<input type="text" id="id" name="id" hidden value="${idOfSel}" />`
      );
      $("#reset").prop("disabled", false);
      $("#add-or-modify").text("Modify");
    }
  });

  $("#reset").on("click", function () {
    reset();
    $("#anagraphic-table").DataTable().row(".selected").deselect();
  });

  $("#delete").click(function () {
    if (idOfSel !== "") {
      var table = $("#anagraphic-table").DataTable();
      var sel = table.row(".selected");
      // var $data = $sel.nodes()[0].children
      $.post("/api/anagraphic/remove/" + idOfSel, () => {
        sel.remove().draw();
        reset();
      });
    }
  });

  function reset() {
    $("#firstName").val("");
    $("#lastName").val("");
    $("#fiscalCode").val("");
    $("#fiscalCode").change();
    $("#phoneNumber").val("");
    $("#contactEmail").val("");
    $("#contactEmail").change();
    $("#specialty-table tbody tr:not(:first)").remove();
    idOfSel = "";
    $("#id").remove();
    $("#reset").prop("disabled", true);
    $("#add-or-modify").text("Add");
  }
});
