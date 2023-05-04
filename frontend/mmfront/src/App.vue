<template>
    <div class="App">
        <highcharts :options="chartOptions"></highcharts>
        <hr/>
        <hr/>
    </div>

</template>

<script>
import Highcharts from 'highcharts'
import accessibility from 'highcharts/modules/accessibility'
import axios from "axios";
accessibility(Highcharts);


export default {
    name: "App",

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
    created(){
        console.log("확인좀 해보자")
        console.log("이럼 감지하나?dd")

    },
    mounted() {
    axios.get("/api/column").then(response => {
        console.log(response.data)

        const chartData = Object.entries(response.data).map(([name, value]) => [name, value])
        this.chartOptions.series[0].data = chartData
    })
        .catch(error => {
            console.log(error)
        })
    }
}
</script>