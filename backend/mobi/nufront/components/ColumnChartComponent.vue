<template>
  <div class="ColumnChartComponent">
    <highcharts :options="chartOptions" />
  </div>
</template>

<script>

import axios from 'axios'

export default {
  name: 'ColumnChartComponent',
  data () {
    return {
      chartOptions: {
        chart: {
          type: 'column',
          accessibility: {
            enabled: false
          }
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
        }
        ]

      }

    }
  },

  mounted () {
    axios.get('/api/column').then((response) => {
      // console.log(response.data)

      // 서버에서 응답받은 data를 컬럼차트 data 포맷에 맞게 변경하여 series 프로퍼티 키값에 추가
      const chartData = Object.entries(response.data).map(([name, value]) => [name, value])
      this.chartOptions.series[0].data = chartData
    })
      .catch((error) => {
        console.log('에러발생: ' + error)
      })
  }
}
</script>
<style scoped>

</style>
