import React, { useState, useEffect } from 'react';
import { fetchUsers, deleteUser } from '../services/api';
import styled from 'styled-components';

const PageWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f4f4f9;
  font-family: Arial, sans-serif;
`;

const Container = styled.div`
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 800px;
`;

const Title = styled.h1`
  text-align: center;
  color: #333;
`;

const UserList = styled.ul`
  list-style: none;
  padding: 0;
`;

const UserItem = styled.li`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  margin: 10px 0;
  background-color: #fafafa;
  border-radius: 5px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
`;

const Button = styled.button`
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  &:hover {
    background-color: #c0392b;
  }
`;

const HomePage = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchUsers()
            .then((data) => {
                setUsers(data);
                setLoading(false);
            })
            .catch((error) => {
                setError('Chyba při načítání uživatelů');
                setLoading(false);
            });
    }, []);

    const handleDelete = (id) => {
        deleteUser(id)
            .then(() => {
                fetchUsers()
                    .then((data) => setUsers(data))
                    .catch((error) => setError('Chyba při načítání uživatelů po smazání'));
            })
            .catch((error) => {
                setError('Chyba při mazání uživatele');
                console.log('Delete user error:', error);
            });

        console.log('Delete user', id);
    };

    if (loading) {
        return <p>Načítám...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    return (
        <PageWrapper>
            <Container>
                <Title>Uživatelé</Title>
                <UserList>
                    {users.map(user => (
                        <UserItem key={user.id}>
                            <span>{user.firstName} {user.lastName} ({user.personId})</span>
                            <Button onClick={() => handleDelete(user.id)}>Delete</Button>
                        </UserItem>
                    ))}
                </UserList>
            </Container>
        </PageWrapper>
    );
};

export default HomePage;
