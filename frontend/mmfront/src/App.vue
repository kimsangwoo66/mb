<template>
    <div class="App">
        <div class="chart-row">
            <ColumnChartComponent/>
        </div>
        <div class="chart-row">
            <PieChartComponent />
        </div>
        <div class="chart-row">
            <LineChartComponent />
        </div>
        <div>
            <AgTableComponent/>
        </div>

    </div>
</template>

<script>

//import axios from "axios";

import PieChartComponent from "@/components/PieChartComponent.vue";
import LineChartComponent from "@/components/LineChartComponent.vue";
import AgTableComponent from "@/components/AgTableComponent.vue";
import ColumnChartComponent from "@/components/ColumnChartComponent.vue";





export default {
    name: "App",
    components: {ColumnChartComponent, AgTableComponent, LineChartComponent,PieChartComponent},

}
</script>
<style>
@import "~ag-grid-community/styles/ag-grid.css";
@import "~ag-grid-community/styles/ag-theme-alpine.css";

.chart-row {
    display: inline-block;
    width: 33%;
    margin: 0;
    vertical-align: top;
    padding: 0 10px;
    box-sizing: border-box;
}



</style>