import { useContext } from 'react';
import { ConfirmContext } from '../providers/ConfirmProvider';

export const useConfirmContext = () => {
    const context = useContext(ConfirmContext);
    if (!context) {
        throw new Error("useConfirmContext must be used within an ConfirmProvider");
    }
    return context;
}
