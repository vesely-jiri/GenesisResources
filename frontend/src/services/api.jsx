const API_BASE_URL = 'http://localhost:8080/api/v1';

async function handleResponse(response) {
  if (!response.ok) {
    const errorData = await response.json().catch(() => ({
      error: 'Neznámá chyba',
      details: 'Server neposkytl detailní zprávu',
    }));
    if (errorData.error == errorData.details) {
      throw new Error(`${errorData.error}`);
    }
    throw new Error(`${errorData.error}: ${errorData.details}`);
  }
  return response.json();
}

export async function fetchUsers() {
  const response = await fetch(`${API_BASE_URL}/users?detail=true`);
  return handleResponse(response);
}

export async function createUser(userData) {
  const response = await fetch(`${API_BASE_URL}/users`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData),
  });
  return handleResponse(response);
}

export async function updateUser(userId, userData) {
  const response = await fetch(`${API_BASE_URL}/users/${userId}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData),
  });
  return handleResponse(response);
}

export async function deleteUser(userId) {
  const response = await fetch(`${API_BASE_URL}/users/${userId}`, {
    method: 'DELETE',
  });
  return handleResponse(response);
}

export async function fetchUser(userId) {
  const response = await fetch(`${API_BASE_URL}/users/${userId}`);
  return handleResponse(response);
}