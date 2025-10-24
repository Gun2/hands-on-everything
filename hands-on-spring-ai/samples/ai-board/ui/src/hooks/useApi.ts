import { useState } from 'react';
import { useGlobalAlert } from '../components/mui/Alert/hooks/useGlobalAlert';

export const useApi = <T>() => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const {showAlert} = useGlobalAlert();

    const request = async (promise: Promise<T>): Promise<T | null> => {
        setLoading(true);
        setError(null);
        try {
            const response = await promise;
            return response;
        } catch (err: any) {
            const message = err.response?.data?.message || '오류가 발생했습니다.';
            setError(message);
            showAlert({
                title: "Error",
                message,
            })
            console.error(err);
            return null;
        } finally {
            setLoading(false);
        }
    };

    return { loading, error, request };
};