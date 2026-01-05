import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { getPost, deletePost } from '../api/api';

const PostDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [post, setPost] = useState(null);

    useEffect(() => {
        const fetchPost = async () => {
            try {
                const response = await getPost(id);
                setPost(response.data);
            } catch (error) {
                console.error(error);
                navigate('/');
            }
        };
        fetchPost();
    }, [id, navigate]);

    const handleDelete = async () => {
        if (window.confirm('Are you sure you want to delete this post?')) {
            await deletePost(id);
            navigate('/');
        }
    };

    if (!post) return <div>Loading...</div>;

    return (
        <div className="post-detail">
            <h2>{post.title}</h2>
            <div className="meta" style={{ color: '#666', marginBottom: '20px' }}>
                By {post.author} | {new Date(post.createdAt).toLocaleString()}
            </div>
            <div className="content" style={{ lineHeight: '1.6', marginBottom: '30px', whiteSpace: 'pre-wrap' }}>
                {post.content}
            </div>
            <div className="actions">
                <Link to={`/edit/${id}`}><button>Edit</button></Link>
                <button onClick={handleDelete} style={{ marginLeft: '10px', backgroundColor: '#ffcccc', color: '#cc0000', borderColor: '#ffaaaa' }}>Delete</button>
                <Link to="/" style={{ marginLeft: '10px' }}>Back to List</Link>
            </div>
        </div>
    );
};

export default PostDetail;
