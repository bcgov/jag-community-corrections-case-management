import axiosInstance from "@/components/form.api";
import updateToken from '@/middleware/update-token';
import {useStore} from "@/stores/store";

const setup = () => {
  axiosInstance.interceptors.request.use(
    async config => {
      const token = await updateToken();
      //console.log("token interceptor: ", token);
      if (token) {
        config.headers["Authorization"] = 'Bearer ' + token;  // for Spring Boot back-end
        const store = useStore();
        config.headers["X-Location-Id"] = store.locationCD;
      }
      return config;
    });
  };

export default setup;