<template>
    <div class="AgTableComponent">
        <div class = "outer">이슈 리스트</div>
        <hr/>
        <div>
            <span>
                <label>필터 조건</label>
                &nbsp;&nbsp;&nbsp;&nbsp;
                 <label>해결 상태</label>
                All<input type="radio" name="radioBtn" value="all" checked="checked"/>
                Open<input type="radio" name="radioBtn" value="open"/>
                Closed<input type="radio" name="radioBtn" value="closed"/>
                &nbsp;&nbsp;
                <label>라벨 필터링</label>
                <select name="labelFilter">
                    <option value="">null</option>
                    <option v-for="label in labels" :value="label" :key="label">{{ label }}</option>

                </select>
                 &nbsp;&nbsp;
                <label>이슈 등록 기간으로 검색</label>
                &nbsp;&nbsp;
                <input type="date"/>
                ~
                <input type="date"/>
                &nbsp;&nbsp;
                <button @click="sendData">이슈 검색</button>

            </span>


        </div>
        <div class = "outer">
        <ag-grid-vue style="width: 1000px; height: 500px; align-content: center;"
                     class="ag-theme-alpine"
                     :columnDefs="columnDefs"
                     :rowData="rowData">
        </ag-grid-vue>
        </div>
    </div>
</template>

<script>
import {AgGridVue} from "ag-grid-vue";
import axios from "axios";

export default {
    name: "AgTableComponent",
    data() {
        return {
            labels: [],
            columnDefs: null,
            rowData: null
        }
    },
    components: {
        AgGridVue
    },
    beforeMount() {
        this.columnDefs = [
            { field: 'make' },
            { field: 'model' },
            { field: 'price' }
        ];

        this.rowData = [
            { make: 'Toyota', model: 'Celica', price: 35000 },
            { make: 'Ford', model: 'Mondeo', price: 32000 },
            { make: 'Porsche', model: 'Boxter', price: 72000 }
        ];

        this.fetchLabels();

    },
    methods: {
        async fetchLabels(){
            try {
                const response = await axios.get("/api/onLabels");
                this.labels = response.data;
            }catch (e) {
                console.log(e);
            }
        },


        async sendData(){
            this.stateValue = document.querySelector(
                "input[name='radioBtn']:checked"
            ).value;
            this.labelValue = document.querySelector("select[name='labelFilter']")
                .value;
            this.startDate = document.querySelector('input[type="date"]').value;
            this.endDate = document.querySelectorAll('input[type="date"]')[1]
                .value;

            console.log("stateValue:", this.stateValue);
            console.log("labelValue:", this.labelValue);
            console.log("startDate:", this.startDate);
            console.log("endDate:", this.endDate);

            await axios.post("/api/searchIssue", {

                    stateValue: this.stateValue,
                    labelValue: this.labelValue,
                    startDate: this.startDate,
                    endDate: this.endDate,

            })

            // }).then(response => {
            //     console.log(response.data)
            // })



        }
    }

}
</script>

<style scoped>
.outer {
    display: flex;
    justify-content: center;
}

</style>