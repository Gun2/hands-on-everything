import React, { useEffect, useState } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import { createPost, getPost, updatePost } from '../api/api';

const PostForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const isEdit = !!id;

    const [post, setPost] = useState({
        title: '',
        content: '',
        author: ''
    });

    useEffect(() => {
        if (isEdit) {
            getPost(id).then(res => setPost(res.data)).catch(console.error);
        }
    }, [id, isEdit]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setPost(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (isEdit) {
                await updatePost(id, post);
            } else {
                await createPost(post);
            }
            navigate('/');
        } catch (error) {
            console.error(error);
            alert('Failed to save post');
        }
    };

    return (
        <div className="post-form">
            <h2>{isEdit ? 'Edit Post' : 'Create Post'}</h2>
            <form onSubmit={handleSubmit} style={{ maxWidth: '600px' }}>
                <div className="form-group" style={{ marginBottom: '15px' }}>
                    <label style={{ display: 'block', marginBottom: '5px' }}>Title</label>
                    <input
                        name="title"
                        value={post.title}
                        onChange={handleChange}
                        required
                        style={{ width: '100%', padding: '8px', fontSize: '1em' }}
                    />
                </div>
                <div className="form-group" style={{ marginBottom: '15px' }}>
                    <label style={{ display: 'block', marginBottom: '5px' }}>Author</label>
                    <input
                        name="author"
                        value={post.author}
                        onChange={handleChange}
                        required
                        style={{ width: '100%', padding: '8px', fontSize: '1em' }}
                    />
                </div>
                <div className="form-group" style={{ marginBottom: '15px' }}>
                    <label style={{ display: 'block', marginBottom: '5px' }}>Content</label>
                    <textarea
                        name="content"
                        value={post.content}
                        onChange={handleChange}
                        rows="10"
                        required
                        style={{ width: '100%', padding: '8px', fontSize: '1em', fontFamily: 'inherit' }}
                    />
                </div>
                <button type="submit">Save</button>
                <Link to="/" style={{ marginLeft: '15px' }}>Cancel</Link>
            </form>
        </div>
    );
};

export default PostForm;
