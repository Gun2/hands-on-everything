import { useConfirmContext } from './useConfirmContext';
import { ConfirmContextValue } from '../providers/ConfirmProvider';

export interface useGlobalAlertResult {
    showConfirm: ConfirmContextValue["showConfirm"];
}

/**
 * 전역 확인창 생성 hook
 */
export const useGlobalConfirm = () : useGlobalAlertResult => {
    const {showConfirm} = useConfirmContext();
    return {showConfirm: showConfirm};
}