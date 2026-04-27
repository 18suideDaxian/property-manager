import Vue from 'vue'
import App from './App'

// 引入 uView UI
import uView from 'uview-ui'
Vue.use(uView)

// 引入封装的请求方法挂载到全局
import { get, post, put, del, upload } from './utils/request.js'
Vue.prototype.$http = { get, post, put, del, upload }

// 引入 auth 工具
import * as auth from './utils/auth.js'
Vue.prototype.$auth = auth

// 引入 config
import config from './utils/config.js'
Vue.prototype.$config = config

Vue.config.productionTip = false

App.mpType = 'app'

const app = new Vue({
  ...App
})
app.$mount()
