/**
 * server인지 확인
 */
export const isServer = () => {
  return typeof window === 'undefined';
}