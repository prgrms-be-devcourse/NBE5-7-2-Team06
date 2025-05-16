import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

const AdminLayout = ({ children }) => {
    const navigate = useNavigate();
    const location = useLocation();

    const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
    // setActiveMenu 삭제하고 이걸로 교체
    const activeMenu = location.pathname.includes('/admin/vacation-request') ? 'vacation-list'
        : location.pathname.includes('/admin/code') ? 'code-management'
            : 'vacation-list';

    // 메뉴 아이템 정의
    const menuItems = [
        {
            id: 'vacation-list',
            label: '휴가 신청 목록',
            icon: '📋',
            path: '/admin/vacation-request'
        },
        // {
        //   id: 'vacation-detail',
        //   label: '휴가 신청 상세',
        //   icon: '📄',
        //   path: '/admin/vacation-request/detail'
        // },
        {
            id: 'code-management',
            label: '코드 관리',
            icon: '⚙️',
            path: '/admin/code'
        },
        // {
        //   id: 'my-vacation',
        //   label: '내 휴가 현황',
        //   icon: '🏖️',
        //   path: '/my-vacation'
        // },
        // {
        //   id: 'approval',
        //   label: '결재 관리',
        //   icon: '✅',
        //   path: '/approval'
        // }
        {
            id: 'vacations',
            label: '휴가 관리',
            icon: '✅',
            path: '/vacations'
        }
    ];

    const handleMenuClick = (path) => {
        navigate(path);
    };

    return (
        <div className="min-h-screen bg-gray-50">
            {/* Header */}
            <header className="bg-white shadow-sm border-b border-gray-200 h-16 fixed top-0 left-0 right-0 z-30">
                <div className="flex items-center justify-between h-full px-4">
                    {/* Left side - Logo and toggle */}
                    <div className="flex items-center">
                        <button
                            onClick={() => setSidebarCollapsed(!sidebarCollapsed)}
                            className="p-2 rounded-md hover:bg-gray-100 lg:hidden"
                        >
                            <span className="text-gray-600">☰</span>
                        </button>
                        <div className="ml-4 flex items-center">
                            <h1 className="text-xl font-bold text-gray-900">휴가 관리 시스템</h1>
                        </div>
                    </div>

                    {/* Right side - User menu */}
                    <div className="flex items-center space-x-4">
                        <button className="p-2 rounded-md hover:bg-gray-100">
                            <span className="text-gray-600">🔔</span>
                        </button>
                        <div className="flex items-center space-x-3">
                            <div className="w-8 h-8 bg-blue-500 rounded-full flex items-center justify-center">
                                <span className="text-white text-sm font-medium">김</span>
                            </div>
                            <div className="hidden md:block text-sm">
                                <div className="font-medium text-gray-900">김철수</div>
                                <div className="text-gray-500">개발팀 대리</div>
                            </div>
                            <button className="text-gray-400 hover:text-gray-600">
                                <span>▼</span>
                            </button>
                        </div>
                    </div>
                </div>
            </header>

            {/* Sidebar */}
            <aside className={`fixed top-16 left-0 h-full bg-white shadow-lg border-r border-gray-200 transition-width duration-300 z-20 ${
                sidebarCollapsed ? 'w-16' : 'w-60'
            }`}>
                {/* Toggle button for desktop */}
                <div className="hidden lg:block absolute -right-3 top-6">
                    <button
                        onClick={() => setSidebarCollapsed(!sidebarCollapsed)}
                        className="w-6 h-6 bg-white rounded-full border border-gray-300 flex items-center justify-center hover:bg-gray-50"
                    >
            <span className={`text-xs transition-transform ${sidebarCollapsed ? 'rotate-180' : ''}`}>
              ◀
            </span>
                    </button>
                </div>

                {/* Navigation */}
                <nav className="p-4">
                    <ul className="space-y-2">
                        {menuItems.map((item) => (
                            <li key={item.id}>
                                <button
                                    onClick={() => handleMenuClick(item.path)}
                                    className={`w-full flex items-center px-3 py-2 rounded-md text-sm transition-colors ${
                                        activeMenu === item.id
                                            ? 'bg-blue-50 text-blue-700 border-l-4 border-blue-700'
                                            : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'
                                    }`}
                                    title={sidebarCollapsed ? item.label : ''}
                                >
                                    <span className="text-lg">{item.icon}</span>
                                    {!sidebarCollapsed && (
                                        <span className="ml-3 font-medium">{item.label}</span>
                                    )}
                                </button>
                            </li>
                        ))}
                    </ul>
                </nav>

                {/* Sidebar footer */}
                <div className="absolute bottom-4 left-4 right-4">
                    <div className={`text-xs text-gray-500 ${sidebarCollapsed ? 'hidden' : 'block'}`}>
                        <div>버전: 1.0.0</div>
                        <div className="mt-1">© 2025 Company</div>
                    </div>
                </div>
            </aside>

            {/* Main content */}
            <main className={`pt-16 transition-all duration-300 ${
                sidebarCollapsed ? 'ml-16' : 'ml-60'
            }`}>
                <div className="min-h-screen">
                    {children}
                </div>
            </main>

            {/* Footer */}
            <footer className={`bg-white border-t border-gray-200 transition-all duration-300 ${
                sidebarCollapsed ? 'ml-16' : 'ml-60'
            }`}>
                <div className="px-6 py-4">
                    <div className="flex flex-col md:flex-row md:items-center md:justify-between">
                        <div className="text-sm text-gray-600">
                            © 2025 휴가 관리 시스템. All rights reserved.
                        </div>
                        <div className="mt-2 md:mt-0 flex space-x-6 text-sm text-gray-600">
                            <a href="#" className="hover:text-gray-900">개인정보처리방침</a>
                            <a href="#" className="hover:text-gray-900">이용약관</a>
                            <a href="#" className="hover:text-gray-900">도움말</a>
                        </div>
                    </div>
                </div>
            </footer>

            {/* Mobile sidebar overlay */}
            {!sidebarCollapsed && (
                <div
                    className="lg:hidden fixed inset-0 bg-black bg-opacity-50 z-10"
                    onClick={() => setSidebarCollapsed(true)}
                ></div>
            )}
        </div>
    );
};

// 사용 예시 컴포넌트
const App = () => {
    return (
        <AdminLayout>
            <div className="p-6">
                <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
                    <h2 className="text-xl font-semibold text-gray-800 mb-4">환영합니다!</h2>
                    <p className="text-gray-600">
                        휴가 관리 시스템에 오신 것을 환영합니다.
                        왼쪽 사이드바에서 원하는 기능을 선택해 주세요.
                    </p>

                    {/* 샘플 통계 카드 */}
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-8">
                        <div className="bg-blue-50 rounded-lg p-4">
                            <div className="flex items-center">
                                <div className="w-10 h-10 bg-blue-500 rounded-lg flex items-center justify-center">
                                    <span className="text-white">📋</span>
                                </div>
                                <div className="ml-4">
                                    <div className="text-sm text-gray-600">총 휴가 신청</div>
                                    <div className="text-xl font-semibold text-gray-900">143</div>
                                </div>
                            </div>
                        </div>

                        <div className="bg-green-50 rounded-lg p-4">
                            <div className="flex items-center">
                                <div className="w-10 h-10 bg-green-500 rounded-lg flex items-center justify-center">
                                    <span className="text-white">✅</span>
                                </div>
                                <div className="ml-4">
                                    <div className="text-sm text-gray-600">승인 완료</div>
                                    <div className="text-xl font-semibold text-gray-900">128</div>
                                </div>
                            </div>
                        </div>

                        <div className="bg-yellow-50 rounded-lg p-4">
                            <div className="flex items-center">
                                <div className="w-10 h-10 bg-yellow-500 rounded-lg flex items-center justify-center">
                                    <span className="text-white">⏳</span>
                                </div>
                                <div className="ml-4">
                                    <div className="text-sm text-gray-600">승인 대기</div>
                                    <div className="text-xl font-semibold text-gray-900">15</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </AdminLayout>
    );
};

export default AdminLayout;