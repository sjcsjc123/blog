//展示好友列表
function setUserInfo(){
    $.ajax({
        type: 'GET',
        url: 'getUserinfo',
        dataType: 'json',
        async: true,
        success: function (res){
            console.log("获取用户信息...");
            console.log(res);
            //响应状态码为200时,添加到聊天好友列表里
            if (res.status === 200){
                let userInfo = res.data.userInfo;
                let friendList = userInfo.friends;
                $("#username").html(userInfo.username);
                let friendListHtml = "";
                /**
                 * 还需要判断是否有聊天记录，没有则不加载。
                 */
                for (let i = 0; i < friendList.length; i++) {
                    friendListHtml +=
                        '<li>' +
                        '<div class="liLeft"><img' +
                        ' src="https://img1.baidu.com/it/u=4096419636,635686539&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"></div>' +
                        '<div class="liRight">' +
                        '<span class="interName" id="' + friendList[i] + 'Red">' + friendList[i] + '</span>' +
                        '<span class="infor"></span>' +
                        '</div>' +
                        '</li>';
                }
                $('.conLeft ul').append(friendListHtml);
                $('.conLeft ul li').on('click',friendLiClickEvent);
                $('.defaultConRight').css("display","-webkit-box");
            }else {
                alert(res.msg);
            }
        }
    })
}

//点击好友列表展示右边聊天框
function friendLiClickEvent(){
    $(this).addClass('bg').siblings().removeClass('bg');
    $('.defaultConRight').css("display","none");
    $('.conRight').css("display","-webkit-box");
    //获取要聊天的用户
    let toUser = $(this).children('.liRight').children('.interName').text();
    $('.headName').html(toUser);
    $('#to').val(toUser);
    $('.newsList').html("");
    let removeRed = document.getElementById(toUser+"Red");
    removeRed.classList.remove('red-point')
}