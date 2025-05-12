import React, { useState, useEffect } from 'react';
import { fetchUsers, deleteUser, createUser } from '../services/api';
import styled from 'styled-components';

const PageWrapper = styled.div`
	display: flex;
	justify-content: center;
	align-items: center;
	min-height: 100vh;
	font-family: Arial, sans-serif;
	position: relative;
`;

const Container = styled.div`
	background: #a2a2a2;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
	width: 100%;
	max-width: 800px;
	position: relative;
`;

const AddButton = styled.button`
	position: absolute;
	top: 10px;
	right: 10px;
	background-color: #3498db;
	color: white;
	border: none;
	padding: 8px 12px;
	border-radius: 5px;
	cursor: pointer;
	font-size: 14px;
	&:hover {
		background-color: #2980b9;
	}
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
	background-color: #6d6d6d;
	border-radius: 5px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
	gap: 10px;
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

const UserInfo = styled.span`
	color: black;
`;

const FormWrapper = styled.div`
	margin-top: 20px;
	display: flex;
	flex-direction: column;
	gap: 8px;
`;

const Input = styled.input`
	padding: 5px;
	border: 1px solid #ccc;
	border-radius: 4px;
`;

const SubmitButton = styled(Button)`
	background-color: #2ecc71;
	&:hover {
		background-color: #27ae60;
	}
`;

const Message = styled.p`
	color: ${({ error }) => (error ? '#e74c3c' : '#2ecc71')};
	margin: 10px 0;
`;

const HomePage = () => {
	const [users, setUsers] = useState([]);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState(null);
	const [showForm, setShowForm] = useState(false);
	const [newUser, setNewUser] = useState({ firstName: '', lastName: '', personId: '' });
	const [message, setMessage] = useState('');

	useEffect(() => {
		fetchUsers()
			.then((data) => {
				setUsers(data);
				setLoading(false);
			})
			.catch(() => {
				setError('Error fetching users');
				setLoading(false);
			});
	}, []);

	const handleDelete = (id) => {
		deleteUser(id)
			.then(() => {
				setUsers((prevUsers) => prevUsers.filter((user) => user.id !== id));
				setMessage('User deleted successfully');
			})
			.catch((error) => {
				setError('Error deleting user');
				console.log('Delete user error:', error);
			});
	};

    const handleAddUser = () => {
        createUser(newUser)
            .then((response) => {
                setUsers((prevUsers) => [...prevUsers, response]);
                setMessage('User added successfully');
                setNewUser({ firstName: '', lastName: '', personId: '' });
                setShowForm(false);
            })
            .catch((error) => {
                if (error.response) {
                    const status = error.response.status;
                    let errorMessage;
                    switch (status) {
                        case 400:
                            errorMessage = 'Wrong input data';
                            break;
                        case 404:
                            errorMessage = 'User not found';
                            break;
                        case 500:
                            errorMessage = 'Internal server error';
                            break;
                        default:
                            errorMessage = `Unknown error: ${status}`;
                    }    
                    setMessage(`Error ${status}: ${errorMessage}`);
                } else if (error.message) {
                    setMessage(`Error adding user: ${error.message}`);
                } else {
                    setMessage('Unknown error adding user');
                }
                console.log('Add user error:', error);
            });
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
				<Title>Genesis users</Title>
				<AddButton onClick={() => setShowForm(!showForm)}>
					{showForm ? 'Close form' : 'Add user'}
				</AddButton>
				{message && <Message error={message.startsWith('Error')}>{message}</Message>}
				{showForm && (
					<FormWrapper>
						<Input
							type="text"
							placeholder="First name"
							value={newUser.firstName}
							onChange={(e) => setNewUser({ ...newUser, firstName: e.target.value })}
						/>
						<Input
							type="text"
							placeholder="Last name"
							value={newUser.lastName}
							onChange={(e) => setNewUser({ ...newUser, lastName: e.target.value })}
						/>
						<Input
							type="text"
							placeholder="Person ID"
							value={newUser.personId}
							onChange={(e) => setNewUser({ ...newUser, personId: e.target.value })}
						/>
						<SubmitButton onClick={handleAddUser}>Uložit</SubmitButton>
					</FormWrapper>
				)}
				<UserList>
					{users.map((user) => (
						<UserItem key={user.id}>
							<UserInfo>
								{user.firstName} {user.lastName} ({user.personId})
							</UserInfo>
							<Button onClick={() => handleDelete(user.id)}>Delete</Button>
						</UserItem>
					))}
				</UserList>
			</Container>
		</PageWrapper>
	);
};

export default HomePage;
