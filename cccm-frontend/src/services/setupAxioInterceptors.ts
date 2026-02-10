import axiosInstance from "@/components/form.api";
import updateToken from '@/middleware/update-token';
import { v4 as uuidv4 } from 'uuid';
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
        config.headers["X-Transaction-Id"] = uuidv4();
      }
      return config;
    });
  };

export default setup;