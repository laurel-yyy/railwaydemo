import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import Home from './pages/Home';
import TicketSearch from './pages/TicketSearch';
import TicketPurchase from './pages/TicketPurchase';
import './index.css';

// 检查用户是否已登录
const isAuthenticated = () => {
  return localStorage.getItem('accessToken') !== null;
};

// 受保护的路由组件
const ProtectedRoute = ({ children }) => {
  if (!isAuthenticated()) {
    return <Navigate to="/login" />;
  }
  return children;
};

function App() {
  return (
    <Router>
      <Routes>
        {/* 公开路由 */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        
        {/* 受保护路由 */}
        <Route 
          path="/" 
          element={
            <ProtectedRoute>
              <Home />
            </ProtectedRoute>
          } 
        />
        
        <Route 
          path="/ticket-search" 
          element={
            <ProtectedRoute>
              <TicketSearch />
            </ProtectedRoute>
          } 
        />
        
        <Route 
          path="/ticket-purchase" 
          element={
            <ProtectedRoute>
              <TicketPurchase />
            </ProtectedRoute>
          } 
        />
        
        {/* 重定向其他路径到首页 */}
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </Router>
  );
}

export default App;