<template>
  <el-container>
    <el-aside width="780px">
      <el-image  fit="fill" :src="require('@/assets/image/login_left.png')">
      </el-image>
    </el-aside>
    <el-main>
      <el-card class="box-card login-card">
        <span class="login-title">元动力后台管理系统</span>
        <span class="login-tip">welcome 欢迎登陆</span>
        <el-form ref="user" :model="user" :rules="rules" label-width="80px">
          <el-form-item label="用户名" prop="userName">
            <el-input
              v-model="user.userName"
              placeholder="请输入用户"
            ></el-input>
          </el-form-item>
          <el-form-item label="密&nbsp;码" prop="password">
            <el-input
              v-model="user.password"
              type="password"
              placeholder="请输入密码"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="toLogin">登陆</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-main>
  </el-container>
</template>

<script>
import { ElMessage } from 'element-plus';
import {getInfo,getById} from '@/api/user'
export default {
  name: "login",
  data() {
    return {
      // 用户信息
      user: {
        userName: "lp_admin",
        password: "lptest1",
      },
      rules: {
        userName: [
          {
            required: true,
            message: "用户名不能为空",
            trigger: "blur",
          },
          {
            min: 3,
            max: 10,
            message: "用户名的长度必须是3-10",
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: "密码不能为空",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    toLogin() {
      // this.$router.push({name:'main'})
      // login(this.user).then(res =>{
      //   console.log(res)
      // })
      this.$refs.user.validate((valid) =>{
        // alert(valid);
        if(valid){// 登陆完成后立刻获取用户的信息
        // console.log(valid)
          this.$store.dispatch("LOGIN", this.user).then(res =>{
            console.log(res);
            if(res.status === 200){
              this.$store.dispatch("GET_INFO").then(()=>{
                this.$router.push({name:'main'})
              })
              // 进行权限信息获取
              // getInfo();
              // setTimeout(()=>{
              //   getById(1)
              // },2000)
              // this.$router.push({name:'main'});//跳转主页面
              // ElMessage('登陆成功！');
            }
          });//调用store中的LOGIN方法，获取promise返回值
        }else{
          ElMessage('输入的数据不合法！');
        }
      });
    },
  },
};
</script>

<style scope>
.el-image {
  height: 885px;
  width: 750px;
}
.el-main {
  position: relative;
}
.login-card {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  margin: auto;
  width: 480px;
  height: 400px;
  padding: 50px;
}
.login-title {
  width: 459px;
  height: 70px;
  font-size: 40px;
  font-family: AlibabaPuHuiTiB;
  color: #333333;
  line-height: 90px;
  letter-spacing: 1px;
  font-weight: 800;
  display: block;
  text-align: left;
}
.login-tip {
  width: 319px;
  height: 70px;
  font-size: 30px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #999999;
  line-height: 90px;
  letter-spacing: 1px;
  display: block;
  text-align: left;
  margin-bottom: 30px;
}
</style>

