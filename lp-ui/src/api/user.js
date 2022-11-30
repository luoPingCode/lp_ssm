import request from "@/api";


//登陆用户校验
export function login(data){
    console.log(data);
    return request({
        url:'/login',
        method:'post',
        data:data
    })
}
//登出
export function logout(){
    return request({
        url:'/logout',
        method:'get'
    })
}
//查询用户列表
export function listUser(data){
    console.log(data)
    return request({
        url:'/ydlUser',
        method:'get',
        params:data
    })
}
//获取用户权限信息
export function getInfo(){
    return request({
        url:'/ydlUser/getInfo',
        method:'get'
    })
}
//根据ID查询
export function getById(id){
    return request({
        url:'/ydlUser/'+id,
        method:'get'
    })
}
//新增用户
export function addUser(data){
    console.log(data);
    return request({
        url:'/ydlUser',
        method:'post',
        data:data
    })
}
//修改用户
export function updateUser(data){
    console.log(data);
    return request({
        url:'/ydlUser',
        method:'put',
        data:data
    })
}
//删除用户请求
export function deteleUser(id){
    console.log('要删除的用户ID'+id);
    return request({
        url:'/ydlUser/'+id,
        method:'delete',
        // data:id
    })
}
//添加用户
// export function addUser(data){
//     return request({
//         url:'/user',
//         method:'post',
//         data:data
//     })
// }