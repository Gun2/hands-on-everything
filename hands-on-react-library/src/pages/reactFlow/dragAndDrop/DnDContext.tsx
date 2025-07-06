import { createContext, useContext, useState } from 'react';
import { NodeType } from './nodeTypes';

type DnDValue = {
  type: NodeType | null;
  setType: (type: NodeType | null) => void;
}
const DnDContext = createContext<DnDValue>({
  type: null,
  setType: (type) => {}
});

type DnDProviderProps = {
  children: React.ReactNode;
}
export const DnDProvider = (
  {
    children
  }: DnDProviderProps
) => {
  const [type, setType] = useState<NodeType | null>(null);

  return (
    <DnDContext.Provider value={{
      type,
      setType
    }}>
      {children}
    </DnDContext.Provider>
  );
}

export default DnDContext;

export const useDnD = () => {
  return useContext(DnDContext);
}