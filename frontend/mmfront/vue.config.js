module.exports = {
  devServer: {
    proxy: {
      // 경로가 '/api'일 경우에는 프록시를 통해 우회하여 요청
      "/api": {
        target: "http://localhost:8080",
      },
    },
  },
};
