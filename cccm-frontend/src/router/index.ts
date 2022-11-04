import Vue from 'vue'
import VueRouter from 'vue-router'

import HomeView from '../components/HomeView.vue'
import CmpFormView from '../components/CmpForm.vue'
import ClientSearchView from '../components/common/FormioClientSearch.vue'
import ClientRecordView from '../components/ClientRecord.vue'
import Unauthorized from '../components/Unauthorized.vue'
import DashboardPOView from '../components/DashboardPOView.vue'
import RNAList from '../components/RNAList.vue'
import DashboardSupervisorView from '../components/DashboardSupervisorView.vue'
import {useStore} from "@/stores/store";

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
      path: '/dashboardpo/:param?', //? makes the param optional
      name: 'dashboardpo',
      component: DashboardPOView,
      meta: {
        isAuthenticated: true
      }
    },
    {
      path: '/dashboardsupervisor',
      name: 'dashboardsupervisor',
      component: DashboardSupervisorView,
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
      path: '/clientrecord/:csNumber/:tabIndex',
      name: 'clientrecord',
      component: ClientRecordView,
      meta: {
        isAuthenticated: true
      }
    },
    {
      path: '/clientrecord/:csNumber/form/:formID',
      name: 'cmpform',
      component: CmpFormView,
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
// po-manage
router.beforeEach((to, from, next) => {
  // Fetch the default location
  const store = useStore();
  store.getUserDefaultLocation();
  store.getUserLocations();
  
  //console.log("In router, store: ", store.locationCD, store.locationDescription);
  
  if (to.meta.isAuthenticated) {
    // Get the actual url of the app, it's needed for Keycloak
    const basePath = window.location.toString();  
    const subjectID = Vue.$keycloak.subject;
    //console.log("Vue.$keycloak: ", Vue.$keycloak);
    if (!Vue.$keycloak.authenticated) {
      // The page is protected and the user is not authenticated. Force a login.
      //console.log("Not authenticated");
      Vue.$keycloak.login({ redirectUri: basePath.slice(0, -to.path.length) + to.path })
     } else if (Vue.$keycloak.hasRealmRole('client-search', 'client-view', 
                'data-view', 'form-add', 'form-delete', 'form-update', 'form-view')
              || Vue.$keycloak.hasRealmRole('po-manage', 'client-search', 'client-view', 
                'data-view', 'form-add', 'form-delete', 'form-update', 'form-view')) {
      // The user was authenticated, and has the app role
      //Refresh the access token and renew the session of the user.
      //console.log("Authenticated and has 'client-search' role");
      Vue.$keycloak.updateToken(70)
        .then(() => {
          // if the login user is supervisor, direct them to dashboardsupervisor view
          if (to.name == 'home') {
            if (Vue.$keycloak.hasRealmRole('po-manage')) {
              next({ name: 'dashboardsupervisor' });
            } else {
              next({ name: 'dashboardpo' })
            }
          } else {
            // if a PO tries to access supervisor dashboard, direct him to PO dashboard.
            if (to.name == 'dashboardsupervisor' && !Vue.$keycloak.hasRealmRole('po-manage')) {
              next({ name: 'dashboardpo' })
            } else {
              // otherwise, direct them to dashboardpo view
              next()
            }
          }
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
