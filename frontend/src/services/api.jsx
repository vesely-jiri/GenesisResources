const API_BASE_URL = 'http://localhost:8080/api/v1';

export async function fetchUsers() {
  const response = await fetch(`${API_BASE_URL}/users?detail=true`);
  const data = await response.json();
  return data;
}


export async function createUser(userData) {
  const response = await fetch(`${API_BASE_URL}/users`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData),
  });
  return response.json();
}

export async function updateUser(userId, userData) {
  const response = await fetch(`${API_BASE_URL}/users/${userId}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData),
  });
  return response.json();
}

export async function deleteUser(userId) {
  const response = await fetch(`${API_BASE_URL}/users/${userId}`, {
    method: 'DELETE',
  });
  return response.json();
}

export async function fetchUser(userId) {
  const response = await fetch(`${API_BASE_URL}/users/${userId}`);
  return response.json();
}
