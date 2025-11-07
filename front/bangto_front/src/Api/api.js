// src/api/index.js

// base URL은 환경변수에서 불러옴
const BASE_URL = process.env.REACT_APP_API_URL;

// 기능별 endpoint를 모듈처럼 구성
export const API = {
  AUTH: {
    LOGIN: `${BASE_URL}/auth/login`,
    SIGNUP: `${BASE_URL}/auth/signup`,
    LOGOUT: `${BASE_URL}/auth/logout`,
  },
  USER: {
    PROFILE: `${BASE_URL}/user/profile`,
    UPDATE: `${BASE_URL}/user/update`,
  },
  PRODUCT: {
    LIST: `${BASE_URL}/products`,
    DETAIL: (id) => `${BASE_URL}/products/${id}`, // 동적 파라미터
  },
  ORDER: {
    CREATE: `${BASE_URL}/orders`,
    DETAIL: (id) => `${BASE_URL}/orders/${id}`,
  },
};
