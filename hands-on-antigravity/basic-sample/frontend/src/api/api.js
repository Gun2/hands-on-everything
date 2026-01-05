import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api/posts',
});

export const getPosts = () => api.get('');
export const getPost = (id) => api.get(`/${id}`);
export const createPost = (post) => api.post('', post);
export const updatePost = (id, post) => api.put(`/${id}`, post);
export const deletePost = (id) => api.delete(`/${id}`);
