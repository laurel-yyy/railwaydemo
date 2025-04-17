import axios from 'axios';

// Create axios instance
const instance = axios.create({
  baseURL: 'http://localhost:8080/api', // Ensure base URL matches backend
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Request interceptor
instance.interceptors.request.use(
  config => {
    console.log('Sending request:', config);
    
    // Get token from localStorage
    const token = localStorage.getItem('accessToken');
    
    // Add token to request header if exists
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    
    return config;
  },
  error => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor
instance.interceptors.response.use(
  response => {
    console.log('API original response data:', response);
    
    // Return response data directly
    return response.data;
  },
  error => {
    console.error('API response error:', error);
    
    if (error.response) {
      // Server returned an error status code
      console.error('Error status code:', error.response.status);
      console.error('Error data:', error.response.data);
      
      switch (error.response.status) {
        case 401: // Unauthorized
          // Clear token and redirect to login page
          localStorage.removeItem('accessToken');
          window.location.href = '/login';
          break;
          
        case 403: // Forbidden
          console.error('No permission to access this resource');
          break;
          
        case 404: // Resource not found
          console.error('Requested resource does not exist');
          break;
          
        case 500: // Server error
          console.error('Server error');
          break;
          
        default:
          console.error(`Request error: ${error.response.status}`);
      }
      
      // Return data from error response
      return Promise.reject(error);
    } else if (error.request) {
      // Request was sent but no response received
      console.error('No server response received, please check network connection');
      return Promise.reject({ msg: 'Network connection error, please check your network' });
    } else {
      // Request configuration error
      console.error('Request configuration error', error.message);
      return Promise.reject({ msg: 'Request configuration error' });
    }
  }
);

export default instance;