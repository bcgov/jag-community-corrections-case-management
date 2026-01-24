import { createRouter, createWebHistory } from 'vue-router'

import HomeView from '../components/HomeView.vue'
import CmpFormView from '../components/CmpForm.vue'
import ClientSearchView from '../components/common/FormioClientSearch.vue'
import ClientRecordView from '../components/ClientRecord.vue'
import Unauthorized from '../components/Unauthorized.vue'
import DashboardPOView from '../components/dashboard/DashboardPOView.vue'
import RNAList from '../components/RNAList.vue'
import DashboardSupervisorView from '../components/dashboard/DashboardSupervisorView.vue'
import DashboardITRPView from "../components/dashboard/DashboardITRPView.vue";
import PrintView from '../components/PrintView.vue'
import { useStore } from "@/stores/store";
import { APP_GLOBALS } from '@/constants/appGlobals';
import { keycloak } from '@/plugins/authentication';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
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
      path: '/dashboarditrp',
      name: 'dashboarditrp',
      component: DashboardITRPView,
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
      path: '/client/:csNumber/form/:formID/:param?', //param is optional 
      name: 'cmpform',
      component: CmpFormView,
      meta: {
        isAuthenticated: true
      }
    },
    {
      path: '/unauthorized',
      name: 'unauthorized',
      component: Unauthorized
    },
    {
      path: '/print/:param', //client/:csNumber/form/:formID',
      name: 'printView',
      component: PrintView
    }
  ]
})

router.beforeEach((to: any, from: any, next: any) => {
  // Fetch the default location
  const store = useStore();
  store.getUserDefaultLocation();
  store.getUserLocations();
  store.getLoginUserGroups();
  store.getLoginUserName();

  //console.log("Store: ", store.locationCD, store.locationDescription, APP_GLOBALS.$USER_GROUP_SUPERVISOR, store.loginUserGroups);
  
  if (to.meta.isAuthenticated) {
    // Keycloak init in main.ts already enforces login-required.
    if (!keycloak.authenticated) {
      next({ name: 'unauthorized' })
    } else if (store.loginUserGroups != null) {
      // The user was authenticated, and has the app role
      //Refresh the access token and renew the session of the user.
      //console.log("Authenticated and has 'client-search' role");
      keycloak.updateToken(70)
        .then(() => {
          if (to.name == APP_GLOBALS.$ROUTER_NAME_HOME) {
            // if the login user is supervisor, direct them to dashboardsupervisor view
            if (store.loginUserGroups.includes(APP_GLOBALS.$USER_GROUP_SUPERVISOR)) {
              next({ name: 'dashboardsupervisor' });
            // if the login user is po, direct them to dashboardpo view
            } else if (store.loginUserGroups.includes(APP_GLOBALS.$USER_GROUP_PO)) {
              next({ name: 'dashboardpo' })
            // if the login user is ITRP, direct them to dashboarditrp view
            } else if (store.loginUserGroups.includes(APP_GLOBALS.$USER_GROUP_ITRP)) {
              next({ name: 'dashboarditrp' })
            // if the login user is research, direct them to clientsearch view
            } else if (store.loginUserGroups.includes(APP_GLOBALS.$USER_GROUP_RESEARCHER) ||
                       store.loginUserGroups.includes(APP_GLOBALS.$USER_GROUP_ADMIN)) {
              next({ name: 'clientsearch' })
            }
          } else {
            // If an unauthorized user tried to access 'Supervisor dashboard' or 'PO dashboard', direct to their respective homepage
            if ((to.name == APP_GLOBALS.$ROUTER_NAME_DASHBOARDSUPERVISOR && !store.hasSupervisorDash()) ||
                to.name == APP_GLOBALS.$ROUTER_NAME_DASHBOARDPO && !store.hasPODash()) {
              if (store.loginUserGroups.includes(APP_GLOBALS.$USER_GROUP_PO)) {
                next({ name: 'dashboardpo' })
              } else if (store.loginUserGroups.includes(APP_GLOBALS.$USER_GROUP_ITRP)) {
                next({ name: 'dashboarditrp' })
              } else {
                next({ name: 'clientsearch' })
              }
            } else {
              next()
            }
          }
        })
        .catch((err: any) => {
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
