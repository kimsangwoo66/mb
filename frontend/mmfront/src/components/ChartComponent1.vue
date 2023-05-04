<template>
    <div id="ChartComponent1">
        <VueHighcharts :options="chartOptions"/>
    </div>
</template>

<script>


export default {
    name: 'ChartComponent1',
    data() {
        return {
            chartOptions: {
                series: [
                    {
                        data: [1,2,3]
                    }
                ]


            }
        }
    }
}
</script>

<style scoped>



</style>