import axiosInstance from "@/components/form.api";
import updateToken from '@/middleware/update-token';

const setup = () => {
  axiosInstance.interceptors.request.use(
    (config) => {
      const token = updateToken();
      console.log("token interceptor: ", token);
      if (token) {
        //config.headers.common['Authorization'] = `Bearer ${token}`;
        config.headers["Authorization"] = 'Bearer ' + token;  // for Spring Boot back-end
        //config.headers["x-access-token"] = token; // for Node.js Express back-end
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

};

export default setup;