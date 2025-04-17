import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const Home = () => {
  const navigate = useNavigate();
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);

  // Get user information
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        // Get user information from localStorage
        const user = JSON.parse(localStorage.getItem('user'));
        setUserData(user);
      } catch (error) {
        console.error('Failed to get user information:', error);
        // If getting information fails, the token may have expired
        if (error.response && error.response.status === 401) {
          handleLogout();
        }
      } finally {
        setLoading(false);
      }
    };

    fetchUserData();
  }, []);

  // Logout
  const handleLogout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('user');
    navigate('/login');
  };

  // Navigate to ticket search page
  const goToTicketSearch = () => {
    navigate('/ticket-search');
  };

  // Navigate to orders page
  const goToOrders = () => {
    navigate('/orders');
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen bg-gray-100">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Header Navigation */}
      <nav className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center">
              <span className="text-xl font-semibold text-gray-800">Railway Demo</span>
            </div>
            <div className="flex items-center">
              {userData && (
                <div className="flex items-center space-x-4">
                  <span className="text-gray-700">
                    Welcome, {userData.realName || userData.username}
                  </span>
                  <button
                    onClick={handleLogout}
                    className="px-3 py-1 bg-red-500 text-white text-sm rounded-md hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-500"
                  >
                    Logout
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      </nav>

      {/* Content Area */}
      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        {/* Feature Card Area */}
        <div className="px-4 py-6 sm:px-0">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {/* Ticket Search Card */}
            <div className="bg-white overflow-hidden shadow rounded-lg">
              <div className="px-4 py-5 sm:p-6">
                <h3 className="text-lg font-medium text-gray-900">Ticket Search</h3>
                <div className="mt-2 max-w-xl text-sm text-gray-500">
                  <p>Search for ticket information and available seats.</p>
                </div>
                <div className="mt-5">
                  <button
                    onClick={goToTicketSearch}
                    className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
                  >
                    Go to Search
                  </button>
                </div>
              </div>
            </div>

            {/* My Orders Card */}
            <div className="bg-white overflow-hidden shadow rounded-lg">
              <div className="px-4 py-5 sm:p-6">
                <h3 className="text-lg font-medium text-gray-900">My Orders</h3>
                <div className="mt-2 max-w-xl text-sm text-gray-500">
                  <p>View your purchased tickets and order history.</p>
                </div>
                <div className="mt-5">
                  <button
                    onClick={goToOrders}
                    className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
                  >
                    View Orders
                  </button>
                </div>
              </div>
            </div>

            {/* Additional feature cards can be added here */}
          </div>
        </div>

        {/* User Information Area */}
        <div className="px-4 py-6 sm:px-0">
          <div className="bg-white rounded-lg shadow-md p-6 mt-6">
            <h2 className="text-lg font-medium text-gray-800 mb-4">Your Account Information</h2>
            {userData && (
              <div className="space-y-2">
                <p><span className="font-medium">Username:</span> {userData.username}</p>
                {userData.realName && (
                  <p><span className="font-medium">Real Name:</span> {userData.realName}</p>
                )}
                {/* More user information can be displayed here */}
              </div>
            )}
          </div>
        </div>
        
        {/* Map Area */}
        <div className="px-4 py-6 sm:px-0">
          <div className="bg-white rounded-lg shadow-md p-6 mt-6">
            <h2 className="text-lg font-medium text-gray-800 mb-4">Railway Network Map</h2>
            <div className="flex justify-center">
              <img 
                src="/map.png" 
                alt="Railway Network Map" 
                className="max-w-full h-auto rounded-lg shadow-sm"
              />
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default Home;