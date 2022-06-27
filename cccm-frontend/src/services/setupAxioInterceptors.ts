import axiosInstance from "@/components/form.api";
import updateToken from '@/middleware/update-token';

const setup = () => {
  axiosInstance.interceptors.request.use(
    async config => {
      const token = await updateToken();
      //console.log("token interceptor: ", token);
      if (token) {
        config.headers["Authorization"] = 'Bearer ' + token;  // for Spring Boot back-end
       }
      return config;
    });
  };

export default setup;