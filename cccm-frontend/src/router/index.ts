import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'
import CrnaCmpView from '../components/crna-cmp/crnaCmpForm.vue'
import SaraCmpView from '../components/sara-cmp/saraCmpForm.vue'
import ClientSearchView from '../views/ClientSearchView.vue'
import ClientRecordView from '../views/ClientRecord.vue'
import Unauthorized from '../views/Unauthorized.vue'
import RNAListView from '../views/RNAList.vue'
import DashboardPOView from '../views/DashboardPOView.vue'
import DashboardSupervisorView from '../views/DashboardSupervisorView.vue'
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
      path: '/dashboardpo/:poID',
      name: 'dashboardpo',
      component: DashboardPOView,
      meta: {
        isAuthenticated: true
      }
    },
    {
      path: '/dashboardsupervisor/:supervisorID',
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
      path: '/clientrecord/:clientID/:csNumber',
      name: 'clientrecord',
      component: ClientRecordView,
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
      path: '/saracmp/:formID',
      name: 'saracmp',
      component: SaraCmpView,
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
// po-manage
router.beforeEach((to, from, next) => {
  // Fetch the default location
  const store = useStore();
  store.getLocation();
  //console.log("In router, store: ", store.locationCD, store.locationDescription);
  
  if (to.meta.isAuthenticated) {
    // Get the actual url of the app, it's needed for Keycloak
    const basePath = window.location.toString();  
    const subjectID = Vue.$keycloak.subject;
    console.log("Vue.$keycloak: ", Vue.$keycloak);
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
              next({ name: 'dashboardsupervisor', params: { supervisorID: subjectID } });
            } else {
              next({ name: 'dashboardpo', params: { poID: subjectID }  })
            }
          } else {
            // if a PO tries to access supervisor dashboard, direct him to PO dashboard.
            if (to.name == 'dashboardsupervisor' && !Vue.$keycloak.hasRealmRole('po-manage')) {
              next({ name: 'dashboardpo', params: { poID: subjectID }  })
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
