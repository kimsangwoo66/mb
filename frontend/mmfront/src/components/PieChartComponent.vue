<template>
    <div class="PieChartComponent">
        <div id="chart-container"></div>
    </div>
</template>

<script>
import Highcharts from 'highcharts'
import accessibility from 'highcharts/modules/accessibility'
import axios from "axios";
accessibility(Highcharts);
export default {
    name: "PieChartComponent",

    data() {
        return {
            chartOptions: {
                chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false,
                    type: 'pie'
                },
                title: {
                    text: '담당자별 이슈 개수',
                    align: 'center'
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                },
                accessibility: {
                    point: {
                        valueSuffix: '%'
                    }
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                        },
                        showInLegend: true
                    }
                },
                series: [{

                    data: []
                }]

            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            accessibility: {
                point: {
                    valueSuffix: '%'
                }
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                    }
                },
                showInLegend: true
            },
        }
    },
    mounted() {
        axios.get("/api/pie").then(response => {

            const data = response.data;

            const chartOptions = {
                chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false,
                    type: 'pie'
                },
                title: {
                    text: '담당자별 이슈 개수',
                    align: 'center'
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                },
                accessibility: {
                    point: {
                        valueSuffix: '%'
                    }
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                        },
                        showInLegend: true,
                        legend: {
                            align: "right"
                        }
                    }
                },
                series: [{

                    colorByPoint: true,
                    data: []
                }]
            };

            // 데이터를 series.data 배열에 적합한 포맷으로 변경하여 추가
            Object.keys(data).forEach(key => {
                chartOptions.series[0].data.push({
                    name: key,
                    y: data[key]
                });
            });


            // 파이 차트 재렌더링
            Highcharts.chart('chart-container', chartOptions);
        })
            .catch(error => {
                console.log(error)
            })
    }
}
</script>

<style scoped>

</style>