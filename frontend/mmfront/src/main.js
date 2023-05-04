import Vue from 'vue'
import HighchartsVue from 'highcharts-vue'
import App from "@/App.vue";
Vue.use(HighchartsVue)

Vue.config.productionTip = false

new Vue({
  render: h => h(App),
}).$mount('#app')


