let ws = {
    singleSend: function (form,to,message){
        if (!window.WebSocket){
            return;
        }
        if (socket.readyState === WebSocket.OPEN){
            let data = {
                "form": form,
                "to": to,
                "message": message,
                "type": "SINGLE_SENDING"
            };
            socket.send(JSON.stringify(data));
        }else {
            console.log("Websocket连接没有开启！");
        }
    },

    register(){
        if (!window.WebSocket) {
            return;
        }
        if (socket.readyState === WebSocket.OPEN){
            let data = {
                "username": username,
                "type": "REGISTER"
            }
            socket.send(JSON.stringify(data));
        }else {
            alert("Websocket连接没有开启！");
        }
    },

    registerReceive(){
        console.log("username为 " + username + " 的用户登记到在线用户表成功！");
    },

    //接收消息,先判断是否是第一次聊天，如果是好友列表添加新的一行。
    singleReceive: function (data){
        console.log(data);
        let form = data.form;
        let message = data.message;
        var answer='';
        answer += '<li style="display: flex;flex-direction: column">' +
                    '<div style="float: left">' +
                        '<div class="answerHead"><img src="https://img1.baidu.com/it/u=4096419636,635686539&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"/></div>' +
                        '<div class="answers">'+ message +'</div>' +
                    '</div>'+
                   '</li>';
        $('.newsList').append(answer);
        let needRed = document.getElementById(form+"Red")
        needRed.classList.add('red-point');
    }
}

//发送消息
function sendMessage(){
    console.log("点击了发送按钮");
    let form = username;
    let to = $('#to').val();
    let message = $('#message').val();
    console.log("发送的消息为:"+message)
    ws.singleSend(form,to,message);
    //消息发出去后要把消息清空
    $('#message').val('');
    let avatarUrl = $('#avatarUrl').attr("src");
    let messageHtml = '';
    messageHtml += '<li style="display: flex;flex-direction: column">'+
            '<div style="float: right">' +
                '<div class="nesHead"><img src="' + avatarUrl + '"/></div>' +
                '<div class="news">' + message + '</div>' +
            '</div>'
        '</li>';
    $('.newsList').append(messageHtml);
    $('.RightCont').scrollTop($('.RightCont')[0].scrollHeight );
}