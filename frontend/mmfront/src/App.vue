<template>
    <div class="App">
        <highcharts :options="chartOptions"></highcharts>
        <hr/>
        <hr/>
        <PieChartComponent/>
        <hr/>
        <hr/>
        <LineChartComponent/>
    </div>

</template>

<script>
import Highcharts from 'highcharts'
import accessibility from 'highcharts/modules/accessibility'
import axios from "axios";

import PieChartComponent from "@/components/PieChartComponent.vue";
import LineChartComponent from "@/components/LineChartComponent.vue";
accessibility(Highcharts);


export default {
    name: "App",
    components: {LineChartComponent,PieChartComponent},

    data() {
        return {
            chartOptions: {
                chart: {
                    type: 'column'
                },
                title: {
                    text: '등록자별 이슈 개수'
                },
                xAxis: {
                    type: 'category'
                },
                yAxis: {
                    title: {
                        text: ''
                    }
                },
                series: [{
                    name: '등록이슈 개수 ',
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
    axios.get("/api/column").then(response => {
        console.log(response.data)

        // 서버에서 응답받은 data를 컬럼차트 data 포맷에 맞게 변경하여 series 프로퍼티 키값에 추가
        const chartData = Object.entries(response.data).map(([name, value]) => [name, value])
        this.chartOptions.series[0].data = chartData
    })
        .catch(error => {
            console.log(error)
        })
    }



}
</script>