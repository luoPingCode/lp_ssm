<template>
  <el-form :inline="true" :model="queryParam" class="demo-form-inline">
    <el-form-item label="用户名：">
      <el-input v-model="queryParam.userName" placeholder="用户名" />
    </el-form-item>
    <el-form-item label="昵称：">
      <el-input v-model="queryParam.nickName" placeholder="昵称" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">查询</el-button>
    </el-form-item>
  </el-form>
  <div class="flex">
    <el-button
      type="primary"
      :icon="Plus"
      v-hasRole="['lp_admin']"
      @click="createUser"
    />
    <el-button type="primary" :icon="Share" />
    <el-button type="primary" :icon="Delete" />
    <el-button type="primary" :icon="Search">Search</el-button>
    <el-button type="primary">
      Upload<el-icon class="el-icon--right"><Upload /></el-icon>
    </el-button>
  </div>
  <el-table :data="tableData" style="width: 100%">
    <el-table-column label="用户名" prop="userName" />
    <el-table-column label="昵称" prop="nickName" />
    <el-table-column label="邮箱" prop="email" />
    <!-- <el-table-column label="昵称" prop="emil" /> -->
    <el-table-column align="right">
      <template #header>
        <el-input v-model="search" size="small" placeholder="Type to search" />
      </template>
      <template #default="scope">
        <el-button size="small" @click="handleEdit(scope.$index, scope.row)"
          >编辑</el-button
        >
        <el-popconfirm
          title="您确定要删除吗?"
          @confirm="confirmEvent(scope.$index, scope.row)"
        >
          <template #reference>
            <el-button
              size="small"
              type="danger"
              >删除</el-button
            >
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>
  <el-pagination
    background
    layout="prev, pager, next"
    :total="total"
    :page-size="queryParam.size"
    @current-change="pageChange"
  />
  <!-- //新增用户弹框 -->
  <el-dialog v-model="userDialogVisible" :title="title" width="30%" center>
    <el-form ref="userFormRef" :model="userForm" label-width="120px">
      <el-form-item label="用户名">
        <el-input v-model="userForm.userName" />
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="userForm.nickName" />
      </el-form-item>
      <el-form-item label="密码" v-if="userForm.userId === null">
        <el-input v-model="userForm.password" />
      </el-form-item>
      <el-form-item label="确认密码" v-if="userForm.userId === null">
        <el-input v-model="userForm.confirmPwassword" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="userDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitUserform">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { listUser, addUser, getById, updateUser, deteleUser } from "@/api/user";
import {
  Delete,
  Edit,
  Search,
  Share,
  Upload,
  Plus,
} from "@element-plus/icons-vue";
import { ElMessage } from 'element-plus'

let title = ref(""); //修改或新增弹窗标题
let userDialogVisible = ref(false);
let tableData = ref([]);
let total = ref(0);
// const pageSize = ref(2)
//   const  page= ref(0)
let queryParam = ref({
  size: 2,
  page: 0,
  userName: "",
  nickName: "",
});

//新增用户弹窗填写值
let userForm = ref({
  userId: null,
  userName: "",
  nickName: "",
  password: "",
  confirmPwassword: "",
});

let getUserList = function () {
  console.log('option:1111');
  listUser(queryParam.value).then((res) => {
    console.log('----------'+res);
    // console.log(queryParam.value.size)
    tableData.value = res.data.content;
    total.value = res.data.totalElements;
    queryParam.value.size = res.data.size;
  });
};
onMounted(() => {
  // getUserList({page: page.value,size: pageSize.value});
  getUserList();
});
//翻页
const pageChange = async function (current) {
  // getUserList({page: current - 1,size: pageSize.value});
  console.log(current);
  queryParam.value.page = current - 1;
  getUserList();
};

const onSubmit = function () {
  getUserList();
};

let createUser = function () {
  //清理数据
  userForm.value = {
    userId: null,
    userName: "",
    nickName: "",
    password: "",
    confirmPwassword: "",
  };
  title.value = "新增用户";
  userDialogVisible.value = true;
};

//提交新增、修改用户表单
let submitUserform = function () {
  // console.log(userForm);
  //对比密码和确认密码

  //校验表单

  //提交Ajax请求
  delete userForm.value.confirmPwassword; //删除确认密码属性
  //判断当前是新增还是修改
  if (userForm.value.userId === null) {
    addUser(userForm.value).then((res) => {
      //新增
      console.log("------" + res);
      if (res.status === 200) {
        userDialogVisible.value = false;
        getUserList();
        ElMessage({message:'添加成功！',type: 'success',});
      }
      
    });
  } else {
    updateUser(userForm.value).then((res) => {
      //修改
      if (res.status === 200) {
        userDialogVisible.value = false;
        // 更新列表
        getUserList();
        ElMessage({message:'修改成功！',type: 'success',});
      }
      
    });
  }
};
//修改用户
const handleEdit = function (index, row) {
  title.value = "修改用户";
  getById(row.userId).then((res) => {
    userForm.value = res.data;
    userDialogVisible.value = true; //打开修改窗口
  });
};
//删除用户
// const handleDelete = function (index, row) {
//   // console.log(index + "-------" + row);
// };
const confirmEvent = function(index, row){
  // console.log(index + "-------" + row.userId);
  deteleUser(row.userId).then(res =>{
    // console.log(res)
    if(res.data === true){
      getUserList();
      ElMessage({message:'删除成功！',type: 'success',});
    }
  });
}
</script>
