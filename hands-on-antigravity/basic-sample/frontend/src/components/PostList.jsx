import React, { useEffect, useState } from 'react';
import { getPosts } from '../api/api';
import { Link } from 'react-router-dom';

const PostList = () => {
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        loadPosts();
    }, []);

    const loadPosts = async () => {
        try {
            const response = await getPosts();
            setPosts(response.data);
        } catch (error) {
            console.error('Failed to fetch posts', error);
        }
    };

    return (
        <div className="post-list">
            <div className="list-header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <h2>Posts</h2>
                <Link to="/create"><button>New Post</button></Link>
            </div>
            {posts.length === 0 ? <p>No posts found.</p> : (
                <ul style={{ listStyle: 'none', padding: 0 }}>
                    {posts.map(post => (
                        <li key={post.id} className="post-item" style={{ border: '1px solid #ddd', padding: '15px', marginBottom: '10px', borderRadius: '8px' }}>
                            <Link to={`/posts/${post.id}`} style={{ fontSize: '1.2em', fontWeight: 'bold' }}>
                                {post.title}
                            </Link>
                            <p className="post-meta" style={{ color: '#666', fontSize: '0.9em' }}>
                                By {post.author} on {new Date(post.createdAt).toLocaleDateString()}
                            </p>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default PostList;
