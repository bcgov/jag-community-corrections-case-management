<template>
  <div class="container panel">

    <div v-if="!chartReady" class="inactive-banner justify-content-center pt-5 text-center">
      <h1><b>No Factors Selected</b></h1>
      <p />
      <p>Please select an item(s) from the "Factor View" dropdown menu</p>
    </div>
    <div class="chart mb-4" :class="[ chartReady  ? 'chartActive' : 'chartInactive' ]">
      <canvas id="justiceChart" style="z-index: 1;"></canvas>
    </div>

  </div>
</template>

<script>

import { Chart, registerables } from 'chart.js';
import axios from "axios";
import { trendStore } from '@/stores/trendstore';
import { mapStores, mapState, mapWritableState } from "pinia/dist/pinia";
import { getChartData } from "@/components/form.api";
export default {
  name: "TrendChart",
  data() {
    return {
      chartData: [],
      chartReady: false,
      inputFilter: {
        clientNumber: null,
        startDate: null,
        endDate: null,
        factors: [],
        chartType: null,
      }
    }
  },
  setup() {
    const store = trendStore()
    return {
      store,
    }
  },
  computed: {
    ...mapStores(trendStore, ['factors', 'startDate', 'endDate'])
  },
  created() {

  },
  beforeUnmount() {
    debugger;
    let tooltipEl = document.getElementById('chartjs-tooltip');
    tooltipEl.remove();
  },

  mounted() {
    let ctx = document.getElementById('justiceChart');
    let csNumber = this.$route.params.csNumber;
    this.inputFilter.clientNumber = csNumber;
    this.trendStore.$subscribe((mutation, state) => {
      if (mutation.payload['factors']) {
        this.inputFilter.factors = mutation.payload.factors;
      }
      if (mutation.payload['chartType']) {
        this.inputFilter.chartType = mutation.payload.chartType;
      }
      this.refreshChart();
    });

    const linePlugin = {
      id: 'draw_vertical_lines',

      afterDatasetsDraw: function (chartInstance) {

        // can draw a vertical line for previous assessment if needed
        chartInstance.data.datasets.forEach(dataset => {
          if (dataset.verticalLines) {
            for (const [key, val] of Object.entries(dataset.verticalLines)) {
              console.log("key val %o %o", key, val);
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
        spanGaps: false,
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
              console.log("Tooltip %o", tooltipEl);

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
              console.log("Model %o", tooltipModel);
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
                  console.log("Tooltip %o", tooltipModel);
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
                  console.log('tip clicked %o %o', event, event.target);
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


    this.refreshChart();

  },

  updated() {

  },
  // watch: {
  //   factors(newValue) {
  //     console.log("FACTORS!!");
  //       alert('factor change ' + newValue);
  //       // this.refreshChart();
  //   },
  //   userEndDate: {
  //     handler: function (newValue, oldValue) {
  //       if (oldValue !== newValue) {
  //         this.refreshChart();
  //       }
  //     },
  //   },
  //   userStartDate: {
  //     handler: function (newValue, oldValue) {
  //       if (oldValue !== newValue) {
  //         console.log("Start date updated %s %s", newValue, oldValue);
  //         this.refreshChart();
  //       }
  //     },
  //   },
  //   filterEndDate: {
  //     handler: function (newValue, oldValue) {
  //       if (oldValue !== newValue) {
  //         console.log("End date updated %s %s", newValue, oldValue);
  //         this.refreshChart();
  //       }
  //     },
  //   },
  //   advancedFilter: {
  //     handler: function (newValue, oldValue) {
  //       if (oldValue !== newValue) {
  //         console.log("Advanced filter updated %s %s", newValue, oldValue);
  //         this.toggleSelected(newValue);
  //       }
  //     },
  //   },

  //   period: {
  //     handler: function (newValue, oldValue) {
  //       console.log("Period updated!! %s %s", newValue, oldValue);
  //       this.refreshChart();
  //     },
  //   }
  // },
  methods: {

    toggleSelected(newValue) {
      let ctx = document.getElementById('justiceChart');
      let chart = Chart.getChart("justiceChart");
      console.log('NewValue %o %o', newValue, ctx);

      if (!newValue) {

        chart.data.datasets.forEach(ds => {
          ds.hidden = false;
        });
      } else {
        // TODO Need to inject this from the incoming JSON as its chart type specific
        switch (newValue.id) {
          // worsened
          case 1: {
            chart.data.datasets.forEach(ds => {
              const lastTwo = ds.data.slice(-2);
              console.log("Checking last 2 entries for each dataset %o", lastTwo);
              if (lastTwo[1] < lastTwo[0]) {
                ds.hidden = false;
                console.log("Keeping %o", ds);

              } else {
                ds.hidden = true;
                console.log("Hiding %o", ds);

              }
            });
            break;
          }
          // improved
          case 2: {
            chart.data.datasets.forEach(ds => {
              const lastTwo = ds.data.slice(-2);
              console.log("Checking last 2 entries for each dataset %o", lastTwo);
              if (lastTwo[1] > lastTwo[0]) {
                ds.hidden = false;
                console.log("Keeping %o", ds);
              } else {
                ds.hidden = true;
                console.log("Hiding %o", ds);

              }
            });
            break;
          }
          case 3: {
            chart.data.datasets.forEach(ds => {
              console.log("Checking last 2 entries for each dataset %o", ds);
              const lastTwo = ds.data.slice(-2);
              if ((lastTwo[1] === 0 && lastTwo[0] === 0) || (lastTwo[1] === 1 && lastTwo[0] === 1)) {
                ds.hidden = null;
              } else {
                ds.hidden = true;

              }
            });
            break;
          }
          case 4: {
            chart.data.datasets.forEach(ds => {
              console.log("Checking last 2 entries for each dataset %o", ds);
              const lastTwo = ds.data.slice(-2);
              if ((lastTwo[1] === 2 && lastTwo[0] === 2) || (lastTwo[1] === 3 && lastTwo[0] === 3)) {
                ds.hidden = null;
              } else {
                ds.hidden = true;

              }
            });
            break;
          }
          default: {
            // do nothing
          }
        }
      }
      chart.update();

    },
    async refreshChart() {
      this.chartReady = false;

      if (this.inputFilter.factors.length > 0 && this.inputFilter.chartType && this.inputFilter.clientNumber) {
        console.log("RefreshChart() called %o", this.inputFilter);

        const [error, data] = await getChartData(this.inputFilter);
        if (error) {
          console.error(error);
        } else {
          console.log("Got chart data %o", data);
          this.updateChart(data);
        }
      }
      // if (this.factors) {
      //   console.log("Factors %o", this.factors);
      //   axios
      //     .get('/forms/client/', {
      //       params: {
      //         factors: factors,
      //         period: this.period,
      //         clientId: this.clientId,
      //         startDate: this.userStartDate,
      //         endDate: this.userEndDate,
      //         advancedFilter: this.advancedFilter
      //       },
      //     })
      //     .then((response) => {
      //       console.log("Got response %o", this.form);
      //       this.updateChart(response.data);
      //     });
      // }
    },

    updateChart(responseData) {
      const chart = Chart.getChart("justiceChart");

      console.log("ResponseData %o", responseData);
      let xSeries = responseData.dataLabels;
      // update start and end dates
      // this.store.commit('updateStartAndEndDateLimits', [xSeries[0], xSeries[xSeries.length - 1]]);
      // this.store.commit('updateStartAndEndDate', [xSeries[0], xSeries[xSeries.length - 1]]);
      // this.store.commit('updateInterventionCount', responseData.interventionCount);
      // this.store.commit('updateCommentCount', responseData.commentCount);


      const data = {
        labels: xSeries,
        datasets: responseData.datasets
      };
      chart.data = data;
      chart.update();
      this.chartReady = true;
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
</style>