<template>
  <div class="LineChartComponent">
      <highcharts :options="chartOptions"></highcharts>
  </div>
</template>

<script>
import Highcharts from 'highcharts'
import accessibility from 'highcharts/modules/accessibility'

import axios from "axios";
accessibility(Highcharts);
export default {
    name: "LineChartComponent",
    data() {
        return {
            chartOptions: {
                chart: {
                    type: 'line'
                },
                title: {
                    text: '일별 이슈 등록 개수'
                },
                xAxis: {
                    title: {
                        text: "날짜"
                    },
                    type: 'category'
                },
                yAxis: {
                    title: {
                        text: ''
                    }
                },
                series: [{
                    name: '이슈 개수',
                    data: [

                    ]
                }, ]

            },
            accessibility: { // 접근성 속성 추가
                enabled: true
            },
        }
    },
    mounted() {

        axios.get("/api/line").then(response => {
            console.log(response.data)


            // 서버에서 응답받은 data를 라인차트 data 포맷에 맞게 변경하여 series 프로퍼티 키값에 추가
            const chartData = Object.entries(response.data).map(([name, value]) => [name, value])
            this.chartOptions.series[0].data = chartData
        })
            .catch(error => {
                console.log(error)
            })


    }
}
</script>

<style scoped>

</style>