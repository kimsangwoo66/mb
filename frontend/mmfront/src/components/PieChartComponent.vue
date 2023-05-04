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
                    name: 'Brands',
                    colorByPoint: true,
                    data: [{
                        name: 'Chrome',
                        y: 1,

                    }, {
                        name: 'Edge',
                        y: 85
                    },  {
                        name: 'Firefox',
                        y: 384
                    }, {
                        name: 'Safari',
                        y: 35
                    }, {
                        name: 'Internet Explorer',
                        y: 28
                    },  {
                        name: 'Opera',
                        y: 96
                    }, {
                        name: 'Sogou Explorer',
                        y: 1
                    }, {
                        name: 'QQ',
                        y: 36
                    }, {
                        name: 'Other',
                        y: 20
                    }]
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
            console.log(response.data)
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
                        showInLegend: true
                    }
                },
                series: [{
                    name: 'Brands',
                    colorByPoint: true,
                    data: []
                }]
            };

            // 데이터를 series.data 배열에 추가
            Object.keys(data).forEach(key => {
                chartOptions.series[0].data.push({
                    name: key,
                    y: data[key]
                });
            });

            // 차트를 그린다.
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