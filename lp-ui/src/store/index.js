import { createStore } from 'vuex'
import user from '@/store/moduels/user'
import actions from '@/store/actions'
import mutations from '@/store/mutations'

const store = createStore({
    modules:{
        user
    },
    actions,
    mutations,
})

export default store