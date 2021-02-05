

var app = new Vue({
    el: '#app',
    data() {
        return {
            dataWidthHeight: {
                iw: "InnerWidth: " + window.innerWidth,
                ow: "OuterWidth: " + window.outerWidth,
                dcw: "ClientWidth(doc): " + document.documentElement.clientWidth,
                rw: "ScrollWidth(doc): " + document.documentElement.scrollWidth,
                bcw: "ClientWidth(body): " + document.body.clientWidth,
                aw: "AvailWidth: " + window.screen.availWidth,
                sw: "ScreenWidth: " + window.screen.width,
                ih: "InnerHeight: " + window.innerHeight,
                oh: "OutHeight: " + window.outerHeight,
                dch: "ClientHeight(doc): " + document.documentElement.clientHeight,
                rh: "ScrollHeight(doc): " + document.documentElement.scrollHeight,
                bch: "ClientHeight(body): " + document.body.clientHeight,
                ah: "AvailHeight: " + window.screen.availHeight,
                sh: "ScreenHeight: " + window.screen.height,
            },
            dataURL: {
                protocol: "协议: " + window.location.protocol,
                hostname: "主机地址名: " + window.location.hostname,
                port: "端口号: " + window.location.port,
                pathname: "路径名: " + window.location.pathname,
            },
            dataHardware: {
                cpu: "CPU个数: " + window.navigator.hardwareConcurrency,
                storage: "设备存储: " + window.navigator.deviceMemory,
                touch: "最大触点数: " + window.navigator.maxTouchPoints,
            },
            dataConnection: {
                na: "用户代理: " + window.navigator.userAgent,
                vendor: "开发商: " + window.navigator.vendor,
                platform: "操作系统: " + window.navigator.platform,
                ol: "联网状态: " + (window.navigator.onLine ? "在线" : "离线"),
                condl: "网络下行速度: " + window.navigator.connection.downlink,
                conet: "网络类型: " + window.navigator.connection.effectiveType,
                conrtt: "估算往返时间: " + window.navigator.connection.rtt,
            },
            dataOther: {
                sc: "色深: " + window.screen.colorDepth,
                sd: "位深: " + window.screen.pixelDepth,
                lang: "语言: " + window.navigator.language
            },
            params: "",
            getRst: "",
            transferData2:""
        }
    },
    mounted() {
        this.getGeolocation();
        this.plot();
        setInterval(() => {
            this.checkVisibility();
        }, 1000);
    },
    // 父组件传入的参数
    props: {
        transferData: String,
    },
    // 动态props
    watch: {
        transferData: function (newVal) {
            this.transferData2 = newVal; //newVal即是transferData
        },
    },
    methods: {
        // title时钟，当页面在前台可见时
        checkVisibility: function () {
            let vs = document.visibilityState;
            let date = new Date(Date.now());
            if (vs == "visible") {
              document.title =
                "VueFlask - " +
                date.getHours() +
                ":" +
                date.getMinutes() +
                ":" +
                date.getSeconds();
            }
        },
        // get data from backend
        get: function () {
            // 路由传参
            axios.get(`http://127.0.0.1:5000/api/?pkg=${this.params}`).then(
                (response) => {
                    if (response.data.error == "error") {
                        console.log("bakend error");
                    } else {
                        this.getRst = response.data.result;
                    }
                },
                (err) => {
                    console.log("frontend error", err);
                }
            );
        },
        get: function () {
            // 问号传参
            axios.get(`http://127.0.0.1:5000/api/${this.params}`).then(
                (response) => {
                    if (response.data.error == "error") {
                        console.log("bakend error");
                    } else {
                        this.getRst = response.data.result;
                    }
                },
                (err) => {
                    console.log("frontend error", err);
                }
            );
        },
        postOne: function () {
            axios
              .post(`http://127.0.0.1:5000/api/`, {
                key: JSON.stringify(this.params)
              })
              .then(
                (response) => {
                    if (response.data.error == "error") {
                        console.log("bakend error");
                    } else {
                        this.getRst = response.data.result;
                    }
                },
                function (err) {
                  console.log(err.data);
                }
              );
          },
        // video drag and drop
        allowDrop(ev) {
            ev.preventDefault();
        },
        drag(ev) {
            ev.dataTransfer.setData("Text", ev.target.innerText);
        },
        drop(ev) {
            ev.preventDefault();
            var data = ev.dataTransfer.getData("Text");
            ev.target.src = data;
        },
        // plot china/world maps
        // if use vue-cli
        // var echarts = require('echarts');
        // import '../assets/js/china.js';
        plot() {
            let myChart = echarts.init(document.getElementById("charts"));
            let option = {
                series: [
                    {
                        type: "map",
                        map: "china",
                        zoom: 1,
                        roam: true,
                        scaleLimit: {
                            min: 1,
                            max: 10,
                        },
                    },
                ],
            };
            myChart.setOption(option);
        },
        // geolocation
        getGeolocation() {
            navigator.geolocation.getCurrentPosition(this.sendNotification);
        },
        sendNotification(position) {
            // get geolocation
            let latitude =
                position.coords.latitude > 0
                    ? position.coords.latitude + " N"
                    : position.coords.latitude + " S";
            let longitude =
                position.coords.longitude > 0
                    ? position.coords.longitude + " E"
                    : position.coords.longitude + " W";
            // Notification
            var n = new Notification("Your Location", {
                body: `${latitude},${longitude}`,
                tag: "backup",
                requireInteraction: false,
                data: {
                    loc: `${latitude},${longitude}`,
                },
            });
            n.onclick = function () {
                n.close();
            };
        },
        // take photo
        getMedia() {
            let video = document.getElementById("video001");
            let constraints = {
                video: { width: 300, height: 300 },
                audio: true
            };
            if (navigator.mediaDevices.getUserMedia(constraints) == 'undefined') {
                alert("can't use media devices!");
            } else {
                var promise = navigator.mediaDevices.getUserMedia(constraints);
            }
            promise.then(function (MediaStream) {
                video.srcObject = MediaStream;
                video.play();
            }).catch(function (PermissionDeniedError) {
                console.log(PermissionDeniedError);
            })
        },
        takePhoto() {
            let video = document.getElementById("video001");
            let canvas = document.getElementById("canvas001");
            let ctx = canvas.getContext('2d');
            ctx.drawImage(video, 0, 0, 240, 240);
        }
    }
});