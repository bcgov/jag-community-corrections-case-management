import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'
import CrnaCmpView from '../views/CrnaCmpView.vue'
import ClientSearchView from '../views/ClientSearchView.vue'
import ClientProfileView from '../components/common/FormioClientProfile.vue'
import Unauthorized from '../views/Unauthorized.vue'
import RNAListView from '../views/RNAList.vue'

Vue.use(VueRouter)

const router = new VueRouter({
  mode: 'history',
  base: import.meta.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: {
        isAuthenticated: true
      }
    },
    {
      path: '/clientsearch',
      name: 'clientsearch',
      component: ClientSearchView,
      meta: {
        isAuthenticated: true
      }
    },
    {
      path: '/clientProfile/:id',
      name: 'clientprofile',
      component: ClientProfileView,
      meta: {
        isAuthenticated: true
      }
    },
    {
      path: '/crnacmp/:formID',
      name: 'crnacmp',
      component: CrnaCmpView,
      meta: {
        isAuthenticated: true
      }
    },
    {
      path: '/rnalist',
      name: 'rnalist',
      component: RNAListView,
      meta: {
        isAuthenticated: true
      }
    },
    {
      path: '/',
      name: 'unauthorized',
      component: Unauthorized
    }
  ]
})

// List of realm roles:
// client-search	
// client-view	
// data-view	
// default-roles-cccm	
// form-add
// form-delete	
// form-update	
// form-view
router.beforeEach((to, from, next) => {
  if (to.meta.isAuthenticated) {
    // Get the actual url of the app, it's needed for Keycloak
    const basePath = window.location.toString();  
    const roles = ['client-search', 'client-view'];
    //console.log("Vue.$keycloak: ", Vue.$keycloak);
    if (!Vue.$keycloak.authenticated) {
      // The page is protected and the user is not authenticated. Force a login.
      //console.log("Not authenticated");
      Vue.$keycloak.login({ redirectUri: basePath.slice(0, -to.path.length) + to.path })
     } else if (Vue.$keycloak.hasRealmRole('client-search', 'client-view', 
              'data-view', 'form-add', 'form-delete', 'form-update', 'form-view')) {
      // The user was authenticated, and has the app role
      //Refresh the access token and renew the session of the user.
      //console.log("Authenticated and has 'client-search' role");
      Vue.$keycloak.updateToken(70)
        .then(() => {
          next()
        })
        .catch(err => {
          console.error(err)
        })
    } else {
      // The user was authenticated, but did not have the correct role
      // Redirect to an error page
      // TODO: build unauthorized view
      next({ name: 'unauthorized' })
    }
  } else {
    // This page did not require authentication
    next()
  }
})

export default router
