$(document).ready(function () {
  $("table")
    .DataTable({
      dom: "Bfrtip",
      buttons: ["excel", "pdf"],
      language: {
        paginate: {
          previous: "<i class='mdi mdi-chevron-left'>",
          next: "<i class='mdi mdi-chevron-right'>",
        },
      },
      drawCallback: function () {
        $(".dataTables_paginate > .pagination").addClass("pagination-rounded");
      },
      initComplete: function () {
        // Apply the search
        this.api()
          .columns()
          .every(function () {
            var that = this;

            $("input", this.footer()).on("keyup change clear", function () {
              if (that.search() !== this.value) {
                that.search(this.value).draw();
              }
            });
          });
      },
    })
    .buttons()
    .container()
    .appendTo("#datatable-buttons_wrapper .col-md-6:eq(0)");
});
