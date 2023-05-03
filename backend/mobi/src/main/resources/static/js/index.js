console.log("확인")

axios({
    method: 'get',
    url: '/sample',
}).then(function (response){
    console.log(response.toString())
});