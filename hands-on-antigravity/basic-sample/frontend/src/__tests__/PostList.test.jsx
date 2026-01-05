import { render, screen, waitFor } from '@testing-library/react';
import PostList from '../components/PostList';
import { BrowserRouter } from 'react-router-dom';
import * as api from '../api/api';
import { vi, describe, it, expect } from 'vitest';

vi.mock('../api/api');

describe('PostList', () => {
    it('renders posts', async () => {
        const posts = [
            { id: 1, title: 'Test Post', content: 'Content', author: 'Author', createdAt: new Date().toISOString() }
        ];
        api.getPosts.mockResolvedValue({ data: posts });

        render(
            <BrowserRouter>
                <PostList />
            </BrowserRouter>
        );

        await waitFor(() => {
            expect(screen.getByText('Test Post')).toBeInTheDocument();
        });
        expect(screen.getByText('By Author on ' + new Date(posts[0].createdAt).toLocaleDateString())).toBeInTheDocument();
    });

    it('renders no posts message', async () => {
        api.getPosts.mockResolvedValue({ data: [] });

        render(
            <BrowserRouter>
                <PostList />
            </BrowserRouter>
        );

        await waitFor(() => {
            expect(screen.getByText('No posts found.')).toBeInTheDocument();
        });
    });
});
