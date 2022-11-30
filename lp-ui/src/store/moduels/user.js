import { getInfo, login, logout } from '@/api/user.js'
import storage from '@/util/storage.js'
const user = {
    state: {
        username: '',
        nickname: '',
        token: '',
        roles:[],//用户角色
        permissions: []
    },
    getters: {
        userIsLogin(state) {
            return state.username !== '' && state.token !== ''
        },
        permissions(state) {
            return state.permissions;
        },
        roles(state) {
            return state.roles;
        }

    },
    // 设置具体要保持的数据，类似java set
    mutations: {
        SAVE_USER(state, username) {
            state.username = username;
        },
        SAVE_NICKNAME(state, nickname) {
            state.nickname = nickname;
        },
        SAVE_TOKEN(state, token) {
            state.token = token;
        },
        SAVE_ROLES(state, roles) {
            state.roles = roles;
        },
        SAVE_PERMISSIONS(state, permissions) {
            state.permissions = permissions;
        }

    },
    actions: {
        // 登陆
        LOGIN({ commit }, user) {
            return new Promise(function (reslove) {
                login(user).then(res => {
                    // 需要将获取的数据，保存起来
                    console.log(res.data.ydlUser)
                    commit("SAVE_USER", res.data.ydlUser.userName);
                    // storage.saveSessionString("username", res.data.ydlUser.username)
                    commit("SAVE_NICKNAME", res.data.ydlUser.nickName);
                    // storage.saveSessionString("username", res.data.ydlUser.nickName)
                    commit("SAVE_TOKEN", res.data.token);
                    // storage.saveSessionString("token", res.data.token)
                    storage.saveSessionObject("loginUser", res.data);
                    reslove(res);//把结果存入
                })
            });
        },
        //鉴权处理，保存进vueX中
        GET_INFO({commit}){
            return new Promise(function(reslove){
                getInfo().then(res =>{
                    console.log(res)
                    commit("SAVE_ROLES", res.data.roles);
                    commit("SAVE_PERMISSIONS",res.data.perms);
                    reslove();
                })
            })
        },
        // 登出
        LOGOUT({ commit }) {
            return new Promise(function (reslove) {
                logout().then(res => {
                    // 需要将获取的数据，保存起来
                    // console.log(res.data.ydlUser)
                    commit("SAVE_USER", '');
                    // storage.saveSessionString("username", res.data.ydlUser.username)
                    commit("SAVE_NICKNAME", '');
                    // storage.saveSessionString("username", res.data.ydlUser.nickName)
                    commit("SAVE_TOKEN", '');
                    // storage.saveSessionString("token", res.data.token)
                    storage.remove("loginUser");
                    reslove(res);//把结果存入
                })
            });
        },
        // 刷新时，重新vuex写入数据
        RECOVERY_USER({ commit }) {
            // 获取登陆的用户数据
            let loginUser = storage.getSessionObject("loginUser");
            console.log(loginUser)
            // 重新保存
            commit("SAVE_USER", loginUser.ydlUser.userName);
            // storage.saveSessionString("username", res.data.ydlUser.username)
            commit("SAVE_NICKNAME", loginUser.ydlUser.nickName);
            // storage.saveSessionString("username", res.data.ydlUser.nickName)
            commit("SAVE_TOKEN", loginUser.token)
        }
    }
}

export default user