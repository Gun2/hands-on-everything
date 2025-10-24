import { useAlertContext } from './useAlertContext';
import { AlertContextValue } from '../providers/AlertProvider';

export interface useGlobalAlertResult {
    showAlert: AlertContextValue["showAlert"];
}

/**
 * 전역
 */
export const useGlobalAlert = () : useGlobalAlertResult => {
    const {showAlert} = useAlertContext();
    return {showAlert};
}