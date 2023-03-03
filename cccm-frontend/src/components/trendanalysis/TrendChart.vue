<template>
  <div class="container panel">
    <div v-if="!chartReady" class="inactive-banner justify-content-center pt-5 text-center">
      <h1><b>No Factors Selected</b></h1>
      <p />
      <p>Please select an item(s) from the "Factor View" dropdown menu</p>
    </div>
    <v-progress-linear v-if="loading" indeterminate height="20" color="primary">Loading chart data...
    </v-progress-linear>

    <v-card class="chart-container">
      <div class="chart mb-4 " :class="[ chartReady  ? 'chartActive' : 'chartInactive' ]">
        <canvas id="justiceChart" style="z-index: 1;"></canvas>
      </div>
    </v-card>

  </div>
</template>

<script>

import { Chart, registerables } from 'chart.js';
import axios from "axios";
import { trendStore } from '@/stores/trendstore';
import { mapStores, mapState, mapWritableState } from "pinia";
import { getChartData } from "@/components/form.api";
export default {
  name: "TrendChart",
  data() {
    return {
      chartData: [],
      chartReady: false,
      loading: false,
    }
  },
  setup() {
    const store = trendStore()
    return {
      store,
    }
  },
  computed: {
    ...mapStores(trendStore, ['data', 'startDate', 'endDate', 'commentCount', 'interventionCount', 'factors', 'advancedFilter', 'filteredData'])
  },
  created() {

  },
  beforeUnmount() {
    let tooltipEl = document.getElementById('chartjs-tooltip');
    tooltipEl.remove();
  },

  mounted() {
    let ctx = document.getElementById('justiceChart');
    let vm = this;
    if (this.store && this.store.filteredData) {
      this.chartReady = true;
      // run with timeout to give chart time to display and then render updates
      setTimeout(function () {
        vm.updateChart(vm.store.filteredData);
      }, 500);


    }

    this.store.$subscribe((mutation, state) => {
      let changed = false;
      if (mutation.payload && mutation.payload.hasOwnProperty('filteredData')) {
        if (mutation.payload.filteredData === null) {
          this.chartReady = false;
        } else {
          this.chartReady = true;

          this.updateChart(mutation.payload.filteredData);
        }
      }

    });

    const linePlugin = {
      id: 'draw_vertical_lines',

      afterDatasetsDraw: function (chartInstance) {

        // can draw a vertical line for previous assessment if needed
        chartInstance.data.datasets.forEach(dataset => {
          if (dataset.verticalLines) {
            for (const [key, val] of Object.entries(dataset.verticalLines)) {
              const axisPoint = chartInstance.scales.x.getPixelForValue(chartInstance.data.labels[chartInstance.data.labels.length - 2]);
              let yAxis = chartInstance.scales.y;
              let ctx = chartInstance.ctx;
              ctx.save();
              ctx.beginPath();
              ctx.moveTo(axisPoint, yAxis.top);
              ctx.lineTo(axisPoint, yAxis.bottom);
              ctx.lineWidth = 2;
              let color = dataset.borderColor.match(/^rgb\((([0-9](,)?)*)+\)/);
              ctx.strokeStyle = 'rgb(' + color[1] + ',0.3)';
              ctx.stroke();
              ctx.textAlign = 'center';
              ctx.fillText(key, axisPoint, yAxis.top + 100);

              ctx.restore();
            }
          }

        });


      },
    }
    Chart.register(...registerables, linePlugin);

    // const drawCustomTooltip = (evt,point, chart) => {
    //
    //   let ctx = document.getElementById('justiceChart');
    //
    //   let tooltipEl = document.getElementById('chartjs-tooltip');
    //   if (!tooltipEl) {
    //     console.log("Drawing tooltip at %o %o %o %o %o",ctx, evt, point, chart, this);
    //     console.log("Drawing %d %d", evt.native.offsetY, evt.native.offsetX);
    //
    //     console.log("location %o", point);
    //     tooltipEl = document.createElement('div');
    //     tooltipEl.style.backgroundColor = "white";
    //     tooltipEl.style.border = "1px black solid";
    //     tooltipEl.style.padding = "8px";
    //     tooltipEl.style.borderRadius = "5px";
    //     tooltipEl.style.width = "100px";
    //     tooltipEl.style.zIndex = "999";
    //     tooltipEl.style.height = "100px";
    //     tooltipEl.style.boxShadow = "5px 5px 5px black";
    //     tooltipEl.style.position = "absolute";
    //     tooltipEl.style.top = evt.native.clientY;
    //     tooltipEl.style.left = evt.native.clientX;
    //     tooltipEl.id = 'chartjs-tooltip';
    //     tooltipEl.innerHTML = '<table>Tooltip ' + evt.y + ',' + evt.x  + '</table>';
    //     tooltipEl.addEventListener('mouseout', function handleClick(event) {
    //         console.log("Hiding tooltip %o", event);
    //      // tooltipEl.remove();
    //     });
    //     document.body.appendChild(tooltipEl);
    //   }
    // }

    new Chart(ctx, {
      type: 'line',
      lineAtIndex: [2, 4, 8],
      options: {
        responsive: true,
        spanGaps: true,
        datasets: {
          line: 5
        },
        // onHover: (evt, chartElement, chart) => {
        //   let point = chart.getElementsAtEventForMode(evt, 'point', {
        //     intersect: true
        //   }, true);
        //   if (point.length > 0) {
        //     drawCustomTooltip(evt,point, this);
        //   }
        // },
        // onClick: (evt, el, chart) => {
        //
        //   let point = chart.getElementsAtEventForMode(evt, 'point', {
        //     intersect: true
        //   }, true);
        //
        //   console.log("Point %o", point[0]);
        //   console.log('Mode point list: ', chart.data.datasets[point[0].datasetIndex].label, chart.data.datasets[point[0].datasetIndex].data[point[0].index]);
        //   console.log("X value %s", chart.data.labels[point[0].index]);
        //   this.$store.commit('updatePointDateSelected', chart.data.labels[point[0].index]);
        //
        // },
        elements: {
          point: {
            pointStyle: 'rectRounded',
            radius: 8,
            hoverRadius: 16,
          }
        },
        plugins: {
          tooltip: {
            enabled: false,

            external: function (context) {
              // Tooltip Element
              let tooltipEl = document.getElementById('chartjs-tooltip');

              // Create element on first render
              if (!tooltipEl) {
                tooltipEl = document.createElement('div');
                tooltipEl.style.backgroundColor = "white";
                tooltipEl.style.border = "1px black solid";
                tooltipEl.style.padding = "8px";
                tooltipEl.style.borderRadius = "5px";
                tooltipEl.style.boxShadow = "5px 5px 5px black";
                tooltipEl.id = 'chartjs-tooltip';
                tooltipEl.innerHTML = '<table></table>';
                document.body.appendChild(tooltipEl);
              }

              // Keep the tooltip visible
              const tooltipModel = context.tooltip;
              if (tooltipModel.opacity === 0) {
                tooltipEl.style.opacity = 1;
                return;
              }

              // Set caret Position
              tooltipEl.classList.remove('above', 'below', 'no-transform');
              if (tooltipModel.yAlign) {
                tooltipEl.classList.add(tooltipModel.yAlign);
              } else {
                tooltipEl.classList.add('no-transform');
              }

              function getBody(bodyItem) {
                return bodyItem.lines;
              }


              // Set Text
              if (tooltipModel.body) {
                const titleLines = tooltipModel.title || [];
                const bodyLines = tooltipModel.body.map(getBody);

                let innerHtml = '<tbody>';

                titleLines.forEach(function (title) {
                  innerHtml += '<tr><td>Date: <b>' + title + '</b></td></tr>';
                });


                bodyLines.forEach(function (body, i) {
                  const colors = tooltipModel.labelColors[i];
                  let style = 'background:' + colors.backgroundColor;
                  style += '; border-color:' + colors.borderColor;
                  style += '; border-width: 2px';
                  const span = '<span style="' + style + '"></span>';
                  innerHtml += '<tr><td>' + span + body + '</td></tr>';
                  innerHtml += '<tr><td><a class="tooltip-link" id="comment-' + i + '" style="color:#000000" href="#/comments/' + tooltipModel.title[0] + '">nn comment(s)</a></td></tr>';
                  innerHtml += '<tr><td><a class="tooltip-link" id="intervention=' + i + '" style="cursor:pointer; color:#000000" href="#/interventions/' + tooltipModel.title[0] + '">nn intervention(s)</a></td></tr>';

                });
                innerHtml += '</tbody>';

                let tableRoot = tooltipEl.querySelector('table');
                tableRoot.innerHTML = innerHtml;
              }


              const tips = document.querySelectorAll('.tooltip-link');

              tips.forEach(tip => {
                tip.addEventListener('click', function handleClick(event) {
                  tip.setAttribute('style', 'font-weight: bold;');
                  tooltipEl.style.opacity = 0;
                });
              });
              const position = context.chart.canvas.getBoundingClientRect();

              // Display, position, and set styles for font
              tooltipEl.style.opacity = .7;
              tooltipEl.style.position = 'absolute';
              tooltipEl.style.left = position.left + window.pageXOffset + tooltipModel.caretX + 'px';
              tooltipEl.style.top = position.top + window.pageYOffset + tooltipModel.caretY + 'px';
              tooltipEl.style.padding = tooltipModel.padding + 'px ' + tooltipModel.padding + 'px';
              tooltipEl.style.transform = 'translate(-50%, 0)';
              tooltipEl.style.transition = 'all .5s ease';
            },
          },
          legend: {
            labels: {
              font: {
                size: 16,
              },
              usePointStyle: true
            },
            align: 'end'
          }
        },
        scales: {
          y: {
            ticks: {
              // eslint-disable-next-line no-unused-vars
              callback: function (value, index, ticks) {
                let response = '';
                switch (value) {
                  case 0:
                    response = 'D: Considerable improvement needed';
                    break;
                  case 1:
                    response = 'C: Some improvement needed';
                    break;
                  case 2:
                    response = 'B: No immediate need';
                    break;
                  case 3:
                    response = "A: Factor seen as asset";
                    break;
                  default:
                    response = '';
                }

                return response;
              }
            },
            beginAtZero: true
          }
        }
      }
    });


  },

  methods: {




    updateChart(data) {
      if (data) {

        const chart = Chart.getChart("justiceChart");
        if (chart) {

          chart.data = data;
          chart.update();
        }
      }
    }
  }
}
</script>

<style scoped>
.chart {
  width: 90%;
}

div.chartActive {
  display: block;
}

div.chartInactive {
  display: none;
}

.inactive-banner {
  min-height: 400px;
  padding: 88px;
  vertical-align: middle;
  border: 1px black solid;

}

#chartjs-tooltip {
  border: 1px solid black;
  box-shadow: 5px 5px black;
}


.chart-container {
  height: 100%;
}
</style>