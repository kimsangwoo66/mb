<template>
  <div class="container">
    <div class="chart-row">
      <ColumnChartComponent />
    </div>
    <div class="chart-row">
      <PieChartComponent />
    </div>
    <div class="chart-row">
      <LineChartComponent />
    </div>
    <div>
      <AgTableComponent />
    </div>
  </div>
</template>

<script>
import ColumnChartComponent from '@/components/ColumnChartComponent.vue'
import PieChartComponent from '@/components/PieChartComponent.vue'
import LineChartComponent from '@/components/LineChartComponent.vue'
import AgTableComponent from '@/components/AgTableComponent.vue'
export default {
  components: {
    AgTableComponent,
    LineChartComponent,
    PieChartComponent,
    ColumnChartComponent

  }
}
</script>

<style>

.chart-row {
  display: inline-block;
  width: 33%;
  margin: 0;
  vertical-align: top;
  padding: 0 10px;
  box-sizing: border-box;
}
</style>
