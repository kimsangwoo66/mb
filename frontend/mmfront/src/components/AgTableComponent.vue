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
            <ag-grid-vue ref="agGrid" style="width: 1400px; height: 700px; align-content: center;"
                         class="ag-theme-alpine"
                         :columnDefs="columnDefs"
                         :rowData="rowData"
                         :paginationPageSize="10"
                         @grid-ready="onGridReady"

            >
            </ag-grid-vue>
        </div>

        <div class="d-flex justify-content-center">
            <b-pagination v-model="currentPage" :total-rows="rows" :per-page="perPage" @input="onPaginationInput"></b-pagination>
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
            rowData: null,
            currentPage: null,
            rows: null,
            perPage: 10,
        }
    },
    components: {
        AgGridVue
    },

    beforeMount() {
        this.columnDefs = [
            { field: 'number' ,headerName: '번호'},
            { field: 'state' , headerName: '상태'},
            { field: 'title' , headerName: '제목'},
            { field: 'closeAt',headerName: '해결일'},
            { field: 'createAt' ,headerName: '등록일'},
            { field: 'register' ,headerName: '등록자'},
            { field: 'labels' ,headerName: '라벨'}
        ];

        this.rowData = [];

        this.fetchLabels();

    },
    methods: {
        onGridReady(params) {
            this.gridApi = params.api;
            this.gridColumnApi = params.columnApi;
        },
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
                currentPage: 1

            }).then(response => {
                console.log("데이터객체확인: ",response.data)
                this.columnDefs = [
                    { field: 'number' ,headerName: '번호'},
                    { field: 'state' , headerName: '상태'},
                    { field: 'title' , headerName: '제목'},
                    { field: 'closeAt',headerName: '해결일'},
                    { field: 'createAt' ,headerName: '등록일'},
                    { field: 'register' ,headerName: '등록자'},
                    { field: 'labels' ,headerName: '라벨'}
                ];

                this.rowData = response.data;
                const totalCnt = response.data.find(obj => obj.totalCnt)?.totalCnt;
                this.rows = totalCnt

                this.$nextTick(() => {
                    if (this.$refs.agGrid && this.$refs.agGrid.api) {
                        this.$refs.agGrid.api.setRowData(this.rowData);
                    }
                });

            })



        },
        async onPaginationInput(event) {
            const currentPage = event;
            console.log("현제 페이지 번호: " + currentPage);
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
                currentPage: currentPage

            }).then(response => {
                console.log("데이터객체확인: ",response.data)
                this.columnDefs = [
                    { field: 'number' ,headerName: '번호'},
                    { field: 'state' , headerName: '상태'},
                    { field: 'title' , headerName: '제목'},
                    { field: 'closeAt',headerName: '해결일'},
                    { field: 'createAt' ,headerName: '등록일'},
                    { field: 'register' ,headerName: '등록자'},
                    { field: 'labels' ,headerName: '라벨'}
                ];

                this.rowData = response.data;
                const totalCnt = response.data.find(obj => obj.totalCnt)?.totalCnt;
                this.rows = totalCnt
                this.currentPage = currentPage
                this.$nextTick(() => {
                    if (this.$refs.agGrid && this.$refs.agGrid.api) {
                        this.$refs.agGrid.api.setRowData(this.rowData);
                    }
                });

            })
        },

    }

}
</script>

<style scoped>
.outer {
    display: flex;
    justify-content: center;
}

</style>
