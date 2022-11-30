//导入用来创建路由和确定路由模式的两个方法
import { login } from '@/api/user';
import store from '@/store';
import storage from '@/util/storage';
import {
    createRouter,
    createWebHistory
} from 'vue-router'
/**
*定义路由信息*
*/
const routes = [{
    name: 'login',
    path: '/login',
    component: () => import('@/components/login'),
},
{
    name: 'main',
    path: '/main',
    component: () => import('@/components/main'),
    children: [
        {
            name:'user',
            path:'/user',
            component:()=>import('@/components/system/user')
        }
    ]
}
]
//创建路由实例并传递`routes`配置
//我们在这里使用html5的路由模式，url中不带有#，部署项目的时候需要注意。
const router = createRouter({
    history: createWebHistory(),
    routes,
})
//全局的路由守卫,会在每次路由进行跳转时执行
router.beforeEach((to) => { //to 你要去那个路由，from 你从那个路由来
    //每次进行路由切换都判断一下有没有登陆，如果没有则路由到登陆界面，反之则放行
    // console.log(to);
    // 1、如果是登陆页面，就放行
    if (to.name === 'login') {
        return true;
    }
    // 2、检查是否登陆，如果已经登陆则放行
    // console.log(login)
    let res = store.getters.userIsLogin
    console.log(res)
    if (!res) {
        //去storage中查看，如果没有，则跳转登陆页面
        if (!storage.getSessionObject("loginUser")) {
            router.push({ name: "login" });
        } else {//如果有，则重新保存等数据
            store.dispatch("RECOVERY_USER");
            //重新加载个人权限信息
            store.dispatch("GET_INFO")
        }
    }
    // 3、没有登陆就跳转登陆页面

    // return true
})
//路由导出实例
export default router
