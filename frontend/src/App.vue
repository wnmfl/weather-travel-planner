<template>
  <div class="app-container">
    <header class="app-header">
      <h1>🌍 Weather Travel Planner</h1>
      <p>天气赋能的旅游计划定制系统</p>
    </header>

    <main class="app-main">
      <div class="welcome-section">
        <h2>欢迎使用天气旅游规划系统</h2>
        <p>输入出发地、目的地和时间，获取最优的旅游路线规划</p>

        <div class="search-form">
          <el-input v-model="searchForm.origin" placeholder="出发地" style="margin: 10px;"></el-input>
          <el-input v-model="searchForm.destination" placeholder="目的地" style="margin: 10px;"></el-input>
          <el-button type="primary" @click="handleSearch">搜索路线</el-button>
        </div>
      </div>
    </main>

    <footer class="app-footer">
      <p>&copy; 2024 Weather Travel Planner</p>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const searchForm = ref({
  origin: '',
  destination: ''
})

const handleSearch = () => {
  alert('功能开发中...')
}
</script>

<style scoped>
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.app-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 40px 20px;
  text-align: center;
}

.app-header h1 {
  margin: 0;
  font-size: 48px;
}

.app-main {
  flex: 1;
  padding: 40px 20px;
}

.welcome-section {
  max-width: 800px;
  margin: 0 auto;
  background: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.app-footer {
  background: rgba(0, 0, 0, 0.1);
  color: white;
  text-align: center;
  padding: 20px;
}
</style>
