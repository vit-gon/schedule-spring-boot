import Vue from 'vue';
import VueResource from 'vue-resource';
import '@babel/polyfill';
import router from '@/router/router';
import App from '@/components/App.vue';
import store from '@/store/store';
import BootstrapVue from 'bootstrap-vue';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';

Vue.use(BootstrapVue)
Vue.use(VueResource)

new Vue({
    el: '#main-container',
    store,
    router,
    render: a => a(App)
});
