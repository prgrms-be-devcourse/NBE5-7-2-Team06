// src/api/axiosInstance.js

import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080', // ✅ 백엔드 API 기본 주소
    withCredentials: true,           // ✅ 쿠키 포함 여부 (refreshToken 사용 시 필수)
});

// 👉 accessToken 자동 주입
api.interceptors.request.use(config => {
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
        config.headers['Authorization'] = `Bearer ${accessToken}`;
    }
    return config;
}, error => {
    return Promise.reject(error);
});

// 👉 응답 에러 처리
let isRefreshing = false;
let refreshSubscribers = [];

function onRefreshed(newAccessToken) {
    refreshSubscribers.forEach(callback => callback(newAccessToken));
    refreshSubscribers = [];
}

api.interceptors.response.use(
    response => response, // ✅ 정상 응답은 그대로 반환
    async error => {

        const originalRequest = error.config;

        const code = error.response?.data?.codeName;

        // ✅ accessToken 만료 → 재발급 시도
        if (error.response?.status === 401 &&
            code === 'UNAUTHORIZED_EXPIRED_TOKEN' &&
            !originalRequest._retry) {

            originalRequest._retry = true;

            return new Promise((resolve,reject) => {
                refreshSubscribers.push(token => {
                    originalRequest.headers['Authorization'] = `Bearer ${token}`;
                    resolve(api(originalRequest));
                });

                if (!isRefreshing) {
                    isRefreshing = true;

                      api.post('/auth/reissue')
                          .then(res =>{
                              const newAccessToken = res.data.accessToken;

                              // 새 accessToken 저장
                              localStorage.setItem('accessToken', newAccessToken);

                              // 모든 요청에 새 토큰 적용
                              api.defaults.headers.common['Authorization'] = `Bearer ${newAccessToken}`;
                              onRefreshed(newAccessToken);
                          }).catch (e =>{
                          window.location.href = '/auth/login';
                          return Promise.reject(e);
                      }) .finally(() => {
                          isRefreshing = false;
                      });
                }
            });
        }else if (error.response?.status === 403) {
            // ✅ 권한 부족: 알림만 띄우고 끝냄
            alert("해당 기능은 관리자만 접근 가능합니다.");
            return Promise.reject(error);
        }else if (error.response?.status === 401) {
            // ✅ 재발급 불가 (블랙리스트, 헤더 없음 등)
            window.location.href = '/auth/login';

        }

        return Promise.reject(error);
    }
);

export default api;
