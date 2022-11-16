function getSpecialtyLabels() {
  var length = parseInt($("#specialty-labels").attr("data-length"));
  var labels = [];

  for (var i = 0; i < length; i++)
    labels.push($("#specialty-labels p")[i].textContent);
  return labels;
}

function getSpecialtyCounts() {
  var length = parseInt($("#specialty-counts").attr("data-length"));
  var counts = [];

  for (var i = 0; i < length; i++)
    counts.push(parseInt($("#specialty-counts p")[i].textContent));
  return counts;
}

new Chart("specialties-radar", {
  type: "radar",
  data: {
    labels: getSpecialtyLabels(),
    datasets: [
      {
        label: "Users",
        data: getSpecialtyCounts(),
        fill: true,
        backgroundColor: "rgba(54, 162, 235, 0.2)",
        borderColor: "rgb(54, 162, 235)",
        pointBackgroundColor: "rgb(54, 162, 235)",
        pointBorderColor: "#fff",
        pointHoverBackgroundColor: "#fff",
        pointHoverBorderColor: "rgb(54, 162, 235)",
      },
    ],
  },
  options: {
    legend: {
      display: false,
    },
    tooltip: {
      display: false,
    },
    title: {
      display: true,
      text: "Specialties",
    },
  },
});

function getQualificationLabels() {
  var length = parseInt($("#qualification-labels").attr("data-length"));
  var labels = [];

  for (var i = 0; i < length; i++)
    labels.push($("#qualification-labels p")[i].textContent);
  return labels;
}

function getQualificationCounts() {
  var length = parseInt($("#qualification-counts").attr("data-length"));
  var counts = [];

  for (var i = 0; i < length; i++)
    counts.push(parseInt($("#qualification-counts p")[i].textContent));
  return counts;
}

function getQualificationColors() {
  var length = parseInt($("#qualification-counts").attr("data-length"));
  var backgroundColors = [];
  var borderColors = [];
  var r, g, b;

  for (var i = 0; i < length; i++) {
    r = parseInt(Math.random() * 255);
    g = parseInt(Math.random() * 255);
    b = parseInt(Math.random() * 255);
    backgroundColors.push(`rgba(${r}, ${g}, ${b}, 0.2)`);
    borderColors.push(`rgb(${r}, ${g}, ${b})`);
  }
  return {
    backgroundColors,
    borderColors,
  };
}

var qualificationColors = getQualificationColors();

new Chart("qualifications-bar", {
  type: "horizontalBar",
  data: {
    labels: getQualificationLabels(),
    datasets: [
      {
        axis: "y",
        label: "Users",
        data: getQualificationCounts(),
        fill: false,
        backgroundColor: qualificationColors.backgroundColors,
        borderColor: qualificationColors.borderColors,
        borderWidth: 1,
      },
    ],
  },
  options: {
    indexAxis: "y",
    legend: { display: false },
    title: {
      display: true,
      text: "Qualification",
    },
  },
});

var turnoColors = [
  "#b91d47",
  "#00aba9",
  "#2b5797",
  "#e8c3b9",
  "#1e7145",
  "#7b5d3e",
  "#5d5f77",
  "#0bad3f",
];

new Chart("turnoA-doughnut", {
  type: "doughnut",
  data: {
    labels: ["A1", "A2", "A3", "A5", "A6", "A7", "A8"],
    datasets: [
      {
        label: "Users",
        backgroundColor: turnoColors,
        data: [
          parseInt($("#turnoA-data p")[0].textContent),
          parseInt($("#turnoA-data p")[1].textContent),
          parseInt($("#turnoA-data p")[2].textContent),
          parseInt($("#turnoA-data p")[3].textContent),
          parseInt($("#turnoA-data p")[4].textContent),
          parseInt($("#turnoA-data p")[5].textContent),
          parseInt($("#turnoA-data p")[6].textContent),
          parseInt($("#turnoA-data p")[7].textContent),
        ],
      },
    ],
  },
  options: {
    responsive: true,
    legend: {
      position: "top",
    },
    title: {
      display: true,
      text: "Turno A",
    },
  },
});

new Chart("turnoB-doughnut", {
  type: "doughnut",
  data: {
    labels: ["B1", "B2", "B3", "B5", "B6", "B7", "B8"],
    datasets: [
      {
        label: "Users",
        backgroundColor: turnoColors,
        data: [
          parseInt($("#turnoB-data p")[0].textContent),
          parseInt($("#turnoB-data p")[1].textContent),
          parseInt($("#turnoB-data p")[2].textContent),
          parseInt($("#turnoB-data p")[3].textContent),
          parseInt($("#turnoB-data p")[4].textContent),
          parseInt($("#turnoB-data p")[5].textContent),
          parseInt($("#turnoB-data p")[6].textContent),
          parseInt($("#turnoB-data p")[7].textContent),
        ],
      },
    ],
  },
  options: {
    responsive: true,
    legend: {
      position: "top",
    },
    title: {
      display: true,
      text: "Turno B",
    },
  },
});

new Chart("turnoC-doughnut", {
  type: "doughnut",
  data: {
    labels: ["C1", "C2", "C3", "C5", "C6", "C7", "C8"],
    datasets: [
      {
        label: "Users",
        backgroundColor: turnoColors,
        data: [
          parseInt($("#turnoC-data p")[0].textContent),
          parseInt($("#turnoC-data p")[1].textContent),
          parseInt($("#turnoC-data p")[2].textContent),
          parseInt($("#turnoC-data p")[3].textContent),
          parseInt($("#turnoC-data p")[4].textContent),
          parseInt($("#turnoC-data p")[5].textContent),
          parseInt($("#turnoC-data p")[6].textContent),
          parseInt($("#turnoC-data p")[7].textContent),
        ],
      },
    ],
  },
  options: {
    responsive: true,
    legend: {
      position: "top",
    },
    title: {
      display: true,
      text: "Turno C",
    },
  },
});

new Chart("turnoD-doughnut", {
  type: "doughnut",
  data: {
    labels: ["D1", "D2", "D3", "D5", "D6", "D7", "D8"],
    datasets: [
      {
        label: "Users",
        backgroundColor: turnoColors,
        data: [
          parseInt($("#turnoD-data p")[0].textContent),
          parseInt($("#turnoD-data p")[1].textContent),
          parseInt($("#turnoD-data p")[2].textContent),
          parseInt($("#turnoD-data p")[3].textContent),
          parseInt($("#turnoD-data p")[4].textContent),
          parseInt($("#turnoD-data p")[5].textContent),
          parseInt($("#turnoD-data p")[6].textContent),
          parseInt($("#turnoD-data p")[7].textContent),
        ],
      },
    ],
  },
  options: {
    responsive: true,
    legend: {
      position: "top",
    },
    title: {
      display: true,
      text: "Turno D",
    },
  },
});
