<template>
  <div class="container panel">
    <div v-if="!chartReady" class="inactive-banner justify-content-center pt-5 text-center">
      <h1><b>No Factors Selected</b></h1>
      <p />
      <p>Please select an item(s) from the "Factor View" dropdown menu</p>
    </div>
    <v-progress-linear v-if="loading" indeterminate height="20" color="primary">Loading chart data...</v-progress-linear>

    <v-card class="chart-container">
      <div class="chart mb-4 " :class="[ chartReady  ? 'chartActive' : 'chartInactive' ]">
        <canvas :id="`${CHART_ID}`" style="z-index: 1;"></canvas>
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
      TOOLTIP_ID: 'chartjs-tooltip',
      CHART_ID: "justiceChart",
      chartData: [],
      chartReady: false,
      loading: false,
      newYAxisType: '',
      newFormType: '',
      showIntervention: true
    }
  },
  props: {
    newChartType: '',
  },
  watch: {
    newChartType() {
      this.newYAxisType = this.store.yaxistype;
      this.showIntervention = this.store.showIntervention;
      
      // Destroy the current chart instance
      this.destroyChart();
      //Recreate the chart based on the report type, so the Y-axis value shows properly
      this.instantiateChart();
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
  beforeDestroy: function () {
    // Hide tooltip popup
    let tooltipEl = document.getElementById(this.TOOLTIP_ID);
    if(tooltipEl != null && tooltipEl.style != null) {
      tooltipEl.style.opacity = 0;
    }
    
    this.$el.removeEventListener('click', this.documentEventListener)
  },
  mounted() {
    this.newYAxisType = this.store.yaxistype;
    this.showIntervention = this.store.showIntervention;
    
    this.$el.addEventListener('click', this.documentEventListener)
    this.instantiateChart();
  },
  methods: {
    destroyChart() {
      // Destroy exiting Chart Instance to reuse <canvas> element
      let chartStatus = Chart.getChart(this.CHART_ID); // <canvas> id
      if (chartStatus != undefined) {
        chartStatus.destroy();
      }
    },
    instantiateChart() {
      let ctx = document.getElementById(this.CHART_ID);

      // Need to assign this to local var, since this.store.filteredDate isn't accessiable from within the setTimeout function.
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
        if (mutation.payload) {
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

      let currentYAxisType = this.newYAxisType;
      let showIntervention = this.showIntervention;
      let toolTipID = this.TOOLTIP_ID;
      //console.log("showIntervention: ", showIntervention);
      new Chart(ctx, {
        type: 'line',
        lineAtIndex: [2, 4, 8],
        options: {
          responsive: true,
          spanGaps: true,
          datasets: {
            line: 5
          },
          onClick: (evt, el, chart) => {
            let pointArray = chart.getElementsAtEventForMode(evt, 'point', {
              intersect: false
              , includeInvisible: true
            }, true);

            //console.log("Point %o", pointArray);
            if(pointArray == null || pointArray.length == 0) {
              // Hijacking touchmove event
              let mouseOutEvent = new MouseEvent('touchmove');
              //console.log("mouseOutEvent %o", mouseOutEvent);
              return chart.canvas.dispatchEvent(mouseOutEvent);
            }
          },
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
              events: ['touchmove', 'click'],
              external: function (context) {
                const {chart, tooltip} = context;
                //console.log("tooltip->external: %o", context);
                // DataPoint tooltip Element
                let tooltipEl = document.getElementById(toolTipID);
                
                let toolTipDeco_outerTableWidth = 700;
                let toolTipDeco_outerTableColSpan = 2;
                
                if (showIntervention) {
                  toolTipDeco_outerTableWidth = 900;
                  toolTipDeco_outerTableColSpan = 3;
                }
                // Create element on first render
                if (!tooltipEl) {
                  tooltipEl = document.createElement('div');
                  tooltipEl.className = "dataPointTooltip";
                  tooltipEl.id = toolTipID;
                  tooltipEl.innerHTML = '<table width=' + toolTipDeco_outerTableWidth + '></table>';
                  document.body.appendChild(tooltipEl);
                } else {
                  tooltipEl.innerHTML = '<table width=' + toolTipDeco_outerTableWidth + '></table>';
                }
                
                const tooltipModel = context.tooltip;
                if (tooltipModel.opacity === 0) {
                  // Hide if tooltip to be hidden
                  tooltipEl.style.opacity = 0;
                  return;
                } else if (tooltipModel.opacity === 1) {
                  // Keep the tooltip visible
                  tooltipEl.style.opacity = 1;
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

                  //console.log("tooltipModel.title: %o", tooltipModel.title);

                  let innerHtml = '<tbody>';

                  let dataLabel;
                  titleLines.forEach(function (title) {
                    innerHtml += '<tr><td colspan=' + toolTipDeco_outerTableColSpan + '><b>Date: </b>' + title + '</td></tr>';
                    dataLabel = title;
                  });

                  //console.log("chart.data: %o", chart.data);
                  if (showIntervention){
                    innerHtml += '<tr><th width="10%">Factors</th><th width="30%">Comments</th><th colspan="2">Interventions</th></tr>'
                  } else {
                    innerHtml += '<tr><th width="10%">Factors</th><th width="30%">Comments</th></tr>'
                  }
                  
                  if(tooltipModel.dataPoints) {
                      for(let j = 0; j < tooltipModel.dataPoints.length; j++) {
                        let dataIndex = tooltipModel.dataPoints[j].dataIndex;
                        let datasetIndex = tooltipModel.dataPoints[j].datasetIndex;
                        let dataset =  tooltipModel.dataPoints[j].dataset;
                        //console.log("dataset.data: ", dataset);
                        const colors = tooltipModel.labelColors[j];
                        let style = 'background-color:' + colors.backgroundColor;
                        style += ';  min-width: .5em; min-height: .5em;width: .5em; height: .5em; display: block; padding: .5em; float:left; margin-right: .2em;';
                        const span = '<span style="' + style + '"></span>';
                        innerHtml += '<tr><td>' + span + dataset.label + '</td>';
                        
                        if(dataset.comments) {
                          for(let k = 0; k < dataset.comments.length; k++) {
                            if(dataLabel == dataset.comments[k].dataLabel) {
                              innerHtml += '<td>' + dataset.comments[k].value+ '</td>';
                            }
                          }
                        }
                        
                        if(showIntervention && dataset.interventions && dataset.interventions.length > 0) {
                          let containItv = dataset.interventions.filter( item => item.dataLabel === dataLabel );
                          if (containItv != null && containItv[0] != null) {
                            innerHtml += '<td width="60%"><table>';
                            //innerHtml += '<tr><th>Type</th><th>Comments</th></tr>'
                          }
                          
                          for(let k = 0; k < dataset.interventions.length; k++) {
                            if(dataLabel == dataset.interventions[k].dataLabel) {
                              let itvType = dataset.interventions[k].typeDesc;
                              if (dataset.interventions[k].type == 'OTHR') {
                                itvType = dataset.interventions[k].typeOverride;
                              }
                              innerHtml += '<tr>'
                              innerHtml += '<td width="20%">' +  itvType + '</td>';
                              innerHtml += '<td width="80% word-wrap:break-word">' + dataset.interventions[k].comment + '</td>';
                              innerHtml += '</tr>'
                            }
                          }
                          if (containItv != null && containItv[0] != null) {
                            innerHtml += '</table></td>' 
                          }
                        }
                        innerHtml += '</tr>' 
                      }
                  }
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
                tooltipEl.style.left = position.left + window.pageXOffset + 450 + tooltipModel.caretX + 'px';
                //alert("position.left + window.pageXOffset + tooltipModel.caretX: " + position.left + ',' + window.pageXOffset + ', ' + tooltipModel.caretX);
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
                  //console.log("this: ", this);
                  let response = '';
                  switch (currentYAxisType) {
                    // Y-axis labled as A B C D
                    case 'ABCD':
                      switch (value) {
                        case 0:
                          response = 'D';
                          break;
                        case 1:
                          response = 'C';
                          break;
                        case 2:
                          response = 'B';
                          break;
                        case 3:
                          response = "A";
                          break;
                        default:
                          response = '';
                      }
                      break;
                    // Y-axis labled as L M H
                    case 'LMH':
                      switch (value) {
                        case 0:
                          response = 'Low';
                          break;
                        case 1:
                          response = 'Medium';
                          break;
                        case 2:
                          response = 'High';
                          break;
                        default:
                          response = '';
                      }
                      break;
                    // Y-axis labled as 0 1 2 3/IN
                    case '0123':
                      switch (value) {
                        case 3:
                          response = '3/IN: Intervene now';
                          break;
                        case 2:
                          response = '2: Definite problem';
                          break;
                        case 1:
                          response = '1: Some problem';
                          break;
                        case 0:
                          response = '0: No problem';
                          break;
                        default:
                          response = '';
                      }
                      break;    
                    default: 
                      response = value;
                      break;                  
                  }
                  return response;
                }
              },
              beginAtZero: false
            }
          }
        }
      });
    },
    updateChart(data) {
      if (data) {
        const chart = Chart.getChart(this.CHART_ID);
        if (chart) {
          let tooltipEl = document.getElementById(this.TOOLTIP_ID);
          if(tooltipEl != null && tooltipEl.style != null) {
            tooltipEl.style.opacity = 0;
          }
          chart.data = data;
          chart.update();
        }
      }
    },
     documentEventListener: function(ev) {
      let tooltipEl = document.getElementById(this.TOOLTIP_ID);
      if(tooltipEl != null && tooltipEl.style != null) {
        tooltipEl.style.opacity = 0;
      }
    }
  }
}
</script>

<style >
.dataPointTooltip {
  background-color: white;
  border-radius: 5px;
  z-index: 10;
}

.dataPointTooltip>table>tbody>tr>th {
  border: 1px solid black;
  padding-left: 10px;
  padding-right: 10px;
}

.dataPointTooltip>table>tbody>tr>td {
  border: 1px solid black;
  padding-left: 1px;
  padding-right: 1px;
}

.dataPointTooltip table tbody tr td table tbody tr:nth-child(even)  {
   background-color:#ddd8d8;
}

.dataPointTooltip table tbody tr td table tbody tr td {
  border: 0px solid black;
  padding-left: 10px;
  padding-right: 10px;
}

.chart {
  width: 100%;
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
  box-shadow: 1px 1px black;
}


.chart-container {
  height: 100%;
}
</style>