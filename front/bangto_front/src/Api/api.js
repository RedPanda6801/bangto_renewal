// src/api/index.js

// base URL은 환경변수에서 불러옴
const BASE_URL = process.env.REACT_APP_API_URL + "/api";

// 기능별 endpoint를 모듈처럼 구성
export const API = {
  AUTH: {
    LOGIN: `${BASE_URL}/login`,
    SIGNUP: `${BASE_URL}/sign`,
    LOGOUT: `${BASE_URL}/logout`,
    SNS_SIGN: `${BASE_URL}/user/sns-sign`,
  },
  USER: {
    URL: `${BASE_URL}/user`,
  },
  SELLER: {
    URL: `${BASE_URL}/seller`,
  },
  APPLY: {
    APPLY: `${BASE_URL}/apply`,
    LIST: `${BASE_URL}/apply/get-list`,
  },
  STORE: {
    INSERT: `${BASE_URL}/store/insert`,
    URL: `${BASE_URL}/store`,
    LIST: `${BASE_URL}/store/get-list`,
  },
  ITEM: {
    DETAIL: `${BASE_URL}/item`,
    LIST: `${BASE_URL}/item/get-list`,
    FILTERED_LIST: `${BASE_URL}/store/get-list/filtered`,
    URL: `${BASE_URL}/seller/item/`,
  },
  OPTION: {
    URL: `${BASE_URL}/seller/option/`,
  },
  COMMENT: {
    URL: `${BASE_URL}/comment`,
  },
  QNA: {
    URL: `${BASE_URL}/qna`,
    ANSWER: `${BASE_URL}/seller/qna/answer`,
    LIST: `${BASE_URL}/qna/get-list`,
    LIST_OF_STORE: `${BASE_URL}/seller/qna/get-list/store`,
    LIST_OF_ITEM: `${BASE_URL}/qna/get-list/item`,
  },
  CART: {
    URL: `${BASE_URL}/cart`,
    UPSERT: `${BASE_URL}/cart/upsert`,
  },
  PAYMENT: {
    URL: `${BASE_URL}/payment`,
    LIST: `${BASE_URL}/get-list`,
    PROCESS: `${BASE_URL}/seller/payment/process`,
    LIST_OF_STORE: `${BASE_URL}/seller/payment/get-list/store`,
  },
  ADMIN: {
    USER: {
      SINGLE: `${BASE_URL}/admin/user`,
      LIST: `${BASE_URL}/admin/user/get-list`,
    },
    SELLER: {
      SINGLE: `${BASE_URL}/admin/seller`,
      LIST: `${BASE_URL}/admin/seller/get-list`,
      BANNED: `${BASE_URL}/admin/seller/banned`,
    },
    APPLY: {
      PROCESS: `${BASE_URL}/admin/apply/process`,
      LIST: `${BASE_URL}/admin/apply/get-list`,
      SINGLE: `${BASE_URL}/admin/apply`,
    },
    STORE: {
      LIST: `${BASE_URL}/admin/store/get-list`,
    },
    PAYMENT: {
      LIST: `${BASE_URL}/admin/payment/get-list`,
      LIST_OF_STORE: `${BASE_URL}/admin/payment/get-list/store`,
    },
  },
};
